package com.techlogn.cleanmate_pos_v36;

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
 * Created by anucha on 2/1/2018.
 */

public class MyAdapterBarcodeOrderDetail extends ArrayAdapter {
    private Context mContext;
    private ArrayList<MyItemBarcodeOrderDetail> mArrayList;
    private int mLayout;

    public MyAdapterBarcodeOrderDetail(Context context, int layout, ArrayList<MyItemBarcodeOrderDetail> arrayList) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(mLayout, parent, false);
        }
        TextView productTH = rowView.findViewById(R.id.text_order);
        TextView barcode = rowView.findViewById(R.id.text_barcode_scan);


        productTH.setText(mArrayList.get(position).getProductTH());
        barcode.setText(mArrayList.get(position).getBarcode());

        System.out.println("IsCheck : "+mArrayList.get(position).getIsCheck());

        MyFont myFont =new MyFont(getContext());
        productTH.setTypeface(myFont.setFont());
        barcode.setTypeface(myFont.setFont());


        return rowView;
    }
}
