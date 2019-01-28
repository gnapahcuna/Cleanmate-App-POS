package com.techlogn.cleanmate_pos_v36;

import android.widget.RelativeLayout;

/**
 * Created by anucha on 1/5/2018.
 */

public class MyItemCategoryList {
    private String mTextProductID;
    private String mTextServiceTH;
    private String mTextServiceEN;
    private String mTextProductTH;
    private String mTextProductEN;
    private String mTextPrice;
    private String mImage;
    private String mColor;
    private String mServiceType;

    public MyItemCategoryList(String productID,String productTH, String productEN,String price,String img,String color,String serviceTH,String serviceEN,String serviceType) {
        mTextProductID=productID;
        mTextProductTH = productTH;
        mTextProductEN = productEN;
        mTextPrice=price;
        mImage = img;
        mColor=color;
        mTextServiceTH=serviceTH;
        mTextServiceEN=serviceEN;
        mServiceType=serviceType;
    }
    public void setProductID(String textProductID) {
        mTextProductID = textProductID;
    }
    public String getProductID() {
        return mTextProductID;
    }
    public void setProductTH(String textProductTH) {
        mTextProductTH = textProductTH;
    }
    public String getProductTH() {
        return mTextProductTH;
    }
    public void setProductEN(String textProductEN) {
        mTextProductEN = textProductEN;
    }

    public String getProductEN() {
        return mTextProductEN;
    }
    public void setPrice(String textPrice) {
        mTextPrice = textPrice;
    }

    public String getPrice() {
        return mTextPrice;
    }
    public void setImage(String img) {
        mImage = img;
    }

    public String getImage() {
        return mImage;
    }
    public void setColor(String color){
        mColor=color;
    }
    public String getColor(){
        return mColor;
    }

    public void setServiceTH(String textServiceTH) {
        mTextServiceTH = textServiceTH;
    }
    public String getServiceTH() {
        return mTextServiceTH;
    }
    public void setServiceEN(String textServiceEN) {
        mTextServiceEN = textServiceEN;
    }
    public String getServiceEN() {
        return mTextServiceEN;
    }

    public void setServiceType(String textServiceType) {
        mServiceType = textServiceType;
    }
    public String getServiceType() {
        return mServiceType;
    }

}

