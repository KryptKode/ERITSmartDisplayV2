package com.kryptkode.cyberman.eritsmartdisplay;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;
import com.kryptkode.cyberman.eritsmartdisplay.utils.WifiHotspot;
import com.kryptkode.cyberman.eritsmartdisplay.views.EditTextDialog;
import com.kryptkode.cyberman.eritsmartdisplay.views.MessageSelectDisplayDialog;
import com.kryptkode.cyberman.eritsmartdisplay.views.PriceSelectDisplayDialog;

public class EritSmartDisplayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener, EditTextDialog.EditTextDialogListener,
         MessageSelectDisplayDialog.SelectDisplayDialogListener {
    public static final String TAG = EritSmartDisplayActivity.class.getSimpleName();
    public static final String POSITION_KEY = "position";
    public static final String FRAG_TAG = "frag";
    public static final String HOME_TAG = "home";
    public static final String BACK_STACK_KEY = "hey_key";
    private int currentPosition;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private HomeFragment homeFragment;
    private PriceBoard priceBoard;
    private WifiHotspot wifiHotspot;
    private boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erit_smart_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_TAG);
        //display the home fragment
        if (homeFragment == null) {
            homeFragment = HomeFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.home_root, homeFragment, HOME_TAG);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(POSITION_KEY);
            selectItem(currentPosition);
        }else{
            setNavItemChecked(0);
        }
        wifiHotspot = new WifiHotspot(this);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                super.onBackPressed();
                fragmentManager.popBackStack(BACK_STACK_KEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                currentPosition = 0;
                setActionBarTitle(currentPosition);
                setNavItemChecked(currentPosition);

            } else {
                if(doubleBackToExitPressedOnce){
                    super.onBackPressed();
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ACTIVITY ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
       /* if(!wifiHotspot.isHotspotOn()){
            Log.i(TAG, "onStart:  WIFI" );
            wifiHotspot.setUpWifiHotspot(true);
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
       /* if(wifiHotspot.isHotspotOn()){
            Log.i(TAG, "onStop: WIFI");
            wifiHotspot.setUpWifiHotspot(false);
        }*/
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
           //TODO Implement share
            shareThisApp();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareThisApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this Site");
        intent.putExtra(Intent.EXTRA_TEXT, "To cater for all your embedded systems needs, check out \n http://erit.com.ng");
        Intent chooserIntent = Intent.createChooser(intent, "Share with...");
        startActivity(chooserIntent);
    }


    private void displayFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_root, fragment, FRAG_TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(BACK_STACK_KEY);
        transaction.commit();
    }

    private void selectItem(int position) {
// update the main content by replacing fragments
        currentPosition = position;
        Fragment fragment = null;
        switch (position) {
            case 0:
                getSupportFragmentManager().popBackStack(BACK_STACK_KEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case 1:
                fragment = new SettingsFragment();
                break;
            case 2:
                fragment = AboutFragment.getInstance();
                break;
            default:
                fragment = HomeFragment.getInstance();

        }
        if(position != 0) {
            displayFragment(fragment);
        }
//Set the action bar title
        setActionBarTitle(position);
        setNavItemChecked(position);
//Close the drawer
        drawer.closeDrawer(GravityCompat.START);
    }

    private  void setNavItemChecked(int postion){
        navigationView.getMenu().getItem(postion).setChecked(true);
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


    private void saveBoard() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_NAME, priceBoard.getName());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_IP_ADDRESS, priceBoard.getIpAddress());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_BOARD_TYPE, priceBoard.createBoardType());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_NUMBER_OF_MSG, priceBoard.getNumberOfMessages());
        SmartDisplayService.insertNewTask(this, contentValues);

    }

    private void updateBoard() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_NAME, priceBoard.getName());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_IP_ADDRESS, priceBoard.getIpAddress());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_BOARD_TYPE, priceBoard.createBoardType());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_NUMBER_OF_MSG, priceBoard.getNumberOfMessages());
        SmartDisplayService.updateTask(this, SmartDisplayContract.SmartDisplayColumns.buildDisplayUri(priceBoard.getId()), contentValues);
    }


    @Override
    public void onMessageDialogPositiveButtonClicked(DialogFragment dialog, PriceBoard priceBoard, boolean isEditing ){
        this.priceBoard = priceBoard;


            if(isEditing){
                updateBoard();
            }else {
                saveBoard();

            }
            dialog.dismiss();

    }


    @Override
    public void onDialogPositiveButtonClicked(DialogFragment dialog, PriceBoard priceBoard,  boolean isEditing) {
        this.priceBoard = priceBoard;
        Log.i(TAG, "onDialogPositiveButtonClicked: Num  " + priceBoard.getNumberOfMessages());
        MessageSelectDisplayDialog messageSelectDisplayDialog = MessageSelectDisplayDialog.getInstance(priceBoard, isEditing);
        messageSelectDisplayDialog.show(getSupportFragmentManager(), "msg");
    }

}
