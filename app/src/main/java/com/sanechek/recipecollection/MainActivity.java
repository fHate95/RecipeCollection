package com.sanechek.recipecollection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.sanechek.recipecollection.ui.activity.ActivityListener;
import com.sanechek.recipecollection.ui.fragment.FavoriteFragment;
import com.sanechek.recipecollection.ui.fragment.FragmentListener;
import com.sanechek.recipecollection.ui.fragment.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Главная активити приложения */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentListener {

    @BindView(R.id.nav_view) NavigationView navView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.content_frame) FrameLayout flContent;

    private Fragment currentFragment;

    private ActivityListener listener;
    public void setListener(ActivityListener listener)
    {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.main);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /* Set Navigation Item Selected listener for navView
         * set first item checked */
        navView.setNavigationItemSelectedListener(this);
        navView.getMenu().getItem(0).setChecked(true);

        MainFragment fragment = new MainFragment();
        setListener((ActivityListener) fragment);

        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.content_frame, fragment)
                .commit();

        currentFragment = fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_search) {
//
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_main:
                fragment = new MainFragment();
                setListener((ActivityListener) fragment);
                break;
            case R.id.nav_favorites:
                fragment = new FavoriteFragment();
                setListener((ActivityListener) fragment);
                break;
            case R.id.nav_settings:

                break;
            case R.id.nav_share:
                final Intent shareAppIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareAppIntent.setType("plain/text");
                shareAppIntent.putExtra(android.content.Intent.EXTRA_TEXT, "[PlayMarket app link]");
                startActivity(Intent.createChooser(shareAppIntent, getString(R.string.share)));
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.content_frame, fragment)
                    .commit();
            currentFragment = fragment;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
