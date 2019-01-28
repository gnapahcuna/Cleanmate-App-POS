package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by anucha on 5/8/2018.
 */

public class ViewHolderReport {

    public TextView mText;

    public ViewHolderReport(View convertView) {
        mText = convertView.findViewById(R.id.text);
    }
}

