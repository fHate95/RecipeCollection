package com.sanechek.recipecollection.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.dialogs.CustomDialog;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.ui.activity.ActivityListener;
import com.sanechek.recipecollection.util.KeyProvider;
import com.sanechek.recipecollection.util.Utils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import io.apptik.widget.MultiSlider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchDetailFragment extends BaseFragment implements ActivityListener {

    private final String TAG = "SearchDetailFragment";

    @BindView(R.id.sliding_layout) SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.et_query) EditText etQuery;
    @BindView(R.id.spinner_diet) Spinner spinnerDiet;
    @BindView(R.id.spinner_health) Spinner spinnerHealth;
    @BindView(R.id.sb_ingr) SeekBar sbIngr;
    @BindView(R.id.tv_ingr_count) TextView tvIngrCount;
    @BindView(R.id.sb_cal) MultiSlider sbCal;
    @BindView(R.id.tv_cal_count) TextView tvCalCount;
    @BindView(R.id.drag_view) ConstraintLayout dragRootView;
    @BindView(R.id.btn_search) Button btnSearch;

    @BindView(R.id.rv_recipes) RecyclerView rvRecipes;

    private String diet = "None";
    private String health = "None";
    private int ingrCount = 10;
    private int thumbValue1 = 50;
    private int thumbValue2 = 600;

    private AppComponent appComponent;

    private Disposable search;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_detail;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appComponent = getAppComponent(requireContext());

        configureSpinners();
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        slidingLayout.setTouchEnabled(false);

        tvIngrCount.setText(String.valueOf(ingrCount));
        sbIngr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ingrCount = i + 1;
                tvIngrCount.setText(String.valueOf(ingrCount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        sbCal.getThumb(0).setValue(0);
//        sbCal.getThumb(1).setValue(600);
        tvCalCount.setText(new StringBuilder(String.valueOf(thumbValue1)).append(" - ").append(String.valueOf(thumbValue2))
                .append(" ").append(getString(R.string.kcal)));
        sbCal.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                switch (thumbIndex) {
                    case 0: thumbValue1 = value;
                        break;
                    case 1: thumbValue2 = value;
                        break;
                }
                tvCalCount.setText(new StringBuilder(String.valueOf(thumbValue1)).append(" - ").append(String.valueOf(thumbValue2))
                .append(" ").append(getString(R.string.kcal)));
            }
        });

        etQuery.setOnFocusChangeListener((view1, b) -> {
            if (!b) {
                Utils.setKeyboardVisibility(requireActivity(), false);
            }
        });
        dragRootView.setOnClickListener(v -> dragRootView.requestFocus());

        btnSearch.setOnClickListener(v -> {
            search = appComponent.getApi().search(etQuery.getText().toString(), 0, 30, ingrCount,
                    spinnerDiet.getSelectedItem().toString().equals(KeyProvider.KEY_NONE) ? null :
                            spinnerDiet.getSelectedItem().toString().toLowerCase().replace(" ", "-"),
                    spinnerHealth.getSelectedItem().toString().equals(KeyProvider.KEY_NONE) ? null :
                            spinnerHealth.getSelectedItem().toString().toLowerCase().replace(" ", "-"),
                    String.valueOf(thumbValue1) + "-" + String.valueOf(thumbValue2))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> {
                        Utils.log(TAG, "Request error: " + throwable.getMessage());
                        if (throwable.getMessage().contains(KeyProvider.ERR_REQUEST_LIMIT)) {
                            CustomDialog.builder(requireContext())
                                    .setTitle(R.string.text_error)
                                    .setMessage(R.string.error_request_limit)
                                    .setShowOnlyOkBtn(true)
                                    .setOnYesBtnClickListener(() -> {})
                                    .show();
                        } else {
                            CustomDialog.builder(requireContext())
                                    .setTitle(R.string.text_error)
                                    .setMessage(R.string.error_unknown)
                                    .setShowOnlyOkBtn(true)
                                    .setOnYesBtnClickListener(() -> {})
                                    .show();
                        }
                    })
                    .subscribe(((hits, throwable) -> {
                        if (hits != null) {
                            if (!hits.getHits().isEmpty()) {
                                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                        }
                    }));
        });
    }

    private void configureSpinners() {
        /* diet spinner */
        ArrayAdapter<CharSequence> dietAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.diet_labels, android.R.layout.simple_spinner_item);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(dietAdapter);
        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Utils.makeToast(requireContext(), spinnerDiet.getSelectedItem().toString().toLowerCase().replace(" ", "-"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /* health spinner */
        ArrayAdapter<CharSequence> healthAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.health_labels, android.R.layout.simple_spinner_item);
        healthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHealth.setAdapter(healthAdapter);
        spinnerHealth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Utils.makeToast(requireContext(), spinnerHealth.getSelectedItem().toString().toLowerCase().replace(" ", "-"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
