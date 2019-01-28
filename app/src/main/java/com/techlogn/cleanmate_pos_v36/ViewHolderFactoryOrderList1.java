package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.TextView;

/**
 * Created by anucha on 2/27/2018.
 */

public class ViewHolderFactoryOrderList1 {
    public TextView textOrderNo;
    public TextView textDate;
    public TextView textStatus;

    public ViewHolderFactoryOrderList1(View convertView) {
        textOrderNo = convertView.findViewById(R.id.orderNo);
        textDate = convertView.findViewById(R.id.date);
        textStatus = convertView.findViewById(R.id.status);
    }
}
