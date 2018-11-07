package com.sanechek.recipecollection.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.adapter.DishTypesAdapter;
import com.sanechek.recipecollection.api.utils.KeyProvider;
import com.sanechek.recipecollection.data.DishType;
import com.sanechek.recipecollection.ui.activity.ActivityListener;
import com.sanechek.recipecollection.ui.activity.DishActivity;
import com.sanechek.recipecollection.util.DisposableManager;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.Realm;

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

        ArrayList<DishType> dishTypes = new ArrayList<>();
        dishTypes.add(new DishType("Salads", "salad"));
        dishTypes.add(new DishType("Soups", "soup"));
        dishTypes.add(new DishType("Desserts", "dessert"));
        dishTypes.add(new DishType("Chicken", "chicken"));
        dishTypes.add(new DishType("Fish", "fish"));
        dishTypes.add(new DishType("Beef", "beef"));
        dishTypes.add(new DishType("Pork", "pork"));

        setRecyclerView(requireContext(), dishTypes);
    }

    /* Setup RecyclerView with dish data */
    private void setRecyclerView(final Context context, final ArrayList<DishType> list) {
        adapter = new DishTypesAdapter(context, list, new DishTypesAdapter.AdapterClickListener() {
            @Override
            public void onItemClick(DishType item) {
                Intent intent = new Intent(requireContext(), DishActivity.class);
                intent.putExtra(KeyProvider.KEY_SEARCH_QUERY, item.getName());
                startActivity(intent);
            }
        }, realm);

        rvDishTypes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        rvDishTypes.setAdapter(adapter);
    }

}