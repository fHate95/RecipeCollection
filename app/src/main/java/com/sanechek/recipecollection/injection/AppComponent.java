package com.sanechek.recipecollection.injection;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sanechek.recipecollection.api.FoodApi;
import com.sanechek.recipecollection.api.modules.InterceptorModule;
import com.sanechek.recipecollection.injection.modules.ApiModule;
import com.sanechek.recipecollection.injection.modules.AppModule;
import com.sanechek.recipecollection.injection.modules.FieldProviderModule;
import com.sanechek.recipecollection.injection.modules.GsonModule;
import com.sanechek.recipecollection.injection.modules.LoggerModule;
import com.sanechek.recipecollection.injection.modules.RepositoryModule;
import com.sanechek.recipecollection.injection.modules.RetrofitModule;
import com.sanechek.recipecollection.log.LogInterceptorModule;
import com.sanechek.recipecollection.util.Repository;

import java.text.SimpleDateFormat;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {
        AppModule.class,
        RetrofitModule.class,
        InterceptorModule.class,
        GsonModule.class,
        LoggerModule.class,
        LogInterceptorModule.class,
        ApiModule.class,
        RepositoryModule.class,
        FieldProviderModule.class
})

@Singleton
public interface AppComponent {

    @Singleton
    Application getApplication();

    @Singleton
    Repository getRepository();

    @Singleton
    Gson getGson();

    @Singleton
    FoodApi getApi();

    @Singleton
    SimpleDateFormat getDateFormat();

    @Singleton
    SharedPreferences getSharedPreference();

}
