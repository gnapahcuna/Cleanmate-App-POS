package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/10/2018.
 */

public class MyItemCheckDelivery {
    private String mIndex;
    private String mCustName;
    private String mOrderNo;
    private String mStatus;

    public MyItemCheckDelivery(String index, String cust, String orderno, String status) {
        mIndex=index;
        mCustName=cust;
        mOrderNo=orderno;
        mStatus=status;
    }
    public void setIndex(String index) {
        mIndex = index;
    }
    public String getIndex() {
        return mIndex;
    }
    public void setCust(String cust) {
        mCustName = cust;
    }
    public String getCust() {
        return mCustName;
    }
    public void setFacrotyOrder(String orderno) {
        mOrderNo = orderno;
    }
    public String getFactoryOrder() {
        return mOrderNo;
    }
    public void setStatus(String status) {
        mStatus = status;
    }
    public String getStatus() {
        return mStatus;
    }
}
