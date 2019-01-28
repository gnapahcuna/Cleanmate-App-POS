package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyAdapterFactoryProductList extends ArrayAdapter {

    Fragment fragment;

    private Context mContext;
    private ArrayList<MyItemFactoryProductList> mArrayList;
    private int mLayout;
    LayoutInflater inflater;
    View rowView, v;



    public MyAdapterFactoryProductList(Context context, int layout, ArrayList<MyItemFactoryProductList> arrayList) {
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
        TextView textNameTH = rowView.findViewById(R.id.textTH);
        TextView textNameEN = rowView.findViewById(R.id.textEN);
        TextView textNum = rowView.findViewById(R.id.textBarcode);
        TextView textID=rowView.findViewById(R.id.textID);
        ImageView img=rowView.findViewById(R.id.img_chk);

        textID.setVisibility(View.GONE);

        MyFont myFont=new MyFont(mContext);
        textIndex.setTypeface(myFont.setFont());
        textNameTH.setTypeface(myFont.setFont());
        textNameEN.setTypeface(myFont.setFont());
        textNum.setTypeface(myFont.setFont());


        final String index = mArrayList.get(position).getIndex();
        final String nameTH = mArrayList.get(position).getNameTH();
        final String nameEN = mArrayList.get(position).getNameEN();
        final String num1 = mArrayList.get(position).getNum1();
        final String num2 = mArrayList.get(position).getNum2();
        final String proID = mArrayList.get(position).getProID();
        final String bar1=mArrayList.get(position).getBar1();

        textIndex.setText(index + " .");
        textNameTH.setText(nameTH);
        textNameEN.setText(nameEN);
        textNum.setText("("+bar1+"/"+num2+")");
        textID.setText(proID);

        if(Integer.parseInt(bar1)==Integer.parseInt(num2)){
            img.setVisibility(View.VISIBLE);
        }else{
            img.setVisibility(View.GONE);
        }





        return rowView;
    }
}

