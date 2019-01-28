package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by anucha on 2/27/2018.
 */

public class MyAdapterFactoryOrder1 extends ArrayAdapter {
    Fragment fragment;
    private Context mContext;
    private ArrayList<CustomItemFactoryOrderList> items;
    private int mLayout;
    LayoutInflater inflater;
    View rowView, v;
    String mBranchID,mDataID;
    ViewHolderFactoryOrderList1 vHolder;
    int mPosition;
    String t1,t2;
    String status="";

    public MyAdapterFactoryOrder1(Context context, int layout, ArrayList<CustomItemFactoryOrderList> arrayList,String branchID,String dataID) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        items = arrayList;
        mBranchID=branchID;
        mDataID=dataID;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /*rowView = convertView;
        v = convertView;*/
        if(convertView == null) {
            /*inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(mLayout, parent, false);*/

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayout, parent, false);
            vHolder = new ViewHolderFactoryOrderList1(convertView);
            convertView.setTag(vHolder);

        } else {
            vHolder = (ViewHolderFactoryOrderList1)convertView.getTag();
        }

        final CustomItemFactoryOrderList item = items.get(position);

        /*System.out.println(item.mDeliveryStatus);
        System.out.println(item.mIsDriverVerify);
        System.out.println(item.mIsCheckerVerify);
        System.out.println(item.mIsBranchEmpVerify);
        System.out.println(item.mIsReturnCustomer);*/

        if(Integer.parseInt(item.mIsCustomerCancel)==0){
            status = "ลูกค้ายกเลิกรายการซัก";
            vHolder.textStatus.setTextColor(Color.RED);
            vHolder.textStatus.setText("" + status);
        }else {
            if (Integer.parseInt(item.mDeliveryStatus) == 0 && Integer.parseInt(item.mIsDriverVerify) == 0 &&
                    Integer.parseInt(item.mIsCheckerVerify) == 0 && Integer.parseInt(item.mIsBranchEmpVerify) == 0
                    && Integer.parseInt(item.mIsReturnCustomer) == 0) {
                status = "ผ้าอยู่กับร้านค้า";
            } else if (Integer.parseInt(item.mDeliveryStatus) == 0 && Integer.parseInt(item.mIsDriverVerify) == 1 &&
                    Integer.parseInt(item.mIsCheckerVerify) == 0 && Integer.parseInt(item.mIsBranchEmpVerify) == 0
                    && Integer.parseInt(item.mIsReturnCustomer) == 0) {
                status = "ผ้าอยู่กับคนขับรถนำส่งโรงงาน";
            } else if (Integer.parseInt(item.mDeliveryStatus) == 0 && Integer.parseInt(item.mIsDriverVerify) == 1 &&
                    Integer.parseInt(item.mIsCheckerVerify) == 1 && Integer.parseInt(item.mIsBranchEmpVerify) == 0
                    && Integer.parseInt(item.mIsReturnCustomer) == 0) {
                status = "ผ้าอยู่กับแผนก Factory Checker In";
            } else if (Integer.parseInt(item.mDeliveryStatus) == 1 && Integer.parseInt(item.mIsDriverVerify) == 0 &&
                    Integer.parseInt(item.mIsCheckerVerify) == 1 && Integer.parseInt(item.mIsBranchEmpVerify) == 0
                    && Integer.parseInt(item.mIsReturnCustomer) == 0) {
                status = "ผ้าอยู่กับแผนก Factory Checker Out";
            } else if (Integer.parseInt(item.mDeliveryStatus) == 1 && Integer.parseInt(item.mIsDriverVerify) == 1 &&
                    Integer.parseInt(item.mIsCheckerVerify) == 1 && Integer.parseInt(item.mIsBranchEmpVerify) == 0
                    && Integer.parseInt(item.mIsReturnCustomer) == 0) {
                status = "ผ้าอยู่กับคนขับรถนำคืนร้านค้า";
            } else if (Integer.parseInt(item.mDeliveryStatus) == 1 && Integer.parseInt(item.mIsDriverVerify) == 1 &&
                    Integer.parseInt(item.mIsCheckerVerify) == 1 && Integer.parseInt(item.mIsBranchEmpVerify) == 1
                    && Integer.parseInt(item.mIsReturnCustomer) == 0) {
                status = "ผ้าอยู่กับร้านค้า";
            } else if (Integer.parseInt(item.mDeliveryStatus) == 1 && Integer.parseInt(item.mIsDriverVerify) == 1 &&
                    Integer.parseInt(item.mIsCheckerVerify) == 1 && Integer.parseInt(item.mIsBranchEmpVerify) == 1
                    && Integer.parseInt(item.mIsReturnCustomer) == 1) {
                status = "ลูกค้ารับผ้าคืน";
            } else {
                status = "ไม่พบสถานะ";
            }
            vHolder.textStatus.setTextColor(Color.parseColor("#303f9f"));
            vHolder.textStatus.setText("" + status);
        }

        vHolder.textOrderNo.setText(item.mOrderNo);
        vHolder.textDate.setText(item.mOrderDate);
        //vHolder.textStatus.setText(""+status);

        MyFont myFont=new MyFont(mContext);
        vHolder.textOrderNo.setTypeface(myFont.setFont());
        vHolder.textDate.setTypeface(myFont.setFont());
        vHolder.textStatus.setTypeface(myFont.setFont());

        Configuration config = mContext.getResources().getConfiguration();
        //if (config.smallestScreenWidthDp >= 320||config.smallestScreenWidthDp >= 480) {
            int width =mContext.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((width*6)/100, 40, 0, 0);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins((width*40)/100, 40, 0, 0);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins((width*65)/100, 40, 0, 0);
            vHolder.textOrderNo.setLayoutParams(layoutParams);
            vHolder.textDate.setLayoutParams(layoutParams1);
            vHolder.textStatus.setLayoutParams(layoutParams2);
            //Toast.makeText(mContext,""+list.getLayoutParams().width,Toast.LENGTH_SHORT).show();
        //}
        //else {
            // fall-back code goes here
        //}

        /*vHolder.textStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                TextView des = dialog.findViewById(R.id.tv_description);
                title.setText("คำอธิบายสถานะ");
                if(Integer.parseInt(item.mDeliveryStatus)==0&&Integer.parseInt(item.mIsDriverVerify)==0&&
                        Integer.parseInt(item.mIsCheckerVerify)==0&& Integer.parseInt(item.mIsReturnCustomer) == 0){
                    status="รับผ้าจากลูกค้า";
                }else if(Integer.parseInt(item.mDeliveryStatus)==0&&Integer.parseInt(item.mIsDriverVerify)==1&&
                        Integer.parseInt(item.mIsCheckerVerify)==0&& Integer.parseInt(item.mIsReturnCustomer) == 0){
                    status="ส่งผ้าเข้าโรงงาน";
                }else if(Integer.parseInt(item.mDeliveryStatus)==0&&Integer.parseInt(item.mIsDriverVerify)==1&&
                        Integer.parseInt(item.mIsCheckerVerify)==1&& Integer.parseInt(item.mIsReturnCustomer) == 0){
                    status="โรงงานรับผ้า";
                }else if(Integer.parseInt(item.mDeliveryStatus)==1&&Integer.parseInt(item.mIsDriverVerify)==0&&
                        Integer.parseInt(item.mIsCheckerVerify)==1&& Integer.parseInt(item.mIsReturnCustomer) == 0){
                    status="เตรียมส่งผ้าคืน";
                }else if(Integer.parseInt(item.mDeliveryStatus)==1&&Integer.parseInt(item.mIsDriverVerify)==1&&
                        Integer.parseInt(item.mIsCheckerVerify)==1&& Integer.parseInt(item.mIsReturnCustomer) == 0){
                    status="ส่งคืนผ้าเข้าสาขา";
                }else if(Integer.parseInt(item.mDeliveryStatus)==1&&Integer.parseInt(item.mIsDriverVerify)==1&&
                        Integer.parseInt(item.mIsCheckerVerify)==1&& Integer.parseInt(item.mIsReturnCustomer) == 1){
                    status="ลูกค้ารับผ้าคืน";
                }
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                declineButton.setVisibility(View.GONE);
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                MyFont myFont=new MyFont(mContext);
                okButton.setTypeface(myFont.setFont());
                declineButton.setTypeface(myFont.setFont());
                title.setTypeface(myFont.setFont());
                des.setTypeface(myFont.setFont());
            }
        });*/


        return convertView;
    }
    private String getFormatedAmount(double amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
