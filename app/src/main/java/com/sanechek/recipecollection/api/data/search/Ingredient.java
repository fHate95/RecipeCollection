package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("uri")
    private String uri;

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
}
