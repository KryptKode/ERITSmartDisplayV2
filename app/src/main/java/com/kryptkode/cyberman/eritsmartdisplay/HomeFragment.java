package com.kryptkode.cyberman.eritsmartdisplay;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kryptkode.cyberman.eritsmartdisplay.adapters.HomeAdapter;
import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;
import com.kryptkode.cyberman.eritsmartdisplay.utils.ItemDivider;
import com.kryptkode.cyberman.eritsmartdisplay.views.EditTextDialog;


public class HomeFragment extends Fragment implements HomeAdapter.HomeAdapterListener, LoaderManager.LoaderCallbacks<Cursor> {


    public static final String TAG = HomeFragment.class.getSimpleName();
    public static final int DISPLAY_LOADER_ID = 200;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private LinearLayoutManager linearLayoutManager;

    private HomeFragmentListener homeFragmentListener;
    private RequestToLoad receiver;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        homeAdapter = new HomeAdapter(getContext());
        homeAdapter.setHomeAdapterListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
        recyclerView.setAdapter(homeAdapter);

        receiver = new RequestToLoad();

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
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.erit_smart_display, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(getContext(), AddNewDisplayActivity.class);
        if (id == R.id.action_add_filling_station_display) {
            /*intent.putExtra(AddNewDisplayActivity.EXTRA_INT, 1);
            startActivity(intent);*/
            EditTextDialog editTextDialog = EditTextDialog.getInstance(null, null, false, EditTextDialog.FILLING_STATION_TYPE);
            editTextDialog.setCancelable(false);
            editTextDialog.show(getChildFragmentManager(), "edit");
            return true;
        } else if (id == R.id.action_add_resturant_display) {
            EditTextDialog editTextDialog = EditTextDialog.getInstance(null, null, false, EditTextDialog.EATRIES_TYPE);
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
    public void onDisplayClicked(long id) {
        HomeFragmentHelper.startDetailActivity(getContext(), id);
    }

    /*Methods for the adapter listener*/
    @Override
    public void onDisplayOverflowClicked(final PriceBoard priceBoard, View view) {
        final long displayId = priceBoard.getPriceId();
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Uri uri = SmartDisplayContract.SmartDisplayColumns.buildDisplayUri(displayId);
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_edit:
                        /*Intent intent = new Intent(getContext(), AddNewDisplayActivity.class);
                        intent.setData(uri);
                        startActivity(intent);
                        return true;*/


                        EditTextDialog editTextDialog = EditTextDialog.getInstance(priceBoard.getPriceName(),
                                priceBoard.getPriceIpAddress(),
                                true,priceBoard.getMessageBoardType().getNumberOfCascades() - 1 ,
                                priceBoard.getNumberOfMessages(), priceBoard.getPriceBoardType().getNumberOfCascades(),
                                priceBoard.getPriceBoardType() == PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE ?
                                        EditTextDialog.EATRIES_TYPE : EditTextDialog.FILLING_STATION_TYPE, priceBoard.getId());
                        editTextDialog.setCancelable(false);
                        editTextDialog.show(getChildFragmentManager(), "");

                        return true;

                    case R.id.action_delete:
                        SmartDisplayService.deleteTask(getContext(), uri);
                        //createLoader();
                        Log.i(TAG, "onMenuItemClick:  CALLED LODER");
                        //Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
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
                            Log.i(TAG, "loadInBackground: ");
                            return getActivity().getContentResolver()
                                    .query(SmartDisplayContract.SmartDisplayColumns.DISPLAY_CONTENT_URI,
                                            null,
                                            null,
                                            null,
                                            null);
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
