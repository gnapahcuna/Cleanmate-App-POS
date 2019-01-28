package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyAdapterCheckDelivery extends ArrayAdapter {

    Fragment fragment;
    private Context mContext;
    private ArrayList<MyItemCheckDelivery> mArrayList;
    private int mLayout;
    LayoutInflater inflater;
    View rowView, v;

    public MyAdapterCheckDelivery(Context context, int layout, ArrayList<MyItemCheckDelivery> arrayList) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        rowView = convertView;
        v = convertView;
        if (rowView == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(mLayout, parent, false);

        }

        TextView textIndex = rowView.findViewById(R.id.index);
        final TextView textCust = rowView.findViewById(R.id.cust_name);
        final TextView textOrderNo = rowView.findViewById(R.id.orderNo);
        final RelativeLayout status = rowView.findViewById(R.id.rl_status);
        MyFont myFont=new MyFont(mContext);
        textIndex.setTypeface(myFont.setFont());
        textOrderNo.setTypeface(myFont.setFont());
        textCust.setTypeface(myFont.setFont());



        final String index = mArrayList.get(position).getIndex();
        final String orderNo = mArrayList.get(position).getFactoryOrder();
        final String cust = mArrayList.get(position).getCust();
        final int state = Integer.parseInt(mArrayList.get(position).getStatus());
        if(state==1) {
            status.setBackgroundColor(Color.parseColor("#00b359"));
        }else{
            status.setBackgroundColor(Color.parseColor("#e60000"));
        }

        textIndex.setText(index + ".");
        textOrderNo.setText(orderNo);
        textCust.setText(cust);


        Configuration config = mContext.getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 320||config.smallestScreenWidthDp >= 480) {
            int width =mContext.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((width*3)/100, 30, 0, 0);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins((width*25)/100, 30, 0, 0);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins((width*60)/100, 30, 0, 0);
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams3.setMargins((width*75)/100, 5, 0, 0);
            RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams4.setMargins((width*85)/100, 5, 0, 0);
            textIndex.setLayoutParams(layoutParams);
            textCust.setLayoutParams(layoutParams1);
            textOrderNo.setLayoutParams(layoutParams2);
            //Toast.makeText(mContext,""+list.getLayoutParams().width,Toast.LENGTH_SHORT).show();
        }
        else {
            // fall-back code goes here
        }


        return rowView;
    }
}
