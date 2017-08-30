package com.kryptkode.cyberman.eritsmartdisplay.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.R;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;

/**
 * Created by Cyberman on 8/22/2017.
 */

public class PriceSelectDisplayDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    public static final String IS_IN_EDIT_MODE_KEY = "board_type";
    public static final String BOARD_KEY = "bpard";

    private PriceSelectDisplayDialogListener listener;

    private AppCompatSpinner appCompatSpinner;
    private AppCompatImageView appCompatImageView;


    private boolean isEditing;
    private boolean hasUserSelected;
    private PriceBoard priceBoard;

    public interface PriceSelectDisplayDialogListener {
        void onPriceDialogPositiveButtonClicked(DialogFragment dialog, PriceBoard priceBoard, boolean isEditing);
    }

    public static PriceSelectDisplayDialog getInstance(PriceBoard priceBoard,  boolean isEditing) {
        PriceSelectDisplayDialog priceSelectDisplayDialog = new PriceSelectDisplayDialog();

        Bundle bundle = new Bundle();
        bundle.putParcelable(BOARD_KEY, priceBoard);
        bundle.putBoolean(IS_IN_EDIT_MODE_KEY, isEditing);
        priceSelectDisplayDialog.setArguments(bundle);
        return priceSelectDisplayDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PriceSelectDisplayDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(context.toString() + "must implement PriceSelectDisplayDialoglListener");
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
        priceBoard = bundle.getParcelable(BOARD_KEY);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View spinnerView = layoutInflater.inflate(R.layout.dialog_price_spinner, null);
        appCompatImageView = (AppCompatImageView) spinnerView.findViewById(R.id.dialog_add_price_board_image);
        appCompatSpinner = (AppCompatSpinner) spinnerView.findViewById(R.id.dialog_price_board_spinner);
        appCompatSpinner.setOnItemSelectedListener(this);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(spinnerView);
        alertDialogBuilder.setTitle(isEditing ? R.string.edit_boad : R.string.add_new_baord);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.dialog_done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(hasUserSelected){
                    listener.onPriceDialogPositiveButtonClicked(PriceSelectDisplayDialog.this, priceBoard, isEditing);
                }else {
                    showToast();
                }
            }
        });

        if(isEditing){
            appCompatSpinner.setSelection(priceBoard.getPriceBoardType().getNumberOfCascades(), true);
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

        return alertDialogBuilder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            try {
                priceBoard.setPriceBoardType(PriceBoard.getPriceBoardTypeFromInt( position));
                setImageResource(priceBoard.getPriceBoardType().getNumberOfCascades());
                hasUserSelected = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
           showToast();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void showToast(){
        Toast.makeText(getContext(), R.string.spinner_toast_select_one_type, Toast.LENGTH_LONG).show();
    }

    //used to change the image resource or video for the proto_simutation of the display
    private void setImageResource(int priceSpinnerPosition) {
        // TODO: Implememt the method to change the image resouce
    }
}
