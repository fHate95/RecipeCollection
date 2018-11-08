package com.sanechek.recipecollection.paging;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.paging.DataSource;
import android.content.Context;

import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.injection.AppComponent;

public class RecipeDataSourceFactory extends DataSource.Factory<Integer, Hit> {

    private RecipeDataSource dataSource;

    private Context context;
    private LifecycleOwner owner;
    private AppComponent appComponent;
    private String searchQuery;

    public RecipeDataSourceFactory(Context context, LifecycleOwner owner, AppComponent appComponent, String searchQuery) {
        this.context = context;
        this.owner = owner;
        this.appComponent = appComponent;
        this.searchQuery = searchQuery;
    }

    @Override
    public DataSource<Integer, Hit> create() {
        dataSource = new RecipeDataSource(context, owner, appComponent, searchQuery);
        return dataSource;
    }

    public RecipeDataSource getDataSource() {
        return dataSource;
    }
}
