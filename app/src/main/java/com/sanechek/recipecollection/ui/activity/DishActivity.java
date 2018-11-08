package com.sanechek.recipecollection.ui.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sanechek.recipecollection.BaseActivity;
import com.sanechek.recipecollection.BuildConfig;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.adapter.PagingAdapter;
import com.sanechek.recipecollection.adapter.RecipeAdapter;
import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.api.utils.KeyProvider;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.paging.MainThreadExecutor;
import com.sanechek.recipecollection.paging.RecipeDataSource;
import com.sanechek.recipecollection.paging.RecipeDataSourceFactory;
import com.sanechek.recipecollection.paging.RecipeViewModel;
import com.sanechek.recipecollection.util.DisposableManager;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/* Screen with list of selected dish typed recipes */
public class DishActivity extends BaseActivity {

    @BindView(R.id.rv_recipes) RecyclerView rvRecipes;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    private Realm realm;
    private AppComponent appComponent;
    private final DisposableManager dManager = new DisposableManager(this);

    private String searchQuery = "fish";

    private RecipeAdapter adapter;
    private PagingAdapter pagingAdapter;

    private RecipeViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        ButterKnife.bind(this);

        appComponent = getAppComponent(this);
        realm = Realm.getDefaultInstance();

        if (getIntent() != null) {
            searchQuery = getIntent().getStringExtra(KeyProvider.KEY_SEARCH_QUERY);
        } else {
            Toast.makeText(this, "Error: query is empty", Toast.LENGTH_SHORT).show();
        }

        /* Configure paging list */
//        RecipeDataSource dataSource = new RecipeDataSource(DishActivity.this, this, appComponent, searchQuery);

        pagingAdapter = new PagingAdapter(new DiffUtil.ItemCallback<Hit>() {
            @Override
            public boolean areItemsTheSame(@NonNull Hit hit, @NonNull Hit t1) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Hit hit, @NonNull Hit t1) {
                return false;
            }
        }, this, new PagingAdapter.AdapterClickListener() {
            @Override
            public void onItemClick(Hit item) {
                Intent intent = new Intent(DishActivity.this, RecipeDetailActivity.class);
                intent.putExtra(KeyProvider.KEY_RECIPE, item.getRecipe());
                startActivity(intent);
            }
        });

        //viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        viewModel = new RecipeViewModel(DishActivity.this, this, appComponent, searchQuery);

        viewModel.getUiList().observe(this, new Observer<PagedList<Hit>>() {
            @Override
            public void onChanged(@Nullable PagedList<Hit> hits) {
                pagingAdapter.submitList(hits);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRecipes.getContext(),
                LinearLayout.VERTICAL);
        rvRecipes.addItemDecoration(dividerItemDecoration);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        rvRecipes.setAdapter(pagingAdapter);

        viewModel.refresh();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refresh();
        });
    }

    private void setRecyclerView(final Context context, final ArrayList<Hit> list) {
        adapter = new RecipeAdapter(context, list, new RecipeAdapter.AdapterClickListener() {
            @Override
            public void onItemClick(Hit item) {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(KeyProvider.KEY_RECIPE, item.getRecipe());
                startActivity(intent);
            }
        }, realm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRecipes.getContext(),
                LinearLayout.VERTICAL);
        rvRecipes.addItemDecoration(dividerItemDecoration);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        rvRecipes.setAdapter(adapter);
    }

    private void getRecipes() {
        ProgressDialog dialog = new ProgressDialog(this);

        Disposable get = appComponent.getApi().search(searchQuery, 0, 8, BuildConfig.APP_ID, BuildConfig.APP_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> dialog.show())
                .doFinally(dialog::dismiss)
                        .doOnError(throwable -> {
                            Toast.makeText(this, "Error occurred: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        })
                .subscribe(((hits, throwable) -> {
                            if (hits != null) {
                                setRecyclerView(this, hits.getHits());
                            } else {
                                Toast.makeText(this, "Error occurred: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                }));
        dManager.disposeOnPause(get);
    }
}
