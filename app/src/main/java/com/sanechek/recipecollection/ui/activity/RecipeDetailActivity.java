package com.sanechek.recipecollection.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.data.DataHelper;
import com.sanechek.recipecollection.data.Favorite;
import com.sanechek.recipecollection.util.KeyProvider;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/* Экран детализации рецепта */
public class RecipeDetailActivity extends BaseActivity {

    private final String TAG = "RecipeDetailActivity";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_source) TextView tvSource;
    @BindView(R.id.tv_content) TextView tvContent;
    @BindView(R.id.btn_more) CardView btnMore;
    @BindView(R.id.iv_photo) AppCompatImageView ivPhoto;
    @BindView(R.id.iv_star) AppCompatImageView ivStar;

    private Realm realm;
    private AppComponent appComponent;

    private Favorite recipe;
    private boolean isFromMenu = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        appComponent = getAppComponent(this);
        realm = Realm.getDefaultInstance();

        /* получаем элемент рецепта из Intent */
        if (getIntent() != null) {
            recipe = getIntent().getParcelableExtra(KeyProvider.KEY_RECIPE);
            isFromMenu = getIntent().getBooleanExtra("from_menu", false);
            if (recipe != null) {
                setupData();
            }
        } else {
            Toast.makeText(this, "Intent is null", Toast.LENGTH_SHORT).show();
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(recipe.getLabel());
        }

        /* View ClickListeners */
        ivStar.setOnClickListener(view -> {
            if (DataHelper.getFavoriteById(realm, recipe.getUri()) != null) {
                Utils.log(TAG, "Favorite not found with id " + recipe.getUri());
                DataHelper.deleteFavorite(realm, recipe.getUri());
                ivStar.setImageResource(R.drawable.ic_star);
            } else {
                //Favorite favorite = new Favorite(recipe);
                DataHelper.addFavorite(realm, recipe);
                ivStar.setImageResource(R.drawable.ic_star_checked);
                Utils.log(TAG, "Favorite created with id " + recipe.getUri());
            }
        });
        tvSource.setOnClickListener(view -> {
            //empty
        });
        /* Получаем url страницы рецепта и открываем в браузере */
        btnMore.setOnClickListener(view -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getUrl()));
                startActivity(browserIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /* Заполняем поля данными */
    private void setupData() {
        Glide.with(this)
                .load(recipe.getImage())
                .into(ivPhoto);
        tvSource.setText(recipe.getSource());
        StringBuilder text = new StringBuilder();
        text.append(getString(R.string.ingredients));
        for (String ingredient : recipe.getIngredientLines()) {
            try {
                text.append("\n");
                text.append("* ").append(ingredient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tvContent.setText(text.toString());
        if (isFromMenu) {
            ivStar.setVisibility(View.INVISIBLE);
        } else {
            if (DataHelper.getFavoriteById(Realm.getDefaultInstance(), recipe.getUri()) != null) {
                ivStar.setImageResource(R.drawable.ic_star_checked);
            } else {
                ivStar.setImageResource(R.drawable.ic_star);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
