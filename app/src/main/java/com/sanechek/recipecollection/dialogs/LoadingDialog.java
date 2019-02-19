package com.sanechek.recipecollection.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.sanechek.recipecollection.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.saeid.fabloading.LoadingView;

public class LoadingDialog extends Dialog {

    //@BindView(R.id.loading_view) LoadingView loadingView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

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
    }

    @Override
    public void dismiss() {
        try {
//            loadingView.pauseAnimation();
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