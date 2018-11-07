package com.sanechek.recipecollection.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sanechek.recipecollection.BaseActivity;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.api.data.search.Ingredient;
import com.sanechek.recipecollection.api.data.search.Recipe;
import com.sanechek.recipecollection.api.utils.KeyProvider;
import com.sanechek.recipecollection.injection.AppComponent;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class RecipeDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_content) TextView tvContent;
    @BindView(R.id.iv_photo) AppCompatImageView ivPhoto;

    private Realm realm;
    private AppComponent appComponent;

    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        appComponent = getAppComponent(this);
        realm = Realm.getDefaultInstance();

        if (getIntent() != null) {
            recipe = getIntent().getParcelableExtra(KeyProvider.KEY_RECIPE);
            if (recipe != null) {
                setupData();
            }
        } else {
            Toast.makeText(this, "Error: query is empty", Toast.LENGTH_SHORT).show();
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(recipe.getLabel());
        }

    }

    private void setupData() {
        Glide.with(this)
                .load(recipe.getImage())
                .into(ivPhoto);
        StringBuilder text = new StringBuilder();
            for (Ingredient ingredient : recipe.getIngredients()) {
                try {
                    text.append("\n");
                    text.append("* ").append(ingredient.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        tvContent.setText(text.toString());
    }

}