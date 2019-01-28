package com.techlogn.cleanmate_pos_v36;

public class CustomItemProduct {
      public String mTextProductID;
      public String mServiceNameTH;
      public String mServiceNameEN;
      public String mTextProductTH;
      public String mTextProductEN;
      public String mTextPrice;
      public String mImage;
      public String mColor;
      public String mServiceType;
      public String mNum;
      //public boolean isChecked;

      public CustomItemProduct(String productID,String serviceTH,String serviceEN,String productTH, String productEN,String price,String img,String color,String serviceType,String num) {
            mTextProductID=productID;
            mServiceNameTH=serviceTH;
            mServiceNameEN=serviceEN;
            mTextProductTH = productTH;
            mTextProductEN = productEN;
            mTextPrice=price;
            mImage = img;
            mColor=color;
            mServiceType=serviceType;
            mNum=num;
            //this.isChecked=isChecked;
      }
}

