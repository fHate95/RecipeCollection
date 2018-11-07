package com.sanechek.recipecollection.api;

import android.support.annotation.CheckResult;

import com.sanechek.recipecollection.api.data.search.Hits;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.sanechek.recipecollection.api.FoodApi.*;

public interface FoodInternal {

    @CheckResult
    @GET("search")
    Single<Hits> search(@Query(ARG_QUERY) String query, @Query(ARG_FROM) int from, @Query(ARG_TO) int to, @Query(ARG_APP_ID) String appId, @Query(ARG_APP_KEY) String appKey);
}
