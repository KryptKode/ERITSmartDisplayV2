package com.kryptkode.cyberman.eritsmartdisplay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class EritSmartDisplayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener {

    public static final String POSITION_KEY = "position";
    public static final String FRAG_TAG = "frag";
    private int currentPosition;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erit_smart_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //display the home fragment
        HomeFragment homeFragment = HomeFragment.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.home_root, homeFragment, null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(POSITION_KEY);
            setActionBarTitle(currentPosition);
            selectItem(currentPosition);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                super.onBackPressed();

                Fragment fragment = fragmentManager.findFragmentByTag(FRAG_TAG);
                if (fragment instanceof HomeFragment) {
                    currentPosition = 0;
                } else if (fragment instanceof SettingsFragment) {
                    currentPosition = 1;
                } /*else if (fragment instanceof AboutFragment) {
                    currentPosition = 2;
                }*/
                setActionBarTitle(currentPosition);
                navigationView.getMenu().getItem(currentPosition).setChecked(true);

            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY, currentPosition);
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selectItem(0);

        } else if (id == R.id.nav_setting) {
            selectItem(1);

        } else if (id == R.id.nav_about) {
            selectItem(2);

        } else if (id == R.id.nav_share) {
            selectItem(3);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void displayFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_root, fragment, FRAG_TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void selectItem(int position) {
// update the main content by replacing fragments
        currentPosition = position;
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = HomeFragment.getInstance();
                break;
            case 1:
                fragment = new SettingsFragment();
                break;
            case 2:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = HomeFragment.getInstance();

        }
        displayFragment(fragment);
//Set the action bar title
        setActionBarTitle(position);
//Close the drawer
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setActionBarTitle(int position) {
        String title;
        switch (position) {
            case 0:
                title = getString(R.string.app_name);
                break;
            case 1:
                title = getString(R.string.action_settings);
                break;
            case 2:
                title = getString(R.string.about);
                break;
            default:
                title = getString(R.string.app_name);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

    }


}
