package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by anucha on 5/8/2018.
 */

public class MyAdapterReport extends ArrayAdapter {
    Fragment fragment;
    private Context mContext;
    private ArrayList<CustomItemReport> items;
    private int mLayout;
    LayoutInflater inflater;
    View  v;
    ViewHolderReport vHolder;

    public MyAdapterReport(Context context, int layout, ArrayList<CustomItemReport> arrayList) {
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
            vHolder = new ViewHolderReport(convertView);
            convertView.setTag(vHolder);

        } else {
            vHolder = (ViewHolderReport)convertView.getTag();
        }
        MyFont myFont=new MyFont(mContext);
        final CustomItemReport item = items.get(position);
        vHolder.mText.setText(item.mText);
        vHolder.mText.setTypeface(myFont.setFont());


        return convertView;
    }
}
