package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyItemCancelOrderdetailLevel1 {
    private String mIndex;
    private String mNameTH;
    private String mNameEN;
    private String mNum2;
    private String mProID;

    public MyItemCancelOrderdetailLevel1(String index, String nameTH, String nameEN, String num2, String proID) {
        mIndex=index;
        mNameTH=nameTH;
        mNameEN=nameEN;
        mNum2=num2;
        mProID=proID;
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
    public void setNameEN(String nameEN) {
        mNameEN = nameEN;
    }
    public String getNameEN() {
        return mNameEN;
    }
    public void setNum2(String num2) {
        mNum2 = num2;
    }
    public String getNum2() {
        return mNum2;
    }
    public void setProID(String proID) {
        mProID = proID;
    }
    public String getProID() {
        return mProID;
    }
}
