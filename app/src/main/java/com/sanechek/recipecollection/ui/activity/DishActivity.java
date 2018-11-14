package com.sanechek.recipecollection.ui.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
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
import com.sanechek.recipecollection.util.KeyProvider;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.paging.RecipeViewModel;
import com.sanechek.recipecollection.util.DisposableManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/* Экран со списком рецептов по запросу */
public class DishActivity extends BaseActivity {

    @BindView(R.id.rv_recipes)
    RecyclerView rvRecipes;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private AppComponent appComponent;

    private String searchQuery = "fish";

    private PagingAdapter pagingAdapter;
    private RecipeViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        ButterKnife.bind(this);

        appComponent = getAppComponent(this);

        /* Получаем query text из Intent */
        if (getIntent() != null) {
            searchQuery = getIntent().getStringExtra(KeyProvider.KEY_SEARCH_QUERY);
        } else {
            Toast.makeText(this, "Error: query is empty", Toast.LENGTH_SHORT).show();
        }

        /* Конфигурация пагинируемого списка */
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
            public void onItemClick(Hit item) { /* Клик по элементу - детализация рецепта */
                Intent intent = new Intent(DishActivity.this, RecipeDetailActivity.class);
                intent.putExtra(KeyProvider.KEY_RECIPE, item.getRecipe());
                startActivity(intent);
            }
        });

        viewModel = new RecipeViewModel(DishActivity.this, this, appComponent, searchQuery);

        viewModel.getUiList().observe(this, hits -> {
            pagingAdapter.submitList(hits);
            swipeRefreshLayout.setRefreshing(false);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvRecipes.getContext(),
                LinearLayout.VERTICAL);
        rvRecipes.addItemDecoration(dividerItemDecoration);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        rvRecipes.setAdapter(pagingAdapter);

        viewModel.refresh();

        /* Обновление списка по активации SwipeRefreshLayout */
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.refresh());
    }
}
