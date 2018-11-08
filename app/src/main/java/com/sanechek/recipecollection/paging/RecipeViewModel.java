package com.sanechek.recipecollection.paging;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.Nullable;

import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.injection.AppComponent;

import java.util.concurrent.Executors;

public class RecipeViewModel extends ViewModel {

    private Context context;

    public RecipeViewModel(Context context, LifecycleOwner owner, AppComponent appComponent, String searchQuery) {
        factory = new RecipeDataSourceFactory(context, owner, appComponent, searchQuery);
        this.context = context;
    }

    MediatorLiveData<PagedList<Hit>> uiList = new MediatorLiveData<PagedList<Hit>>();
    LiveData<PagedList<Hit>> pagedList;

    private RecipeDataSourceFactory factory;

    private PagedList.Config config = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .build();

    public void refresh() {
        if (pagedList != null) {
            factory.getDataSource().invalidate();
        } else {
//            ProgressDialog dialog = new ProgressDialog(context);
//            dialog.show();

            pagedList = new LivePagedListBuilder<>(factory, config).build();
            uiList.addSource(pagedList, hits -> {
//                dialog.dismiss();
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
