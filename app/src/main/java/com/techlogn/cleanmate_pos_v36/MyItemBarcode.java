package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/1/2018.
 */

public class MyItemBarcode {

    private String mOrder;
    private String mBarcode;

    public MyItemBarcode (String orderNo, String barcode) {
        mOrder=orderNo;
        mBarcode=barcode;
    }

    public void setOrderNo(String order) {
        mOrder = order;
    }
    public String getOrderNo() {
        return mOrder;
    }
    public void setBarcode(String barcode) {
        mBarcode = barcode;
    }
    public String getBarcode() {
        return mBarcode;
    }
}
