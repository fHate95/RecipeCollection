package com.sanechek.recipecollection.api.data;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public abstract class BaseRequest<T extends BaseResponse>{

    @SerializedName("request")
    private final String request;

    protected BaseRequest(String request) {
        this.request = request;
    }

    public abstract Class<T> getResponseClass();

    public final String getRequest(){
        return request;
    }
}
