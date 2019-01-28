package com.techlogn.cleanmate_pos_v36;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by anucha on 2/1/2018.
 */

public class MyAdapterBarcode extends ArrayAdapter {
    private Context mContext;
    private ArrayList<MyItemBarcode> mArrayList;
    private int mLayout;

    public MyAdapterBarcode(Context context, int layout, ArrayList<MyItemBarcode> arrayList) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(mLayout, parent, false);
        }
        TextView orderNo = rowView.findViewById(R.id.text_order);
        TextView barcode = rowView.findViewById(R.id.text_barcode_scan);
        int width=mContext.getResources().getDisplayMetrics().widthPixels;

        RelativeLayout.LayoutParams l1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        l1.setMargins(0, 40, 0, 20);
        RelativeLayout.LayoutParams l2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        l1.setMargins((width*10)/100, 0, 0, 0);
        l2.setMargins((width*60)/100, 0, 0, 0);

       // RelativeLayout rl = rowView.findViewById(R.id.rl_pk);
       // rl.getLayoutParams().height=(mContext.getResources().getDisplayMetrics().heightPixels*70)/100;
        //orderNo.setLayoutParams(l1);
        //barcode.setLayoutParams(l2);


        orderNo.setText(mArrayList.get(position).getOrderNo());
        barcode.setText(mArrayList.get(position).getBarcode());


        MyFont myFont =new MyFont(getContext());
        orderNo.setTypeface(myFont.setFont());
        barcode.setTypeface(myFont.setFont());

        return rowView;
    }
}
