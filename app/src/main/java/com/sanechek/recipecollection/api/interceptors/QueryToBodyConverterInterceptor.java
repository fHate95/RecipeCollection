package com.sanechek.recipecollection.api.interceptors;

import com.sanechek.recipecollection.api.modules.InterceptorModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Переносит аргументы запроса и URL в тело запроса.
 * Добавляет дополнительные параметры, если это необходимо.
 */
public class QueryToBodyConverterInterceptor implements Interceptor {

    public static final String ARG_REQUEST = "Request";

    private final iFieldProvider fieldProvider;

    private final String serverUrl;

    public QueryToBodyConverterInterceptor(iFieldProvider fieldProvider,
                                           String serverUrl) {
        this.serverUrl = serverUrl;
        this.fieldProvider = fieldProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();

        JSONObject jsonObject;

        String body = InterceptorModule.bodyToString(request);
        if(body != null && !body.isEmpty()){
            try {
                jsonObject = new JSONObject(body);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            jsonObject = new JSONObject();
        }
        for(int i = 0; i<url.querySize(); i++){
            try {
                jsonObject.put(url.queryParameterName(i),url.queryParameterValue(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String requestIdentifier = request.url().encodedPath().substring(1);

        try {
            jsonObject.put(ARG_REQUEST,requestIdentifier);
            fieldProvider.addParamsIfNeed(requestIdentifier,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // TODO: 11.07.18 Получить URL другим путём
        Request newRequest = new Request.Builder()
                .url(serverUrl)
                .post(RequestBody.create(InterceptorModule.JSON,jsonObject.toString()))
                .addHeader("Content-Type","application/json")
                .build();

        return chain.proceed(newRequest);
    }

    public interface iSubjectProvider {
        String getSubject();
    }

    public interface iFieldProvider{
        /**
         * Добавляет дополнительные поля к запросу, если это необходимо.
         * @param request
         * @param object
         */
        void addParamsIfNeed(String request, JSONObject object) throws JSONException;
    }
}
