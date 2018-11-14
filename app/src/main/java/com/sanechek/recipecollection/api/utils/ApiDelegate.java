package com.sanechek.recipecollection.api.utils;

import com.sanechek.recipecollection.api.FoodApi;
import com.sanechek.recipecollection.api.FoodInternal;
import com.sanechek.recipecollection.api.data.search.Hits;

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
    public Single<Hits> search(String query, int from, int to, String appId, String appKey) {
        return internal.search(query, from, to, appId, appKey);
    }
}
