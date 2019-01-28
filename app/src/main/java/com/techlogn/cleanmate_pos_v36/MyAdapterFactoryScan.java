package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
 * Created by anucha on 2/12/2018.
 */

public class MyAdapterFactoryScan extends ArrayAdapter {

    Fragment fragment;

    private Context mContext;
    private ArrayList<MyItemFactoryScan> mArrayList;
    private int mLayout;
    LayoutInflater inflater;
    View rowView, v;
    String mOrderDetail,mProID,mOrderNo,mProTH,mProEN,mNum,mBar1;

    public MyAdapterFactoryScan(Context context, int layout, ArrayList<MyItemFactoryScan> arrayList, String proID, String orderID, String proTH, String proEN, String Num, String bar1) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        mArrayList = arrayList;
        mProID=proID;
        mOrderNo=orderID;
        mProTH=proTH;
        mProEN=proEN;
        mNum=Num;
        mBar1=bar1;
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
        TextView textNameTH = rowView.findViewById(R.id.orderNo);
        ImageView cap=rowView.findViewById(R.id.btn_cap);


        MyFont myFont=new MyFont(mContext);
        textIndex.setTypeface(myFont.setFont());
        textNameTH.setTypeface(myFont.setFont());

        final String index = mArrayList.get(position).getIndex();
        final String nameTH = mArrayList.get(position).getNameTH();


        textIndex.setText(index + " .");
        textNameTH.setText(nameTH);


        String isImage=mArrayList.get(position).getIsImage();
        //System.out.println(isImage);
        if(isImage.trim().equals("1")){
            cap.setImageResource(R.drawable.icon_camera2);
        }else if(isImage.trim().equals("0")){
            cap.setImageResource(R.drawable.icon_camera1);
        }

        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,ManageCaptureActivity.class);
                i.putExtra("send","123");
                i.putExtra("AA",mArrayList.get(position).getOrderDetailID());
                i.putExtra("proID",mProID);
                i.putExtra("orderNo",mOrderNo);
                i.putExtra("productTH",mProTH);
                i.putExtra("productEN",mProEN);
                i.putExtra("Num",mNum);
                i.putExtra("bar1",mBar1);

                System.out.println("Adapter Num : "+mNum);
                System.out.println("Adapter : "+mBar1);
                mContext.startActivity(i);
            }
        });

        return rowView;
    }

}

