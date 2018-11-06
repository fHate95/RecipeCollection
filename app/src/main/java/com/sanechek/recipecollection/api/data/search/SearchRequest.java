package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.SerializedName;

public class SearchRequest {

    @SerializedName("q")
    private String q;

    @SerializedName("from")
    private int from;

    @SerializedName("to")
    private int to;

    @SerializedName("app_id")
    private String appId;

    @SerializedName("app_key")
    private String appKey;

    public SearchRequest() {

    }

    public void setQ(String q) {
        this.q = q;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
