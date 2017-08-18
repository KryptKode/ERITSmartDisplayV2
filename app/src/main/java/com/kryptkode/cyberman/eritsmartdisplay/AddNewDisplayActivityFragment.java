package com.kryptkode.cyberman.eritsmartdisplay;

import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddNewDisplayActivityFragment extends Fragment {
    public static final String WHICH_DISPLAY = "which_display";
    private int whichDisplay; //1=filling station, 2=eatries
    private TextInputEditText boardNameEditText;
    private TextInputEditText ipAddressEditText;
    private AppCompatSpinner messageBoardSpinner;
    private AppCompatImageView messageBoardImageView;
    private AppCompatSpinner priceBoardSpinner;
    private AppCompatImageView priceBoardImageView;

    public AddNewDisplayActivityFragment() {
    }


    public static AddNewDisplayActivityFragment getInstance(int extra){
        AddNewDisplayActivityFragment addNewDisplayActivityFragment =
                new AddNewDisplayActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(WHICH_DISPLAY, extra);
        addNewDisplayActivityFragment.setArguments(bundle);
        return  addNewDisplayActivityFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        whichDisplay = getArguments().getInt(WHICH_DISPLAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_display, container, false);
        boardNameEditText = (TextInputEditText) view.findViewById(R.id.add_boardName_et);
        ipAddressEditText = (TextInputEditText) view.findViewById(R.id.add_ip_et);
        messageBoardSpinner = (AppCompatSpinner) view.findViewById(R.id.message_board_spinner);
        messageBoardImageView = (AppCompatImageView) view.findViewById(R.id.add_message_board_image);
        priceBoardImageView = (AppCompatImageView) view.findViewById(R.id.add_price_board_image);
        priceBoardSpinner = (AppCompatSpinner) view.findViewById(R.id.price_board_spinner);

        switch (whichDisplay){
            case 1: //filling station

                break;
            case 2://eatries
                AddNewDisplayHelper.toggleViewVisibility //do not show
                        (new View[]{priceBoardImageView, priceBoardSpinner}, false);
                break;
        }


        return view;
    }

}
