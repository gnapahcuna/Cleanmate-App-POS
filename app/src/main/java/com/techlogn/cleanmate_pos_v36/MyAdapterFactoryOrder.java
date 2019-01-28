package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

public class MyAdapterFactoryOrder extends ArrayAdapter {

    Fragment fragment;
    private Context mContext;
    private ArrayList<MyItemFactoryOrder> mArrayList;
    private int mLayout;
    LayoutInflater inflater;
    View rowView, v;
    String mBranchID,searchtext,mType;

    public MyAdapterFactoryOrder(Context context, int layout, ArrayList<MyItemFactoryOrder> arrayList,String branchID,String search,String type) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        mArrayList = arrayList;
        mBranchID=branchID;
        searchtext=search;
        mType=type;

        //keys=key;
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
        final TextView textOrderNo = rowView.findViewById(R.id.orderNo);
        final TextView textDate = rowView.findViewById(R.id.date);
        MyFont myFont=new MyFont(mContext);
        textIndex.setTypeface(myFont.setFont());
        textOrderNo.setTypeface(myFont.setFont());
        textDate.setTypeface(myFont.setFont());


        final String index = mArrayList.get(position).getIndex();
        final String orderNo = mArrayList.get(position).getFactoryOrder();
        final String date = mArrayList.get(position).getDate();

        textIndex.setText(index + " .");
        textOrderNo.setText(orderNo);
        textDate.setText(date);


        ImageView img_chk=rowView.findViewById(R.id.img_chk);
        String isBar1 = mArrayList.get(position).getmBarcod();
        String isBar2 = mArrayList.get(position).getmBarcodPackage();
        if(isBar1.trim().equals("0")&&isBar2.trim().equals("0")){
            img_chk.setVisibility(View.GONE);
        }else {
            img_chk.setVisibility(View.VISIBLE);
        }
        /*ImageView barcode=rowView.findViewById(R.id.btn_barcode);
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,BarcodePayment.class);
                i.putExtra("orderNo",textOrderNo.getText().toString());
                i.putExtra("branchID",mBranchID);
                mContext.startActivity(i);
            }
        });*/
        System.out.println("searchtext : "+searchtext);
        ImageView img_preview=rowView.findViewById(R.id.img_preview);
        img_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(mContext,PreviewActivity.class);
                in.putExtra("key","manage");
                in.putExtra("orderNo",orderNo);
                in.putExtra("branchID",mBranchID);
                in.putExtra("searchtext", searchtext);
                in.putExtra("type", mType);
                //in.putExtra("Search",keys);
                mContext.startActivity(in);
            }
        });


        Configuration config = mContext.getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 320||config.smallestScreenWidthDp >= 480) {
            int width =mContext.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((width*3)/100, 40, 0, 0);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins((width*15)/100, 40, 0, 0);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins((width*45)/100, 40, 0, 0);
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams3.setMargins((width*75)/100, 5, 0, 0);
            RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams4.setMargins((width*85)/100, 5, 0, 0);
            textIndex.setLayoutParams(layoutParams);
            textOrderNo.setLayoutParams(layoutParams1);
            textDate.setLayoutParams(layoutParams2);
            img_preview.setLayoutParams(layoutParams3);
            img_chk.setLayoutParams(layoutParams4);
            //Toast.makeText(mContext,""+list.getLayoutParams().width,Toast.LENGTH_SHORT).show();
        }
        else {
            // fall-back code goes here
        }


        return rowView;
    }
}
