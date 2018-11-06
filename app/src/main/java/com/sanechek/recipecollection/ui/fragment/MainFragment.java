package com.sanechek.recipecollection.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sanechek.recipecollection.R;

import butterknife.BindView;

public class MainFragment extends BaseFragment {

    @BindView(R.id.rv_dish_types) RecyclerView rvDishTypes;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setRecyclerView() {

    }
}