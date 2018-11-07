package com.sanechek.recipecollection.api.interceptors;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Добавляет дополнительные заголовки
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
//                .addHeader("Accept-Language", Locale.getDefault().getLanguage())
//                .addHeader("X-Timeout","120")
                .build();

        return chain.proceed(request);
    }
}
