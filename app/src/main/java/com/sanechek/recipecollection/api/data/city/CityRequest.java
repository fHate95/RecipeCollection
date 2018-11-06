package com.sanechek.recipecollection.api.data.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sanechek.recipecollection.api.data.BaseRequest;

public class CityRequest extends BaseRequest<CityResponse> {

    @SerializedName("lang")
    private String lang;

    public CityRequest() {
        super("public-api/v1.2/locations");
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public Class<CityResponse> getResponseClass() {
        return CityResponse.class;
    }
}
