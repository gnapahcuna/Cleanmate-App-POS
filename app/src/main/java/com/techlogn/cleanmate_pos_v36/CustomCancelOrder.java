package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 2/28/2018.
 */

public class CustomCancelOrder {
    public String mIndex;
    public String mOrderNo;
    public String mOrderDate;
    public String mTelephonNo;
    public String mIsAddition;
    public String mAdditionNetAmount;
    public String mNetAmount;
    public String mDeliveryStatus;
    public String mIsDriverVerify;
    public String mIsCheckerVerify;
    public String mIsReturnCustomer;
    public String mIsBranchEmpVerify;
    public String mIsCustomerCancel;

    public CustomCancelOrder(String index,String orderno,String date,String phone, String addition,
                                      String additionNetAmount,String netAmount,String deliveryStatus,String isDriverVerify,
                                      String isCheckerVerify, String isReturnCustomer,String isBranchEmpVerify,String isCustomerCancel) {
        mIndex=index;
        mOrderNo=orderno;
        mOrderDate=date;
        mTelephonNo=phone;
        mIsAddition=addition;
        mAdditionNetAmount=additionNetAmount;
        mNetAmount=netAmount;
        mDeliveryStatus=deliveryStatus;
        mIsDriverVerify = isDriverVerify;
        mIsCheckerVerify = isCheckerVerify;
        mIsReturnCustomer=isReturnCustomer;
        mIsBranchEmpVerify=isBranchEmpVerify;
        mIsCustomerCancel=isCustomerCancel;
    }
}
