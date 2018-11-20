package com.sanechek.recipecollection.api.utils;

import com.sanechek.recipecollection.BuildConfig;
import com.sanechek.recipecollection.api.FoodApi;
import com.sanechek.recipecollection.api.FoodInternal;
import com.sanechek.recipecollection.api.data.search.Hits;
import com.sanechek.recipecollection.util.Utils;

import io.reactivex.Single;

/* Делегат для апи. Оборачивет поля для некоторых запросов в объект. (На данном этапе обёртка не используется)
 * Требуется для Retrofit, т.к. он реализует все методы интерфейса, включая дефолтные. */
public class ApiDelegate implements FoodApi {

    private static final String TAG = "ApiDelegate";

    private final FoodInternal internal;

    public ApiDelegate(FoodInternal internal) {
        this.internal = internal;
    }

    @Override
    public Single<Hits> search(String query, int from, int to) {
        Utils.log(TAG, "search api call with params: query = " + query + ", from = " + from
        + ", to = " + to + ", app_id = " + BuildConfig.APP_ID + ", app_key = " + BuildConfig.APP_KEY);

        return internal.search(query, from, to, BuildConfig.APP_ID, BuildConfig.APP_KEY);
    }
}
