package com.sanechek.recipecollection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sanechek.recipecollection.injection.AppComponent;

/* Базовая активити, провайдит получение компонента приложения */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static AppComponent getAppComponent(Context context) {
        if (context instanceof App) {
            return ((App)context).getAppComponent();
        } else {
            return getAppComponent(context.getApplicationContext());
        }
    }

}
