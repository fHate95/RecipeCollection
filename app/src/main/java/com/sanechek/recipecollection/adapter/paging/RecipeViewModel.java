package com.sanechek.recipecollection.adapter.paging;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;

import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.injection.AppComponent;


/* ViewModel - настраивает конфигурацию пагинируемого листа */
public class RecipeViewModel extends ViewModel {

    private RecipeDataSourceFactory factory;

    private CallbackInterface callbackInterface;
    public interface CallbackInterface {
        void onDataLoaded(int size);
        void onLoadError();
        void onLoadErrorOkPressed();
    }

    public RecipeViewModel(Context context, LifecycleOwner owner, AppComponent appComponent, String searchQuery, CallbackInterface callbackInterface) {
        factory = new RecipeDataSourceFactory(context, owner, appComponent, searchQuery, callbackInterface);
    }

    private MediatorLiveData<PagedList<Hit>> uiList = new MediatorLiveData<PagedList<Hit>>();
    private LiveData<PagedList<Hit>> pagedList;

    private PagedList.Config config = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(15)
            .setInitialLoadSizeHint(15)
            .build();

    public void refresh() {
        if (pagedList != null) {
            factory.getDataSource().invalidate();
        } else {
            pagedList = new LivePagedListBuilder<>(factory, config).build();
            uiList.addSource(pagedList, hits -> {
                uiList.setValue(hits);
            });
        }
    }

    public MediatorLiveData<PagedList<Hit>> getUiList() {
        return uiList;
    }

    public RecipeDataSourceFactory getFactory() {
        return factory;
    }

    public PagedList.Config getConfig() {
        return config;
    }
}
