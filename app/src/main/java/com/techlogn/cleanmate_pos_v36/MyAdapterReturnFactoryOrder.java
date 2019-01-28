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
 * Created by anucha on 2/28/2018.
 */

public class MyAdapterReturnFactoryOrder extends ArrayAdapter {

    Fragment fragment;

    private Context mContext;
    private ArrayList<MyItemReturnFactoryOrder> mArrayList;
    private int mLayout;
    LayoutInflater inflater;
    View rowView, v;

    public MyAdapterReturnFactoryOrder(Context context, int layout, ArrayList<MyItemReturnFactoryOrder> arrayList) {
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
        TextView textProductTH = rowView.findViewById(R.id.textTH);
        TextView textProductEN = rowView.findViewById(R.id.textEN);
        TextView textBarcode = rowView.findViewById(R.id.textBarcode);


        MyFont myFont=new MyFont(mContext);
        textIndex.setTypeface(myFont.setFont());
        textBarcode.setTypeface(myFont.setFont());
        textProductTH.setTypeface(myFont.setFont());
        textProductTH.setTypeface(myFont.setFont());


        final String index = mArrayList.get(position).getIndex();
        final String productTH = mArrayList.get(position).getProductTH();
        final String productEN = mArrayList.get(position).getProductEN();
        final String barcode = mArrayList.get(position).getBarcode();

        textIndex.setText(index + " .");
        textProductTH.setText(productTH);
        textProductEN.setText(productEN);
        textBarcode.setText(barcode);

        return rowView;
    }
}

