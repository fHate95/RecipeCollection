package com.sanechek.recipecollection.data;


import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Menu extends RealmObject {

    public static final String PRIMARY_KEY = "day";

    @PrimaryKey
    private int day;
    private Favorite breakfast;
    private Favorite lunch;
    private Favorite dinner;

    public int getDay() {
        return day;
    }

    public Favorite getBreakfast() {
        return breakfast;
    }

    public Favorite getLunch() {
        return lunch;
    }

    public Favorite getDinner() {
        return dinner;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setBreakfast(Favorite breakfast) {
        this.breakfast = breakfast;
    }

    public void setLunch(Favorite lunch) {
        this.lunch = lunch;
    }

    public void setDinner(Favorite dinner) {
        this.dinner = dinner;
    }

    /* Realm stuff */
    public static void create(Realm realm, Menu item) {
        realm.copyToRealmOrUpdate(item);
    }

    public static void delete(Realm realm, int id) {
        Menu item = realm.where(Menu.class).equalTo(PRIMARY_KEY, id).findFirst();
        if (item != null) {
            item.deleteFromRealm();
        }
    }
}
