package com.sanechek.recipecollection.log;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class LoggerData implements Parcelable {
    @SerializedName("request")
    String request;
    @SerializedName("requestUrl")
    String requestUrl;
    @SerializedName("response")
    String response;
    @SerializedName("response_code")
    int responseCode;
    @SerializedName("request_date")
    Date date;
    @SerializedName("response_date")
    Date endDate;
    @SerializedName("is_socket_used")
    boolean isSocketUsed;

    public LoggerData() {
    }

    protected LoggerData(Parcel in) {
        request = in.readString();
        requestUrl = in.readString();
        response = in.readString();
        responseCode = in.readInt();
        isSocketUsed = in.readByte() != 0;
        date = new Date(in.readLong());
        endDate = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(request);
        dest.writeString(requestUrl);
        dest.writeString(response);
        dest.writeInt(responseCode);
        dest.writeByte((byte) (isSocketUsed ? 1 : 0));
        dest.writeLong(date.getTime());
        dest.writeLong(endDate.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoggerData> CREATOR = new Creator<LoggerData>() {
        @Override
        public LoggerData createFromParcel(Parcel in) {
            return new LoggerData(in);
        }

        @Override
        public LoggerData[] newArray(int size) {
            return new LoggerData[size];
        }
    };

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public boolean isSocketUsed() {
        return isSocketUsed;
    }

    public void setSocketUsed(boolean socketUsed) {
        isSocketUsed = socketUsed;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Date getDate() {
        return date;
    }

    public void setSendDate(Date date) {
        this.date = date;
    }

    public void setSendDate(long l){
        this.date = new Date(l);
    }

    @Override
    public String toString() {
        return "RequestDate="+date+"\t EndDate="+endDate+"\t Time="+((date!=null && endDate!=null)? (endDate.getTime()-date.getTime()):0)+"\t RequestUrl=\""+requestUrl+"\"\t Request=\""+request+"\"\t responseCode="+responseCode+"\t response=\""+response+"\"";
    }

    public void setEndDate(long l) {
        this.endDate = new Date(l);
    }
}
