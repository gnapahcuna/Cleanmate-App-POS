package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by anucha on 2/16/2018.
 */

public class ViewHolderSearch {
    public RoundedImageView image;
    public TextView textProductTH;
    public TextView textprice;
    public TextView textProductEN;
    public RelativeLayout viewColor;
    public ImageView more;
    public TextView serTH;
    public TextView serEN;
    public TextView serType;
    public TextView proID;
    public TextView string_image;
    public TextView color_code;
    public ImageView selector;
    public CheckBox checkBox;
    public TextView num,thb;

    public ViewHolderSearch(View convertView) {
        textProductTH = convertView.findViewById(R.id.text_catTH);
        textProductEN = convertView.findViewById(R.id.name_cateEN);
        textprice = convertView.findViewById(R.id.text_price);
        image = convertView.findViewById(R.id.image_product);
        //checkBox=convertView.findViewById(R.id.checkBox);
        viewColor = convertView.findViewById(R.id.view_color);
        more=convertView.findViewById(R.id.img_more);

        serTH=convertView.findViewById(R.id.textSerTH);
        serEN=convertView.findViewById(R.id.textSerEN);
        serType=convertView.findViewById(R.id.textSerType);
        proID=convertView.findViewById(R.id.textProID);
        string_image=convertView.findViewById(R.id.textImage);
        color_code=convertView.findViewById(R.id.textColor);

        num=convertView.findViewById(R.id.text_num);
        thb=convertView.findViewById(R.id.textTHB);

    }
}
