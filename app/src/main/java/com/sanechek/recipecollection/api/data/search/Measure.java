package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.SerializedName;

public class Measure {

    @SerializedName("uri")
    private String uri;

    @SerializedName("label")
    private String label;

    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }
}
