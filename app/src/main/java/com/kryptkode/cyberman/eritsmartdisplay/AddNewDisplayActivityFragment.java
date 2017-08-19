package com.kryptkode.cyberman.eritsmartdisplay;

import android.content.ContentValues;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddNewDisplayActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public static final String WHICH_DISPLAY = "which_display";
    private int whichDisplay; //1=filling station, 2=eatries
    private TextInputEditText boardNameEditText;
    private TextInputEditText ipAddressEditText;
    private AppCompatSpinner messageBoardSpinner;
    private AppCompatImageView messageBoardImageView;
    private AppCompatSpinner priceBoardSpinner;
    private AppCompatImageView priceBoardImageView;

    private PriceBoard.PriceBoardType priceBoardType;
    private MessageBoard.MessageBoardType messageBoardType;

    private String boardName;
    private String boardIp;
    private String boardType;

    public AddNewDisplayActivityFragment() {
    }


    public static AddNewDisplayActivityFragment getInstance(int extra) {
        AddNewDisplayActivityFragment addNewDisplayActivityFragment =
                new AddNewDisplayActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(WHICH_DISPLAY, extra);
        addNewDisplayActivityFragment.setArguments(bundle);
        return addNewDisplayActivityFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        whichDisplay = getArguments().getInt(WHICH_DISPLAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);


        View view = inflater.inflate(R.layout.fragment_add_new_display, container, false);
        boardNameEditText = (TextInputEditText) view.findViewById(R.id.add_boardName_et);
        ipAddressEditText = (TextInputEditText) view.findViewById(R.id.add_ip_et);
        messageBoardSpinner = (AppCompatSpinner) view.findViewById(R.id.message_board_spinner);
        messageBoardImageView = (AppCompatImageView) view.findViewById(R.id.add_message_board_image);
        priceBoardImageView = (AppCompatImageView) view.findViewById(R.id.add_price_board_image);
        priceBoardSpinner = (AppCompatSpinner) view.findViewById(R.id.price_board_spinner);

        priceBoardSpinner.setOnItemSelectedListener(this);
        messageBoardSpinner.setOnItemSelectedListener(this);

        switch (whichDisplay) {
            case 1: //filling station

                break;
            case 2://eatries
                AddNewDisplayHelper.toggleViewVisibility //do not show
                        (new View[]{priceBoardImageView, priceBoardSpinner}, false);
                break;
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_new_display, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_save:
                saveEntries();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //for the message spinner
        if (parent == messageBoardSpinner) {
            try {
                messageBoardType = MessageBoard.getMessageBoardTypeFromInt(position + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { //for the price spinner
            try {
                priceBoardType = PriceBoard.getPriceBoardTypeFromInt(position + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void saveEntries() {
        String priceTypeString = priceBoardType != null ?
                String.valueOf(priceBoardType.getNumberOfCascades()) : "";
        String messageTypeString = messageBoardType != null ?
                String.valueOf(messageBoardType.getNumberOfCascades()): "";

        boardName = boardNameEditText.getText().toString();
        boardIp = ipAddressEditText.getText().toString();
        boardType = messageTypeString + "-" + priceTypeString;

        ContentValues contentValues = new ContentValues();
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_NAME,boardName );
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_IP_ADDRESS, boardIp);
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_BOARD_TYPE, boardType);

        SmartDisplayService.insertNewTask(getContext(), contentValues);
        getActivity().finish(); //end the activity
    }
}
