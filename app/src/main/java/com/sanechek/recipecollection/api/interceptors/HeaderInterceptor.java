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
                .addHeader("Accept-Language", Locale.getDefault().getLanguage())
                .addHeader("X-Timeout","120")
//                .addHeader("X-Authorization", "678b1e3b-145b-458a-be95-db5fc1c42f9a") //test
                .addHeader("X-Authorization", "6628e91c-248c-4568-b513-dc55cc0d26de") //main
                .build();

        return chain.proceed(request);
    }
}
