package com.sanechek.recipecollection.api.data.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    @SerializedName("uri")
    private String uri;

    @SerializedName("text")
    private String text;

    @SerializedName("quantity")
    private float quantity;

    @SerializedName("measure")
    private Measure measure;

    @SerializedName("weight")
    private float weight;

    @SerializedName("food")
    private Food food;

    public String getUri() {
        return uri;
    }

    public String getText() {
        return text;
    }

    public float getQuantity() {
        return quantity;
    }

    public Measure getMeasure() {
        return measure;
    }

    public float getWeight() {
        return weight;
    }

    public Food getFood() {
        return food;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uri);
        parcel.writeString(text);
        parcel.writeFloat(quantity);
        parcel.writeParcelable(measure, i);
        parcel.writeFloat(weight);
        parcel.writeParcelable(food, i);
    }

    protected Ingredient(Parcel in) {
        uri = in.readString();
        text = in.readString();
        quantity = in.readFloat();
        measure = in.readParcelable(Measure.class.getClassLoader());
        weight = in.readFloat();
        food = in.readParcelable(Food.class.getClassLoader());
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    /* Parcelable stuff */
}
