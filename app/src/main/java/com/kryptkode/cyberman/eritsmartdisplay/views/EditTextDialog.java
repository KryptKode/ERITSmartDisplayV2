package com.kryptkode.cyberman.eritsmartdisplay.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.R;
import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;

/**
 * Created by Cyberman on 8/22/2017.
 */

public class EditTextDialog extends DialogFragment implements TextWatcher {


    public static final String IS_IN_EDIT_MODE_KEY = "positive_button";
    public static final String PRICE_BOARD_KEY = "price_board_key";

    private EditTextDialogListener listener;

    private AlertDialog dialog;
    private TextInputEditText nameTextInputEditText;
    private TextInputEditText ipAddressTextInputEditText;
    private AppCompatSpinner messageTypeSpinner;
    private TextInputEditText messageCountEditText;
    private AppCompatImageView messagePreviewImageView;

    private PriceBoard priceBoard;

    private boolean isEditing;



   /* public static EditTextDialog getInstance(String boardName, String boardIpAddress, boolean isEditing, int boardType) {
        EditTextDialog editTextDialog = new EditTextDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_IN_EDIT_MODE_KEY, isEditing);
        bundle.putString(NAME_KEY, boardName);
        bundle.putString(IP_ADDRESS_KEY, boardIpAddress);
        bundle.putInt(BOARD_TYPE_KEY, boardType);
        editTextDialog.setArguments(bundle);
        return editTextDialog;
    }*/

    public static EditTextDialog getInstance(PriceBoard priceBoard, boolean isEditing) {
        EditTextDialog editTextDialog = new EditTextDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_IN_EDIT_MODE_KEY, isEditing);
       bundle.putParcelable(PRICE_BOARD_KEY, priceBoard);
        editTextDialog.setArguments(bundle);

        return editTextDialog;
    }

    public void setImageResource(int imageResource) {
    }

    public interface EditTextDialogListener {

        void onDialogPositiveButtonClicked(DialogFragment dialog, PriceBoard priceBoard, boolean isEditing);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (EditTextDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(context.toString() + "must implement EditTextDialogListener");
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
        priceBoard = bundle.getParcelable(PRICE_BOARD_KEY);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_edit_text, null);
        nameTextInputEditText = (TextInputEditText) view.findViewById(R.id.add_boardName_et);
        ipAddressTextInputEditText = (TextInputEditText) view.findViewById(R.id.add_ip_et);
        ipAddressTextInputEditText.addTextChangedListener(this);


        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (String split : splits) {
                            if (Integer.valueOf(split) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        ipAddressTextInputEditText.setFilters(filters);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle(isEditing ? getString(R.string.edit_boad) : getString(R.string.add_new_baord));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                priceBoard.setName( nameTextInputEditText.getText().toString());
                priceBoard.setIpAddress(ipAddressTextInputEditText.getText().toString());

                if (getResources().getBoolean(R.bool.sw_700_device)) {

                    priceBoard.setNumberOfMessages(Integer.parseInt(messageCountEditText.getText().toString()));
                }

                listener.onDialogPositiveButtonClicked(EditTextDialog.this, priceBoard, isEditing);
            }
        });
        if (isEditing) {
            builder.setNeutralButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: Add save
                }
            });
        }
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();


        //initially disable the button
//        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        return dialog;
    }

    private void showToast() {
        Toast.makeText(getContext(), R.string.spinner_toast_select_one_type, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);

        if (isEditing) {
            nameTextInputEditText.setText(priceBoard.getName());
            ipAddressTextInputEditText.setText(priceBoard.getIpAddress());
        }

        //for large-screen devices
        if (getResources().getBoolean(R.bool.sw_700_device)) {
            messageTypeSpinner = (AppCompatSpinner) getView().findViewById(R.id.message_board_spinner);
            messageTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            });
            messagePreviewImageView = (AppCompatImageView) getView().findViewById(R.id.add_message_board_image);
            messageCountEditText = (TextInputEditText) getView().findViewById(R.id.add_num_of_msgs);
            if (isEditing) {
                messageTypeSpinner.setSelection(priceBoard.getMessageBoardType().getNumberOfCascades(), true);
                messageCountEditText.setText(priceBoard.getNumberOfMessages());
            }

        }
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

}
