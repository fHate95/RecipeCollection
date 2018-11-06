package com.sanechek.recipecollection.injection;

import android.app.Application;

import com.sanechek.recipecollection.util.Repository;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {
        AppModule.class,
        RepositoryModule.class
})

@Singleton
public interface AppComponent {

    @Singleton
    Application getApplication();

    @Singleton
    Repository getRepository();

}
