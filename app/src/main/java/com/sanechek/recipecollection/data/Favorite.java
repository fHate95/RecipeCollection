package com.sanechek.recipecollection.data;

import com.sanechek.recipecollection.api.data.search.Recipe;
import com.sanechek.recipecollection.util.Utils;

import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Favorite extends RealmObject {

    /* Primary key id */
    private static final String FIELD_ID = "uri";

    @PrimaryKey
    private String uri;
    private String label;
    private String image;
    private String source;
    private String url;
    private String shareAs;
    private int yield;
    private float calories;
    private float totalWeight;
    private RealmList<String> dietLabels;
    private RealmList<String> healthLabels;
    private RealmList<String> ingredientLines;

    public Favorite() {

    }

    public Favorite(Recipe recipe) {
        this.uri = recipe.getUri();
        this.label = recipe.getLabel();
        this.image = recipe.getImage();
        this.source = recipe.getSource();
        this.url = recipe.getUrl();
        this.shareAs = recipe.getShareAs();
        this.yield = recipe.getYield();
        this.calories = recipe.getCalories();
        this.totalWeight = recipe.getTotalWeight();
        this.dietLabels = new RealmList<>();
        this.dietLabels.addAll(Arrays.asList(recipe.getDietLabels()));
        this.healthLabels = new RealmList<>();
        this.healthLabels.addAll(Arrays.asList(recipe.getHealthLabels()));
        this.ingredientLines = new RealmList<>();
        //this.ingredientLines.addAll(Arrays.asList(recipe.getIngredientLines()));
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setShareAs(String shareAs) {
        this.shareAs = shareAs;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public void setTotalWeight(float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void setDietLabels(RealmList<String> dietLabels) {
        this.dietLabels = dietLabels;
    }

    public void setHealthLabels(RealmList<String> healthLabels) {
        this.healthLabels = healthLabels;
    }

    public void setIngredientLines(RealmList<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public static String getFieldId() {
        return FIELD_ID;
    }

    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }

    public String getImage() {
        return image;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public String getShareAs() {
        return shareAs;
    }

    public int getYield() {
        return yield;
    }

    public float getCalories() {
        return calories;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public RealmList<String> getDietLabels() {
        return dietLabels;
    }

    public RealmList<String> getHealthLabels() {
        return healthLabels;
    }

    public RealmList<String> getIngredientLines() {
        return ingredientLines;
    }

    /* Realm stuff */
    public static void create(Realm realm, Favorite item) {
        FavoriteParent parent = realm.where(FavoriteParent.class).findFirst();
        RealmList<Favorite> items = parent.getFavorites();

        Favorite newItem = realm.createObject(Favorite.class, item.getUri());
        newItem = item;
        items.add(newItem);
    }

    public static void delete(Realm realm, String id) {
        Favorite item = realm.where(Favorite.class).equalTo(FIELD_ID, id).findFirst();
        if (item != null) {
            item.deleteFromRealm();
        }
    }
}
