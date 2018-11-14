package com.sanechek.recipecollection.paging;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.paging.PositionalDataSource;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.sanechek.recipecollection.BuildConfig;
import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.util.DisposableManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/* DataSource ответа от сервера, по сути - элемента рецепта */
public class RecipeDataSource extends PositionalDataSource<Hit> {

    private final String TAG = "RecipeDataSource";

    private final DisposableManager dManager;

    private AppComponent appComponent;
    private Context context;
    private String searchQuery;

    public RecipeDataSource(Context context, LifecycleOwner owner, AppComponent appComponent, String searchQuery) {
        this.appComponent = appComponent;
        this.searchQuery = searchQuery;
        this.context = context;
        this.dManager = new DisposableManager(owner);
    }

    /* Начальная загрузка данных */
    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Hit> callback) {
        Log.d(TAG, "loadInitial, requestedStartPosition = " + params.requestedStartPosition +
                ", requestedLoadSize = " + params.requestedLoadSize);

        /* Запрос к API - Поиск рецептов */
        Disposable search = appComponent.getApi().search(searchQuery, 0, 10, BuildConfig.APP_ID, BuildConfig.APP_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    if (throwable.getMessage().contains("HTTP 401")) {
                        Toast.makeText(context, "Error: Request limit exceeded." + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(((hits, throwable) -> {
                    if (hits != null) {
                        callback.onResult(hits.getHits(), 0);
                    }
                }));
        dManager.disposeOnPause(search);
    }

    /** Дозагрузка данных при пролистывании списка
     * @param params - параметры, содержащие позицию начала, и кол-во элементов
     * @param callback - callback передающий данные в адаптер */
    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Hit> callback) {
        Log.d(TAG, "loadRange, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize);

        /* Запрос к API - Поиск рецептов */
        Disposable search = appComponent.getApi().search(searchQuery, params.startPosition, params.startPosition + params.loadSize, BuildConfig.APP_ID, BuildConfig.APP_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    if (throwable.getMessage().contains("HTTP 401")) {
                        Toast.makeText(context, "Error: Request limit exceeded." + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(((hits, throwable) -> {
                    if (hits != null) {
                        callback.onResult(hits.getHits());
                    } else {
                        Log.v(TAG, "Error occurred: " + throwable.getMessage());
                    }
                }));
        dManager.disposeOnPause(search);
    }
}