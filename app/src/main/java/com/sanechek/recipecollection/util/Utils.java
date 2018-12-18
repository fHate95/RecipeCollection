package com.sanechek.recipecollection.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utils {

    public static void log(String tag, String message) {
        Log.v(tag, message);
    }

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void makeToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    public static void setKeyboardVisibility(Activity activity, boolean visible) {
        try {
            if (visible) {
                View view = activity.getCurrentFocus();
                if (view instanceof EditText) {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).showSoftInput(view, InputMethodManager.SHOW_FORCED);
                } else {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

            } else {
                clearFocus(activity.getWindow());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void clearFocus(Window window) {

        List<EditText> list = new ArrayList<>();
        for (View v: window.getDecorView().getFocusables(View.FOCUS_FORWARD)) {
            if (v instanceof EditText) {
                list.add((EditText) v);
            }
        }
        for (EditText et: list) {
            if (et.isEnabled()) {
                et.setEnabled(false);
                et.setEnabled(true);
                et.clearFocus();
            }
            else {
                et.clearFocus();
            }
        }
        window.getDecorView().clearFocus();
    }

}
