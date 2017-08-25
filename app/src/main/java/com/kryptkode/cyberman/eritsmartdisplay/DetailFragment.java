package com.kryptkode.cyberman.eritsmartdisplay;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = DetailFragment.class.getSimpleName();
    public static final String BUNDLE_KEY = "mKey";
    private static final int DETAIL_LOADER_ID = 400;
    private static final String MY_PREFS = "prefs";
    private static final String NUM_OF_MESSAGES_KEY = "_Key";


    private AppCompatSpinner messagesSpinner;
    private EditText messageEditText;
    private TextInputLayout messagesTextInputLayout;
    private EditText pmsThreeEditText;
    private EditText dpkThreeEditText;
    private EditText agoThreeEditText;
    private EditText pmsTwoEditText;
    private EditText dpkTwoEditText;
    private EditText agoTwoEditText;
    private ProgressBar loadingIndicatorProgressBar;
    private TextView loadingIndicatorTextView;

    private Cursor mCursor;
    private Uri uri;

    public DetailFragment() {
        // Required empty public constructor
    }


    public interface DetailFragmentListener {

    }

    public static DetailFragment getInstance(Uri uri) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, uri);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        uri = bundle.getParcelable(BUNDLE_KEY);
        createLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        messagesSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner);
        messagesTextInputLayout = (TextInputLayout) view.findViewById(R.id.edit_enter_message_text_input_layout);
        messageEditText = (EditText) view.findViewById(R.id.edit_enter_message);
        dpkThreeEditText = (EditText) view.findViewById(R.id.dpk_000);
        dpkTwoEditText = (EditText) view.findViewById(R.id.dpk_00);
        pmsThreeEditText = (EditText) view.findViewById(R.id.pms_000);
        pmsTwoEditText = (EditText) view.findViewById(R.id.pms_00);
        agoThreeEditText = (EditText) view.findViewById(R.id.ago_000);
        agoTwoEditText = (EditText) view.findViewById(R.id.ago_00);

        loadingIndicatorProgressBar = (ProgressBar) view.findViewById(R.id.loading_indicator);
        loadingIndicatorTextView = (TextView) view.findViewById(R.id.tv_loading);

        setUpSpinner();
        return view;
    }

    private void setUpSpinner() {
        int numOfMessages = getContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)
                .getInt(NUM_OF_MESSAGES_KEY, 8);
        Log.v(TAG, "Num_of_msg-->" + numOfMessages);
        List<String> spinnerEntries = new ArrayList<>();
        for (int i = 1; i <= numOfMessages ; i++) {
            spinnerEntries.add(getString(R.string.message_x, i));
        }
        Log.v(TAG, "Array-->" + spinnerEntries.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, spinnerEntries
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        messagesSpinner.setAdapter(adapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_smart_display, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_sync:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case DETAIL_LOADER_ID:
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
                                    .query(uri,
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
        mCursor = data;
        Log.i(TAG, "onLoadFinished: " + data.getCount());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void createLoader() {
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(DETAIL_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(DETAIL_LOADER_ID, null, this);
        } else {
            loaderManager.restartLoader(DETAIL_LOADER_ID, null, this);
        }

    }
}
