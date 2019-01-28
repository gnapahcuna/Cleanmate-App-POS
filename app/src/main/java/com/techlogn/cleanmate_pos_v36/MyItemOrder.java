package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 1/6/2018.
 */

public class MyItemOrder {
    private String mTextProductID;
    private String mServiceNameTH;
    private String mServiceNameEN;
    private String mTextProductTH;
    private String mTextProductEN;
    private String mTextPrice;
    private String mImage;
    private String mColor;
    private String mCount;

    public MyItemOrder(String productID,String serviceTH,String serviceEN,String productTH, String productEN,String price,String img,String color,String count ) {

        mTextProductID=productID;
        mServiceNameTH=serviceTH;
        mServiceNameEN=serviceEN;
        mTextProductTH = productTH;
        mTextProductEN = productEN;
        mTextPrice=price;
        mImage = img;
        mColor=color;
        mCount=count;

    }
    public void setProducID(String textProductID) {
        mTextProductID = textProductID;
    }
    public String getProductID() {
        return mTextProductID;
    }
    public void setServiceTH(String textServiceTH) {
        mServiceNameTH = textServiceTH;
    }
    public String getServiceTH() {
        return mServiceNameTH;
    }
    public void setServiceEN(String textServiceEN) {
        mServiceNameEN = textServiceEN;
    }
    public String getServiceEN() {
        return mServiceNameEN;
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
    public void setCount(String count){
        mCount=count;
    }
    public String getCount(){
        return mCount;
    }
}

