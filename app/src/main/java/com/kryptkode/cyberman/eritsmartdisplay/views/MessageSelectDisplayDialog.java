package com.kryptkode.cyberman.eritsmartdisplay.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.R;

/**
 * Created by Cyberman on 8/22/2017.
 */

public class MessageSelectDisplayDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    public static final String IS_IN_EDIT_MODE_KEY = "positive_button";
    public static final String BOARD_TYPE_KEY = "board_type";
    public static final String MESSAGE_SPINNER_POSITION_KEY = "spinner_position";
    public static final String NUMBER_OF_MESSAGE_KEY = "num_of_mgsg";
    public static final String PRICE_SPINNER_POSITION_KEY = "price_key";
    public static final String BOARD_ID_KEY = "ID_";

    private SelectDisplayDialogListener listener;

    private AlertDialog dialog;
    private AppCompatSpinner appCompatSpinner;
    private AppCompatImageView appCompatImageView;
    private TextInputEditText textInputEditText;


    private boolean isEditing;
    private int boardType;
    private int numberOfMessages;
    private int messageSpinnerPositon;
    private int priceSpinnerPosition;
    private long boardId;


    public interface SelectDisplayDialogListener {
        void onMessageDialogPositiveButtonClicked(DialogFragment dialog, int messageSpinnerPositon, int numberOfMessages, int boardType, int priceSpinnerPosition, long boardId, boolean isEditing);
    }

    public static MessageSelectDisplayDialog getInstance(boolean isEditing, int messageSpinnerPositon,
                                                         int numberOfMessages, int boardType, int priceSpinnerPosition, long boardId) {
        MessageSelectDisplayDialog priceSelectDisplayDialog = new MessageSelectDisplayDialog();

        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_IN_EDIT_MODE_KEY, isEditing);
        bundle.putInt(MESSAGE_SPINNER_POSITION_KEY, messageSpinnerPositon);
        bundle.putInt(NUMBER_OF_MESSAGE_KEY, numberOfMessages);
        bundle.putInt(BOARD_TYPE_KEY, boardType);
        bundle.putInt(PRICE_SPINNER_POSITION_KEY, priceSpinnerPosition);
        bundle.putLong(BOARD_ID_KEY, boardId);
        priceSelectDisplayDialog.setArguments(bundle);
        return priceSelectDisplayDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SelectDisplayDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(context.toString() + "must implement SelectDisplayDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        isEditing = bundle.getBoolean(IS_IN_EDIT_MODE_KEY);
        boardType = bundle.getInt(BOARD_TYPE_KEY);
        numberOfMessages = bundle.getInt(NUMBER_OF_MESSAGE_KEY);
        messageSpinnerPositon = bundle.getInt(MESSAGE_SPINNER_POSITION_KEY);
        priceSpinnerPosition = bundle.getInt(PRICE_SPINNER_POSITION_KEY);
        boardId = bundle.getLong(BOARD_ID_KEY);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View spinnerView = layoutInflater.inflate(R.layout.dialog_message_spinner, null);
        appCompatImageView = (AppCompatImageView) spinnerView.findViewById(R.id.add_message_board_image);
        appCompatSpinner = (AppCompatSpinner) spinnerView.findViewById(R.id.message_board_spinner);
        textInputEditText = (TextInputEditText) spinnerView.findViewById(R.id.add_num_of_msgs);
        appCompatSpinner.setOnItemSelectedListener(this);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(spinnerView);
        alertDialogBuilder.setTitle(isEditing ? R.string.edit_boad : R.string.add_new_baord);
        alertDialogBuilder.setMessage("Message Board Type");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(boardType == EditTextDialog.EATRIES_TYPE ? getString(R.string.dialog_done) : getString(R.string.dialog_next), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                numberOfMessages = Integer.parseInt(textInputEditText.getText().toString());
                listener.onMessageDialogPositiveButtonClicked(MessageSelectDisplayDialog.this, messageSpinnerPositon, numberOfMessages,priceSpinnerPosition,  boardType, boardId, isEditing);

            }
        });

        if (isEditing) {
            textInputEditText.setText(String.valueOf(numberOfMessages));
            appCompatSpinner.setSelection(messageSpinnerPositon, true);
            alertDialogBuilder.setNeutralButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: Add save
                }
            });
        }

        alertDialogBuilder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = alertDialogBuilder.create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            messageSpinnerPositon = position + 1;
            setImageResource(messageSpinnerPositon);
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        } else {
            showToast();
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        messageSpinnerPositon = -100;
    }


    private void showToast() {
        Toast.makeText(getContext(), R.string.spinner_toast_select_one_type, Toast.LENGTH_LONG).show();
    }

    //used to change the image resource or video for the proto_simutation of the display
    private void setImageResource(int boardType) {
        // TODO: Implememt the method to change the image resouce
    }
}