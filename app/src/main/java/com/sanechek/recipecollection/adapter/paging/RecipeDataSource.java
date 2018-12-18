package com.sanechek.recipecollection.adapter.paging;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.paging.PositionalDataSource;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.dialogs.CustomDialog;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.util.DisposableManager;
import com.sanechek.recipecollection.util.KeyProvider;
import com.sanechek.recipecollection.util.Utils;

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
    private RecipeViewModel.CallbackInterface callbackInterface;

    public RecipeDataSource(Context context, LifecycleOwner owner, AppComponent appComponent, String searchQuery, RecipeViewModel.CallbackInterface callbackInterface) {
        this.appComponent = appComponent;
        this.searchQuery = searchQuery;
        this.context = context;
        this.dManager = new DisposableManager(owner);
        this.callbackInterface = callbackInterface;
    }

    /* Начальная загрузка данных */
    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Hit> callback) {
        Log.d(TAG, "loadInitial, requestedStartPosition = " + params.requestedStartPosition +
                ", requestedLoadSize = " + params.requestedLoadSize);

        /* Запрос к API - Поиск рецептов */
        Disposable search = appComponent.getApi().search(searchQuery, 0, params.requestedLoadSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Utils.log(TAG, "Request error: " + throwable.getMessage());
                    callbackInterface.onLoadError();
                    if (throwable.getMessage().contains(KeyProvider.ERR_REQUEST_LIMIT)) {
                        CustomDialog.builder(context)
                                .setTitle(R.string.text_error)
                                .setMessage(R.string.error_request_limit)
                                .setShowOnlyOkBtn(true)
                                .setOnYesBtnClickListener(() -> callbackInterface.onLoadErrorOkPressed())
                                .show();
                    } else {
                        CustomDialog.builder(context)
                                .setTitle(R.string.text_error)
                                .setMessage(R.string.error_unknown)
                                .setShowOnlyOkBtn(true)
                                .setOnYesBtnClickListener(() -> callbackInterface.onLoadErrorOkPressed())
                                .show();
                    }
                })
                .subscribe(((hits, throwable) -> {
                    if (hits != null) {
                        callbackInterface.onDataLoaded(hits.getHits().size());
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
        Disposable search = appComponent.getApi().search(searchQuery, params.startPosition, params.startPosition + params.loadSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    if (throwable.getMessage().contains(KeyProvider.ERR_REQUEST_LIMIT)) {
                        Utils.log(TAG, "Request error: " + throwable.getMessage());
                    }
                })
                .subscribe(((hits, throwable) -> {
                    if (hits != null) {
                        callback.onResult(hits.getHits());
                    } else {
                        Utils.log(TAG, "Error occurred: " + throwable.getMessage());
                    }
                }));
        dManager.disposeOnPause(search);
    }
}