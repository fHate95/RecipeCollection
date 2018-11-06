package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutrientInfo {

    @SerializedName("uri")
    @Expose
    private String uri;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("quantity")
    @Expose
    private float quantity;

    @SerializedName("unit")
    @Expose
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
