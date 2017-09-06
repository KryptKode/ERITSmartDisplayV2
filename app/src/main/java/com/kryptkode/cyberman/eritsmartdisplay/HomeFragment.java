package com.kryptkode.cyberman.eritsmartdisplay;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;

import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoard;
import com.kryptkode.cyberman.eritsmartdisplay.views.EmptyRecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.adapters.HomeAdapter;
import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;
import com.kryptkode.cyberman.eritsmartdisplay.utils.ItemDivider;
import com.kryptkode.cyberman.eritsmartdisplay.views.EditTextDialog;


public class HomeFragment extends Fragment implements HomeAdapter.HomeAdapterListener, LoaderManager.LoaderCallbacks<Cursor> {


    public static final String TAG = HomeFragment.class.getSimpleName();
    public static final int DISPLAY_LOADER_ID = 200;
    private EmptyRecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private GridLayoutManager gridLayoutManager;
    private View emptyView;

    private HomeFragmentListener homeFragmentListener;
    private RequestToLoad receiver;
    private String sortOrderKey;
    private boolean autoEnableHotspotIsActive;

    private  ViewGroup homeViewGroup;
    public HomeFragment() {
        // Required empty public constructor
    }

    public interface HomeFragmentListener {

    }


    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.homeFragmentListener = (HomeFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeFragmentListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (EmptyRecyclerView) view.findViewById(R.id.home_recycler_view);
        emptyView = view.findViewById(R.id.empty_root);
        homeAdapter = new HomeAdapter(getContext());
        homeAdapter.setHomeAdapterListener(this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
        }
        else{
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setEmptyView(emptyView);
        receiver = new RequestToLoad();

        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (preferences.contains(getString(R.string.sort_key))) {

            sortOrderKey = preferences.getString(getString(R.string.sort_key),
                    getString(R.string.sort_default));
            autoEnableHotspotIsActive = preferences.getBoolean(getString(R.string.auto_turn_on_wifi_key),
                    getResources().getBoolean(R.bool.auto_turn_on_wifi));

            Toast.makeText(getContext(), "Hotspot auto start: " + autoEnableHotspotIsActive, Toast.LENGTH_SHORT).show();
        }

        homeViewGroup = (ViewGroup) view.findViewById(R.id.home_root);

        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(receiver, new IntentFilter(SmartDisplayService.ACTION_READ_DB));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        createLoader();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(prefListener);
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(prefListener);
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getString(R.string.sort_key))) {
                sortOrderKey = sharedPreferences.getString(key, getString(R.string.sort_default));
                Log.i(TAG, "onSharedPreferenceChanged: " + sortOrderKey);
                createLoader();
            }else if (key.equals(getString(R.string.auto_turn_on_wifi_key))){
                autoEnableHotspotIsActive = sharedPreferences.getBoolean(getString(R.string.auto_turn_on_wifi_key),
                        getResources().getBoolean(R.bool.auto_turn_on_wifi));

                Toast.makeText(getContext(), "Hotspot auto start: " + autoEnableHotspotIsActive, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.erit_smart_display, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        PriceBoard priceBoard = new PriceBoard();
        Intent intent = new Intent(getContext(), AddNewDisplayActivity.class);
        if (id == R.id.action_add_filling_station_display) {
            /*intent.putExtra(AddNewDisplayActivity.EXTRA_INT, 1);
            startActivity(intent);*/
            priceBoard.setMessageBoardType(MessageBoard.MessageBoardType.MESSAGE_BOARD_TYPE_NONE);
            EditTextDialog editTextDialog = EditTextDialog.getInstance(priceBoard, false);
            editTextDialog.setCancelable(false);
            editTextDialog.show(getChildFragmentManager(), "edit");
            return true;
        } else if (id == R.id.action_add_resturant_display) {
            priceBoard.setPriceBoardType(PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE);
            EditTextDialog editTextDialog = EditTextDialog.getInstance(priceBoard, false);
            editTextDialog.setCancelable(false);
            editTextDialog.show(getChildFragmentManager(), "edit");
            return true;
        } else if (id == R.id.action_add_custom_display) {
            intent.putExtra(AddNewDisplayActivity.EXTRA_INT, 3);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class RequestToLoad extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(SmartDisplayService.DISPLAY_PAYLOAD, false)) {
                Log.i(TAG, "onReceive: ");
                Fade fade = new Fade();
                fade.setDuration(1000);
                TransitionManager.beginDelayedTransition(homeViewGroup, fade);
                createLoader();
            }

        }
    }


    private void createLoader() {
        LoaderManager loaderManager = getLoaderManager();
        Log.i(TAG, "createLoader: ");
        Loader<Cursor> loader = loaderManager.getLoader(DISPLAY_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(DISPLAY_LOADER_ID, null, this);
            Log.i(TAG, "createLoader: INIT");
        } else {
            loaderManager.restartLoader(DISPLAY_LOADER_ID, null, this);
            Log.i(TAG, "createLoader: RESTART");
        }

    }

    /*Methods for the adapter listener*/
    @Override
    public void onDisplayClicked(PriceBoard priceBoard) {
        HomeFragmentHelper.startDetailActivity(getContext(), priceBoard);
    }

    /*Methods for the adapter listener*/
    @Override
    public void onDisplayOverflowClicked(final PriceBoard priceBoard, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_edit:
                        EditTextDialog editTextDialog = EditTextDialog.getInstance(priceBoard, true);
                        editTextDialog.setCancelable(false);
                        editTextDialog.show(getChildFragmentManager(), "");
                        return true;

                    case R.id.action_delete:
                        SmartDisplayService.deleteTask(getContext(), priceBoard.buildBoardUri());
                        Log.i(TAG, "onMenuItemClick:  CALLED LODER");
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;

                }
            }
        });

        popupMenu.inflate(R.menu.menu_home_item);
        popupMenu.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DISPLAY_LOADER_ID:
                return new AsyncTaskLoader<Cursor>(getContext()) {
                    Cursor cursor;
                    String sortOrder;
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        if (cursor != null) {
                            deliverResult(cursor);
                            Log.i(TAG, "onStartLoading: ");
                        } else {
                            forceLoad();
                        }
                    }

                    @Override
                    public Cursor loadInBackground() {
                        try {

                            if (sortOrderKey.equals(getString(R.string.sort_by_name))) {
                                sortOrder = SmartDisplayContract.SmartDisplayColumns.NAME_SORT;
                            } else {
                                sortOrder = null;
                            }
                            Log.i(TAG, "loadInBackground: ");
                            return getActivity().getContentResolver()
                                    .query(SmartDisplayContract.SmartDisplayColumns.DISPLAY_CONTENT_URI,
                                            null,
                                            null,
                                            null,
                                            sortOrder);
                        } catch (Exception e) {
                            Log.i(TAG, "loadInBackground: ");
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void deliverResult(Cursor data) {
                        cursor = data;
                        Log.i(TAG, "deliverResult: ");
                        super.deliverResult(data);


                    }
                };
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        homeAdapter.swapCursor(data);
        Log.i(TAG, "onLoadFinished: " + data.getCount());
        homeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        homeAdapter.swapCursor(null);
    }



}
