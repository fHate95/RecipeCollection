package com.sanechek.recipecollection.log;

import android.support.annotation.NonNull;

import com.sanechek.recipecollection.api.modules.InterceptorModule;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogInterceptor implements Interceptor {

    private final Logger logger;

    public LogInterceptor(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        LoggerData loggerData = new LoggerData();
        loggerData.setRequest(InterceptorModule.bodyToString(request));
        loggerData.setSendDate(System.currentTimeMillis());

        Response response;
        try {
            response = chain.proceed(request);
            loggerData.setResponse(InterceptorModule.bodyToString(response));
        } finally {
            loggerData.setEndDate(System.currentTimeMillis());
            logger.addLogData(loggerData);
        }

        return response;
    }
}
