package com.sanechek.recipecollection.api;

import android.support.annotation.CheckResult;
import com.sanechek.recipecollection.api.data.search.Hits;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/* Api интерфейс */
public interface FoodApi {

    String ARG_QUERY = "q";             //Текст поиска для запроса
    String ARG_APP_ID = "app_id";       //APP_ID (предоставляется в ЛК api)
    String ARG_APP_KEY = "app_key";     //APP_KEY (предоставляется в ЛК api)
    String ARG_FROM = "from";           //Поиск с {позиции)
    String ARG_TO = "to";               //Поиск до {позиции)

    String ARG_INGR = "ingr";           //Максимальное кол-во ингридиентов
    String ARG_DIET = "diet";           //Метка диеты (“balanced”, “high-protein”, “high-fiber”, “low-fat”, “low-carb”, “low-sodium”)
    String ARG_HEALTH = "health";       //Метка здорового питания(“vegetarian”, “low-sugar”, “pork-free”, “No-oil-added”, “low-fat”, etc.)
    String ARG_CALORIES = "calories";    //Кол-во калорий в диапазоне a-b

    /* GET запрос - поиск рецептов */
    @CheckResult
    @GET("search")
    Single<Hits> search(@Query(ARG_QUERY) String query, @Query(ARG_FROM) int from, @Query(ARG_TO) int to);

    @CheckResult
    @GET("search")
    Single<Hits> search(@Query(ARG_QUERY) String query, @Query(ARG_FROM) int from, @Query(ARG_TO) int to,
                        @Query(ARG_INGR) int ingr, @Query(ARG_DIET) String diet, @Query(ARG_HEALTH) String health,
                        @Query(ARG_CALORIES) String calories);

}
