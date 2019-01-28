package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderSuppliesOrderLevel1 {
      public TextView textProductTH;
      public TextView textprice;
      public TextView num,thb;

      public ViewHolderSuppliesOrderLevel1(View convertView) {
            textProductTH = convertView.findViewById(R.id.text_namrTH);
            textprice = convertView.findViewById(R.id.text_price);
            num=convertView.findViewById(R.id.text_num);
            thb=convertView.findViewById(R.id.textTHB);

      }
}

