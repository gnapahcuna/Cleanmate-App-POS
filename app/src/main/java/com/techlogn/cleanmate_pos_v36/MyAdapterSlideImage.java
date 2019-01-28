package com.techlogn.cleanmate_pos_v36;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anucha on 1/3/2018.
 */

public class MyAdapterSlideImage extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    private GetIPAPI getIPAPI;

    public MyAdapterSlideImage(Context context, ArrayList<String> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        //myImage.setImageResource(Integer.parseInt(images.get(position)));
        String chk=images.get(position).substring(0,1);
        if(chk.endsWith("@")){
            String subImg = images.get(position).substring(1).trim();
            Picasso.with(context).load(subImg).into(myImage);
            view.addView(myImageLayout, 0);
        }
        if(images.get(position).length()>100){
            final byte[] decodedBytes = Base64.decode(images.get(position), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            myImage.setImageBitmap(decodedByte);
            view.addView(myImageLayout, 0);
        }else {
            getIPAPI=new GetIPAPI();
            String subImg = getIPAPI.IPAddressImage+"/" + images.get(position).substring(3).trim();
            Picasso.with(context).load(subImg).into(myImage);
            view.addView(myImageLayout, 0);
        }
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}