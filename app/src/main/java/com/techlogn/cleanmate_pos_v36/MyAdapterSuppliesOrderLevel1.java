package com.techlogn.cleanmate_pos_v36;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by anucha on 1/3/2018.
 */

public class MyAdapterSuppliesOrderLevel1 extends ArrayAdapter {

    ViewHolderSuppliesOrder vHolder;
    int mPosition;
    View v;
    Fragment fragment;
    private Context mContext;
    private ArrayList<CustomItemSuppliesOrder> items;
    private int mLayout;
    LayoutInflater inflater;

    public MyAdapterSuppliesOrderLevel1(Context context, int layout, ArrayList<CustomItemSuppliesOrder> arrayList) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        items = arrayList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            this.mPosition = position;

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayout, parent, false);
            vHolder = new ViewHolderSuppliesOrder(convertView);
            convertView.setTag(vHolder);

        } else {
            vHolder = (ViewHolderSuppliesOrder) convertView.getTag();
        }

        final CustomItemSuppliesOrder item = items.get(position);
        vHolder.textProductTH.setText(item.mSuppliesNameTH);
        DecimalFormat formatter = new DecimalFormat("#,###,###.##");
        String prices = formatter.format(Double.parseDouble(item.mTextPrice));
        vHolder.textprice.setText(prices);

        vHolder.num.setText(item.mNum);
        vHolder.thb.setText("บาท");
        return convertView;
    }
}