package com.sanechek.recipecollection.api.interceptors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sanechek.recipecollection.api.modules.InterceptorModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Named;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import tech.gusavila92.websocketclient.WebSocketClient;

import static com.sanechek.recipecollection.api.modules.RetrofitModule.WS_CONNECTION_URL;


/**
 * Передаёт данные по веб-сокетам.
 * В случае успеха, цепочка Interceptor-ов прерывается.
 *
 * В случае ошибки продолжает цепочку Interceptor-ов, таким образом,
 * запрос будет выполняться обычным путём.
 */
public class WsInterceptor implements Interceptor {

    private AtomicInteger msgIdCntr = new AtomicInteger();

    private static final String ARG_REQUEST_ID = "ID";
    private static final String TAG = "WEB_SOCKET";

    private WebSocketClient client;

    private final Map<Long,Pair<CountDownLatch,MessageHolder>> callbacks = new ConcurrentHashMap<>();

    private final SignatureInterceptor.iSignatureFactory signatureFactory;

    private boolean initializationSkipped;

    public WsInterceptor(SignatureInterceptor.iSignatureFactory signatureFactory, @Nullable @Named(WS_CONNECTION_URL) String wsConnectionUrl) {
        this.signatureFactory = signatureFactory;

        initializationSkipped = wsConnectionUrl == null;

        if(!initializationSkipped){
            URI uri;
            try {
                uri = new URI(wsConnectionUrl);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            client = new WebSocketClient(uri) {

                @Override
                public void onOpen() {
                    Log.d(TAG,"OnOpen");
                }

                @Override
                public void onTextReceived(String message) {
                    Log.d(TAG,"OnTextReceived: "+message);
                    long msgId = -1;
                    try {
                        JSONObject object = new JSONObject(message);
                        if(object.has(ARG_REQUEST_ID)) {
                            msgId = object.getLong(ARG_REQUEST_ID);
                        }
                        else {
                            Log.e(TAG,"message does not contains an RequestID: "+message);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Pair<CountDownLatch,MessageHolder> pair = callbacks.remove(msgId);
                    if(pair != null) {
                        Log.d(TAG,"listener for "+msgId+" found");
                        pair.second.msg = message;
                        pair.first.countDown();
                    }
                    else {
                        Log.d(TAG,"listener for "+msgId+" not found");
                    }
                }

                @Override
                public void onBinaryReceived(byte[] data) {
                    Log.d(TAG,"onBinaryReceived: "+ new String(data));
                }

                @Override
                public void onPingReceived(byte[] data) {
//                Log.d(TAG,"onPingReceived: "+ new String(data));
                }

                @Override
                public void onPongReceived(byte[] data) {
//                Log.d(TAG,"onPongReceived: "+ new String(data));
                }

                @Override
                public void onException(Exception e) {
                    Log.d(TAG,"onException",e);
                }

                @Override
                public void onCloseReceived() {
                    Log.d(TAG,"onCloseReceived");
                }
            };
            client.setConnectTimeout(10000);
            client.setReadTimeout(60000);
            client.enableAutomaticReconnection(5000);
            client.connect();
        }
    }


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        if(initializationSkipped){
            return chain.proceed(chain.request());
        }

        long msgId = msgIdCntr.incrementAndGet();

        Request request = chain.request();

        String json = InterceptorModule.bodyToString(chain.request());

        JsonParser parser = new JsonParser();
        JsonElement requestJsonElement = parser.parse(json);
        JsonObject requestJsonObj = requestJsonElement.getAsJsonObject();

        requestJsonObj.addProperty(ARG_REQUEST_ID,msgId);
        json = requestJsonObj.toString();

        String signature = signatureFactory.getStringSignatureForString(json);
        String msg = json+signature;

        CountDownLatch countDownLatch = new CountDownLatch(1);
        MessageHolder callback = new MessageHolder();
        callbacks.put(msgId, new Pair<>(countDownLatch,callback));

        client.send(msg);
        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
        callbacks.remove(msgId);

        if(callback.msg == null){
            Log.d(TAG,"attempt to request via http");
            Response response = chain.proceed(request);
            Log.d(TAG,"proceeded wia http");
            return response;
        }
        else {
            Log.d(TAG,"keep using web socket");
            ResponseBody body = ResponseBody.create(InterceptorModule.JSON,callback.msg);

            return new Response.Builder()
                    .code(200)
                    .request(request)
                    .body(body)
                    .protocol(Protocol.HTTP_1_1)
                    .message("Emulated response. Data was obtained via websocket")
                    .build();
        }
    }

    private static final class MessageHolder {
        volatile String msg;
    }
}
