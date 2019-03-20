package com.sanechek.recipecollection.api.data.search;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.sanechek.recipecollection.api.data.BaseResponse;
import com.sanechek.recipecollection.data.FavoriteParent;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Recipe implements Parcelable {

    /* Primary key id */
    private static final String FIELD_ID = "uri";

    @SerializedName("uri")
    private String uri;

    @SerializedName("label")
    private String label;

    @SerializedName("image")
    private String image;

    @SerializedName("source")
    private String source;

    @SerializedName("url")
    private String url;

    @SerializedName("shareAs")
    private String shareAs;

    @SerializedName("yield")
    private float yield;

    @SerializedName("calories")
    private float calories;

    @SerializedName("totalWeight")
    private float totalWeight;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @SerializedName("dietLabels")
    private String[] dietLabels;

    @SerializedName("healthLabels")
    private String[] healthLabels;

    @SerializedName("ingredientLines")
    private String[] ingredientLines;

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

    public float getYield() {
        return yield;
    }

    public float getCalories() {
        return calories;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String[] getDietLabels() {
        return dietLabels;
    }

    public String[] getHealthLabels() {
        return healthLabels;
    }

    public String[] getIngredientLines() {
        return ingredientLines;
    }

    public static String getFieldId() {
        return FIELD_ID;
    }

    public Recipe() { }

    public Recipe(String label, String uri, String url, String[] ingredientLines) {
        this.label = label;
        this.uri = uri;
        this.url = url;
        this.ingredientLines = ingredientLines;
    }

    /* Parcelable stuff */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uri);
        parcel.writeString(label);
        parcel.writeString(image);
        parcel.writeString(source);
        parcel.writeString(url);
        parcel.writeString(shareAs);
        parcel.writeFloat(yield);
        parcel.writeFloat(calories);
        parcel.writeFloat(totalWeight);
        parcel.writeTypedList(ingredients);
        parcel.writeStringArray(dietLabels);
        parcel.writeStringArray(healthLabels);
        parcel.writeStringArray(ingredientLines);
    }

    protected Recipe(Parcel in) {
        uri = in.readString();
        label = in.readString();
        image = in.readString();
        source = in.readString();
        url = in.readString();
        shareAs = in.readString();
        yield = in.readFloat();
        calories = in.readFloat();
        totalWeight = in.readFloat();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        dietLabels = in.createStringArray();
        healthLabels = in.createStringArray();
        ingredientLines = in.createStringArray();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
