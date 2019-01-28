package com.techlogn.cleanmate_pos_v36;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderSuppliesOrder {
      public TextView textProductTH;
      public TextView textprice;
      public ImageView more;
      public TextView num,thb;

      public ViewHolderSuppliesOrder(View convertView) {
            textProductTH = convertView.findViewById(R.id.text_namrTH);
            textprice = convertView.findViewById(R.id.text_price);
            more=convertView.findViewById(R.id.img_more);
            num=convertView.findViewById(R.id.text_num);
            thb=convertView.findViewById(R.id.textTHB);

      }
}

