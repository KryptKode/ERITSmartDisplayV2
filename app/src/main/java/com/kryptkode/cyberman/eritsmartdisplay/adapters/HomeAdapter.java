package com.kryptkode.cyberman.eritsmartdisplay.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kryptkode.cyberman.eritsmartdisplay.R;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    public static final String TAG = HomeAdapter.class.getSimpleName();
    public static final int IMG_TAG = 1;
    private HomeAdapterListener homeAdapterListener;
    private Context context;
    private LayoutInflater layoutInflater;
    private Cursor mCursor;

    public HomeAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeViewHolder(layoutInflater.inflate(R.layout.home_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        PriceBoard priceBoard = getItem(position);
        ImageView displayAvatar = holder.displayAvatar;
        TextView displayName = holder.displayName;
        ImageButton displayOverflowButton = holder.displayOverflowButton;
        Log.i(TAG, String.valueOf("onBindViewHolder: " + (priceBoard == null)));
        //set the display name to the name if the user entered it, else, use the IP
        if (priceBoard != null) {
            if (priceBoard.getPriceName() != null || !TextUtils.isEmpty(priceBoard.getName())) {
                displayName.setText(priceBoard.getPriceName());
                Log.i(TAG, "onBindViewHolder: " + displayName.getText());
            }else{
                displayName.setText(priceBoard.getPriceIpAddress());
                Log.i(TAG, "onBindViewHolder: " + displayName.getText());
            }
            holder.itemView.setTag(priceBoard.getPriceId());
            displayOverflowButton.setTag(priceBoard.getPriceId());
        }

        //TODO Select the Display Image based on the Board Type


    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public PriceBoard getItem(int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid item position requested");
        }

        try {
            return new PriceBoard(mCursor);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public interface HomeAdapterListener {
        void onDisplayClicked(long  id);

        void onDisplayOverflowClicked(long id, View view);
    }

    public void setHomeAdapterListener(HomeAdapterListener homeAdapterListener) {
        this.homeAdapterListener = homeAdapterListener;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView displayAvatar;
        TextView displayName;
        ImageButton displayOverflowButton;

        public HomeViewHolder(View itemView) {
            super(itemView);
            displayAvatar = (ImageView) itemView.findViewById(R.id.display_imageView);
            displayName = (TextView) itemView.findViewById(R.id.display_name_textView);
            displayOverflowButton = (ImageButton) itemView.findViewById(R.id.display_overflow);

            itemView.setOnClickListener(this);
            displayOverflowButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == displayOverflowButton) {
                homeAdapterListener.onDisplayOverflowClicked((Long) v.getTag(), v);
            } else {
                    homeAdapterListener.onDisplayClicked((Long) v.getTag());
            }
        }
    }


    public void swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (this.mCursor == c) {
            return; // bc nothing has changed
        }
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
    }
}

