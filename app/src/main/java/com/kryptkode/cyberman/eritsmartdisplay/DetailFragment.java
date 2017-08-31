package com.kryptkode.cyberman.eritsmartdisplay;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoard.MSG;
import static com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard.AGO;
import static com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard.DPK;
import static com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard.PMS;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TextView.OnEditorActionListener {
    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER_ID = 400;
    public static final String BOARD_KEY = "board_type";

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
    private RelativeLayout priceRoot;

    private TreeMap<String, String> messagesTreeMap;
    private PriceBoard priceBoard;

    private RequestToLoad receiver;
    private  int currentSelection;
    private int previousSelection;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        int id = v.getId();
        switch (id) {
            case R.id.edit_enter_message:
                saveMessage(messagesSpinner.getSelectedItemPosition() + 1);
                break;
        }
        return true;
    }

    public void saveMessage(int postion){
        String key = MSG + String.valueOf(postion);
        String text = messageEditText.getText().toString();
        messagesTreeMap.put(key, text);
        DetailFragmentHelper.dismissKeyboard(getContext(), getView());
        Toast.makeText(getContext(), R.string.temp_saved, Toast.LENGTH_LONG).show();
    }


    public interface DetailFragmentListener {
        void onSyncButtonClicked();

        void onSaveButtonClicked(PriceBoard priceBoard);
    }

    public static DetailFragment getInstance(PriceBoard priceBoard) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BOARD_KEY, priceBoard);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        priceBoard = bundle.getParcelable(BOARD_KEY);

        receiver = new RequestToLoad();
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(receiver, new IntentFilter(SmartDisplayService.ACTION_READ_DB));
        //createLoader();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
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

        priceRoot = (RelativeLayout) view.findViewById(R.id.price_root);
        if(priceBoard.getPriceBoardType() == PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE){
            priceRoot.setVisibility(View.GONE);
        }

        messagesSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);

        messageEditText.setOnEditorActionListener(this);
        messageEditText.setImeActionLabel(getString(R.string.save), EditorInfo.IME_ACTION_DONE);

        loadingIndicatorProgressBar = (ProgressBar) view.findViewById(R.id.loading_indicator);
        loadingIndicatorTextView = (TextView) view.findViewById(R.id.tv_loading);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(fabClickListener);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpSpinner();
        if (priceBoard.getPriceValuesMap() != null &&
                priceBoard.getPriceBoardType() != PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE) {
            decodePriceTreeMap(priceBoard.getPriceValuesMap());
        }

    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (priceBoard.getPriceBoardType() != PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE){
                priceBoard.setPriceValuesMap(createPriceTreeMap());
            }
            priceBoard.setMessagesMap(messagesTreeMap);
            DetailFragmentHelper.saveMessages(getContext(), priceBoard);
            Snackbar.make(getView().findViewById(R.id.fragment_root), "Saving...", Snackbar.LENGTH_LONG).show();

            //TODO Implement send

        }
    };

    private void setUpSpinner() {
        if (priceBoard.getMessagesMap() != null) {
            messagesTreeMap = priceBoard.getMessagesMap();
            Log.i(TAG, "setUpSpinner: " + priceBoard.getMessagesMap().toString());
        } else {
            messagesTreeMap = new TreeMap<>();
            for (int i = 1; i <= priceBoard.getNumberOfMessages() ; i++) {
                messagesTreeMap.put(MSG + i, "");
            }
            Log.i(TAG, "setUpSpinner: tree " );
        }
        Log.v(TAG, "Num_of_msg-->" + priceBoard.getNumberOfMessages());
        List<String> spinnerEntries = new ArrayList<>();
        for (int i = 1; i <= priceBoard.getNumberOfMessages(); i++) {
            spinnerEntries.add(getString(R.string.message_x, i));
        }
        Log.v(TAG, "Array-->" + spinnerEntries.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, spinnerEntries
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        messagesSpinner.setAdapter(adapter);
        messagesSpinner.setSelection(currentSelection, true);

    }

    AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //get the message associated with the spinner and display on the edittext
            previousSelection = currentSelection;
            saveMessage(previousSelection + 1);
            currentSelection = position;

            String key = MSG + String.valueOf(position + 1);
            String message = messagesTreeMap.containsKey(key) ? messagesTreeMap.get(key) : "";
            messageEditText.setText(message);

            if (!TextUtils.isEmpty(message)) {
                messagesTextInputLayout.setHint(getString(R.string.content_of_message) + " " + (position + 1));
            } else {
                messagesTextInputLayout.setHint(getString(R.string.enter_the_message) + " " + (position + 1));
            }

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
                //sync code

                //TODO Implement sync
                DetailFragmentHelper.displayLoadingIndicatiors(new View[] {loadingIndicatorTextView,
                        loadingIndicatorProgressBar}, true);
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
                                    .query(priceBoard.buildBoardUri(),
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
        DetailFragmentHelper.displayLoadingIndicatiors(new View[] {loadingIndicatorTextView,
                loadingIndicatorProgressBar}, false);
        try {
            priceBoard = new PriceBoard(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUpSpinner();
        if (priceBoard.getPriceBoardType() != PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE){
            decodePriceTreeMap(priceBoard.getPriceValuesMap());
        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private TreeMap<String, String> createPriceTreeMap() {
        TreeMap<String, String> priceValuesTreeMap = new TreeMap<>();
        String pmsData = pmsThreeEditText.getText().toString() + ":" + pmsTwoEditText.getText().toString() + " ";
        String dpkData = dpkThreeEditText.getText().toString() + ":" + dpkTwoEditText.getText().toString() + " ";
        String agoData = agoThreeEditText.getText().toString() + ":" + agoTwoEditText.getText().toString() + " ";

        priceValuesTreeMap.put(AGO, agoData);
        priceValuesTreeMap.put(DPK, dpkData);
        priceValuesTreeMap.put(PMS, pmsData);

        return priceValuesTreeMap;
    }

    private void decodePriceTreeMap(TreeMap<String, String> priceValuesMap) {
        pmsThreeEditText.setText(priceValuesMap.get(PMS).split(":")[0].trim());
        pmsTwoEditText.setText(priceValuesMap.get(PMS).split(":")[1].trim());
        dpkThreeEditText.setText(priceValuesMap.get(DPK).split(":")[0].trim());
        dpkTwoEditText.setText(priceValuesMap.get(DPK).split(":")[1].trim());
        agoThreeEditText.setText(priceValuesMap.get(AGO).split(":")[0].trim());
        agoTwoEditText.setText(priceValuesMap.get(AGO).split(":")[1].trim());
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

    private class RequestToLoad extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(SmartDisplayService.DISPLAY_PAYLOAD, false)) {
                Log.i(TAG, "onReceive: ");
                createLoader();
            }

        }
    }

}
