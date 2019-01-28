package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyItemCancelOrderdetailLevel2 {
    private String mIndex;
    private String mBarcode;

    public MyItemCancelOrderdetailLevel2(String index, String barcode) {
        mIndex=index;
        mBarcode=barcode;
    }
    public void setIndex(String index) {
        mIndex = index;
    }
    public String getIndex() {
        return mIndex;
    }
    public void setBarcode(String barcode) {
        mBarcode = barcode;
    }
    public String getBarcode() {
        return mBarcode;
    }
}
