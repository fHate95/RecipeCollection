package com.sanechek.recipecollection.api;

import android.support.annotation.CheckResult;
import com.sanechek.recipecollection.api.data.search.Hits;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/* Api интерфейс */
public interface FoodApi {

    String ARG_QUERY = "q";         //Текст поиска для запроса
    String ARG_APP_ID = "app_id";   //APP_ID (предоставляется в ЛК api)
    String ARG_APP_KEY = "app_key"; //APP_KEY (предоставляется в ЛК api)
    String ARG_FROM = "from";       //Поиск с {позиции)
    String ARG_TO = "to";           //Поиск до {позиции)

    /* GET запрос - поиск рецептов */
    @CheckResult
    @GET("search")
    Single<Hits> search(@Query(ARG_QUERY) String query, @Query(ARG_FROM) int from, @Query(ARG_TO) int to, @Query(ARG_APP_ID) String appId, @Query(ARG_APP_KEY) String appKey);

}
