package com.sanechek.recipecollection.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sanechek.recipecollection.BaseActivity;
import com.sanechek.recipecollection.BuildConfig;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.adapter.RecipeAdapter;
import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.api.utils.KeyProvider;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.util.DisposableManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/* Screen with list of selected dish typed recipes */
public class DishActivity extends BaseActivity {

    @BindView(R.id.rv_recipes) RecyclerView rvRecipes;

    private Realm realm;
    private AppComponent appComponent;
    private final DisposableManager dManager = new DisposableManager(this);

    private String searchQuery = "fish";

    private RecipeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        ButterKnife.bind(this);

        appComponent = getAppComponent(this);
        realm = Realm.getDefaultInstance();

        if (getIntent() != null) {
            searchQuery = getIntent().getStringExtra(KeyProvider.KEY_SEARCH_QUERY);
            getRecipes();
        } else {
            Toast.makeText(this, "Error: query is empty", Toast.LENGTH_SHORT).show();
        }

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

    private void onGetRecipes() {

    }
}
