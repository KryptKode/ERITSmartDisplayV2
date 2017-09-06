package com.kryptkode.cyberman.eritsmartdisplay;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements View.OnClickListener{

    public static final String URL = "http://www.erit.com.ng";

    private AppCompatImageView eritLogoImageView;
    private TextView eritWebsiteTextView;
    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment getInstance(){
        return  new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        eritLogoImageView = (AppCompatImageView) view .findViewById(R.id.erit_logo);
        eritWebsiteTextView = (TextView) view.findViewById(R.id.erit_site);

        eritWebsiteTextView.setMovementMethod(LinkMovementMethod.getInstance());
        eritLogoImageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse(URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(intent, getString(R.string.open_with)));
    }
}
