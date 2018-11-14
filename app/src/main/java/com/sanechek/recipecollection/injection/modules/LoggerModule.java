package com.sanechek.recipecollection.injection.modules;

import com.sanechek.recipecollection.log.Logger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/* provide logger */
@Module
public class LoggerModule {

    @Provides
    @Singleton
    public Logger provideLogger(){
        return new Logger();
    }
}
