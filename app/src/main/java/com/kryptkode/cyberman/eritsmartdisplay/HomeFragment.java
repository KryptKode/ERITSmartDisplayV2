package com.kryptkode.cyberman.eritsmartdisplay;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.adapters.HomeAdapter;
import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.utils.ItemDivider;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeAdapter.HomeAdapterListener, LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG = HomeFragment.class.getSimpleName();
    public static final int DISPLAY_LOADER_ID = 200;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HomeFragmentListener homeFragmentListener;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        homeAdapter = new HomeAdapter(getContext());
        homeAdapter.setHomeAdapterListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
        recyclerView.setAdapter(homeAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        createLoader();
    }

    private void createLoader() {
        if (getActivity().getSupportLoaderManager() != null) {
            getActivity().getSupportLoaderManager().restartLoader(DISPLAY_LOADER_ID, null, this);
        } else {
            getActivity().getSupportLoaderManager().initLoader(DISPLAY_LOADER_ID, null, this);

        }
    }

    /*Methods for the adapter listener*/
    @Override
    public void onDisplayClicked(int position) {

    }

    /*Methods for the adapter listener*/
    @Override
    public void onDisplayOverflowClicked(int position, View view) {
        //TODO Create Contextual Menu

        PopupMenu popupMenu = new PopupMenu(getContext(), view );
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_edit:
                        Intent intent = new Intent(getContext(), AddNewDisplayActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.action_delete:
                        Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();
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
                        }else {
                            forceLoad();
                        }
                    }

                    @Override
                    public Cursor loadInBackground() {
                        try {
                            return getActivity().getContentResolver()
                                    .query(SmartDisplayContract.SmartDisplayColumns.DISPLAY_CONTENT_URI,
                                            null,
                                            null,
                                            null,
                                            null);
                        } catch (Exception e){
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void deliverResult(Cursor data) {
                        cursor = data;
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

    }
}
