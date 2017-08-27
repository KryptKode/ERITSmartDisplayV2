package com.kryptkode.cyberman.eritsmartdisplay;

import android.content.ContentValues;
import android.os.Bundle;
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
import com.kryptkode.cyberman.eritsmartdisplay.views.EditTextDialog;
import com.kryptkode.cyberman.eritsmartdisplay.views.MessageSelectDisplayDialog;
import com.kryptkode.cyberman.eritsmartdisplay.views.PriceSelectDisplayDialog;

public class EritSmartDisplayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener, EditTextDialog.EditTextDialogListener,
        PriceSelectDisplayDialog.PriceSelectDisplayDialogListener, MessageSelectDisplayDialog.SelectDisplayDialogListener {
    public static final String TAG = EritSmartDisplayActivity.class.getSimpleName();
    public static final String POSITION_KEY = "position";
    public static final String FRAG_TAG = "frag";
    private int currentPosition;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

 /*   private String boardName;
    private String boardIp;
    private int messageBoardType;
    private int numberOfMessages;
    private int priceBoardType;
    private long boardId;*/
    private PriceBoard priceBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erit_smart_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //display the home fragment
        HomeFragment homeFragment = HomeFragment.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.home_root, homeFragment, FRAG_TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(POSITION_KEY);
            setActionBarTitle(currentPosition);
            selectItem(currentPosition);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        priceBoard = new PriceBoard();
    }


    @Override
    public void onBackPressed() {
        int count = 0;
        Toast toast;
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
                } else if (fragment instanceof AboutFragment) {
                    currentPosition = 2;
                }
                setActionBarTitle(currentPosition);
                navigationView.getMenu().getItem(currentPosition).setChecked(true);

            } else {
                count++;
                toast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT);
                toast.show();
                if (count > 1) {
                    super.onBackPressed();
                }
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
                fragment = AboutFragment.getInstance();
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

    @Override
    public void onPriceDialogPositiveButtonClicked(DialogFragment dialog, int boardType, boolean isEditing) {
//        this.priceBoardType = boardType;
        try {
            priceBoard.setPriceBoardType(PriceBoard.getPriceBoardTypeFromInt(boardType));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isEditing){
            updateBoard();
        }else {
            saveBoard();

        }
        dialog.dismiss();

    }

    private void saveBoard() {
        String boardtype = priceBoard.getMessageBoardType().getNumberOfCascades() + "|" +
                priceBoard.getPriceBoardType().getNumberOfCascades();
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(DetailFragment.NUM_OF_MESSAGES_KEY, numberOfMessages);
//        editor.apply();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_NAME, priceBoard.getName());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_IP_ADDRESS, priceBoard.getIpAddress());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_BOARD_TYPE, boardtype);
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_NUMBER_OF_MSG, priceBoard.getNumberOfMessages());
        SmartDisplayService.insertNewTask(this, contentValues);

    }

    private void updateBoard() {
        String boardtype = priceBoard.getMessageBoardType().getNumberOfCascades() + "|" +
                priceBoard.getPriceBoardType().getNumberOfCascades();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_NAME, priceBoard.getName());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_IP_ADDRESS, priceBoard.getIpAddress());
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_BOARD_TYPE, boardtype);
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_NUMBER_OF_MSG, priceBoard.getNumberOfMessages());
        SmartDisplayService.updateTask(this, SmartDisplayContract.SmartDisplayColumns.buildDisplayUri(priceBoard.getId()), contentValues);
    }


    @Override
    public void onMessageDialogPositiveButtonClicked(DialogFragment dialog, int messageSpinnerPositon, int numberOfMessages, int boardType, int priceSpinnerPosition, long boardId, boolean isEditing) {
//        this.numberOfMessages = numberOfMessages;


        if(boardType == EditTextDialog.EATRIES_TYPE){

//            this.priceBoardType = 3;
            try {
                priceBoard.setPriceBoardType(PriceBoard.getPriceBoardTypeFromInt(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //this.messageBoardType = messageSpinnerPositon;

        priceBoard.setNumberOfMessages(numberOfMessages);
        try {
            priceBoard.setMessageBoardType(MessageBoard.getMessageBoardTypeFromInt(messageSpinnerPositon));
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (boardType == EditTextDialog.FILLING_STATION_TYPE || boardType == EditTextDialog.CUSTOM_TYPE) {
            PriceSelectDisplayDialog priceSelectDisplayDialog = PriceSelectDisplayDialog.getInstance(priceSpinnerPosition, isEditing);
            priceSelectDisplayDialog.setCancelable(false);
            priceSelectDisplayDialog.show(getSupportFragmentManager(), "dialog");
        } else {
            if(isEditing){
                updateBoard();
            }else {
                saveBoard();

            }
            dialog.dismiss();
        }
    }


    @Override
    public void onDialogPositiveButtonClicked(DialogFragment dialog, String boardName, String boardIpAddress,
                                              int messageBoardType, int numOfMessages, int priceSpinnerPositon, int boardType,
                                              long boardId, boolean isEditing) {
        /*this.boardName = boardName;
        this.boardIp = boardIpAddress;

        this.numberOfMessages = numOfMessages;

        this.messageBoardType = messageBoardType;*/

        priceBoard.setName(boardName);
        priceBoard.setIpAddress(boardIpAddress);
        priceBoard.setNumberOfMessages(numOfMessages);
        priceBoard.setId(boardId);
        try {
            priceBoard.setMessageBoardType(MessageBoard.getMessageBoardTypeFromInt(numOfMessages));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "onDialogPositiveButtonClicked: Num  " + numOfMessages);

        MessageSelectDisplayDialog messageSelectDisplayDialog = MessageSelectDisplayDialog.getInstance(isEditing, messageBoardType,
                numOfMessages, priceSpinnerPositon,
                boardType, boardId);
        messageSelectDisplayDialog.show(getSupportFragmentManager(), "msg");
    }

}
