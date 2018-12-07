package com.sanechek.recipecollection.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.adapter.FavoriteAdapter;
import com.sanechek.recipecollection.data.DataHelper;
import com.sanechek.recipecollection.data.Favorite;
import com.sanechek.recipecollection.ui.activity.ActivityListener;
import com.sanechek.recipecollection.ui.activity.RecipeDetailActivity;
import com.sanechek.recipecollection.util.KeyProvider;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.Realm;

public class FavoriteFragment extends BaseFragment implements ActivityListener {

    @BindView(R.id.rv_favorite) RecyclerView rvFavorite;

    private FavoriteAdapter adapter;
    private boolean isResumed = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Favorite> favorites = new ArrayList<>(DataHelper.getFavorites(Realm.getDefaultInstance()));

        setRecyclerView(requireContext(), favorites);
    }

    /* Настройка списка */
    private void setRecyclerView(final Context context, final ArrayList<Favorite> list) {
        adapter = new FavoriteAdapter(context, list, new FavoriteAdapter.AdapterClickListener() {
            @Override
            public void onItemClick(Favorite item) { /* Клик по элементу - открытие детализации рецепта*/
                Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
                intent.putExtra(KeyProvider.KEY_RECIPE, item);
                startActivity(intent);
            }
        });

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(rvFavorite.getContext(), R.anim.rv_layout_animation_slide);
        rvFavorite.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        rvFavorite.setAdapter(adapter);
        rvFavorite.setLayoutAnimation(controller);
        rvFavorite.scheduleLayoutAnimation();
        isResumed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && isResumed) {
            adapter.refresh(new ArrayList<>(DataHelper.getFavorites(Realm.getDefaultInstance())));
        }
    }
}
