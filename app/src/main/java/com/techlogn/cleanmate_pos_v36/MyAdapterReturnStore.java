package com.techlogn.cleanmate_pos_v36;

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
 * Created by anucha on 2/1/2018.
 */

public class MyAdapterReturnStore extends ArrayAdapter {
    private Context mContext;
    private ArrayList<MyItemReturnStore> mArrayList;
    private int mLayout;

    public MyAdapterReturnStore(Context context, int layout, ArrayList<MyItemReturnStore> arrayList) {
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
        TextView index = rowView.findViewById(R.id.text_index);
        ImageView verify=rowView.findViewById(R.id.img_verify);

        String IsVerify = mArrayList.get(position).getVerify();
        if(IsVerify.trim().endsWith("รับออเดอร์แล้ว")){
            verify.setVisibility(View.VISIBLE);
        }else{
            verify.setVisibility(View.GONE);
        }


        orderNo.setText(mArrayList.get(position).getOrderNo());
        index.setText(""+mArrayList.get(position).getIndex());


        MyFont myFont =new MyFont(getContext());
        orderNo.setTypeface(myFont.setFont());
        index.setTypeface(myFont.setFont());

        return rowView;
    }
}
