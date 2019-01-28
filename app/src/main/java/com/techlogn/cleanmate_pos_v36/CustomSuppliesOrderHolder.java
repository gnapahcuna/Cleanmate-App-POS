package com.techlogn.cleanmate_pos_v36;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.libRG.CustomTextView;

public class CustomSuppliesOrderHolder extends RecyclerView.ViewHolder {
      public TextView textOrder;
      public TextView textDate;
      public ListView list;
      public CustomTextView total;
      public CustomTextView textStatus;
      public Button btnGet;

      public CustomSuppliesOrderHolder(View view) {
            super(view);
            textOrder = view.findViewById(R.id.textOrder);
            textDate = view.findViewById(R.id.textDate);
            list = view.findViewById(R.id.list);
            total =  view.findViewById(R.id.total_price);
            textStatus = view.findViewById(R.id.textStatus);
            btnGet = view.findViewById(R.id.btn_get);
      }

}
