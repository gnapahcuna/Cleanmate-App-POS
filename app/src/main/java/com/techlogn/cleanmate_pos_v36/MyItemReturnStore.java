package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/1/2018.
 */

public class MyItemReturnStore {

    private int mIndex=0;
    private String mOrder;
    private String isVerify;

    public MyItemReturnStore(int index, String order,String verify) {
        mIndex=index;
        mOrder=order;
        isVerify=verify;
    }
    public void setIndex(int index) {
        mIndex = index;
    }
    public int getIndex() {
        return mIndex;
    }
    public void setOrderNo(String order) {
        mOrder = order;
    }
    public String getOrderNo() {
        return mOrder;
    }
    public void setVerify(String verify) {
        isVerify = verify;
    }
    public String getVerify() {
        return isVerify;
    }
}
