package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/12/2018.
 */

public class MyItemFactoryScan {
    private String mIndex;
    private String mNameTH;
    private String mOrderDetailID;
    private String IsImage;

    public MyItemFactoryScan(String index,String nameTH,String ID,String isImage) {
        mIndex=index;
        mNameTH=nameTH;
        mOrderDetailID=ID;
        IsImage=isImage;
    }
    public void setIndex(String index) {
        mIndex = index;
    }
    public String getIndex() {
        return mIndex;
    }
    public void setNameTH(String nameTH) {
        mNameTH = nameTH;
    }
    public String getNameTH() {
        return mNameTH;
    }
    public void setOrderDetailID(String id) {
        mOrderDetailID = id;
    }
    public String getOrderDetailID() {
        return mOrderDetailID;
    }
    public void setIsImage(String isImage) {
        IsImage = isImage;
    }
    public String getIsImage() {
        return IsImage;
    }

}
