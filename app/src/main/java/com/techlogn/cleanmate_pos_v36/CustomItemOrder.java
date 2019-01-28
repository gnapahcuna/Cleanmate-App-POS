package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/20/2018.
 */

public class CustomItemOrder {
    public String mTextProductID;
    public String mServiceNameTH;
    public String mServiceNameEN;
    public String mTextProductTH;
    public String mTextProductEN;
    public String mTextPrice;
    public String mImage;
    public String mColor;
    public String mCount;
    public String mSerType;
    public boolean chk;
    public int mStatus;

    public CustomItemOrder(String productID,String serviceTH,String serviceEN,String productTH, String productEN,String price,
                           String img,String color,String count,String serType,boolean check,int status) {
        mTextProductID=productID;
        mServiceNameTH=serviceTH;
        mServiceNameEN=serviceEN;
        mTextProductTH = productTH;
        mTextProductEN = productEN;
        mTextPrice=price;
        mImage = img;
        mColor=color;
        mCount=count;
        mSerType=serType;
        chk=check;
        mStatus=status;
    }
}
