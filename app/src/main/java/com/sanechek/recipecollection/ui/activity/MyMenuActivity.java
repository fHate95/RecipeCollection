package com.sanechek.recipecollection.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.data.AppSettings;
import com.sanechek.recipecollection.data.DataHelper;
import com.sanechek.recipecollection.util.KeyProvider;
import com.sanechek.recipecollection.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

@SuppressLint("SetTextI18n")
public class MyMenuActivity extends BaseActivity {

    @BindView(R.id.root_view) ConstraintLayout rootView;
    @BindView(R.id.sb_cal) SeekBar sbCal;
    @BindView(R.id.tv_cal_count) TextView tvCalCount;
    @BindView(R.id.tv_cal_label) TextView tvCalLabel;
    @BindView(R.id.spinner_diet) Spinner spinnerDiet;
    @BindView(R.id.spinner_health) Spinner spinnerHealth;
    @BindView(R.id.btn_apply) Button btnApply;

    private AppSettings appSettings;
    private int recommendedCal = -1;

    int minimumValueCal = 1200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);
        ButterKnife.bind(this);

        appSettings = DataHelper.getAppSettings();
        if (appSettings.isCaloriesSetted()) {
            recommendedCal = appSettings.getCalories();
            updateRecommendedCal();
        }

        configureSpinners();
        tvCalCount.setText(String.valueOf(2000) + " " + getString(R.string.kcal));
        sbCal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvCalCount.setText((minimumValueCal + i) + " " + getString(R.string.kcal));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnApply.setOnClickListener(view -> {
            Intent intent = new Intent(this, MyMenuResultActivity.class);
            intent.putExtra("cal", minimumValueCal + sbCal.getProgress());
            intent.putExtra("diet", spinnerDiet.getSelectedItem().toString().equals(KeyProvider.KEY_NONE) ? null :
                    spinnerDiet.getSelectedItem().toString().toLowerCase().replace(" ", "-"));
            intent.putExtra("health", spinnerHealth.getSelectedItem().toString().equals(KeyProvider.KEY_NONE) ? null :
                    spinnerHealth.getSelectedItem().toString().toLowerCase().replace(" ", "-"));
            startActivity(intent);
            finish();
        });
    }

    private void updateRecommendedCal() {
        tvCalLabel.setText(getString(R.string.max_cal_label) + " (" + getString(R.string.recommended) + " " + recommendedCal + ")");
    }

    /* Настройка спиннеров */
    private void configureSpinners() {
        /* diet spinner */
        ArrayAdapter<CharSequence> dietAdapter = ArrayAdapter.createFromResource(this,
                R.array.diet_labels, android.R.layout.simple_spinner_item);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(dietAdapter);
        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (appSettings.isCaloriesSetted()) {
                    recommendedCal = appSettings.getCalories();
                    if (pos == 0 || pos == 1) {
                        recommendedCal = appSettings.getCalories();
                    } else if (pos == 2 || pos == 3) {
                        recommendedCal += recommendedCal * 0.2;
                    } else if (pos == 4 || pos == 5) {
                        recommendedCal -= recommendedCal * 0.3;
                    }
                    updateRecommendedCal();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /* health spinner */
        ArrayAdapter<CharSequence> healthAdapter = ArrayAdapter.createFromResource(this,
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
