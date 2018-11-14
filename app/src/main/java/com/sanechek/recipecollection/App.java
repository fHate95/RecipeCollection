package com.sanechek.recipecollection;

import android.support.multidex.MultiDexApplication;

import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.injection.modules.AppModule;
import com.sanechek.recipecollection.injection.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/* App класс приложения.
* использование данного класса вместо дефолтного Application
 * указывается в манифесте для application тэга */

public class App extends MultiDexApplication {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //MultiDex.install(this);

        /* Инициализация компонента приложения */
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this)).build();
        /* Инициализация Realm */
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                //.initialData(realm -> realm.createObject(DataParent.class))
                .deleteRealmIfMigrationNeeded()
                .build();
        //Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
    }
    /* Получение компонента приложения
    * Логика получения через context описана в кастомных Activity и Fragment классах */
    public AppComponent getAppComponent() {
        return appComponent;
    }

}

