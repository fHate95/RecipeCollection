package com.sanechek.recipecollection.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanechek.recipecollection.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class CustomDialog extends Dialog {

    private String topText;
    private String bottomText;
    private boolean showCancelBtn = false;
    private boolean closeClickOutside = false;
    private static List<CustomDialog> dialogList = new ArrayList<>();
    private boolean showOnlyOkBtn = false;

    private Action onYesBtnClickCallback;
    private Action onNoBtnClickCallback;
    private Action onCancelClickCallback;
    private Action onDismissListener;

    @CheckResult
    public static Builder builder(Context context) {
        return new Builder(context);
    }

    @CheckResult
    public static Builder builder(Fragment fragment) {
        return new Builder(fragment.requireContext());
    }

    @CheckResult
    public static Builder error(Context context) {
        return new Builder(context).setTitle(R.string.text_error);
    }

    @CheckResult
    public static Builder error(Fragment fragment) {
        return new Builder(fragment.requireContext()).setTitle(R.string.text_error);
    }

    public static class Builder {
        private Context mContext;
        private String title;
        private String message;
        private Action onYesBtnClickListener;
        private Action onNoBtnClickListener;
        private Action onCancelClickListener;
        private Action onDismissListener;
        private boolean closeByClickOutside;
        private boolean showCancelBtn;
        private boolean showOnlyOkBtn;


        private Builder(Context context) {
            this.mContext = context;
        }

        @CheckResult
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        @CheckResult
        public Builder setTitle(@StringRes int title) {
            return setTitle(mContext.getString(title));
        }



        @CheckResult
        public Builder setTitle(Function<Context,String> titleSourceFunc) {
            try {
                this.title = titleSourceFunc.apply(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        @CheckResult
        public Builder setMessage(String message){
            this.message = message;
            return this;
        }

        @CheckResult
        public Builder setMessage(Function<Context,String> msgSourceFunc) {
            try {
                this.message = msgSourceFunc.apply(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        @CheckResult
        public Builder setMessage(@StringRes int message) {
            return this.setMessage(mContext.getString(message));
        }

        @CheckResult
        public Builder setOnYesBtnClickListener(Action onYesBtnClickListener) {
            this.onYesBtnClickListener = onYesBtnClickListener;
            return this;
        }

        @CheckResult
        public Builder setOnDismissListener(Action onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        @CheckResult
        public Builder setOnNoBtnClickListener(Action onNoBtnClickListener) {
            this.onNoBtnClickListener = onNoBtnClickListener;
            return this;
        }

        @CheckResult
        public Builder setOnCancelClickListener(Action onCancelClickListener) {
            this.onCancelClickListener = onCancelClickListener;
            return this;
        }

        @CheckResult
        public Builder setCloseByClickOutside(boolean closeByClickOutside) {
            this.closeByClickOutside = closeByClickOutside;
            return this;
        }

        @CheckResult
        public Builder setShowCancelBtn(boolean showCancelBtn) {
            this.showCancelBtn = showCancelBtn;
            return this;
        }

        @CheckResult
        public Builder setShowOnlyOkBtn(boolean showOnlyOkBtn) {
            this.showOnlyOkBtn = showOnlyOkBtn;
            if(showOnlyOkBtn){
                showCancelBtn = false;
            }
            return this;
        }

        @CheckResult
        public CustomDialog build() {
            CustomDialog dialog = new CustomDialog(mContext, title, message);
            dialog.onYesBtnClickCallback = onYesBtnClickListener;
            dialog.onNoBtnClickCallback = onNoBtnClickListener;
            dialog.onCancelClickCallback = onCancelClickListener;
            dialog.onDismissListener = onDismissListener;
            dialog.closeClickOutside = closeByClickOutside;
            dialog.showCancelBtn = showCancelBtn;
            dialog.showOnlyOkBtn = showOnlyOkBtn;
            return dialog;
        }

        public void show() {
            build().show();
        }
    }

    public CustomDialog(Context context, String topText, String bottomText, boolean showCancelBtn, boolean closeClickOutside) {
        super(context);
        this.topText = topText;
        this.bottomText = bottomText;
        this.showCancelBtn = showCancelBtn;
        this.closeClickOutside = closeClickOutside;
        dialogList.add(this);
    }

    public CustomDialog(Context context, @StringRes int title, @StringRes int message) {
        this(context,context.getString(title),context.getString(message));
    }

    public CustomDialog(Context context, String topText, String bottomText) {
        super(context);
        this.topText = topText;
        this.bottomText = bottomText;
        dialogList.add(this);
    }

    public CustomDialog(Context context, String topText, String bottomText, boolean showOnlyOkBtn) {
        super(context);
        this.topText = topText;
        this.bottomText = bottomText;
        this.showOnlyOkBtn = showOnlyOkBtn;
        dialogList.add(this);
    }

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_text) TextView tvText;
    @BindView(R.id.btn_yes) Button btnYes;
    @BindView(R.id.btn_no) Button btnNo;
    @BindView(R.id.btn_cancel) Button btnCancel;
    @BindView(R.id.ll_cancel) LinearLayout llCancel;
    @BindView(R.id.ll_no) LinearLayout llNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom);
        ButterKnife.bind(this);

        View v = Objects.requireNonNull(getWindow()).getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        setCanceledOnTouchOutside(closeClickOutside);
        setCancelable(false);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (onDismissListener != null) {
                    try {
                        onDismissListener.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        tvTitle.setText(topText);
        tvText.setText(bottomText);

        if (showOnlyOkBtn) {
            llCancel.setVisibility(View.GONE);
            llNo.setVisibility(View.GONE);
            btnYes.setText(R.string.text_ok);
        } else {
            if (showCancelBtn) {
                llCancel.setVisibility(View.VISIBLE);
            } else {
                llCancel.setVisibility(View.GONE);
            }
        }

        btnYes.setOnClickListener(view -> {
            dismiss();
            yesBtnPressed();
        });
        btnNo.setOnClickListener(view -> {
            dismiss();
            noBtnPressed();
        });
        btnCancel.setOnClickListener(view -> {
            dismiss();
            cancelBtnPressed();
        });
    }

    public void yesBtnPressed() {
        if (onYesBtnClickCallback != null) {
            try {
                onYesBtnClickCallback.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void noBtnPressed() {
        if (onNoBtnClickCallback != null) {
            try {
                onNoBtnClickCallback.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cancelBtnPressed(){
        if (onCancelClickCallback != null) {
            try {
                onCancelClickCallback.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dismiss() {
        try {
            dialogList.remove(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.dismiss();
    }

    public static void closeAllDialogs() {
        for (int i = dialogList.size()-1; i >= 0; i--) {
            try {
                dialogList.get(i).dismiss();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
