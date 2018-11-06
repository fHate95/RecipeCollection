package com.sanechek.recipecollection.injection.modules;

import android.content.Context;

import com.sanechek.recipecollection.util.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    @Inject
    public Repository provideRepository(Context context) {
        return new Repository(context);
    }

}
