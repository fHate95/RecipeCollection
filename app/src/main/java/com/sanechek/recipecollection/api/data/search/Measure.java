package com.sanechek.recipecollection.api.data.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Measure implements Parcelable {

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

    protected Measure(Parcel in) {
        uri = in.readString();
        label = in.readString();
    }

    public static final Creator<Measure> CREATOR = new Creator<Measure>() {
        @Override
        public Measure createFromParcel(Parcel in) {
            return new Measure(in);
        }

        @Override
        public Measure[] newArray(int size) {
            return new Measure[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uri);
        parcel.writeString(label);
    }
}
