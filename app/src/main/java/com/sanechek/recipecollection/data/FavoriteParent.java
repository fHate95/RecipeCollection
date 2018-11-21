package com.sanechek.recipecollection.data;

import io.realm.RealmList;
import io.realm.RealmObject;

public class FavoriteParent extends RealmObject {

    @SuppressWarnings("unused")
    private RealmList<Favorite> favorites;

    public RealmList<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(RealmList<Favorite> favorites) {
        this.favorites = favorites;
    }

}
