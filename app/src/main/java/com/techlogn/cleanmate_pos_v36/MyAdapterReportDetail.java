package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by anucha on 5/8/2018.
 */

public class MyAdapterReportDetail extends ArrayAdapter {
    Fragment fragment;
    private Context mContext;
    private ArrayList<CustomItemReportDetail> items;
    private int mLayout;
    LayoutInflater inflater;
    View  v;
    ViewHolderReportDetail vHolder;

    public MyAdapterReportDetail(Context context, int layout, ArrayList<CustomItemReportDetail> arrayList) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        items = arrayList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /*rowView = convertView;
        v = convertView;*/
        if(convertView == null) {
            /*inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(mLayout, parent, false);*/

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayout, parent, false);
            vHolder = new ViewHolderReportDetail(convertView);
            convertView.setTag(vHolder);

        } else {
            vHolder = (ViewHolderReportDetail)convertView.getTag();
        }
        MyFont myFont=new MyFont(mContext);
        final CustomItemReportDetail item = items.get(position);
        vHolder.mIndex.setText(""+item.sIndex);
        vHolder.mOrderNo.setText(""+item.sOrderNo);
        try {
            vHolder.mTotal.setText(""+getFormatedAmount(Integer.parseInt(item.sTotal))+" บาท");
        }catch (Exception ex){
            vHolder.mTotal.setText(""+getFormatedAmount(0)+" บาท");
        }

        vHolder.mIndex.setTextColor(Color.BLACK);
        vHolder.mOrderNo.setTextColor(Color.BLACK);
        vHolder.mTotal.setTextColor(Color.BLACK);
        if(Integer.parseInt(item.sIsCancel)==0){
            vHolder.mIndex.setTextColor(Color.RED);
            vHolder.mOrderNo.setTextColor(Color.RED);
            vHolder.mTotal.setTextColor(Color.RED);
        }else if(Integer.parseInt(item.sIsPayment)==0){
            vHolder.mIndex.setTextColor(Color.parseColor("#ffcc00"));
            vHolder.mOrderNo.setTextColor(Color.parseColor("#ffcc00"));
            vHolder.mTotal.setTextColor(Color.parseColor("#ffcc00"));
        }else{
            vHolder.mIndex.setTextColor(Color.BLACK);
            vHolder.mOrderNo.setTextColor(Color.BLACK);
            vHolder.mTotal.setTextColor(Color.BLACK);
        }


        vHolder.mIndex.setTypeface(myFont.setFont());
        vHolder.mOrderNo.setTypeface(myFont.setFont());
        vHolder.mTotal.setTypeface(myFont.setFont());

        Configuration config = mContext.getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 320||config.smallestScreenWidthDp >= 480) {
            int width =mContext.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((width*8)/100, 40, 0, 0);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins((width*30)/100, 40, 0, 0);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins((width*65)/100, 40, 0, 0);
            vHolder.mIndex.setLayoutParams(layoutParams);
            vHolder.mOrderNo.setLayoutParams(layoutParams1);
            vHolder.mTotal.setLayoutParams(layoutParams2);
            //Toast.makeText(mContext,""+list.getLayoutParams().width,Toast.LENGTH_SHORT).show();
        }
        else {
            // fall-back code goes here
        }


        return convertView;
    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
