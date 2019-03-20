package com.sanechek.recipecollection.api;

import android.support.annotation.CheckResult;

import com.sanechek.recipecollection.api.data.search.Hits;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.sanechek.recipecollection.api.FoodApi.*;

/* Внутренний интерфейс API - делагат выполняет запрос отсюда
* В ответе получаем Single ответа сервера (Reactive) */
public interface FoodInternal {

    @CheckResult
    @GET("search")
    Single<Hits> search(@Query(ARG_QUERY) String query, @Query(ARG_FROM) int from, @Query(ARG_TO) int to, @Query(ARG_APP_ID) String appId, @Query(ARG_APP_KEY) String appKey);

    @CheckResult
    @GET("search")
    Single<Hits> search(@Query(ARG_QUERY) String query, @Query(ARG_FROM) int from, @Query(ARG_TO) int to,
                        @Query(ARG_INGR) int ingr, @Query(ARG_DIET) String diet, @Query(ARG_HEALTH) String health,
                        @Query(ARG_CALORIES) String calories,
                        @Query(ARG_APP_ID) String appId, @Query(ARG_APP_KEY) String appKey);

    @CheckResult
    @GET("search")
    Single<Hits> search(@Query(ARG_QUERY) String query, @Query(ARG_FROM) int from, @Query(ARG_TO) int to,
                        @Query(ARG_DIET) String diet, @Query(ARG_HEALTH) String health,
                        @Query(ARG_CALORIES) String calories,
                        @Query(ARG_APP_ID) String appId, @Query(ARG_APP_KEY) String appKey);
}
