package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by anucha on 1/4/2018.
 */

public class MyAdapterCategory extends ArrayAdapter {

    Fragment fragment;

    private Context mContext;
    private ArrayList<MyItemCategory> mArrayList;
    private int mLayout;

    private int mCount=0;
    private int num=0;

    EditText count;
    LayoutInflater inflater;
    View rowView;
    String result;

    public MyAdapterCategory(Context context, int layout, ArrayList<MyItemCategory> arrayList) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        mArrayList = arrayList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        rowView = convertView;
        if (rowView == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(mLayout, parent, false);

        }

        TextView textCateTH = rowView.findViewById(R.id.text_catTH);
        View viewColor = rowView.findViewById(R.id.view_color);

        final String nameTH=mArrayList.get(position).getProductTH();
        textCateTH.setText(nameTH+" ( "+mArrayList.get(position).getProductEN()+" )");


        String color = mArrayList.get(position).getColor();
        viewColor.setBackgroundColor(Color.parseColor(color));

        MyFont myFont=new MyFont(getContext());
        textCateTH.setTypeface(myFont.setFont());

        return rowView;
    }
}
