package com.sanechek.recipecollection.api;

import android.support.annotation.CheckResult;

import com.sanechek.recipecollection.api.data.city.City;
import com.sanechek.recipecollection.api.data.city.CityRequest;
import com.sanechek.recipecollection.api.data.city.CityResponse;
import com.sanechek.recipecollection.api.data.search.Hits;
import com.sanechek.recipecollection.api.data.search.SearchRequest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FoodInternal {

    @CheckResult
    @GET("search")
    Single<Hits> search(@Body SearchRequest request);

    @CheckResult
    @GET("public-api/v1.2/locations")
    Single<List<City>> getCity(@Query("lang") String lang);

    @CheckResult
    @GET("public-api/v1.2/locations")
    Single<List<City>> getCity();
}
