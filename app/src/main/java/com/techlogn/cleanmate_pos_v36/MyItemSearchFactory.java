package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/11/2018.
 */

public class MyItemSearchFactory {
    private String mIndex;
    private String mNameTH;
    private String mNameEN;
    private String mNum1,mNum2;

    public MyItemSearchFactory(String index,String nameTH,String nameEN,String num1,String num2) {
        mIndex=index;
        mNameTH=nameTH;
        mNameEN=nameEN;
        mNum1=num1;
        mNum2=num2;
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
    public void setNum1(String num1) {
        mNum1 = num1;
    }
    public String getNum1() {
        return mNum1;
    }
    public void setNum2(String num2) {
        mNum2 = num2;
    }
    public String getNum2() {
        return mNum2;
    }

}
