package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by anucha on 1/3/2018.
 */

public class FragmentReturnCustomerList extends Fragment {

    private View myView;
    private String dataID,branchID,orderNo="",netAmount="",isPayment="",addition="",isCancel="",IscancelService="",isExpress="",isMember="",discountAmount="";
    private TextView textOrderNo,textNetAmount,textIsPayment,textAddition,textTotal,textCancel;
    private MyFont myFont;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private Button btn_ok,btn_back;
    private int total=0;

    private String color1 = "",color2 = "",key;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater=inflater;
        mContainer=container;


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/

        getIPAPI=new GetIPAPI();


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            myFont = new MyFont(getActivity());


            SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("ID", Activity.MODE_PRIVATE);
            Map<String, ?> entries2 = sharedPreferences2.getAll();
            Set<String> keys2 = entries2.keySet();
            String[] getData2;
            List<String> list2 = new ArrayList<String>(keys2);
            for (String temp : list2) {
                //System.out.println(temp+" = "+sharedPreferences.getStringSet(temp,null));
                for (int i = 0; i < sharedPreferences2.getStringSet(temp, null).size(); i++) {
                    getData2 = sharedPreferences2.getStringSet(temp, null).toArray(new String[sharedPreferences2.getStringSet(temp, null).size()]);
                    //System.out.println(temp + " : " + getData2[i]);
                    char chk = getData2[i].charAt(1);
                    if (chk == 'a') {
                        dataID = getData2[i].substring(3);
                    } else if (chk == 'b') {
                        branchID = getData2[i].substring(3);
                    }
                }
            }

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                myView = mLayoutInflater.inflate(R.layout.fragment_return_customer_list, mContainer, false);

                key = bundle.getString("orderNo");
                final String search = bundle.getString("key");

                textOrderNo = myView.findViewById(R.id.textOrderNo);
                textNetAmount = myView.findViewById(R.id.textOwe);
                textIsPayment = myView.findViewById(R.id.textIsPayment);
                textAddition = myView.findViewById(R.id.textAddition);
                textTotal = myView.findViewById(R.id.textResult);
                textCancel = myView.findViewById(R.id.textCancel);
                TextView text1 = myView.findViewById(R.id.textView1);
                text1.setText("เลขที่ใบรับผ้า");
                TextView text2 = myView.findViewById(R.id.textView2);
                TextView text3 = myView.findViewById(R.id.textView3);
                TextView text4 = myView.findViewById(R.id.textView4);
                TextView text5 = myView.findViewById(R.id.textView5);
                TextView text6 = myView.findViewById(R.id.textView6);
                TextView t=myView.findViewById(R.id.textTitle);
                t.setTypeface(myFont.setFont());
                textOrderNo.setTypeface(myFont.setFont());
                textNetAmount.setTypeface(myFont.setFont());
                textIsPayment.setTypeface(myFont.setFont());
                textAddition.setTypeface(myFont.setFont());
                textTotal.setTypeface(myFont.setFont());
                text1.setTypeface(myFont.setFont());
                text2.setTypeface(myFont.setFont());
                text3.setTypeface(myFont.setFont());
                text4.setTypeface(myFont.setFont());
                text5.setTypeface(myFont.setFont());
                text6.setTypeface(myFont.setFont());

                TextView Text5 = myView.findViewById(R.id.textView5);
                Text5.setTypeface(myFont.setFont());

                btn_ok = myView.findViewById(R.id.btn_ok);
                btn_back = myView.findViewById(R.id.btn_back);
                btn_ok.setTypeface(myFont.setFont());
                btn_back.setTypeface(myFont.setFont());

                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("key", search);
                        FragmentReturnCustomer fragmentReturnCustomer= new FragmentReturnCustomer();
                        fragmentReturnCustomer.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragmentReturnCustomer)
                                .commit();
                    }
                });
                System.out.println("Order No. : "+key);
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setIcon(R.mipmap.loading);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("กรุณารอสักครู่....");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
                Ion.with(getActivity())
                        .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/ReturnCustomer1.php")
                        .setBodyParameter("branchID", branchID)
                        .setBodyParameter("search", key)
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {
                                dialog.dismiss();
                                //try {
                                if (result.size() == 0) {
                                    new MyToast(getActivity(), "ไม่พบข้อมูล", 0);
                                    myView = mLayoutInflater.inflate(R.layout.fragment_return_customer, mContainer, false);
                                } else {
                                    int cancel=0;
                                    for (int i = 0; i < result.size(); i++) {
                                        JsonObject jsonObject = (JsonObject) result.get(i);
                                        orderNo = jsonObject.get("OrderNo").getAsString();
                                        //netAmount = jsonObject.get("NetAmount").getAsString().substring(0, jsonObject.get("NetAmount").getAsString().indexOf('.'));
                                        int specai = 0;
                                        try {
                                            specai = (int)Double.parseDouble(jsonObject.get("SpecialDiscount").getAsString());
                                        }catch (Exception ex){
                                            specai=0;
                                        }
                                        if(jsonObject.get("NetAmount").getAsString().substring(0, jsonObject.get("NetAmount").getAsString().indexOf('.')).isEmpty()){
                                            int totals=0;
                                            netAmount=""+totals;
                                        }else{
                                            int totals=Integer.parseInt(jsonObject.get("NetAmount").getAsString().substring(0, jsonObject.get("NetAmount").getAsString().indexOf('.')));
                                            netAmount = ""+ totals;
                                        }
                                        addition = jsonObject.get("AdditionAmount").getAsString();
                                        isCancel = jsonObject.get("IsCancel").getAsString();
                                        isMember=jsonObject.get("CustomerType").getAsString();
                                        isExpress=jsonObject.get("IsExpressLevel").getAsString();
                                        IscancelService = jsonObject.get("IsCancelService").getAsString();
                                        discountAmount=jsonObject.get("DiscountAmount").getAsString();

                                        if(Integer.parseInt(isMember)==1&&Integer.parseInt(isExpress)==0){
                                            cancel+=((int)Double.parseDouble(isCancel)-(int)((Double.parseDouble(IscancelService))))+(int)((Double.parseDouble(discountAmount)));
                                        }else if(Integer.parseInt(isMember)==0&&Integer.parseInt(isExpress)==1){
                                            cancel+=((int)Double.parseDouble(isCancel)-((int)((Double.parseDouble(isCancel)*50)/100)))+(int)((Double.parseDouble(discountAmount)));
                                        }else if(Integer.parseInt(isMember)==0&&Integer.parseInt(isExpress)==2){
                                            cancel+=((int)Double.parseDouble(isCancel)*2)+(int)((Double.parseDouble(discountAmount)));
                                        }else{
                                            cancel+=(int)Double.parseDouble(isCancel)+(int)((Double.parseDouble(discountAmount)));
                                        }


                                        if (Integer.parseInt(jsonObject.get("IsPayment").getAsString()) == 1) {
                                            isPayment = "ชำระเงินแล้ว";
                                            color1="#0059b3";
                                            //if (Integer.parseInt(jsonObject.get("IsAddition").getAsString()) == 1) {
                                            if((0 + (int)Double.parseDouble(addition))>cancel){
                                                total = (0 + (int)Double.parseDouble(addition))-cancel;
                                            }else{
                                                //total = cancel-(0 + (int)Double.parseDouble(addition));
                                                total=0;
                                            }
                                                /*} else {
                                                    total = 0;
                                                }*/
                                        } else if (Integer.parseInt(jsonObject.get("IsPayment").getAsString()) == 0) {
                                            isPayment = "ค้างชำระ";
                                            color1="#ff3300";
                                            //if (Integer.parseInt(jsonObject.get("IsAddition").getAsString()) == 1) {
                                            if((Integer.parseInt(netAmount) + (int)Double.parseDouble(addition))>cancel){
                                                total = (Integer.parseInt(netAmount) + (int)Double.parseDouble(addition))-cancel;
                                            }else{
                                                total = cancel-(Integer.parseInt(netAmount) + (int)Double.parseDouble(addition));
                                            }

                                            //} else {
                                            //total = 0 + Integer.parseInt(netAmount);
                                            //}
                                        }
                                    }
                                    if((int)Double.parseDouble(addition)>0){
                                        color2="#ff3300";
                                    }else{
                                        color2="#0059b3";
                                    }

                                    textOrderNo.setText(orderNo);
                                    textNetAmount.setText(netAmount + ".00");
                                    textIsPayment.setText(isPayment);
                                    textIsPayment.setTextColor(Color.parseColor(color1));
                                    textAddition.setText((int)Double.parseDouble(addition) + ".00");
                                    textCancel.setText(cancel + ".00");
                                    textAddition.setTextColor(Color.parseColor(color2));
                                    textTotal.setText("" + getFormatedAmount(total) + ".00");
                                }

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(isPayment.equals("ค้างชำระ")){
                                            Intent i = new Intent(getActivity(), TypePaymentActivity.class);
                                            i.putExtra("ID",""+0);
                                            i.putExtra("orderNo", orderNo);
                                            i.putExtra("branchID", branchID);
                                            i.putExtra("total", total);
                                            i.putExtra("back", 1);
                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);

                                        }else{
                                            final Dialog dialog1 = new Dialog(getActivity());
                                            dialog1.setContentView(R.layout.custon_alert_dialog);
                                            dialog1.show();
                                            TextView title = dialog1.findViewById(R.id.tv_quit_learning);
                                            TextView des = dialog1.findViewById(R.id.tv_description);
                                            title.setText("แจ้งเตือน");
                                            des.setText("ยืนยันการทำรายการ");
                                            Button okButton = dialog1.findViewById(R.id.btn_ok);
                                            okButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                                                    dialog.setIcon(R.mipmap.loading);
                                                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    dialog.setMessage("กรุณารอสักครู่....");
                                                    dialog.setIndeterminate(true);
                                                    dialog.setCancelable(false);
                                                    dialog.show();
                                                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                                                    String datesCreate = "" + df.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());
                                                    Ion.with(getActivity())
                                                            .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/IsReturnCustomer1.php")
                                                            .setBodyParameter("IsReturnCustomer", "" + 1)
                                                            .setBodyParameter("OrderNo", orderNo)
                                                            .setBodyParameter("Date", datesCreate)
                                                            .asString()
                                                            .setCallback(new FutureCallback<String>() {
                                                                @Override
                                                                public void onCompleted(Exception e, String result) {
                                                                    dialog1.dismiss();
                                                                    dialog.dismiss();
                                                                    new MyToast(getActivity(), result, 1);
                                                                    FragmentReturnCustomer fragmentReturnCustomer = new FragmentReturnCustomer();
                                                                    getFragmentManager()
                                                                            .beginTransaction()
                                                                            .replace(R.id.content_frame, fragmentReturnCustomer)
                                                                            .commit();

                                                                }
                                                            });
                                                }
                                            });
                                            Button declineButton = dialog1.findViewById(R.id.btn_cancel);
                                            declineButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog1.dismiss();
                                                }
                                            });
                                            okButton.setTypeface(myFont.setFont());
                                            declineButton.setTypeface(myFont.setFont());
                                            title.setTypeface(myFont.setFont());
                                            des.setTypeface(myFont.setFont());
                                        }

                                    }
                                });
                            }
                        });

            }
        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return myView;
    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
