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

/* Модули для dagger injection */
@Component(modules = {
        AppModule.class,            //Модуль приложения
        RetrofitModule.class,       //Модуль Retrofit
        InterceptorModule.class,    //Модуль интерсепторов (OkHttp)
        GsonModule.class,           //Модуль GSON
        LoggerModule.class,         //Модуль логгера
        LogInterceptorModule.class, //Модуль интерсепотра логгера
        ApiModule.class,            //Модуль Api
        RepositoryModule.class,     //Модуль репозитория
        FieldProviderModule.class   //Модуль интерсептора для переноса аргументов в тело запроса
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
