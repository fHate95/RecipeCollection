package com.sanechek.recipecollection.api.data.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sanechek.recipecollection.api.data.BaseResponse;

import java.util.List;

public class Recipe implements Parcelable {

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

    @SerializedName("yield")
    private int yield;

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

    protected Recipe(Parcel in) {
        uri = in.readString();
        label = in.readString();
        image = in.readString();
        source = in.readString();
        url = in.readString();
        yield = in.readInt();
        calories = in.readFloat();
        totalWeight = in.readFloat();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        dietLabels = in.createStringArray();
        healthLabels = in.createStringArray();
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

    public int getYield() {
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
        parcel.writeInt(yield);
        parcel.writeFloat(calories);
        parcel.writeFloat(totalWeight);
        parcel.writeTypedList(ingredients);
        parcel.writeStringArray(dietLabels);
        parcel.writeStringArray(healthLabels);
    }

    /* Parcelable stuff */
}
