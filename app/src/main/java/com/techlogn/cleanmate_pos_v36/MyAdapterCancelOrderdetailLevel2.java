package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyAdapterCancelOrderdetailLevel2 extends ArrayAdapter {

    Fragment fragment;

    private Context mContext;
    private ArrayList<MyItemCancelOrderdetailLevel2> mArrayList;
    private int mLayout;
    LayoutInflater inflater;
    View rowView, v;



    public MyAdapterCancelOrderdetailLevel2(Context context, int layout, ArrayList<MyItemCancelOrderdetailLevel2> arrayList) {
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
        TextView textBarcode = rowView.findViewById(R.id.orderNo);


        MyFont myFont=new MyFont(mContext);
        textIndex.setTypeface(myFont.setFont());
        textBarcode.setTypeface(myFont.setFont());


        final String index = mArrayList.get(position).getIndex();
        final String barcode = mArrayList.get(position).getBarcode();
        textIndex.setText(index + " .");
        textBarcode.setText(barcode);

        return rowView;
    }
}

