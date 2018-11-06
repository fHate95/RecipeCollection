package com.sanechek.recipecollection.api;

import android.support.annotation.CheckResult;

import com.sanechek.recipecollection.BuildConfig;
import com.sanechek.recipecollection.api.data.city.City;
import com.sanechek.recipecollection.api.data.city.CityResponse;
import com.sanechek.recipecollection.api.data.search.Hits;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {

    String ARG_QUERY = "q";
    String ARG_APP_ID = "app_id";
    String ARG_APP_KEY = "app_key";
    String ARG_FROM = "from";
    String ARG_TO = "to";

    @CheckResult
    @GET("search")
    Single<Hits> search(@Query(ARG_QUERY) String query, @Query(ARG_FROM) int from, @Query(ARG_TO) int to, @Query(ARG_APP_ID) String appId, @Query(ARG_APP_KEY) String appKey);

    @CheckResult
    @GET("public-api/v1.2/locations")
    Single<List<City>> getCity(@Query("lang") String lang);

    @CheckResult
    @GET("public-api/v1.2/locations")
    Single<List<City>> getCity();

}
