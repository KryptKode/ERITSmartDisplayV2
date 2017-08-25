package com.kryptkode.cyberman.eritsmartdisplay;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoardData;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> , TextView.OnEditorActionListener{
    public static final String TAG = DetailFragment.class.getSimpleName();
    public static final String BUNDLE_KEY = "mKey";
    private static final int DETAIL_LOADER_ID = 400;
    private static final String MY_PREFS = "prefs";
    private static final String NUM_OF_MESSAGES_KEY = "_Key";
    public static final String MSG = "message";


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
    private FloatingActionButton fab;

    private SharedPreferences preferences;
    private Uri uri;
    private TreeMap<String, String> messagesTreeMap;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        int id = v.getId();
        switch (id) {
            case R.id.edit_enter_message:
                String key = MSG + String.valueOf(messagesSpinner.getSelectedItemPosition() + 1);
                String text = messageEditText.getText().toString();
                messagesTreeMap.put(key, text);
                DetailFragmentHelper.dismissKeyboard(getContext(), getView());
                Toast.makeText(getContext(), "Saved " + key, Toast.LENGTH_LONG).show();
                break;
        }
            return true;
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

        messagesSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);

        messageEditText.setOnEditorActionListener(this);
        messageEditText.setImeActionLabel(getString(R.string.save), EditorInfo.IME_ACTION_DONE);

        loadingIndicatorProgressBar = (ProgressBar) view.findViewById(R.id.loading_indicator);
        loadingIndicatorTextView = (TextView) view.findViewById(R.id.tv_loading);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(fabClickListener);
        preferences = getContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        messagesTreeMap = new TreeMap<>();
        setUpSpinner();
        return view;
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DetailFragmentHelper.saveMessages(getContext(), uri, messagesTreeMap);
            Snackbar.make(getView().findViewById(R.id.fragment_root), "Saving...", Snackbar.LENGTH_LONG).show();

        }
    };

    private void setUpSpinner() {
        int numOfMessages = preferences.getInt(NUM_OF_MESSAGES_KEY, 8);
        Log.v(TAG, "Num_of_msg-->" + numOfMessages);
        List<String> spinnerEntries = new ArrayList<>();
        for (int i = 1; i <= numOfMessages; i++) {
            spinnerEntries.add(getString(R.string.message_x, i));
        }
        Log.v(TAG, "Array-->" + spinnerEntries.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, spinnerEntries
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        messagesSpinner.setAdapter(adapter);

    }

    AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //get the message associated with the spinner and display on the edittext
          /*  if (!firstTime){*/

            String key = MSG + String.valueOf(position + 1);
            String message = messagesTreeMap.containsKey(key) ?  messagesTreeMap.get(key): "" ;
            messageEditText.setText(message);

            if (!TextUtils.isEmpty(message)) {
                messagesTextInputLayout.setHint(getString(R.string.content_of_message) + " " + (position + 1));
            } else {
                messagesTextInputLayout.setHint(getString(R.string.enter_the_message) + " " + (position + 1));
            }
            /*}
            firstTime = false;*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


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
        Log.i(TAG, "onLoadFinished: " + data.getCount());
        data.moveToFirst();
        showPriceData(DetailFragmentHelper.parsePriceString(data));
        addToMap(DetailFragmentHelper.getMessages(data));
        Log.i(TAG, "onLoadFinished: " + messagesTreeMap.size());

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

    private void showPriceData(PriceBoardData priceBoardData) {
        pmsThreeEditText.setText(priceBoardData.getPmsPrice().split(":")[0].trim());
        pmsTwoEditText.setText(priceBoardData.getPmsPrice().split(":")[1].trim());
        dpkThreeEditText.setText(priceBoardData.getDpkPrice().split(":")[0].trim());
        dpkTwoEditText.setText(priceBoardData.getDpkPrice().split(":")[1].trim());
        agoThreeEditText.setText(priceBoardData.getAgoPrice().split(":")[0].trim());
        agoTwoEditText.setText(priceBoardData.getAgoPrice().split(":")[1].trim());
    }

    private void addToMap(TreeMap<String, String> map){
        for (String key : map.keySet()) {
            messagesTreeMap.put(key, map.get(key));
        }
    }
}
