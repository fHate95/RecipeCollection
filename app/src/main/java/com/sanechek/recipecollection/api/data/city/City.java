package com.sanechek.recipecollection.api.data.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("name")
    @Expose
    private String name;


    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }
}
