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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.R;
import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;

/**
 * Created by Cyberman on 8/22/2017.
 */

public class MessageSelectDisplayDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, TextWatcher {
    public static final String IS_IN_EDIT_MODE_KEY = "positive_button";
    public static final String PRICE_TYPE = "board_type";

    private SelectDisplayDialogListener listener;

    private AlertDialog dialog;
    private AppCompatSpinner appCompatSpinner;
    private AppCompatImageView appCompatImageView;
    private TextInputEditText textInputEditText;


    private boolean isEditing;
    private PriceBoard priceBoard;
    private boolean status;


    public interface SelectDisplayDialogListener {
        void onMessageDialogPositiveButtonClicked(DialogFragment dialog, PriceBoard priceBoard, boolean isEditing);
    }

    public static MessageSelectDisplayDialog getInstance(PriceBoard priceBoard, boolean isEditing) {
        MessageSelectDisplayDialog messageSelectDisplayDialog = new MessageSelectDisplayDialog();

        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_IN_EDIT_MODE_KEY, isEditing);
        bundle.putParcelable(PRICE_TYPE, priceBoard);
        messageSelectDisplayDialog.setArguments(bundle);
        return messageSelectDisplayDialog;
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
        priceBoard = bundle.getParcelable(PRICE_TYPE);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View spinnerView = layoutInflater.inflate(R.layout.dialog_message_spinner, null);
        appCompatImageView = (AppCompatImageView) spinnerView.findViewById(R.id.add_message_board_image);
        appCompatSpinner = (AppCompatSpinner) spinnerView.findViewById(R.id.message_board_spinner);
        textInputEditText = (TextInputEditText) spinnerView.findViewById(R.id.add_num_of_msgs);
        textInputEditText.addTextChangedListener(this);
        appCompatSpinner.setOnItemSelectedListener(this);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(spinnerView);
        alertDialogBuilder.setTitle(isEditing ? R.string.edit_boad : R.string.add_new_baord);
        alertDialogBuilder.setMessage("Message Board Type");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(priceBoard.getPriceBoardType() == PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE? getString(R.string.dialog_done) : getString(R.string.dialog_next), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                 priceBoard.setNumberOfMessages(Integer.parseInt(textInputEditText.getText().toString()));
                listener.onMessageDialogPositiveButtonClicked(MessageSelectDisplayDialog.this, priceBoard, isEditing);

            }
        });

        if (isEditing) {
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

        if(isEditing){
            textInputEditText.setText(String.valueOf(priceBoard.getNumberOfMessages()));
            appCompatSpinner.setSelection(priceBoard.getMessageBoardType().getNumberOfCascades() - 1, true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            try {
                priceBoard.setMessageBoardType(MessageBoard.getMessageBoardTypeFromInt(position + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            setImageResource(priceBoard.getMessageBoardType().getNumberOfCascades());
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        } else {
            showToast();
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }


    private void showToast() {
        Toast.makeText(getContext(), R.string.spinner_toast_select_one_type_show_msg, Toast.LENGTH_LONG).show();
    }

    //used to change the image resource or video for the proto_simutation of the display
    private void setImageResource(int boardType) {
        // TODO: Implememt the method to change the image resouce
    }
}