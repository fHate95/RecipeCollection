package com.sanechek.recipecollection.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.adapter.RecipeAdapter;
import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.data.Favorite;
import com.sanechek.recipecollection.dialogs.CustomDialog;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.ui.activity.ActivityListener;
import com.sanechek.recipecollection.ui.activity.DishActivity;
import com.sanechek.recipecollection.ui.activity.RecipeDetailActivity;
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
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.tv_empty) TextView tvEmpty;

    @BindView(R.id.rv_recipes) RecyclerView rvRecipes;

    private RecipeAdapter adapter;

    private String diet = "None";
    private String health = "None";
    private int ingrCount = 10;
    private int thumbValue1 = 50;
    private int thumbValue2 = 600;

    private AppComponent appComponent;

    private Disposable search;

    private int activeItemPosition = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_detail;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appComponent = getAppComponent(requireContext());

        configureSpinners();
        setRecyclerView();
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

        btnSearch.setOnClickListener(v -> getRecipes());
    }

    private void getRecipes() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        search = appComponent.getApi().search(etQuery.getText().toString(), 0, 30, ingrCount,
                spinnerDiet.getSelectedItem().toString().equals(KeyProvider.KEY_NONE) ? null :
                        spinnerDiet.getSelectedItem().toString().toLowerCase().replace(" ", "-"),
                spinnerHealth.getSelectedItem().toString().equals(KeyProvider.KEY_NONE) ? null :
                        spinnerHealth.getSelectedItem().toString().toLowerCase().replace(" ", "-"),
                String.valueOf(thumbValue1) + "-" + String.valueOf(thumbValue2))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    progressBar.setVisibility(View.GONE);
                    Utils.log(TAG, "Request error: " + throwable.getMessage());
                    if (throwable.getMessage().contains(KeyProvider.ERR_REQUEST_LIMIT)) {
                        CustomDialog.builder(requireContext())
                                .setTitle(R.string.text_error)
                                .setMessage(R.string.error_request_limit)
                                .setShowOnlyOkBtn(true)
                                .setOnYesBtnClickListener(() -> slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED))
                                .show();
                    } else {
                        CustomDialog.builder(requireContext())
                                .setTitle(R.string.text_error)
                                .setMessage(R.string.error_unknown)
                                .setShowOnlyOkBtn(true)
                                .setOnYesBtnClickListener(() -> slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED))
                                .show();
                    }
                })
                .subscribe(((hits, throwable) -> {
                    if (hits != null) {
                        if (!hits.getHits().isEmpty()) {
                            progressBar.setVisibility(View.GONE);
                            adapter.updateItems(hits.getHits());
                        } else {
                            progressBar.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }));
    }

    @Override
    public void onResume() {
        if (adapter != null && activeItemPosition != -1) {
            adapter.notifyItemChanged(activeItemPosition);
        }
        super.onResume();
    }

    private void setRecyclerView() {
        adapter = new RecipeAdapter(requireContext(), new RecipeAdapter.AdapterClickListener() {
            @Override
            public void onItemClick(Hit item, int position) {
                Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
                intent.putExtra(KeyProvider.KEY_RECIPE, new Favorite(item.getRecipe()));
                startActivity(intent);
                activeItemPosition = position;
            }
        });

        rvRecipes.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false));
        rvRecipes.setAdapter(adapter);
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
