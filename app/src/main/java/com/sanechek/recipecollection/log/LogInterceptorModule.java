package com.sanechek.recipecollection.log;


import com.sanechek.recipecollection.api.modules.RetrofitModule;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;

@Module
public class LogInterceptorModule {

    @Provides
    @Inject
    @Named(RetrofitModule.LOG_INTERCEPTOR)
    public Interceptor provideLogInterceptor(Logger logger){
        return new LogInterceptor(logger);
    }
}
