package com.sanechek.recipecollection.api.data.search;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Hits {

    @SerializedName("q")
    private String q;

    @SerializedName("from")
    private int from;

    @SerializedName("to")
    private int to;

//    @SerializedName("params")
//    @Expose
//    private String[][] params;

    @SerializedName("count")
    private long count;

    @SerializedName("more")
    private boolean more;

    @SerializedName("hits")
    private ArrayList<Hit> hits;

    public String getQ() {
        return q;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

//    public String[][] getParams() {
//        return params;
//    }

    public long getCount() {
        return count;
    }

    public boolean isMore() {
        return more;
    }

    public ArrayList<Hit> getHits() {
        return hits;
    }
}
