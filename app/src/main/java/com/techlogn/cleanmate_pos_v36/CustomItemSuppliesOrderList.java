package com.techlogn.cleanmate_pos_v36;

import java.util.ArrayList;

public class CustomItemSuppliesOrderList {

      public String order;
      public String date;
      public ArrayList<CustomItemSuppliesOrder> content;
      public String status;
      public double total;
      public double isCheck;


      public CustomItemSuppliesOrderList(String title,String date, ArrayList<CustomItemSuppliesOrder> list,String status, Double total,int isCheck) {
            this.order = title;
            this.date = date;
            this.content = list;
            this.status=status;
            this.total = total;
            this.isCheck = isCheck;
      }
}
