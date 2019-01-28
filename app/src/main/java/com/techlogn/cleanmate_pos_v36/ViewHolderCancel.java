package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.TextView;

/**
 * Created by anucha on 5/8/2018.
 */

public class ViewHolderCancel {

    public TextView mText;

    public ViewHolderCancel(View convertView) {
        mText = convertView.findViewById(R.id.text);
    }
}

