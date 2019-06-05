package com.sanechek.recipecollection.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.data.AppSettings;
import com.sanechek.recipecollection.data.DataHelper;
import com.sanechek.recipecollection.ui.activity.ActivityListener;

import butterknife.BindView;

public class MyDietFragment extends BaseFragment implements ActivityListener {

    private static final int SEX_MALE = 0;
    private static final int SEX_FEMALE = 1;

    @BindView(R.id.switch_calculate_calories) SwitchCompat switchCalculateCalories;
    @BindView(R.id.et_weight) EditText etWeight;
    @BindView(R.id.et_height) EditText etHeight;
    @BindView(R.id.et_years) EditText etYears;
    @BindView(R.id.spinner_activity) Spinner spinnerActivity;
    @BindView(R.id.spinner_sex) Spinner spinnerSex;
    @BindView(R.id.btn_ok) Button btnOk;
    @BindView(R.id.cl_calculate_calories)
    ConstraintLayout clCalculateCalories;

    private double amr = -0.0;
    private int sex = -1;

    private AppSettings appSettings;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_diet;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configureActivitySpinner();
        configureSexSpinner();

        appSettings = DataHelper.getAppSettings();
        if (appSettings.isCaloriesSetted()) {
            switchCalculateCalories.setChecked(true);
        } else {
            switchCalculateCalories.setChecked(false);
        }


        switchCalculateCalories.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                clCalculateCalories.setVisibility(View.VISIBLE);
            } else {
                clCalculateCalories.setVisibility(View.GONE);
                appSettings.setCalories(-1);
                DataHelper.createOrUpdateSettings(appSettings);
            }
        });

        btnOk.setOnClickListener(v -> {
            if (amr != -0.0 && sex != -1 &&
                    !etHeight.getText().toString().isEmpty() &&
                    !etWeight.getText().toString().isEmpty() &&
                    !etYears.getText().toString().isEmpty()) {
                double calories = calculateCalories(sex, Integer.parseInt(etWeight.getText().toString()),
                        Integer.parseInt(etHeight.getText().toString()),
                        Integer.parseInt(etYears.getText().toString()), amr);

                appSettings.setCalories((int) calories);
                DataHelper.createOrUpdateSettings(appSettings);
                Toast.makeText(requireContext(), R.string.saved, Toast.LENGTH_SHORT).show();
                callback.showMainFragment();
            }
        });
    }

    private void configureActivitySpinner() {
        ArrayAdapter<CharSequence> dietAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.activity_labels, android.R.layout.simple_spinner_item);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(dietAdapter);
        spinnerActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch(pos) {
                    case 0: amr = 1.2;
                        break;
                    case 1: amr = 1.375;
                        break;
                    case 2: amr = 1.55;
                        break;
                    case 3: amr = 1.725;
                        break;
                    case 4: amr = 1.9;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void configureSexSpinner() {
        ArrayAdapter<CharSequence> dietAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.sex_labels, android.R.layout.simple_spinner_item);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(dietAdapter);
        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch(pos) {
                    case 0: sex = SEX_MALE;
                        break;
                    case 1: sex = SEX_FEMALE;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private double calculateCalories(int sex, double weight, double height, int age, double amr) {
        if (sex == SEX_MALE) {
            return calculateMaleBMR(weight, height, age) * amr;
        } else {
            return calculateFemaleBMR(weight, height, age) * amr;
        }
    }

    private double calculateMaleBMR(double weight, double height, int age) {
        return 447.593 + (9.247 * weight) + (3.098 * height) - (5.667 * age);
    }

    private double calculateFemaleBMR(double weight, double height, int age) {
        return 88.362 + (13.397 * weight) + (4.799 * height) - (4.330 * age);
    }

}
