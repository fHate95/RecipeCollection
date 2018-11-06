package com.sanechek.recipecollection.api.data.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sanechek.recipecollection.api.data.BaseResponse;

import java.util.List;

public class CityResponse extends BaseResponse {

    @SerializedName("slug")
    List<City> cities;

    public List<City> getCities() {
        return cities;
    }
}
