package com.sanechek.recipecollection.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.adapter.DishTypesAdapter;
import com.sanechek.recipecollection.util.KeyProvider;
import com.sanechek.recipecollection.data.DishType;
import com.sanechek.recipecollection.ui.activity.ActivityListener;
import com.sanechek.recipecollection.ui.activity.DishActivity;
import com.sanechek.recipecollection.view.ZoomLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.Realm;

/* Главный фрагмент (экран выбора типа блюд) */
public class MainFragment extends BaseFragment implements ActivityListener {

    @BindView(R.id.rv_dish_types) RecyclerView rvDishTypes;

    private Realm realm;

    private DishTypesAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realm = Realm.getDefaultInstance();

        /* Инициализация основных типов блюд */
        ArrayList<DishType> dishTypes = new ArrayList<>();
        dishTypes.add(new DishType("Salads", "salad", R.drawable.img_salad));
        dishTypes.add(new DishType("Soups", "soup", R.drawable.img_soup));
        dishTypes.add(new DishType("Desserts", "dessert", R.drawable.img_dessert));
        dishTypes.add(new DishType("Chicken", "chicken", R.drawable.img_chicken));
        dishTypes.add(new DishType("Fish", "fish", R.drawable.img_fish));
        dishTypes.add(new DishType("Beef", "beef", R.drawable.img_beef));
        dishTypes.add(new DishType("Pork", "pork", R.drawable.img_pork));

        setRecyclerView(requireContext(), dishTypes);
    }

    /* Настройка списка */
    private void setRecyclerView(final Context context, final ArrayList<DishType> list) {
        adapter = new DishTypesAdapter(context, list, new DishTypesAdapter.AdapterClickListener() {
            @Override
            public void onItemClick(DishType item) { /* Клик по элементу - открытие списка рецептов */
                Intent intent = new Intent(requireContext(), DishActivity.class);
                intent.putExtra(KeyProvider.KEY_SEARCH_QUERY, item.getQuery());
                intent.putExtra("title", item.getName());
                startActivity(intent);
            }
        }, realm);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvDishTypes);

        rvDishTypes.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rvDishTypes.setAdapter(adapter);
    }

}