package com.sanechek.recipecollection.api.interceptors;

import android.util.Base64;
import android.util.Log;

import com.sanechek.recipecollection.api.modules.InterceptorModule;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Добавляет подпись в заголовок
 */
public class SignatureInterceptor implements Interceptor {

    private final iSignatureFactory security;

    public SignatureInterceptor(iSignatureFactory security) {
        this.security = security;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        String reqBodyString = InterceptorModule.bodyToString(oldRequest);
        String signature = security.getStringSignatureForString(reqBodyString);
        if(signature == null || signature.length() == 0){
            signature = Base64.encodeToString(" ".getBytes(),Base64.DEFAULT); // (" ".getBytes());
        }
        Request newRequest = oldRequest.newBuilder()
                .addHeader("X-Signature", signature.trim())
                .build();

        Log.d("Signature","Added signature to request " + reqBodyString);

        return chain.proceed(newRequest);
    }

    public interface iSignatureFactory {
        String getStringSignatureForString(String input);
    }
}
