package com.sanechek.recipecollection.api.utils;


import android.content.Context;

import com.google.gson.Gson;
import com.sanechek.recipecollection.BuildConfig;
import com.sanechek.recipecollection.api.FoodApi;
import com.sanechek.recipecollection.api.FoodInternal;
import com.sanechek.recipecollection.api.data.BaseRequest;
import com.sanechek.recipecollection.api.data.BaseResponse;
import com.sanechek.recipecollection.api.data.city.City;
import com.sanechek.recipecollection.api.data.city.CityRequest;
import com.sanechek.recipecollection.api.data.city.CityResponse;
import com.sanechek.recipecollection.api.data.search.Hits;
import com.sanechek.recipecollection.api.data.search.SearchRequest;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Делегат для апи. Оборачивет поля для некоторых запросов в объект.
 * Требуется для Retrofit, т.к. он реализует все методы интерфейса, в т.ч. и дефолтные.
 */
public class ApiDelegate implements FoodApi {
    private static final String TAG = "ApiDelegate";

    private final FoodInternal internal;

    private iProcessor socketProcessor;
    private iProcessor webProcessor;

    public ApiDelegate(FoodInternal internal, Context context) {
        this.internal = internal;
    }

    protected <Rsp extends BaseResponse, Req extends BaseRequest<Rsp>> Single<Rsp> processInternal(Req request) {
//        request.setSubject(DataHelper.getAppSettings().getSubject());

        this.webProcessor = new WebProcessor(new Gson(), BuildConfig.SERVER_URL);
//        this.socketProcessor = new SocketProcessor(context, security.getServerUrl(), new Gson(), security);

        //return Single.fromCallable(()->socketProcessor.process(request)).onErrorResumeNext(Single.fromCallable(()->webProcessor.process(request)))
        return Single.fromCallable(() -> webProcessor.process(request))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnError(throwable -> )
                .onErrorResumeNext(Single.never());
    }

    @Override
    public Single<Hits> search(String query, int from, int to, String appId, String appKey) {
        SearchRequest request = new SearchRequest();
        request.setQ(query);
        request.setFrom(from);
        request.setTo(to);
        request.setAppId(appId);
        request.setAppKey(appKey);
        //return processInternal(request);
        return internal.search(request);
    }

    @Override
    public Single<List<City>> getCity(String lang) {
        CityRequest request = new CityRequest();
        request.setLang(lang);
        return internal.getCity(lang);
        //return processInternal(request);
    }

    @Override
    public Single<List<City>> getCity() {
        return internal.getCity();
    }
}
