package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyItemFactoryOrder {
    private String mIndex;
    private String mOrderNo;
    private String mDate;
    private String mBarcod;
    private String mBarcodePackage;

    public MyItemFactoryOrder(String index,String orderno,String date,String bar1,String bar2) {
        mIndex=index;
        mOrderNo=orderno;
        mDate=date;
        mBarcod=bar1;
        mBarcodePackage=bar2;
    }
    public void setIndex(String index) {
        mIndex = index;
    }
    public String getIndex() {
        return mIndex;
    }
    public void setFacrotyOrder(String orderno) {
        mOrderNo = orderno;
    }
    public String getFactoryOrder() {
        return mOrderNo;
    }
    public void setDate(String date) {
        mDate = date;
    }
    public String getDate() {
        return mDate;
    }
    public void setBarcode(String bar1) {
        mBarcod = bar1;
    }
    public String getmBarcod() {
        return mBarcod;
    }
    public void setBarcodePackage(String bar2) {
        mBarcodePackage = bar2;
    }
    public String getmBarcodPackage() {
        return mBarcodePackage;
    }
}
