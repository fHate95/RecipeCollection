package com.sanechek.recipecollection.api.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import com.google.gson.annotations.SerializedName;


public class BaseResponse implements Parcelable {

    public static final Creator<BaseResponse> CREATOR = new Creator<BaseResponse>() {
        @Override
        public BaseResponse createFromParcel(Parcel in) {
            return new BaseResponse(in);
        }

        @Override
        public BaseResponse[] newArray(int size) {
            return new BaseResponse[size];
        }
    };

    private static final String F_NAME_REPLY = "Reply";

    @SerializedName(F_NAME_REPLY)
    private String reply;

    protected BaseResponse(Parcel source) {
        reply = source.readString();
    }

    public BaseResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    @CallSuper
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reply);
    }
}
