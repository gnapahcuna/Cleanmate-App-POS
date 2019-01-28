package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/1/2018.
 */

public class MyItemBarcodeOrderDetail {

    private String mBarcode;
    private String proTH;
    private String IsCheck;

    public MyItemBarcodeOrderDetail(String barcode, String productTH,String isCheck) {
        mBarcode=barcode;
        proTH=productTH;
        IsCheck=isCheck;
    }

    public void setBarcode(String barcode) {
        mBarcode = barcode;
    }
    public String getBarcode() {
        return mBarcode;
    }
    public void setProductTH(String productTH) {
        mBarcode = productTH;
    }
    public String getProductTH (){
        return proTH;
    }
    public void setIsCheck(String isCheck) {
        IsCheck = isCheck;
    }
    public String getIsCheck (){
        return IsCheck;
    }
}
