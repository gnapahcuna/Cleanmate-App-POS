package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * Created by anucha on 5/8/2018.
 */

public class FragmentReportDetail extends Fragment {

    private View myView;
    private String index,getDate,dd="",mm="",dd4="",mm4="",dates,dataID,branchID;
    private com.fourmob.datetimepicker.date.DatePickerDialog mDatePicker1;
    private Calendar mCalendar;
    private ArrayList<CustomItemReportDetail>mListReport;
    private ListView mListView;
    private TextView textDate,textTotal,textOwe,textCancel,textSpecail;
    private MyAdapterReportDetail myAdapter;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private GetIPAPI getIPAPI;
    int specai = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_report_detial, container, false);


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/

        getIPAPI=new GetIPAPI();


        textTotal = myView.findViewById(R.id.textTotal);
        textDate = myView.findViewById(R.id.textDate);
        textOwe = myView.findViewById(R.id.textOwe);
        textCancel = myView.findViewById(R.id.textCancel);
        textSpecail=myView.findViewById(R.id.textSpecail);
        textSpecail.setVisibility(View.GONE);

        TextView t1 = myView.findViewById(R.id.textTitle1);
        TextView t2 = myView.findViewById(R.id.textTitle2);
        t2.setText("เลขที่ใบรับผ้า");
        TextView t3 = myView.findViewById(R.id.textTitle3);



        RelativeLayout back = myView.findViewById(R.id.btn_scan);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentReport fragmentReport = new FragmentReport();
                //fragmentManageLevel0.setArguments(edit);
                getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentReport)
                        .commit();

            }
        });

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
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
            System.out.println(dates + " : " + branchID);

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                index = bundle.getString("index");
                getDate = bundle.getString("Date");

            }
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            final String date_today = "" + df.format("yyyy-MM-dd", new java.util.Date());
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setIcon(R.mipmap.loading);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กรุณารอสักครู่....");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            Ion.with(getActivity())
                    .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/ReportAppToDay.php")
                    .setBodyParameter("date", date_today)
                    .setBodyParameter("branchID", branchID)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            //try {
                                int total = 0;
                                int owe = 0;
                                int cancel = 0;
                                //Toast.makeText(getActivity(), "" + result.size(), Toast.LENGTH_SHORT).show();
                                if (result.size() == 0) {
                                    mListReport.clear();
                                    myAdapter = new MyAdapterReportDetail(getActivity(), R.layout.list_report_detail, mListReport);
                                    mListView.setAdapter(myAdapter);
                                    new MyToast(getActivity(), "ไม่มีข้อมูลในวันที่กำหนด", 1);
                                    textOwe.setText("ยอดค้างชำระ : ไม่มีข้อมูล");
                                    textTotal.setText("ยอดรวม : ไม่มีข้อมูล");
                                    textCancel.setText("ยอดการยกเลิก : ไม่มีข้อมูล");
                                    textCancel.setVisibility(View.GONE);
                                    textDate.setText("ยอดขายประจำวันที่ : ไม่มีข้อมูล");
                                } else {
                                    for (int i = 0; i < result.size(); i++) {
                                        JsonObject jsonObject = (JsonObject) result.get(i);
                                        String IsOwe=jsonObject.get("Owe").getAsString().substring(0, jsonObject.get("Owe").getAsString().indexOf('.'));
                                        String IsTotal=jsonObject.get("total").getAsString().substring(0, jsonObject.get("total").getAsString().indexOf('.'));
                                        String IsCancel=jsonObject.get("cancel").getAsString().substring(0, jsonObject.get("cancel").getAsString().indexOf('.'));
                                        specai=0;
                                        try {
                                            specai = (int)Double.parseDouble(jsonObject.get("SpecialDiscount").getAsString());
                                        }catch (Exception ex){
                                            specai=0;
                                        }
                                        if(IsOwe.isEmpty()){
                                            owe+=0;
                                        }else if(!IsOwe.isEmpty()&& Integer.parseInt(jsonObject.get("isCancel").getAsString())==1) {
                                            owe += Integer.parseInt(IsOwe);
                                        }
                                        if(IsTotal.isEmpty()){
                                            total+=0;
                                        }else if(!IsTotal.isEmpty()&& Integer.parseInt(jsonObject.get("isCancel").getAsString())==1){
                                            total += Integer.parseInt(IsTotal);
                                        }
                                        if(IsCancel.isEmpty()){
                                            cancel+=0;
                                        }else {
                                            cancel += Integer.parseInt(IsCancel);
                                        }

                                        mListReport.add(new CustomItemReportDetail(i + 1, jsonObject.get("OrderNo").getAsString(),""+IsTotal
                                                ,jsonObject.get("isCancel").getAsString(),jsonObject.get("isPayment").getAsString()));
                                    }
                                    textOwe.setText("ยอดค้างชำระ : "+getFormatedAmount(owe)+" บาท");
                                    textTotal.setText("ยอดรวมสุทธิ : " + getFormatedAmount(total-owe) + " บาท");
                                    textDate.setText("ยอดขายประจำวันที่ : " + date_today);
                                    textCancel.setText("ยอดการยกเลิก : "+getFormatedAmount(cancel) + " บาท");
                                    if(specai==0){
                                        textSpecail.setVisibility(View.GONE);
                                    }else{
                                        textSpecail.setVisibility(View.VISIBLE);
                                        textSpecail.setText("ยอดส่วนลดพิเศษ : "+getFormatedAmount(specai) + " บาท");
                                    }
                                    myAdapter = new MyAdapterReportDetail(getActivity(), R.layout.list_report_detail, mListReport);
                                    mListView.setAdapter(myAdapter);
                                    //}else {
                                    //new MyToast(getActivity(),"ไม่มีข้อมูลในวันที่กำหนด",1);
                                    //}
                                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            CustomItemReportDetail item = mListReport.get(position);
                                            //Toast.makeText(getActivity(), item.sOrderNo , Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(), PreviewActivity.class);
                                            intent.putExtra("branchID", branchID);
                                            intent.putExtra("orderNo", item.sOrderNo);
                                            intent.putExtra("key", "report");
                                            intent.putExtra("Date", date_today);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                                }
                            /*} catch (Exception ex) {
                                new MyToast(getActivity(), "การเชื่อมต่อมีปัญหา", 0);
                            }*/

                            dialog.dismiss();
                        }

                    });

            System.out.println("Date : " + getDate);


            MyFont myFont = new MyFont(getActivity());
            textTotal.setTypeface(myFont.setFont());
            textDate.setTypeface(myFont.setFont());
            textOwe.setTypeface(myFont.setFont());
            textCancel.setTypeface(myFont.setFont());
            t1.setTypeface(myFont.setFont());
            t2.setTypeface(myFont.setFont());
            t3.setTypeface(myFont.setFont());

            TextView Text5 = myView.findViewById(R.id.textView5);
            Text5.setTypeface(myFont.setFont());

            mListView = myView.findViewById(R.id.listReportDetail);

            mListReport = new ArrayList<>();
            mCalendar = Calendar.getInstance();
            dtpicker();


            ImageView imgDate = myView.findViewById(R.id.img_calendar);
            imgDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatePicker1.setYearRange(2018, 2030);
                    mDatePicker1.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "datePicker");
                }
            });
        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return myView;
    }
    private void dtpicker() {
        mDatePicker1 = DatePickerDialog.newInstance(onDateSetListener1,
                mCalendar.get(Calendar.YEAR),       // ปี
                mCalendar.get(Calendar.MONTH),      // เดือน
                mCalendar.get(Calendar.DAY_OF_MONTH),// วัน (1-31)
                false);
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener1 =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, final int day) {
                    dd = "" + day;
                    mm = "" + (month+1);

                    if (dd.length() == 1) {dd4 = "0" + dd;}
                    if (mm.length() == 1) {mm4 = "0" + mm;}
                    if (dd.length() == 2) {dd4 = dd;}
                    if (mm.length() == 2) {mm4 = mm;}
                    dates=year+"-"+mm4+"-"+dd4;

                    textDate.setText("ยอดขายประจำวันที่ : "+dates);
                    System.out.println(dates+" : "+branchID);

                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setIcon(R.mipmap.loading);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("กรุณารอสักครู่....");
                    dialog.setIndeterminate(true);
                    dialog.show();
                    Ion.with(getActivity())
                            .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/ReportAppToDay.php")
                            .setBodyParameter("date", dates)
                            .setBodyParameter("branchID", branchID)
                            .asJsonArray()
                            .setCallback(new FutureCallback<JsonArray>() {
                                @Override
                                public void onCompleted(Exception e, JsonArray result) {
                                    //try {
                                        int total = 0;
                                        int owe=0;
                                        int cancel=0;
                                        //Toast.makeText(getActivity(), "" + result.size(), Toast.LENGTH_SHORT).show();
                                        if (result.size() == 0) {
                                            mListReport.clear();
                                            myAdapter = new MyAdapterReportDetail(getActivity(), R.layout.list_report_detail, mListReport);
                                            mListView.setAdapter(myAdapter);
                                            new MyToast(getActivity(), "ไม่มีข้อมูลในวันที่กำหนด", 1);
                                            textOwe.setText("ยอดค้างชำระ : ไม่มีข้อมูล");
                                            textTotal.setText("ยอดรวม : ไม่มีข้อมูล");
                                            textDate.setText("ยอดขายประจำวันที่ : ไม่มีข้อมูล");
                                            textCancel.setText("*ยอดการยกเลิก : ไม่มีข้อมูล");
                                        } else {
                                            mListReport.clear();
                                            for (int i = 0; i < result.size(); i++) {
                                                JsonObject jsonObject = (JsonObject) result.get(i);
                                                if(jsonObject.get("Owe").getAsString().substring(0, jsonObject.get("Owe").getAsString().indexOf('.')).isEmpty()){
                                                    owe+=0;
                                                }else if(!jsonObject.get("Owe").getAsString().substring(0, jsonObject.get("Owe").getAsString().indexOf('.')).isEmpty()&&
                                                        Integer.parseInt(jsonObject.get("isCancel").getAsString())==1) {
                                                    owe += Integer.parseInt(jsonObject.get("Owe").getAsString().substring(0, jsonObject.get("Owe").getAsString().indexOf('.')));
                                                }
                                                specai=0;
                                                try {
                                                    specai = (int)Double.parseDouble(jsonObject.get("SpecialDiscount").getAsString());
                                                }catch (Exception ex){
                                                    specai=0;
                                                }

                                                if(jsonObject.get("total").getAsString().substring(0, jsonObject.get("total").getAsString().indexOf('.')).isEmpty()){
                                                    total+=0;
                                                }else if(!jsonObject.get("total").getAsString().substring(0, jsonObject.get("total").getAsString().indexOf('.')).isEmpty()&&
                                                        Integer.parseInt(jsonObject.get("isCancel").getAsString())==1){
                                                    total += Integer.parseInt(jsonObject.get("total").getAsString().substring(0, jsonObject.get("total").getAsString().indexOf('.')));
                                                }
                                                if(jsonObject.get("cancel").getAsString().substring(0, jsonObject.get("cancel").getAsString().indexOf('.')).isEmpty()){
                                                    cancel+=0;
                                                }else {
                                                    cancel += Integer.parseInt(jsonObject.get("cancel").getAsString().substring(0, jsonObject.get("cancel").getAsString().indexOf('.')));
                                                }

                                                mListReport.add(new CustomItemReportDetail(i + 1, jsonObject.get("OrderNo").getAsString(),
                                                        jsonObject.get("total").getAsString().substring(0, jsonObject.get("total").getAsString().indexOf('.')),
                                                        jsonObject.get("isCancel").getAsString(),jsonObject.get("isPayment").getAsString()));
                                                //Toast.makeText(getActivity(), "" + result.size() + " , " + jsonObject.get("OrderNo").getAsString(), Toast.LENGTH_SHORT).show();
                                            }
                                            textOwe.setText("ยอดค้างชำระ : "+getFormatedAmount(owe)+" บาท");
                                            textCancel.setText("*ยอดการยกเลิก : "+getFormatedAmount(cancel)+" บาท");
                                            if(specai==0){
                                                textSpecail.setVisibility(View.GONE);
                                            }else{
                                                textSpecail.setVisibility(View.VISIBLE);
                                                textSpecail.setText("ยอดส่วนลดพิเศษ : "+getFormatedAmount(specai) + " บาท");
                                            }
                                            textTotal.setText("ยวดรวมสุทธิ : " + getFormatedAmount(total-owe) + " บาท");
                                            myAdapter = new MyAdapterReportDetail(getActivity(), R.layout.list_report_detail, mListReport);
                                            mListView.setAdapter(myAdapter);
                                            //}else {
                                            //new MyToast(getActivity(),"ไม่มีข้อมูลในวันที่กำหนด",1);
                                            //}
                                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    CustomItemReportDetail item = mListReport.get(position);
                                                    //Toast.makeText(getActivity(), item.sOrderNo , Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getActivity(), PreviewActivity.class);
                                                    intent.putExtra("branchID", branchID);
                                                    intent.putExtra("orderNo", item.sOrderNo);
                                                    intent.putExtra("key", "report");
                                                    intent.putExtra("Date", dates);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    /*}catch (Exception ex){
                                        new MyToast(getActivity(),"การเชื่อมต่อมีปัญหา",0);
                                    }*/

                                    dialog.dismiss();
                                }

                            });
                }
            };
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

}


