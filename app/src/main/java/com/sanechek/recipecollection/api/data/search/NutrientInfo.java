package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutrientInfo {

    @SerializedName("uri")
    private String uri;

    @SerializedName("label")
    private String label;

    @SerializedName("quantity")
    private float quantity;

    @SerializedName("unit")
    private String unit;

    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
