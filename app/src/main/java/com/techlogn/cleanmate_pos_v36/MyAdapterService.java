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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anucha on 1/3/2018.
 */

public class MyAdapterService extends ArrayAdapter {
    private Context mContext;
    private ArrayList<MyItemService> mArrayList;
    private int mLayout;
    private GetIPAPI getIPAPI;
    public MyAdapterService(Context context, int layout, ArrayList<MyItemService> arrayList) {
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
        TextView textTitle = rowView.findViewById(R.id.text_catTH);
        TextView textSub = rowView.findViewById(R.id.name_cateEN);
        ImageView image = rowView.findViewById(R.id.list_image);

        getIPAPI=new GetIPAPI();

        //sub="http://techlogn.ddns.net:8090/cleanmate_web/"+arrImg.get(0).substring(3).trim();
        String subImg = getIPAPI.IPAddress+"/cleanmate_web/" + mArrayList.get(position).getImage().substring(3).trim();

        textTitle.setText(mArrayList.get(position).getTextTitle());
        textSub.setText(mArrayList.get(position).getTextSubTitle());

        Picasso.with(getContext()).load(subImg).into(image);

        MyFont myFont =new MyFont(getContext());
        textTitle.setTypeface(myFont.setFont());
        textSub.setTypeface(myFont.setFont());

        return rowView;
    }
}