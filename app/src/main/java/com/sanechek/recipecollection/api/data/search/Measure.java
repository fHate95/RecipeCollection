package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Measure {

    @SerializedName("uri")
    @Expose
    private String uri;

    @SerializedName("label")
    @Expose
    private String label;

    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }
}
