package com.sanechek.recipecollection;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.injection.modules.AppModule;
import com.sanechek.recipecollection.injection.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends MultiDexApplication {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //MultiDex.install(this);

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this)).build();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                //.initialData(realm -> realm.createObject(DataParent.class))
                .deleteRealmIfMigrationNeeded()
                .build();
        //Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static AppComponent getAppComponent(Context context) {
        if (context instanceof App) {
            return ((App) context).getAppComponent();
        } else {
            return getAppComponent(context.getApplicationContext());
        }
    }

}

