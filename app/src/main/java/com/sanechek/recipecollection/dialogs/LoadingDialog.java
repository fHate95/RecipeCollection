package com.sanechek.recipecollection.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.sanechek.recipecollection.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.saeid.fabloading.LoadingView;

public class LoadingDialog extends Dialog {

    @BindView(R.id.loading_view) LoadingView loadingView;

    public LoadingDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        ButterKnife.bind(this);
        View v = Objects.requireNonNull(getWindow()).getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        loadingView.addAnimation(Color.parseColor("#FFC107"), R.drawable.ic_loading_1, //loading 4 color
                LoadingView.FROM_TOP);
        loadingView.addAnimation(Color.parseColor("#FFC107"), R.drawable.ic_loading_1, //loading 4 color
                LoadingView.FROM_TOP);
        loadingView.addAnimation(Color.parseColor("#4CAF50"), R.drawable.ic_loading_3, //loading 2 color
                LoadingView.FROM_RIGHT);
        loadingView.addAnimation(Color.parseColor("#FF5252"), R.drawable.ic_loading_4, //loading 3 color
                LoadingView.FROM_BOTTOM);
        loadingView.addAnimation(Color.parseColor("#FFC107"), R.drawable.ic_loading_2, //loading 4 color
                LoadingView.FROM_LEFT);

        loadingView.addListener(new LoadingView.LoadingListener() {
            @Override public void onAnimationStart(int currentItemPosition) {
            }

            @Override public void onAnimationRepeat(int nextItemPosition) {
            }

            @Override public void onAnimationEnd(int nextItemPosition) {
                loadingView.startAnimation();
            }
        });
        loadingView.startAnimation();
    }

    @Override
    public void dismiss() {
        try {
            loadingView.pauseAnimation();
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}