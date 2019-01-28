package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.TextView;

/**
 * Created by anucha on 2/28/2018.
 */

public class ViewHolderCancelOrderList {
    public TextView textIndex;
    public TextView textOrderNo;
    public TextView textDate;
    public TextView textStatus;
    //public Button cancel;

    public ViewHolderCancelOrderList(View convertView) {
        textIndex = convertView.findViewById(R.id.index);
        textOrderNo = convertView.findViewById(R.id.orderNo);
        textDate = convertView.findViewById(R.id.date);
        textStatus = convertView.findViewById(R.id.status);
        //cancel = convertView.findViewById(R.id.cancel);
    }
}
