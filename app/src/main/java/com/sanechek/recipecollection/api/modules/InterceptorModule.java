package com.sanechek.recipecollection.api.modules;

import android.support.annotation.Nullable;

import com.sanechek.recipecollection.api.interceptors.HeaderInterceptor;
import com.sanechek.recipecollection.api.interceptors.QueryToBodyConverterInterceptor;
import com.sanechek.recipecollection.api.interceptors.SignatureInterceptor;
import com.sanechek.recipecollection.api.interceptors.WsInterceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.sanechek.recipecollection.api.modules.RetrofitModule.CONNECTION_URL;
import static com.sanechek.recipecollection.api.modules.RetrofitModule.WS_CONNECTION_URL;

@Module
public class InterceptorModule {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Singleton
    @Provides
    @Inject
    @Named(RetrofitModule.QUERY_TO_BODY_CONVERTER)
    public Interceptor provideQueryToBodyConverter(QueryToBodyConverterInterceptor.iFieldProvider fieldProvider,
                                                   @Named(CONNECTION_URL) String connectionUrl) {
        return new QueryToBodyConverterInterceptor(fieldProvider, connectionUrl);
    }

    @Provides
    @Inject
    @Singleton
    @Named(RetrofitModule.SIGNATURE_INTERCEPTOR)
    public Interceptor provideSignatureInterceptor(SignatureInterceptor.iSignatureFactory signatureFactory){
        return new SignatureInterceptor(signatureFactory);
    }

    @Provides
    @Singleton
    @Named(RetrofitModule.HEADER_INTERCEPTOR)
    public Interceptor provideHeaderInterceptor(){
        return new HeaderInterceptor();
    }

    @Provides
    @Inject
    @Singleton
    @Named(RetrofitModule.WS_INTERCEPTOR)
    public Interceptor provideWsInterceptor(SignatureInterceptor.iSignatureFactory security, @Nullable @Named(WS_CONNECTION_URL) String wsConnectionUrl){
        return new WsInterceptor(security, wsConnectionUrl);
    }

    /**
     * Позволяет получить тело запроса в виде строки
     * @param request запрос
     * @return тело запроса в виде стркои
     */
    public static String bodyToString(final Request request){
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Позволяет получить тело ответа в виде строки
     * @param response ответ
     * @return тело ответа в виде стркои
     */
    public static String bodyToString(final Response response){
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Buffer buffer = source.buffer();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            buffer.copyTo(outputStream);
            return outputStream.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public interface iPublicKeyProvider {
        @Nullable
        String getPublicKeyBase64();
    }
}
