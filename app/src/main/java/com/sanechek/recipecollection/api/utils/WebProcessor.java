package com.sanechek.recipecollection.api.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.sanechek.recipecollection.api.data.BaseRequest;
import com.sanechek.recipecollection.api.data.BaseResponse;

import org.apache.commons.io.IOUtils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.NoSuchElementException;

public class WebProcessor implements iProcessor {

    private final Gson gson;

    private final URL url;

    public WebProcessor(Gson gson, String url) {
        this.gson = gson;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public <Rsp extends BaseResponse, Req extends BaseRequest<Rsp>> Rsp process(Req request) throws Exception {
        String requestJson = gson.toJson(request);
        Log.v("TAG_CONNECTION", "URL: " + requestJson);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setConnectTimeout(5000);

        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestProperty("X-Timeout", "120");
//        connection.setRequestProperty("Accept-Language", Locale.getDefault().getLanguage());

        connection.connect();

        OutputStream oStream = connection.getOutputStream();
        oStream.write(requestJson.getBytes());
        oStream.flush();
        oStream.close();

        Log.v("TAG_CONNECTION", "URL: " + requestJson);

        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new Exception("Api unavailable");
        }

        String rspBody = IOUtils.toString(connection.getInputStream(), "utf-8");
        Rsp result = gson.fromJson(rspBody, request.getResponseClass());

        if (result == null) {
            throw new NoSuchElementException("No value present");
        }

        return result;
    }
}
