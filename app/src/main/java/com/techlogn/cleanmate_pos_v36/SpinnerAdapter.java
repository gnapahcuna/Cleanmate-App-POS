package com.techlogn.cleanmate_pos_v36;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by anucha on 2/7/2018.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    String []mList;
    Context mContext;
    Typeface typeface;
    public SpinnerAdapter(Context context, int textViewResourceId, String[] list) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub
        mList=list;
        mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View v = super.getView(position, convertView, parent);
        ((TextView) v).setTextSize(16);
        ((TextView) v).setTextColor(Color.BLACK);

        MyFont myFont=new MyFont(mContext);
        if(position<=mList.length){
            ((TextView) v).setTypeface(myFont.setFont());
        }

        return v;
    }

    @Override
    public int getCount() {

        // TODO Auto-generated method stub
        int count = super.getCount();

        return count>0 ? count-1 : count ;


    }


}
