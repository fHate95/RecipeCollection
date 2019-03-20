package com.sanechek.recipecollection.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.data.Menu;

import java.util.List;

import butterknife.BindView;

public class MenuFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.iv_image) ImageView ivImage;

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
        }

        Glide.with(requireContext())
                .load(menuItem.getBreakfast().getImage())
                .into(ivImage);

    }

    @Override
    public void onClick(View view) {

    }
}
