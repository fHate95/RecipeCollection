package com.sanechek.recipecollection.injection.modules;

import com.sanechek.recipecollection.api.FoodApi;
import com.sanechek.recipecollection.api.FoodInternal;
import com.sanechek.recipecollection.api.utils.ApiDelegate;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/* Api модуль. Провайдит FoodApi интерфейс  */
@Module
public class ApiModule {

    @Provides
    @Singleton
    @Inject
    public FoodApi provideApi(Retrofit retrofit) {
        return new ApiDelegate(retrofit.create(FoodInternal.class));
    }
}
