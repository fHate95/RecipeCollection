package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sanechek.recipecollection.api.data.BaseResponse;

import java.util.List;

public class Hits {

    @SerializedName("q")
    @Expose
    private String q;

    @SerializedName("from")
    @Expose
    private int from;

    @SerializedName("to")
    @Expose
    private int to;

    @SerializedName("params")
    @Expose
    private String[][] params;

    @SerializedName("count")
    @Expose
    private long count;

    @SerializedName("more")
    @Expose
    private boolean more;

    @SerializedName("hits")
    @Expose
    private List<Hit> hits;

    public String getQ() {
        return q;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public String[][] getParams() {
        return params;
    }

    public long getCount() {
        return count;
    }

    public boolean isMore() {
        return more;
    }

    public List<Hit> getHits() {
        return hits;
    }
}
