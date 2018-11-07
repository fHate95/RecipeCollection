package com.sanechek.recipecollection.api.utils;

import com.sanechek.recipecollection.api.FoodApi;
import com.sanechek.recipecollection.api.FoodInternal;
import com.sanechek.recipecollection.api.data.search.Hits;

import io.reactivex.Single;

/**
 * Api delegate. Can wrap some requests into request object if needed.
 * Need for Retrofit, it's realizes all api interface methods.
 */
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
