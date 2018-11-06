package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("uri")
    @Expose
    private String uri;

    @SerializedName("quantity")
    @Expose
    private float quantity;

    @SerializedName("measure")
    @Expose
    private Measure measure;

    @SerializedName("weight")
    @Expose
    private float weight;

    @SerializedName("food")
    @Expose
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
