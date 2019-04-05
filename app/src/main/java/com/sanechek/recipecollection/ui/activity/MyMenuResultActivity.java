package com.sanechek.recipecollection.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.data.DataHelper;
import com.sanechek.recipecollection.data.Favorite;
import com.sanechek.recipecollection.data.Menu;
import com.sanechek.recipecollection.dialogs.CustomDialog;
import com.sanechek.recipecollection.dialogs.LoadingDialog;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.ui.fragment.FragmentListener;
import com.sanechek.recipecollection.ui.fragment.MenuFragment;
import com.sanechek.recipecollection.util.Utils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class MyMenuResultActivity extends BaseActivity implements ViewPager.OnPageChangeListener, FragmentListener {

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private String diet;
    private String health;
    private int totalCal;
    private float breakfastCal;
    private float lunchCal;
    private float dinnerCal;

    private Disposable search;

    private ArrayList<Hit> hits;
    private ArrayList<Hit> breakfastHits;
    private ArrayList<Hit> lunchHits;
    private ArrayList<Hit> dinnerHits;
    private ArrayList<Menu> menu;

    private AppComponent appComponent;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu_result);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        appComponent = getAppComponent(this);
        menu = DataHelper.getMenu(Realm.getDefaultInstance());

        Intent intent = getIntent();
        if (menu.isEmpty()) {
            if (intent != null) {
                totalCal = intent.getIntExtra("cal", 0);
                diet = intent.getStringExtra("diet");
                health = intent.getStringExtra("health");
                breakfastCal = totalCal * 0.3f;
                lunchCal = totalCal * 0.5f;
                dinnerCal = totalCal * 0.2f;
                getBreakfastHits();
                Utils.log("TAG_MENU", "loadHits");
            }
        } else {
            Utils.log("TAG_MENU", "configurePager");
            configurePager();
        }
    }

    private void configurePager() {
        if (menu.isEmpty()) {
            menu = DataHelper.getMenu(Realm.getDefaultInstance());
        }
        PagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
//        viewPager.setPageTransformer(false, (page, position) -> {
//            page.setCameraDistance(20000);
//            if (position < -1) {
//                page.setAlpha(0);
//            }
//            else if (position <= 0) {
//                page.setAlpha(1);
//                page.setPivotX(page.getWidth());
//                page.setRotationY(90 * Math.abs(position));
//            } else if (position <= 1) {
//                page.setAlpha(1);
//                page.setPivotX(0);
//                page.setRotationY(-90 * Math.abs(position));
//            } else {
//                page.setAlpha(0);
//            }
//            if (Math.abs(position) <= 0.5) {
//                page.setScaleY(Math.max(.4f, 1 - Math.abs(position)));
//            } else if (Math.abs(position) <= 1) {
//                page.setScaleY(Math.max(.4f, 1 - Math.abs(position)));
//
//            }
//        });
        int dayOfWeek = 0;
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY: dayOfWeek = 0;
                break;
            case Calendar.TUESDAY: dayOfWeek = 1;
                break;
            case Calendar.WEDNESDAY: dayOfWeek = 2;
                break;
            case Calendar.THURSDAY: dayOfWeek = 3;
                break;
            case Calendar.FRIDAY: dayOfWeek = 4;
                break;
            case Calendar.SATURDAY: dayOfWeek = 5;
                break;
            case Calendar.SUNDAY: dayOfWeek = 6;
                break;
        }
        viewPager.setCurrentItem(dayOfWeek);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        viewPager.setCurrentItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MenuFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return menu.size();
        }

    }

    @Override
    public List<Menu> getMenu() {
        return menu;
    }

    private void getBreakfastHits() {
        search = appComponent.getApi().search("breakfast", 0, 20, diet, health, "100-" + String.valueOf((int) breakfastCal))
//                String.valueOf((int)(totalCal / 3)) + "-" + String.valueOf((int)(totalCal / 2)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    loadingDialog = new LoadingDialog(this);
                    loadingDialog.show();
                })
                .doOnError(throwable -> {
                    Utils.log("TAG_LOAD_BREAKFAST", throwable.getMessage());
                    CustomDialog.builder(this)
                            .setTitle(R.string.text_error)
                            .setMessage(throwable.getMessage())
                            .setShowOnlyOkBtn(true)
                            .setOnYesBtnClickListener(() -> {
                                DataHelper.clearMenus(Realm.getDefaultInstance());
                                Intent intent = new Intent(this, MyMenuActivity.class);
                                startActivity(intent);
                                this.finish();
                            })
                            .show();
                })
                .subscribe(((hits, throwable) -> {
                    if (hits != null) {
                        if (!hits.getHits().isEmpty()) {
                            if (hits.getHits().size() < 7) {
                                loadingDialog.dismiss();
                                showErrorLoadingDialog();
                            } else {
                                this.breakfastHits = new ArrayList<>(hits.getHits());
                                getLunchHits();
                            }
                        } else {
                            loadingDialog.dismiss();
                            Utils.log("TAG_LOAD_BREAKFAST", "hits is empty");
                        }
                    } else {
                        loadingDialog.dismiss();
                        Utils.log("TAG_LOAD_BREAKFAST", "hits = null");
                    }
                }));
    }

    private void getLunchHits() {
        search = appComponent.getApi().search("lunch", 0, 20, diet, health, "100-" + String.valueOf((int) lunchCal))
//                String.valueOf((int)(totalCal / 3)) + "-" + String.valueOf((int)(totalCal / 2)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Utils.log("TAG_LOAD_LUNCH", throwable.getMessage());
                    CustomDialog.builder(this)
                            .setTitle(R.string.text_error)
                            .setMessage(throwable.getMessage())
                            .setShowOnlyOkBtn(true)
                            .setOnYesBtnClickListener(() -> {
                                DataHelper.clearMenus(Realm.getDefaultInstance());
                                Intent intent = new Intent(this, MyMenuActivity.class);
                                startActivity(intent);
                                this.finish();
                            })
                            .show();
                })
                .subscribe(((hits, throwable) -> {
                    if (hits != null) {
                        if (!hits.getHits().isEmpty()) {
                            if (hits.getHits().size() < 7) {
                                loadingDialog.dismiss();
                                showErrorLoadingDialog();
                            } else {
                                this.lunchHits = new ArrayList<>(hits.getHits());
                                getDinnerHits();
                            }
                        } else {
                            loadingDialog.dismiss();
                            Utils.log("TAG_LOAD_LUNCH", "hits is empty");
                        }
                    } else {
                        loadingDialog.dismiss();
                        Utils.log("TAG_LOAD_LUNCH", "hits = null");
                    }
                }));
    }

    private void getDinnerHits() {
        search = appComponent.getApi().search("dinner", 0, 20, diet, health, "100-" + String.valueOf((int) dinnerCal))
                //String.valueOf((int)(totalCal / 3)) + "-" + String.valueOf((int)(totalCal / 2)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Utils.log("TAG_LOAD_DINNER", throwable.getMessage());
                    CustomDialog.builder(this)
                            .setTitle(R.string.text_error)
                            .setMessage(throwable.getMessage())
                            .setShowOnlyOkBtn(true)
                            .setOnYesBtnClickListener(() -> {
                                DataHelper.clearMenus(Realm.getDefaultInstance());
                                Intent intent = new Intent(this, MyMenuActivity.class);
                                startActivity(intent);
                                this.finish();
                            })
                            .show();
                })
                .doFinally(() -> {
                    loadingDialog.dismiss();
                })
                .subscribe(((hits, throwable) -> {
                    if (hits != null) {
                        if (!hits.getHits().isEmpty()) {
                            if (hits.getHits().size() < 7) {
                                loadingDialog.dismiss();
                                showErrorLoadingDialog();
                            } else {
                                this.dinnerHits = new ArrayList<>(hits.getHits());
                                setWeekMenu();
                            }
                        } else {
                            Utils.log("TAG_LOAD_DINNER", "hits is empty");
                        }
                    } else {
                        Utils.log("TAG_LOAD_DINNER", "hits = null");
                    }
                }));
    }

    private void setWeekMenu() {
        for (int i = 0; i < 7; i++) {
            Menu menu = new Menu();
            menu.setDay(i);
            menu.setBreakfast(new Favorite(breakfastHits.get(RandomUtils.nextInt(0, breakfastHits.size() - 1)).getRecipe()));
            menu.setLunch(new Favorite(lunchHits.get(RandomUtils.nextInt(0, lunchHits.size() - 1)).getRecipe()));
            menu.setDinner(new Favorite(dinnerHits.get(RandomUtils.nextInt(0, dinnerHits.size() - 1)).getRecipe()));
            DataHelper.addMenu(Realm.getDefaultInstance(), menu);
        }
        configurePager();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                CustomDialog.builder(this)
                        .setTitle(R.string.text_warning)
                        .setMessage(R.string.refresh_confirm)
                        .setOnYesBtnClickListener(() -> {
                            DataHelper.clearMenus(Realm.getDefaultInstance());
                            Intent intent = new Intent(this, MyMenuActivity.class);
                            startActivity(intent);
                            this.finish();
                        })
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showErrorLoadingDialog() {
        CustomDialog.builder(this)
                .setTitle(R.string.text_error)
                .setMessage(R.string.menu_error)
                .setShowOnlyOkBtn(true)
                .setOnYesBtnClickListener(() -> {
                    DataHelper.clearMenus(Realm.getDefaultInstance());
                    Intent intent = new Intent(this, MyMenuActivity.class);
                    startActivity(intent);
                    this.finish();
                })
                .show();
    }

}
