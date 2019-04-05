package com.sanechek.recipecollection.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.data.Menu;
import com.sanechek.recipecollection.ui.activity.RecipeDetailActivity;
import com.sanechek.recipecollection.util.KeyProvider;
import com.sanechek.recipecollection.util.Utils;

import java.util.List;

import butterknife.BindView;

public class MenuFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tv_day) TextView tvDay;
    @BindView(R.id.tv_breakfast) TextView tvBreakfast;
    @BindView(R.id.iv_breakfast) ImageView ivBreakfast;
    @BindView(R.id.tv_breakfast_title) TextView tvBreakfastTitle;
    @BindView(R.id.tv_lunch) TextView tvLunch;
    @BindView(R.id.iv_lunch) ImageView ivLunch;
    @BindView(R.id.tv_lunch_title) TextView tvLunchTitle;
    @BindView(R.id.tv_dinner) TextView tvDinner;
    @BindView(R.id.iv_dinner) ImageView ivDinner;
    @BindView(R.id.tv_dinner_title) TextView tvDinnerTitle;

    private List<Menu> menu;
    private int position;
    private Menu menuItem;

    public static MenuFragment newInstance(int position) {
        Bundle arguments = new Bundle();
        arguments.putInt("position", position);
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            position = getArguments().getInt("position");
            menu = callback.getMenu();
            menuItem = menu.get(position);
            setFields();
        }

        ivBreakfast.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
            intent.putExtra(KeyProvider.KEY_RECIPE, menuItem.getBreakfast());
            intent.putExtra("from_menu", true);
            startActivity(intent);
        });
        ivLunch.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
            intent.putExtra(KeyProvider.KEY_RECIPE, menuItem.getLunch());
            intent.putExtra("from_menu", true);
            startActivity(intent);
        });
        ivDinner.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
            intent.putExtra(KeyProvider.KEY_RECIPE, menuItem.getDinner());
            intent.putExtra("from_menu", true);
            startActivity(intent);
        });

    }

    @SuppressLint("SetTextI18n")
    private void setFields() {
        tvDay.setText(getString(Utils.getStringId(requireContext(), "day_of_week_" + position)));
        tvBreakfast.setText(getString(R.string.breakfast) + " (" +
                String.valueOf((int)(menuItem.getBreakfast().getCalories() / menuItem.getBreakfast().getYield()))
                + " " + getString(R.string.kcal) + " " + getString(R.string.per_serving) + ")");
        tvBreakfastTitle.setText(menuItem.getBreakfast().getLabel());
        tvLunch.setText(getString(R.string.lunch) + " (" +
                String.valueOf((int)(menuItem.getLunch().getCalories() / menuItem.getLunch().getYield()))
                + " " + getString(R.string.kcal) + " " + getString(R.string.per_serving) + ")");
        tvLunchTitle.setText(menuItem.getLunch().getLabel());
        tvDinner.setText(getString(R.string.dinner) + " (" +
                String.valueOf((int)(menuItem.getDinner().getCalories() / menuItem.getDinner().getYield()))
                + " " + getString(R.string.kcal) + " " + getString(R.string.per_serving) + ")");
        tvDinnerTitle.setText(menuItem.getDinner().getLabel());

        Glide.with(requireContext())
                .load(menuItem.getBreakfast().getImage())
                .into(ivBreakfast);
        Glide.with(requireContext())
                .load(menuItem.getLunch().getImage())
                .into(ivLunch);
        Glide.with(requireContext())
                .load(menuItem.getDinner().getImage())
                .into(ivDinner);
    }

    @Override
    public void onClick(View view) {

    }
}
