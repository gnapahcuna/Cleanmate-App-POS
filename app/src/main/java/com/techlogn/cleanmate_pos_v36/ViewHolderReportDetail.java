package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.TextView;

/**
 * Created by anucha on 5/8/2018.
 */

public class ViewHolderReportDetail {

    public TextView mIndex;
    public TextView mOrderNo;
    public TextView mTotal;

    public ViewHolderReportDetail(View convertView) {
        mIndex = convertView.findViewById(R.id.index);
        mOrderNo = convertView.findViewById(R.id.orderNo);
        mTotal = convertView.findViewById(R.id.total);
    }
}

