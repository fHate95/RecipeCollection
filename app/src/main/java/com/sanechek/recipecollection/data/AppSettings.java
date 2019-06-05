package com.sanechek.recipecollection.data;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AppSettings extends RealmObject {

    public static final String PRIMARY_KEY_FIELD = "id";

    @PrimaryKey
    private String id = "AppSettings";

    private int calories = -1;

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        Realm.getDefaultInstance().executeTransaction(realm -> this.calories = calories);
    }

    public boolean isCaloriesSetted() {
        return calories != -1;
    }
}
