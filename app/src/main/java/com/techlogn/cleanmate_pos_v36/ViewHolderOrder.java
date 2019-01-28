package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by anucha on 2/20/2018.
 */

public class ViewHolderOrder {
    public RoundedImageView image;
    public TextView textProductTH;
    public TextView textprice;
    public TextView textProductEN;
    public RelativeLayout viewColor;
    public ImageView more;
    public TextView textCount,thb;
    public CheckBox checkBox;

    public ViewHolderOrder(View convertView) {
        textProductTH = convertView.findViewById(R.id.text_catTH);
        textProductEN = convertView.findViewById(R.id.name_cateEN);
        textprice = convertView.findViewById(R.id.text_price);
        image = convertView.findViewById(R.id.image_product);
        viewColor = convertView.findViewById(R.id.view_color);
        more=convertView.findViewById(R.id.img_more);
        //textSerTH=convertView.findViewById(R.id.price_search);
        //textSerEN=convertView.findViewById(R.id.serviceEN);
        textCount=convertView.findViewById(R.id.count);
        thb=convertView.findViewById(R.id.textTHB);
        checkBox=convertView.findViewById(R.id.check2);
    }
}
