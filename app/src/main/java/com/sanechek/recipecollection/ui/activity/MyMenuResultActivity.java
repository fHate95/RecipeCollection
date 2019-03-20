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

import com.sanechek.recipecollection.R;
import com.sanechek.recipecollection.api.data.search.Hit;
import com.sanechek.recipecollection.data.DataHelper;
import com.sanechek.recipecollection.data.Favorite;
import com.sanechek.recipecollection.data.Menu;
import com.sanechek.recipecollection.injection.AppComponent;
import com.sanechek.recipecollection.ui.fragment.FragmentListener;
import com.sanechek.recipecollection.ui.fragment.MenuFragment;
import com.sanechek.recipecollection.util.Utils;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class MyMenuResultActivity extends BaseActivity implements ViewPager.OnPageChangeListener, FragmentListener {

    @BindView(R.id.pager) ViewPager viewPager;

    private String diet;
    private String health;
    private int totalCal;
    private float breakfastCal;
    private float lunchCal;
    private float dinnerCal;

    private Disposable search;

    private ArrayList<Hit> hits;
    private ArrayList<Menu> menu;

    private AppComponent appComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu_result);
        ButterKnife.bind(this);

        appComponent = getAppComponent(this);

        Intent intent = getIntent();
        if (intent != null) {
            totalCal = intent.getIntExtra("cal", 0);
            diet = intent.getStringExtra("diet");
            health = intent.getStringExtra("health");
            breakfastCal = totalCal * 0.3f;
            lunchCal = totalCal * 0.5f;
            dinnerCal = totalCal * 0.2f;
            getRecipes(totalCal);
        } else {
            configurePager();
        }
    }

    private void configurePager() {
        menu = DataHelper.getMenu(Realm.getDefaultInstance());
        PagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
        viewPager.setPageTransformer(false, (page, position) -> {
            page.setCameraDistance(20000);
            if (position < -1) {
                page.setAlpha(0);
            }
            else if (position <= 0) {
                page.setAlpha(1);
                page.setPivotX(page.getWidth());
                page.setRotationY(90 * Math.abs(position));
            } else if (position <= 1) {
                page.setAlpha(1);
                page.setPivotX(0);
                page.setRotationY(-90 * Math.abs(position));
            } else {
                page.setAlpha(0);
            }
            if (Math.abs(position) <= 0.5) {
                page.setScaleY(Math.max(.4f, 1 - Math.abs(position)));
            } else if (Math.abs(position) <= 1) {
                page.setScaleY(Math.max(.4f, 1 - Math.abs(position)));

            }
        });
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

    @SuppressLint("CheckResult")
    private void getRecipes(int cal) {
        int position = RandomUtils.nextInt(0, 30);
        search = appComponent.getApi().search("", 0, 40, diet, health, String.valueOf((int)(totalCal / 3)) + "-" + String.valueOf((int)(totalCal / 2)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Utils.log("TAG_TAG_TAG_SUKA", throwable.getMessage());
                })
                .subscribe(((hits, throwable) -> {
                    if (hits != null) {
                        if (!hits.getHits().isEmpty()) {
                            this.hits = new ArrayList<>(hits.getHits());
                            setWeekMenu();
                        } else {
                            Utils.log("TAG_TAG_TAG_SUKA", "hits is empty");
                        }
                    } else {
                        Utils.log("TAG_TAG_TAG_SUKA", "hits = null");
                    }
                }));
    }

    private void setWeekMenu() {
        Utils.log("TAG_TAG_TAG_SUKA", "setWeekMenu");
        for (int i = 0; i < 7; i++) {
            Menu menu = new Menu();
            menu.setDay(i);
            Favorite breakfast = new Favorite();
            menu.setBreakfast(new Favorite(hits.get(RandomUtils.nextInt(0, hits.size())).getRecipe()));
            menu.setLunch(new Favorite(hits.get(RandomUtils.nextInt(0, hits.size())).getRecipe()));
            menu.setDinner(new Favorite(hits.get(RandomUtils.nextInt(0, hits.size())).getRecipe()));
            DataHelper.addMenu(Realm.getDefaultInstance(), menu);
            configurePager();
        }
    }

}
