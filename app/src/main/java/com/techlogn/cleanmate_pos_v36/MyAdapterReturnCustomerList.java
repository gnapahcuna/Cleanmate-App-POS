package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyAdapterReturnCustomerList extends ArrayAdapter {

    Fragment fragment;

    private Context mContext;
    private ArrayList<MyItemReturnCustomerList> mArrayList;
    private int mLayout;
    LayoutInflater inflater;
    View rowView, v;



    public MyAdapterReturnCustomerList(Context context, int layout, ArrayList<MyItemReturnCustomerList> arrayList) {
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
        TextView textOrderno = rowView.findViewById(R.id.orderNo);
        TextView textCustomer = rowView.findViewById(R.id.customer);


        MyFont myFont=new MyFont(mContext);
        textIndex.setTypeface(myFont.setFont());
        textOrderno.setTypeface(myFont.setFont());
        textCustomer.setTypeface(myFont.setFont());


        final String index = mArrayList.get(position).getIndex();
        final String orderNo = mArrayList.get(position).getOrderNo();
        final String customer = mArrayList.get(position).getCustomer();
        textIndex.setText(index + " .");
        textOrderno.setText(orderNo);
        textCustomer.setText(customer);

        Configuration config = mContext.getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 320||config.smallestScreenWidthDp >= 480) {
            int width =mContext.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((width*10)/100, 40, 0, 0);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins((width*30)/100, 40, 0, 0);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins((width*68)/100, 40, 0, 0);
            textIndex.setLayoutParams(layoutParams);
            textOrderno.setLayoutParams(layoutParams1);
            textCustomer.setLayoutParams(layoutParams2);
            //Toast.makeText(mContext,""+list.getLayoutParams().width,Toast.LENGTH_SHORT).show();
        }
        else {
            // fall-back code goes here
        }

        return rowView;
    }
}

