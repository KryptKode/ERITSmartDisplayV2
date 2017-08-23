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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.kryptkode.cyberman.eritsmartdisplay.R;

/**
 * Created by Cyberman on 8/22/2017.
 */

public class EditTextDialog extends DialogFragment implements TextWatcher {
    public static final String DISPLAY_MESSAGE_KEY = "message";
    public static final String POSITIVE_BUTTOn_KEY = "positive_button";
    public static final String IP_ADDRESS_KEY = "ip";
    public static final String NAME_KEY = "name";
    public static final String TEXT = "text";

    private EditTextDialogListener listener;

    private AlertDialog dialog;
    private TextInputEditText nameTextInputEditText;
    private TextInputEditText ipAddressTextInputEditText;

    private String dialogTitle;
    private boolean isEditing;
    private String boardName;
    private String boardIpAddress;


    public static EditTextDialog getInstance( String boardName, String boardIpAddress, boolean isEditing) {
        EditTextDialog editTextDialog = new EditTextDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(POSITIVE_BUTTOn_KEY, isEditing);
        bundle.putString(NAME_KEY, boardName);
        bundle.putString(IP_ADDRESS_KEY, boardIpAddress);
        editTextDialog.setArguments(bundle);

        return editTextDialog;
    }

    public interface EditTextDialogListener {

        void onDialogPositiveButtonClicked(DialogFragment dialog, String boardName, String boardIpAddress);
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
        isEditing = bundle.getBoolean(POSITIVE_BUTTOn_KEY);
        boardName = bundle.getString(NAME_KEY);
        boardIpAddress = bundle.getString(IP_ADDRESS_KEY);
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
        builder.setTitle(isEditing ? getString(R.string.edit_boad): getString(R.string.add_new_baord) );
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boardName = nameTextInputEditText.getText().toString();
                boardIpAddress = ipAddressTextInputEditText.getText().toString();
                listener.onDialogPositiveButtonClicked(EditTextDialog.this, boardName, boardIpAddress);
            }
        });
        if(isEditing){
            builder.setNeutralButton(R.string.R_id_save, new DialogInterface.OnClickListener() {
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

    @Override
    public void onStart() {
        super.onStart();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)){
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }else{
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }

}
