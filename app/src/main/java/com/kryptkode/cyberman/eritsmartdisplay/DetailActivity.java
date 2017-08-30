package com.kryptkode.cyberman.eritsmartdisplay;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;
import com.kryptkode.cyberman.eritsmartdisplay.views.EditTextDialog;

import static com.kryptkode.cyberman.eritsmartdisplay.HomeFragmentHelper.EXTRA_KEY;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Uri uri = getIntent().getData();
        PriceBoard priceBoard = getIntent().getParcelableExtra(EXTRA_KEY);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.detail_root, DetailFragment.getInstance(uri,  priceBoard.getPriceBoardType() == PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE ?
                EditTextDialog.EATRIES_TYPE : EditTextDialog.FILLING_STATION_TYPE));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();

    }
}
