package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyItemReturnFactoryOrder {
    private String mIndex;
    private String mBarcode;
    private String mProductTH;
    private String mProductEN;

    public MyItemReturnFactoryOrder(String index,String productTH,String productEN,String barcode) {
        mIndex=index;
        mBarcode=barcode;
        mProductTH=productTH;
        mProductEN=productEN;
    }
    public void setIndex(String index) {
        mIndex = index;
    }
    public String getIndex() {
        return mIndex;
    }
    public void setProductTH(String productTH) {
        mProductTH = productTH;
    }
    public String getProductTH() {
        return mProductTH;
    }
    public void setProductEN(String productEN) {
        mProductEN = productEN;
    }
    public String getProductEN() {
        return mProductEN;
    }
    public void setBarcode(String barcode) {
        mBarcode = barcode;
    }
    public String getBarcode() {
        return mBarcode;
    }
}
