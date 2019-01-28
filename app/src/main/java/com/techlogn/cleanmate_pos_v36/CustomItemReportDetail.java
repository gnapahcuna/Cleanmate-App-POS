package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 5/8/2018.
 */

public class CustomItemReportDetail {
    public int sIndex;
    public String sOrderNo;
    public String sTotal;
    public String sIsCancel;
    public String sIsPayment;

    public CustomItemReportDetail(int index,String orderNo,String total,String iscancel,String isPayment) {
        sIndex=index;
        sOrderNo=orderNo;
        sTotal=total;
        sIsCancel=iscancel;
        sIsPayment=isPayment;
    }
}
