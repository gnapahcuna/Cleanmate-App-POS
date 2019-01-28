package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyItemReturnCustomerList {
    private String mIndex;
    private String mOrderNo;
    private String mCustomer;

    public MyItemReturnCustomerList(String index, String orderno,String customer) {
        mIndex=index;
        mOrderNo=orderno;
        mCustomer=customer;
    }
    public void setIndex(String index) {
        mIndex = index;
    }
    public String getIndex() {
        return mIndex;
    }
    public void setmOrderNo(String orderno) {
        mOrderNo = orderno;
    }
    public String getOrderNo() {
        return mOrderNo;
    }
    public void setCustomer(String customer) {
        mCustomer = customer;
    }
    public String getCustomer() {
        return mCustomer;
    }
}
