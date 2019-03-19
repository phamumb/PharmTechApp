package com.bhcc.app.pharmtech.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.view.favorites.FavoriteActivity;
import com.bhcc.app.pharmtech.view.filter.FilterActivity;
import com.bhcc.app.pharmtech.view.quiz.QuizActivity;
import com.bhcc.app.pharmtech.view.review.ReviewActivity;
import com.bhcc.app.pharmtech.view.study.MedicineListActivity;

/**
 * Created by Luat on 10/25/2017.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);
        setUpToolbar();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment==null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment) // Find the fragment container(Hosting Activity layout) and add fragment.
                    .commit();
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.home:
                startActivity(MainActivity.newIntent(this));break;
            case R.id.study:
                startActivity(MedicineListActivity.newIntent(this)); break;
            case R.id.quiz:
                startActivity(QuizActivity.newIntent(this)); break;
            case R.id.filter:
                startActivity(FilterActivity.newIntent(this)); break;
            case R.id.favorite:
                startActivity(FavoriteActivity.newIntent(this)); break;
            case R.id.review:
                startActivity(ReviewActivity.newItent(this)); break;
        }
        closeDrawer();

        return false;
    }

    private boolean closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed();
        }
    }
}
