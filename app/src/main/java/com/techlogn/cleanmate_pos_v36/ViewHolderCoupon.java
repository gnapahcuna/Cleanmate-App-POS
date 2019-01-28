package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by anucha on 2/24/2018.
 */

public class ViewHolderCoupon {

    public TextView textNo;
    public TextView textPrice;
    public TextView textCouponNo;
    public ImageView clear;


    public ViewHolderCoupon(View convertView) {
        textCouponNo = convertView.findViewById(R.id.text_coupon_no);
        textPrice = convertView.findViewById(R.id.text_value);
        clear = convertView.findViewById(R.id.img_clear);
        textNo=convertView.findViewById(R.id.text_num);

    }
}
