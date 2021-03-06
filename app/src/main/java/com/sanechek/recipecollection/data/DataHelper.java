package com.sanechek.recipecollection.data;


import android.support.annotation.NonNull;

import com.sanechek.recipecollection.util.Utils;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class DataHelper {

    public static void createOrUpdateSettings(final AppSettings item) {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.copyToRealmOrUpdate(item));
    }

    @NonNull
    public static AppSettings getAppSettings() {
        AppSettings settings = Realm.getDefaultInstance().where(AppSettings.class).findFirst();
        if (settings != null) {
            return settings;
        } else {
            return new AppSettings();
        }
    }


    public static void clearAppSettings() {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.where(AppSettings.class).findAll().deleteAllFromRealm());
    }

    public static void addFavorite(Realm realm, final Favorite item) {
        realm.executeTransaction(realm1 -> Favorite.create(realm1, item));
    }

    public static void deleteFavorite(Realm realm, String id) {
        realm.executeTransaction(realm1 -> Favorite.delete(realm1, id));
    }

    public static void deleteAllFavorites(Realm realm) {
        realm.executeTransaction(realm1 -> {
            FavoriteParent parent = realm.where(FavoriteParent.class).findFirst();
            RealmList<Favorite> items = parent.getFavorites();
            items.removeAll(items);
        });
    }

    public static ArrayList<Favorite> getFavorites(Realm realm) {
        ArrayList<Favorite> items = new ArrayList<>();
        try {
            RealmList realmList = realm.where(FavoriteParent.class).findFirst().getFavorites();
            items.addAll(realmList);
        } catch (Exception e) {
            Utils.log("TAG_REALM", "'get' error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    public static Favorite getFavoriteById(Realm realm, String id) {
        return realm.where(Favorite.class).equalTo(Favorite.getFieldId(), id).findFirst();
    }

    public static void addMenu(Realm realm, final Menu item) {
        realm.executeTransaction(realm1 -> Menu.create(realm1, item));
    }

    public static void deleteMenu(Realm realm, final Menu item) {
        realm.executeTransaction(realm1 -> Menu.delete(realm1, item.getDay()));
    }

    public static void clearMenus(Realm realm) {
        realm.executeTransaction(realm1 -> realm1.where(Menu.class).findAll().deleteAllFromRealm());
    }

    public static ArrayList<Menu> getMenu(Realm realm) {
        RealmResults<Menu> realmResults = Realm.getDefaultInstance().where(Menu.class).findAll();
        return new ArrayList<>(realmResults);
    }

}
