package com.techlogn.cleanmate_pos_v36;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by anucha on 1/13/2018.
 */

public class MyFont {
    private Typeface typeface;
    private Context mContext;

    public MyFont(Context context) {
        mContext=context;
    }
    public Typeface setFont(){
        this.typeface=Typeface.createFromAsset(mContext.getAssets(), "fonts/Ayuthaya.ttf");
        //this.typeface=Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans_ExtraBold.ttf");
        return typeface;
    }
}
