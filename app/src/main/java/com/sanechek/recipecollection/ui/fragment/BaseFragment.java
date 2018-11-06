package com.sanechek.recipecollection.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanechek.recipecollection.App;
import com.sanechek.recipecollection.injection.AppComponent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/* Provides callback interface and ButterKnife view binding for all fragments */
public abstract class BaseFragment extends Fragment {

    public FragmentListener callback;

    /* Reference to layout id */
    @LayoutRes
    protected abstract int getLayoutId();
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static AppComponent getAppComponent(Context context) {
        if (context instanceof App) {
            return ((App)context).getAppComponent();
        } else {
            return getAppComponent(context.getApplicationContext());
        }
    }
}
