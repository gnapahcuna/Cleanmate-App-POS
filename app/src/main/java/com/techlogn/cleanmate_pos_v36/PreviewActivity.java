package com.techlogn.cleanmate_pos_v36;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.hoin.btsdk.BluetoothService;
import com.hoin.btsdk.PrintPic;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PreviewActivity extends AppCompatActivity implements Runnable {

    ArrayList<String> arrBranchName, arrBranchPhone, arrOrderDate,
            arrAppointDate, arrCustomerName, arrPhoneCustomer, arrOrderNo,
            arrProductName, arrCountProduct, arrPriceProduct, arrTotalProduct,
            arrServiceName, arrNetAmount, arrPromotion, arrCouponDiscount, arrMemberType, arrMemberDiscount,arrIsPayment,
            arrPaymentType,arrCash, arrBranchCode,arrSpecailDiscount;
    LinearLayout layout_head;
    int totals;
    int priceTotal;
    int counts;
    int ii = 0;
    SharedPreferences sharedPreferences;

    String branchID, orderNo, key, orderID,IsPayment,PaymentType;
    ImageView back;

    TextView textBranchName, textBranchPhone, textOrderDate, textAppoint,
            textCustomerName, textOrderNo, textCustomerPhone,textTitleBill;


    MyFont myFont;

    //Print
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    ImageButton mSearch;
    ImageButton mPrint;
    Bitmap mBitmapInsurance = null;
    ByteArrayOutputStream bos = null;
    Bitmap bitmap = null;
    TextView tv;

    TextView index,index1,index2,index22,index3,index33,index4,product,product1,product2,product22,product3,product33,product4,count,count1,count2,count22,count3,count33,count4,text_status,status,text_paymentType,payment,text_cash,cash,text_change,change,count5,count6;
    ArrayList<TextView>arr_text_index,arr_text_product,arr_text_count,arr_text_price,arr_text_total;
    ArrayList<String>arr_text_service;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private GetIPAPI getIPAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        //Log
        /*Bugfender.init(this, "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getApplication());*/

        getIPAPI=new GetIPAPI();

        cd = new ConnectionDetector(PreviewActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            branchID = getIntent().getExtras().get("branchID").toString();
            orderNo = getIntent().getExtras().get("orderNo").toString();
            System.out.println("branch : " + branchID + "orderNo : " + orderNo);
            try {
                orderID = getIntent().getExtras().get("orderID").toString();
            } catch (Exception e) {
            }

            key = getIntent().getExtras().get("key").toString();

            arrBranchName = new ArrayList<>();
            arrBranchPhone = new ArrayList<>();
            arrOrderDate = new ArrayList<>();
            arrCustomerName = new ArrayList<>();
            arrAppointDate = new ArrayList<>();
            arrPhoneCustomer = new ArrayList<>();
            arrOrderNo = new ArrayList<>();
            arrProductName = new ArrayList<>();
            arrCountProduct = new ArrayList<>();
            arrPriceProduct = new ArrayList<>();
            arrTotalProduct = new ArrayList<>();
            arrServiceName = new ArrayList<>();
            arrNetAmount = new ArrayList<>();
            arrPromotion = new ArrayList<>();
            arrCouponDiscount = new ArrayList<>();
            arrMemberType = new ArrayList<>();
            arrMemberDiscount = new ArrayList<>();
            arrIsPayment = new ArrayList<>();

            arrPaymentType = new ArrayList<>();
            arrCash = new ArrayList<>();

            arrBranchCode=new ArrayList<>();

            arrSpecailDiscount=new ArrayList<>();


            arr_text_index = new ArrayList<>();
            arr_text_product = new ArrayList<>();
            arr_text_count = new ArrayList<>();
            arr_text_price = new ArrayList<>();
            arr_text_total = new ArrayList<>();

            arr_text_service = new ArrayList<>();

            textTitleBill=findViewById(R.id.txtTitleBill);
            textBranchName = findViewById(R.id.text_branchName);
            textBranchPhone = findViewById(R.id.text_branchPhone);
            textOrderDate = findViewById(R.id.text_orderDate);
            textAppoint = findViewById(R.id.text_appoint);
            textCustomerName = findViewById(R.id.text_nameCustomer);
            textCustomerPhone = findViewById(R.id.text_phoneCustomer);
            textOrderNo = findViewById(R.id.text_orderNo);

            TextView textIndex = findViewById(R.id.textIndex);
            TextView textList = findViewById(R.id.textList);
            TextView textCount = findViewById(R.id.textCount);
            TextView textPrice = findViewById(R.id.textPrice);
            TextView textTotal = findViewById(R.id.textOwe);

            //TextView textOrderNo1 = findViewById(R.id.text_orderNo);

            myFont = new MyFont(PreviewActivity.this);
            textBranchName.setTypeface(myFont.setFont());
            textBranchPhone.setTypeface(myFont.setFont());
            textOrderDate.setTypeface(myFont.setFont());
            textAppoint.setTypeface(myFont.setFont());
            textCustomerName.setTypeface(myFont.setFont());
            textCustomerPhone.setTypeface(myFont.setFont());
            textOrderNo.setTypeface(myFont.setFont());

            textTitleBill.setTypeface(myFont.setFont());
            textIndex.setTypeface(myFont.setFont());
            textList.setTypeface(myFont.setFont());
            textCount.setTypeface(myFont.setFont());
            textPrice.setTypeface(myFont.setFont());
            textTotal.setTypeface(myFont.setFont());
            //textOrderNo1.setTypeface(myFont.setFont());

            sharedPreferences = PreviewActivity.this.getSharedPreferences("total", Context.MODE_PRIVATE);

            mPrint = findViewById(R.id.btn_print);
            mSearch = findViewById(R.id.btn_scan);
            tv = findViewById(R.id.textCountDown);


            mPrint.setEnabled(false);
            mPrint.setVisibility(View.GONE);
            mSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent serverIntent = new Intent(PreviewActivity.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
            });
            mPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MyToast(PreviewActivity.this, "กำลังปริ้นใบเสร็จ...", 2);
                    printImage();
                }
            });

            mService = new BluetoothService(PreviewActivity.this, mHandler);
            if (mService.isAvailable() == false) {
                new MyToast(PreviewActivity.this, "Bluetooth is not available", 0);
            }

            RelativeLayout.LayoutParams rl =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams rl1 =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams rl2 =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams rl3 =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams rl4 =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

            final int width=getResources().getDisplayMetrics().widthPixels;
            rl.setMargins((width*1)/100, 0, 0, 0);
            rl1.setMargins((width*15)/100, 0, 0, 0);
            rl2.setMargins((width*57)/100, 0, 0, 0);
            rl3.setMargins((width*72)/100, 0, 0, 0);
            rl4.setMargins((width*85)/100, 0, 0, 0);

            rl.addRule(RelativeLayout.BELOW, R.id.line2);
            rl1.addRule(RelativeLayout.BELOW, R.id.line2);
            rl2.addRule(RelativeLayout.BELOW, R.id.line2);
            rl3.addRule(RelativeLayout.BELOW, R.id.line2);
            rl4.addRule(RelativeLayout.BELOW, R.id.line2);

            textIndex.setLayoutParams(rl);
            textList.setLayoutParams(rl1);
            textCount.setLayoutParams(rl2);
            textPrice.setLayoutParams(rl3);
            textTotal.setLayoutParams(rl4);

            back = findViewById(R.id.imageButton);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (key.trim().equals("order")) {
                        Intent in = new Intent(PreviewActivity.this, BarcodePayment.class);
                        in.putExtra("ID", "" + 1);
                        in.putExtra("orderNo", orderNo);
                        in.putExtra("branchID", branchID);
                        startActivity(in);
                        overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                        finish();
                    } else if (key.trim().equals("manage")) {
                        Intent in = new Intent(PreviewActivity.this, MenuActivity.class);
                        in.putExtra("F", "manage");
                        in.putExtra("orderNo", orderNo);
                        in.putExtra("branchID", branchID);
                        in.putExtra("IsSearch", ""+1);

                        //in.putExtra("Search", getIntent().getExtras().get("Search").toString());
                        startActivity(in);
                        overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                        finish();
                    } else if (key.trim().equals("report")) {
                        Intent in = new Intent(PreviewActivity.this, MenuActivity.class);
                        in.putExtra("F", "report");
                        in.putExtra("orderNo", orderNo);
                        in.putExtra("branchID", branchID);
                        in.putExtra("Date", getIntent().getExtras().get("Date").toString());
                        startActivity(in);
                        overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                        finish();
                    }
                    //Close Bluetooth
                }
            });

            final ProgressDialog dialog = new ProgressDialog(PreviewActivity.this);
            dialog.setIcon(R.mipmap.loading);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กรุณารอสักครู่....");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            Ion.with(PreviewActivity.this)
                    .load(getIPAPI.IPAddress+"/CleanmatePOS/PreviewSlipTest.php")
                    .setBodyParameter("branchID", branchID)
                    .setBodyParameter("orderNo", orderNo)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            ArrayList<String> arr_service = new ArrayList<>();
                            ArrayList<String> arr_product = new ArrayList<>();
                            ArrayList<String> arr_price = new ArrayList<>();
                            ArrayList<String> arr_total = new ArrayList<>();
                            ArrayList<String> arr_count = new ArrayList<>();

                            //error
                            for (int i = 0; i < result.size(); i++) {
                                //System.out.println("return : "+result);
                                dialog.dismiss();
                                try {
                                    JsonObject jsonObject = (JsonObject) result.get(i);
                                    arrServiceName.add(jsonObject.get("ServiceNameTH").getAsString());
                                    arrBranchName.add(jsonObject.get("BranchNameTH").getAsString());
                                    arrBranchCode.add(jsonObject.get("BranchCode").getAsString());
                                    arrBranchPhone.add(jsonObject.get("phone_branch").getAsString());
                                    arrOrderDate.add(jsonObject.get("OrderDate").getAsString());
                                    arrCustomerName.add(jsonObject.get("FirstName").getAsString() + " " + jsonObject.get("LastName").getAsString());
                                    arrAppointDate.add(jsonObject.get("AppointmentDate").getAsString());
                                    arrPhoneCustomer.add(jsonObject.get("phone_customer").getAsString());
                                    arrOrderNo.add(jsonObject.get("OrderNo").getAsString());
                                    arrProductName.add(jsonObject.get("ServiceNameTH").getAsString() + "," + jsonObject.get("ProductNameTH").getAsString());
                                    arrCountProduct.add(jsonObject.get("counts").getAsString());
                                    //arrTotalProduct.add(jsonObject.get("sum_product").getAsString());
                                    try{
                                        int a=(int)Double.parseDouble(jsonObject.get("NetAmount").getAsString());
                                        arrNetAmount.add(""+a);
                                    }catch (Exception ex){
                                        arrNetAmount.add(""+0);
                                    }

                                    arrPromotion.add(jsonObject.get("PromoDiscount").getAsString());
                                    try{
                                        int a=(int)Double.parseDouble(jsonObject.get("CouponDiscount").getAsString());
                                        arrCouponDiscount.add(""+a);
                                    }catch (Exception ex){
                                        arrCouponDiscount.add(""+0);
                                    }
                                    arrMemberType.add(jsonObject.get("MemberTypeID").getAsString().trim());
                                    arrMemberDiscount.add(jsonObject.get("MemberDiscount").getAsString().trim());

                                    arrIsPayment.add(jsonObject.get("IsPayment").getAsString().trim());
                                    System.out.println(jsonObject.get("PaymentCash").getAsString()+" : ");
                                    if(Integer.parseInt(arrIsPayment.get(0))==1){
                                        arrPaymentType.add(jsonObject.get("PaymentType").getAsString().trim());
                                        try{
                                            int chk = (int)Double.parseDouble(jsonObject.get("PaymentCash").getAsString().substring(0, jsonObject.get("PaymentCash").getAsString().indexOf('.')));
                                            arrCash.add(jsonObject.get("PaymentCash").getAsString().substring(0, jsonObject.get("PaymentCash").getAsString().indexOf('.')));
                                        }catch (Exception ex){
                                            arrCash.add("0");
                                        }

                                    }

                                    try {
                                        int a = (int)Double.parseDouble(jsonObject.get("SpecialDiscount").getAsString().trim());
                                        arrSpecailDiscount.add(""+a);
                                    }catch (Exception ex){
                                        arrSpecailDiscount.add(""+0);
                                    }

                                    arr_product.add(jsonObject.get("ProductNameTH").getAsString());
                                    arr_service.add(jsonObject.get("ServiceNameTH").getAsString());
                                    arr_price.add(getFormatedAmount(Integer.parseInt(jsonObject.get("Amount").getAsString().substring(0, jsonObject.get("Amount").getAsString().indexOf('.')))));
                                    arr_total.add(getFormatedAmount(Integer.parseInt(jsonObject.get("total").getAsString().substring(0, jsonObject.get("total").getAsString().indexOf('.')))));
                                    arr_count.add(jsonObject.get("counts").getAsString());
                                }catch (Exception ex){
                                    final Dialog dialog = new Dialog(PreviewActivity.this);
                                    dialog.setContentView(R.layout.custon_alert_dialog);
                                    dialog.show();
                                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                                    TextView des = dialog.findViewById(R.id.tv_description);
                                    title.setText("แจ้งเตือน");
                                    des.setText("เกิดข้อผิดพลาด กดปุ่ม ตกลง");
                                    Button declineButton = dialog.findViewById(R.id.btn_cancel);
                                    declineButton.setVisibility(View.GONE);
                                    Button okButton = dialog.findViewById(R.id.btn_ok);
                                    okButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            if (key.trim().equals("order")) {
                                                Intent in = new Intent(PreviewActivity.this, BarcodePayment.class);
                                                in.putExtra("ID", "" + 1);
                                                in.putExtra("orderNo", orderNo);
                                                in.putExtra("branchID", branchID);
                                                startActivity(in);
                                                overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                                                finish();
                                            } else if (key.trim().equals("manage")) {
                                                Intent in = new Intent(PreviewActivity.this, MenuActivity.class);
                                                in.putExtra("F", "manage");
                                                in.putExtra("orderNo", orderNo);
                                                in.putExtra("branchID", branchID);
                                                in.putExtra("IsSearch", ""+1);

                                                //in.putExtra("Search", getIntent().getExtras().get("Search").toString());
                                                startActivity(in);
                                                overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                                                finish();
                                            } else if (key.trim().equals("report")) {
                                                Intent in = new Intent(PreviewActivity.this, MenuActivity.class);
                                                in.putExtra("F", "report");
                                                in.putExtra("orderNo", orderNo);
                                                in.putExtra("branchID", branchID);
                                                in.putExtra("Date", getIntent().getExtras().get("Date").toString());
                                                startActivity(in);
                                                overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                                                finish();
                                            }
                                        }
                                    });
                                    MyFont myFont = new MyFont(PreviewActivity.this);
                                    declineButton.setTypeface(myFont.setFont());
                                    okButton.setTypeface(myFont.setFont());
                                    title.setTypeface(myFont.setFont());
                                    des.setTypeface(myFont.setFont());
                                }

                            }

                            HashSet<String> hashSet1 = new HashSet<>();
                            hashSet1.addAll(arrServiceName);
                            arrServiceName.clear();
                            arrServiceName.addAll(hashSet1);

                            HashSet<String> hashSet2 = new HashSet<>();
                            hashSet2.addAll(arrCustomerName);
                            arrCustomerName.clear();
                            arrCustomerName.addAll(hashSet2);

                            HashSet<String> hashSet3 = new HashSet<>();
                            hashSet3.addAll(arrPhoneCustomer);
                            arrPhoneCustomer.clear();
                            arrPhoneCustomer.addAll(hashSet3);

                            HashSet<String> hashSet4 = new HashSet<>();
                            hashSet4.addAll(arrBranchName);
                            arrBranchName.clear();
                            arrBranchName.addAll(hashSet4);

                            HashSet<String> hashSet44 = new HashSet<>();
                            hashSet44.addAll(arrBranchCode);
                            arrBranchCode.clear();
                            arrBranchCode.addAll(hashSet44);

                            HashSet<String> hashSet5 = new HashSet<>();
                            hashSet5.addAll(arrBranchPhone);
                            arrBranchPhone.clear();
                            arrBranchPhone.addAll(hashSet5);

                            HashSet<String> hashSet6 = new HashSet<>();
                            hashSet6.addAll(arrAppointDate);
                            arrAppointDate.clear();
                            arrAppointDate.addAll(hashSet6);


                            HashSet<String> hashSet7 = new HashSet<>();
                            hashSet7.addAll(arrOrderNo);
                            arrOrderNo.clear();
                            arrOrderNo.addAll(hashSet7);

                            HashSet<String> hashSet8 = new HashSet<>();
                            hashSet8.addAll(arrOrderDate);
                            arrOrderDate.clear();
                            arrOrderDate.addAll(hashSet8);

                            HashSet<String> hashSet9 = new HashSet<>();
                            hashSet9.addAll(arrNetAmount);
                            arrNetAmount.clear();
                            arrNetAmount.addAll(hashSet9);

                            HashSet<String> hashSet10 = new HashSet<>();
                            hashSet10.addAll(arrPromotion);
                            arrPromotion.clear();
                            arrPromotion.addAll(hashSet10);

                            HashSet<String> hashSet11 = new HashSet<>();
                            hashSet11.addAll(arrCouponDiscount);
                            arrCouponDiscount.clear();
                            arrCouponDiscount.addAll(hashSet11);


                            HashSet<String> hashSet12 = new HashSet<>();
                            hashSet12.addAll(arrMemberType);
                            arrMemberType.clear();
                            arrMemberType.addAll(hashSet12);


                            HashSet<String> hashSet13 = new HashSet<>();
                            hashSet13.addAll(arrMemberDiscount);
                            arrMemberDiscount.clear();
                            arrMemberDiscount.addAll(hashSet13);


                            HashSet<String> hashSet14 = new HashSet<>();
                            hashSet14.addAll(arrIsPayment);
                            arrIsPayment.clear();
                            arrIsPayment.addAll(hashSet14);


                            HashSet<String> hashSet15 = new HashSet<>();
                            hashSet15.addAll(arrPaymentType);
                            arrPaymentType.clear();
                            arrPaymentType.addAll(hashSet15);


                            HashSet<String> hashSet16 = new HashSet<>();
                            hashSet16.addAll(arrCash);
                            arrCash.clear();
                            arrCash.addAll(hashSet16);

                            System.out.println("arrSpecailDiscount : "+arrSpecailDiscount);
                            HashSet<String> hashSet17= new HashSet<>();
                            hashSet17.addAll(arrSpecailDiscount);
                            arrSpecailDiscount.clear();
                            arrSpecailDiscount.addAll(hashSet17);

                            if(Integer.parseInt(arrIsPayment.get(0))==1){
                                IsPayment="ชำระเงินแล้ว";
                                if(Integer.parseInt(arrPaymentType.get(0))==1){
                                    PaymentType="เงินสด";
                                }else if(Integer.parseInt(arrPaymentType.get(0))==2){
                                    PaymentType="บัตรเครดิต";
                                }
                            }else if(Integer.parseInt(arrIsPayment.get(0))==0){
                                IsPayment="ค้างชำระ";
                            }

                            textBranchName.setText("สาขา" + arrBranchName.get(0)+" ("+arrBranchCode.get(0)+")");
                            textBranchPhone.setText("โทร : " + formatPhoneNumber(arrBranchPhone.get(0)));
                            textOrderDate.setText("วันที่ทำรายการ : " + arrOrderDate.get(0));
                            textAppoint.setText("วันที่นัดรับผ้า : " + arrAppointDate.get(0));
                            textCustomerName.setText("ชื่อลูกค้า : คุณ " + arrCustomerName.get(0));
                            textOrderNo.setText("เลขที่ใบรับผ้า : "+arrOrderNo.get(0));
                            textCustomerPhone.setText("เบอร์โทรศัพท์มือถือ : " + formatPhoneNumber(arrPhoneCustomer.get(0)));

                            layout_head = findViewById(R.id.list_bill);
                            final LinearLayout layout_test = new LinearLayout(PreviewActivity.this);
                            layout_test.setOrientation(LinearLayout.VERTICAL);

                            ImageView imageView = findViewById(R.id.img_orderno);
                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                            try {
                                BitMatrix bitMatrix = null;
                                try {
                                    int widths=getResources().getDisplayMetrics().widthPixels;
                                    int heights=getResources().getDisplayMetrics().heightPixels;
                                    bitMatrix = multiFormatWriter.encode(arrOrderNo.get(0), BarcodeFormat.CODE_128, (widths*75)/100, (heights*10)/100);
                                } catch (Exception ex) {
                                    //text = "0.00";
                                    //bitMatrix = multiFormatWriter.encode(arrOrderNo.get(0), BarcodeFormat.CODE_128, 500, 200);
                                }
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                imageView.setImageBitmap(bitmap);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            for (int i = 0; i < arrServiceName.size(); i++) {
                                ii++;
                                final TextView service = new TextView(PreviewActivity.this);
                                service.setText("รายการ" + arrServiceName.get(i));
                                service.setTextColor(Color.parseColor("#000000"));
                                service.setTextSize(16);
                                service.setGravity(Gravity.CENTER);
                                service.setTypeface(myFont.setFont());

                                arr_text_service.add(arrServiceName.get(i));

                                Ion.with(PreviewActivity.this)
                                        .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/PreviewProduct.php")
                                        .setBodyParameter("branchID", branchID)
                                        .setBodyParameter("orderNo", orderNo)
                                        .setBodyParameter("serviceName", "" + arrServiceName.get(i))
                                        .asJsonArray()
                                        .setCallback(new FutureCallback<JsonArray>() {
                                            @Override
                                            public void onCompleted(Exception e, JsonArray result) {

                                                layout_test.addView(service);
                                                int indexs = 0;
                                                for (int i = 0; i < result.size(); i++) {
                                                    indexs++;
                                                    dialog.dismiss();
                                                    JsonObject jsonObject = (JsonObject) result.get(i);

                                                    priceTotal += Integer.parseInt(jsonObject.get("total").getAsString().substring(0, jsonObject.get("total").getAsString().indexOf('.')));
                                                    counts += Integer.parseInt(jsonObject.get("counts").getAsString());


                                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    layoutParams.setMargins(40, 2, 30, 5);


                                                    TableRow.LayoutParams lp =
                                                            new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                                                    TableRow.LayoutParams.WRAP_CONTENT);
                                                    TableRow.LayoutParams lp1 =
                                                            new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                                                    TableRow.LayoutParams.WRAP_CONTENT);
                                                    TableRow.LayoutParams lp2 =
                                                            new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                                                    TableRow.LayoutParams.WRAP_CONTENT);
                                                    TableRow.LayoutParams lp3 =
                                                            new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                                                    TableRow.LayoutParams.WRAP_CONTENT);
                                                    TableRow.LayoutParams lp4 =
                                                            new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                                                    TableRow.LayoutParams.WRAP_CONTENT);

                                                    /*lp.setMargins(0, 2, 0, 5);
                                                    lp1.setMargins(80, 2, 0, 5);
                                                    lp2.setMargins(325, 2, 0, 5);
                                                    lp3.setMargins(400, 2, 0, 5);
                                                    lp4.setMargins(475, 2, 0, 5);*/
                                                    Configuration config = PreviewActivity.this.getResources().getConfiguration();
                                                    if (config.smallestScreenWidthDp >= 320||config.smallestScreenWidthDp >= 480) {
                                                        int width=getResources().getDisplayMetrics().widthPixels;
                                                        lp.setMargins(0, 2, 0, 5);
                                                        lp1.setMargins((width*13)/100, 2, 0, 5);
                                                        lp2.setMargins((width*55)/100, 2, 0, 5);
                                                        lp3.setMargins((width*67)/100, 2, 0, 5);
                                                        lp4.setMargins((width*80)/100, 2, 0, 5);
                                                    }else{
                                                        lp.setMargins(0, 2, 0, 5);
                                                        lp1.setMargins(80, 2, 0, 5);
                                                        lp2.setMargins(325, 2, 0, 5);
                                                        lp3.setMargins(400, 2, 0, 5);
                                                        lp4.setMargins(475, 2, 0, 5);
                                                    }



                                                    RelativeLayout layout_order = new RelativeLayout(PreviewActivity.this);


                                                    TextView index_detail = new TextView(PreviewActivity.this);
                                                    TextView product_detail = new TextView(PreviewActivity.this);
                                                    TextView count_detail = new TextView(PreviewActivity.this);
                                                    TextView price_detail = new TextView(PreviewActivity.this);
                                                    TextView total_detail = new TextView(PreviewActivity.this);

                                                    index_detail.setTypeface(myFont.setFont());
                                                    product_detail.setTypeface(myFont.setFont());
                                                    count_detail.setTypeface(myFont.setFont());
                                                    price_detail.setTypeface(myFont.setFont());
                                                    total_detail.setTypeface(myFont.setFont());

                                                    index_detail.setTextColor(Color.parseColor("#000000"));
                                                    index_detail.setTextSize(14);
                                                    index_detail.setText("" + indexs);

                                                    product_detail.setTextColor(Color.parseColor("#000000"));
                                                    product_detail.setTextSize(14);
                                                    product_detail.setText(jsonObject.get("ProductNameTH").getAsString());

                                                    count_detail.setTextColor(Color.parseColor("#000000"));
                                                    count_detail.setTextSize(14);
                                                    count_detail.setText(jsonObject.get("counts").getAsString());


                                                    price_detail.setTextColor(Color.parseColor("#000000"));
                                                    price_detail.setTextSize(14);
                                                    price_detail.setText(getFormatedAmount(Integer.parseInt(jsonObject.get("Amount").getAsString().substring(0, jsonObject.get("Amount").getAsString().indexOf('.')))));


                                                    total_detail.setTextColor(Color.parseColor("#000000"));
                                                    total_detail.setTextSize(14);
                                                    total_detail.setText(getFormatedAmount(Integer.parseInt(jsonObject.get("total").getAsString().substring(0, jsonObject.get("total").getAsString().indexOf('.')))));

                                                    arr_text_index.add(index_detail);
                                                    arr_text_product.add(product_detail);
                                                    arr_text_count.add(count_detail);
                                                    arr_text_price.add(price_detail);
                                                    arr_text_total.add(total_detail);


                                                    layout_order.addView(index_detail, lp);
                                                    layout_order.addView(product_detail, lp1);
                                                    layout_order.addView(count_detail, lp2);
                                                    layout_order.addView(price_detail, lp3);
                                                    layout_order.addView(total_detail, lp4);

                                                    layout_test.addView(layout_order, layoutParams);


                                                }
                                                //sharedPreferences.edit().clear().apply();

                                                //getItem();


                                            }
                                        });


                            }
                            totals = priceTotal;
                            SharedPreferences.Editor editorSP = sharedPreferences.edit();
                            HashSet<String> mSet = new HashSet<>();
                            mSet.add("<a>" + counts);
                            mSet.add("<b>" + priceTotal);
                            mSet.add("<c>" + 0);
                            mSet.add("<d>" + 0);
                            mSet.add("<e>" + totals);
                            editorSP.putStringSet("total", mSet);
                            editorSP.apply();


                            double a = 0;
                            for (int i = 0; i < arrPriceProduct.size(); i++) {
                                a += Double.parseDouble(arrPriceProduct.get(i));
                            }
                            layout_head.addView(layout_test);

                            RelativeLayout line = new RelativeLayout(PreviewActivity.this);
                            RelativeLayout.LayoutParams layoutParams_head = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT, 1);
                            layoutParams_head.setMargins(0, 40, 0, 20);
                            line.setBackgroundColor(Color.parseColor("#000000"));
                            layout_head.addView(line, layoutParams_head);


                            int count_ = 0;
                            for (int i = 0; i < arrCountProduct.size(); i++) {
                                count_ += Integer.parseInt(arrCountProduct.get(i));
                            }
                            int price;
                            price = Integer.parseInt(arrNetAmount.get(0));

                            double aa = Double.parseDouble(arrPromotion.get(0));
                            int promo = (int) aa;
                            System.out.println(promo);

                            int total = price;

                            double bb = Double.parseDouble(arrMemberDiscount.get(0));
                            int member = (int) bb;
                        /*if(Integer.parseInt(arrMemberType.get(0))==1){
                            member=(total*10)/100;
                        }else{
                            member=0;
                        }*/
                            total = total + member + promo;


                            TableRow.LayoutParams lp =
                                    new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                            TableRow.LayoutParams.WRAP_CONTENT);
                            TableRow.LayoutParams lp1 =
                                    new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                            TableRow.LayoutParams.WRAP_CONTENT);
                            TableRow.LayoutParams lp2 =
                                    new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                            TableRow.LayoutParams.WRAP_CONTENT);

                            /*lp.setMargins(40, 2, 0, 0);
                            lp1.setMargins(430, 2, 0, 0);
                            lp2.setMargins(530, 2, 0, 0);*/
                            Configuration config = PreviewActivity.this.getResources().getConfiguration();
                            if (config.smallestScreenWidthDp >= 320||config.smallestScreenWidthDp >= 480) {
                                int width = getResources().getDisplayMetrics().widthPixels;
                                lp.setMargins((width * 15) / 100, 2, 0, 0);
                                lp1.setMargins((width * 60) / 100, 2, 0, 0);
                                lp2.setMargins((width * 80) / 100, 2, 0, 0);
                            }else{
                                lp.setMargins(40, 2, 0, 0);
                                lp1.setMargins(430, 2, 0, 0);
                                lp2.setMargins(530, 2, 0, 0);
                            }

                            RelativeLayout layout_total = new RelativeLayout(PreviewActivity.this);
                            index = new TextView(PreviewActivity.this);
                            product = new TextView(PreviewActivity.this);
                            count = new TextView(PreviewActivity.this);

                            index.setTypeface(myFont.setFont());
                            product.setTypeface(myFont.setFont());
                            count.setTypeface(myFont.setFont());

                            index.setTextColor(Color.parseColor("#000000"));
                            index.setTextSize(14);
                            index.setText("รวมจำนวนผ้า");

                            product.setTextColor(Color.parseColor("#000000"));
                            product.setTextSize(14);
                            product.setText("" + count_);
                            count.setTextColor(Color.parseColor("#000000"));
                            count.setTextSize(14);
                            count.setText("ชิ้น");
                            layout_total.addView(index, lp);
                            layout_total.addView(product, lp1);
                            layout_total.addView(count, lp2);
                            layout_head.addView(layout_total);


                            RelativeLayout layout_total1 = new RelativeLayout(PreviewActivity.this);
                            index1 = new TextView(PreviewActivity.this);
                            product1 = new TextView(PreviewActivity.this);
                            count1 = new TextView(PreviewActivity.this);

                            index1.setTypeface(myFont.setFont());
                            product1.setTypeface(myFont.setFont());
                            count1.setTypeface(myFont.setFont());

                            index1.setTextColor(Color.parseColor("#000000"));
                            index1.setTextSize(14);
                            index1.setText("ราคารวม");

                            product1.setTextColor(Color.parseColor("#000000"));
                            product1.setTextSize(14);
                            product1.setText("" + getFormatedAmount(total) + ".00");

                            count1.setTextColor(Color.parseColor("#000000"));
                            count1.setTextSize(14);
                            count1.setText("บาท");

                            layout_total1.addView(index1, lp);
                            layout_total1.addView(product1, lp1);
                            layout_total1.addView(count1, lp2);
                            layout_head.addView(layout_total1);


                            RelativeLayout layout_total2 = new RelativeLayout(PreviewActivity.this);
                            index2 = new TextView(PreviewActivity.this);
                            product2 = new TextView(PreviewActivity.this);
                            count2 = new TextView(PreviewActivity.this);

                            index2.setTypeface(myFont.setFont());
                            product2.setTypeface(myFont.setFont());
                            count2.setTypeface(myFont.setFont());

                            index2.setTextColor(Color.parseColor("#000000"));
                            index2.setTextSize(14);
                            index2.setText("ส่วนลดโปรโมชั่น");

                            product2.setTextColor(Color.parseColor("#000000"));
                            product2.setTextSize(14);
                            product2.setText("" + getFormatedAmount(promo) + ".00");

                            count2.setTextColor(Color.parseColor("#000000"));
                            count2.setTextSize(14);
                            count2.setText("บาท");

                            layout_total2.addView(index2, lp);
                            layout_total2.addView(product2, lp1);
                            layout_total2.addView(count2, lp2);
                            layout_head.addView(layout_total2);


                            //
                            RelativeLayout layout_total22 = new RelativeLayout(PreviewActivity.this);
                            index22 = new TextView(PreviewActivity.this);
                            product22 = new TextView(PreviewActivity.this);
                            count22 = new TextView(PreviewActivity.this);

                            index22.setTypeface(myFont.setFont());
                            product22.setTypeface(myFont.setFont());
                            count22.setTypeface(myFont.setFont());

                            index22.setTextColor(Color.parseColor("#000000"));
                            index22.setTextSize(14);
                            index22.setText("ส่วนลดคูปองเงินสด");

                            product22.setTextColor(Color.parseColor("#000000"));
                            product22.setTextSize(14);
                            product22.setText("" + getFormatedAmount(Integer.parseInt(arrCouponDiscount.get(0))) + ".00");

                            count22.setTextColor(Color.parseColor("#000000"));
                            count22.setTextSize(14);
                            count22.setText("บาท");

                            layout_total22.addView(index22, lp);
                            layout_total22.addView(product22, lp1);
                            layout_total22.addView(count22, lp2);
                            layout_head.addView(layout_total22);
                            //


                            RelativeLayout layout_total3 = new RelativeLayout(PreviewActivity.this);
                            index3 = new TextView(PreviewActivity.this);
                            product3 = new TextView(PreviewActivity.this);
                            count3 = new TextView(PreviewActivity.this);

                            index3.setTextColor(Color.parseColor("#000000"));
                            index3.setTextSize(14);
                            index3.setText("ส่วนลดสมาชิก");

                            index3.setTypeface(myFont.setFont());
                            product3.setTypeface(myFont.setFont());
                            count3.setTypeface(myFont.setFont());

                            product3.setTextColor(Color.parseColor("#000000"));
                            product3.setTextSize(14);
                            product3.setText("" + getFormatedAmount(member) + ".00");

                            count3.setTextColor(Color.parseColor("#000000"));
                            count3.setTextSize(14);
                            count3.setText("บาท");

                            layout_total3.addView(index3, lp);
                            layout_total3.addView(product3, lp1);
                            layout_total3.addView(count3, lp2);
                            layout_head.addView(layout_total3);

                            //
                            if(Integer.parseInt(arrSpecailDiscount.get(0))!=0) {
                                RelativeLayout layout_total33 = new RelativeLayout(PreviewActivity.this);
                                index33 = new TextView(PreviewActivity.this);
                                product33 = new TextView(PreviewActivity.this);
                                count33 = new TextView(PreviewActivity.this);

                                index33.setTextColor(Color.parseColor("#000000"));
                                index33.setTextSize(14);
                                index33.setText("ส่วนลดพิเศษ");

                                index33.setTypeface(myFont.setFont());
                                product33.setTypeface(myFont.setFont());
                                count33.setTypeface(myFont.setFont());

                                product33.setTextColor(Color.parseColor("#000000"));
                                product33.setTextSize(14);
                                product33.setText("" + getFormatedAmount(Integer.parseInt(arrSpecailDiscount.get(0))) + ".00");

                                count33.setTextColor(Color.parseColor("#000000"));
                                count33.setTextSize(14);
                                count33.setText("บาท");
                                layout_total33.addView(index33, lp);
                                layout_total33.addView(product33, lp1);
                                layout_total33.addView(count33, lp2);
                                layout_head.addView(layout_total33);
                            }
                            //

                            RelativeLayout layout_total4 = new RelativeLayout(PreviewActivity.this);
                            index4 = new TextView(PreviewActivity.this);
                            product4 = new TextView(PreviewActivity.this);
                            count4 = new TextView(PreviewActivity.this);

                            index4.setTextColor(Color.parseColor("#000000"));
                            index4.setTextSize(16);
                            index4.setText("ราคาสุทธิ");

                            index4.setTypeface(myFont.setFont());
                            product4.setTypeface(myFont.setFont());
                            count4.setTypeface(myFont.setFont());

                            product4.setTextColor(Color.parseColor("#000000"));
                            product4.setTextSize(16);
                            product4.setText("" + getFormatedAmount(price-Integer.parseInt(arrSpecailDiscount.get(0))) + ".00");

                            count4.setTextColor(Color.parseColor("#000000"));
                            count4.setTextSize(16);
                            count4.setText("บาท");

                            layout_total4.addView(index4, lp);
                            layout_total4.addView(product4, lp1);
                            layout_total4.addView(count4, lp2);
                            layout_head.addView(layout_total4);

                            RelativeLayout line1 = new RelativeLayout(PreviewActivity.this);
                            RelativeLayout.LayoutParams layoutParams_head1 = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT, 1);
                            layoutParams_head.setMargins(0, 40, 0, 20);
                            line1.setBackgroundColor(Color.parseColor("#000000"));
                            layout_head.addView(line1, layoutParams_head1);

                            RelativeLayout layout_status = new RelativeLayout(PreviewActivity.this);
                            text_status = new TextView(PreviewActivity.this);
                            status = new TextView(PreviewActivity.this);

                            text_status.setTextColor(Color.parseColor("#000000"));
                            text_status.setTextSize(16);
                            text_status.setText("สถานะการชำระเงิน : ");

                            text_status.setTypeface(myFont.setFont());
                            status.setTypeface(myFont.setFont());

                            status.setTextColor(Color.parseColor("#000000"));
                            status.setTextSize(16);
                            status.setText(IsPayment);

                            layout_status.addView(text_status, lp);
                            layout_status.addView(status, lp1);
                            layout_head.addView(layout_status);

                            if(Integer.parseInt(arrIsPayment.get(0))==1&&Integer.parseInt(arrNetAmount.get(0))!=0) {
                                //ชำระเงิน
                                RelativeLayout line2 = new RelativeLayout(PreviewActivity.this);
                                RelativeLayout.LayoutParams layoutParams_head2 = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT, 1);
                                layoutParams_head.setMargins(0, 40, 0, 20);
                                line2.setBackgroundColor(Color.parseColor("#000000"));
                                layout_head.addView(line2, layoutParams_head2);

                                //รูปแบบการชำระเงิน
                                RelativeLayout layout_payment_type = new RelativeLayout(PreviewActivity.this);
                                text_paymentType = new TextView(PreviewActivity.this);
                                payment = new TextView(PreviewActivity.this);

                                text_paymentType.setTextColor(Color.parseColor("#000000"));
                                text_paymentType.setTextSize(16);
                                text_paymentType.setText("ชำระด้วย : ");

                                text_paymentType.setTypeface(myFont.setFont());
                                payment.setTypeface(myFont.setFont());

                                payment.setTextColor(Color.parseColor("#000000"));
                                payment.setTextSize(16);
                                payment.setText(PaymentType);
                                layout_payment_type.addView(text_paymentType, lp);
                                layout_payment_type.addView(payment, lp1);
                                layout_head.addView(layout_payment_type);

                                //รับเงิน
                                RelativeLayout layout_payment_cash = new RelativeLayout(PreviewActivity.this);
                                text_cash = new TextView(PreviewActivity.this);
                                cash = new TextView(PreviewActivity.this);

                                text_cash.setTextColor(Color.parseColor("#000000"));
                                text_cash.setTextSize(16);
                                text_cash.setText("รับเงินมา : ");

                                text_cash.setTypeface(myFont.setFont());
                                cash.setTypeface(myFont.setFont());

                                cash.setTextColor(Color.parseColor("#000000"));
                                cash.setTextSize(16);
                                System.out.println("arrCash : "+arrCash);
                                cash.setText(getFormatedAmount((int)Double.parseDouble(arrCash.get(0)))+ ".00");


                                count5 = new TextView(PreviewActivity.this);
                                count5.setTypeface(myFont.setFont());
                                count5.setTextColor(Color.parseColor("#000000"));
                                count5.setTextSize(16);
                                count5.setText("บาท");

                                layout_payment_cash.addView(text_cash, lp);
                                layout_payment_cash.addView(cash, lp1);
                                layout_payment_cash.addView(count5, lp2);
                                layout_head.addView(layout_payment_cash);

                                //ทอนเงิน
                                RelativeLayout layout_payment_change = new RelativeLayout(PreviewActivity.this);
                                text_change = new TextView(PreviewActivity.this);
                                change = new TextView(PreviewActivity.this);

                                text_change.setTextColor(Color.parseColor("#000000"));
                                text_change.setTextSize(16);
                                text_change.setText("เงินทอน : ");

                                text_change.setTypeface(myFont.setFont());
                                change.setTypeface(myFont.setFont());

                                change.setTextColor(Color.parseColor("#000000"));
                                change.setTextSize(16);
                                int aaa = Integer.parseInt(arrCash.get(0)) - (Integer.parseInt(arrNetAmount.get(0))-Integer.parseInt(arrSpecailDiscount.get(0)));
                                change.setText(""+getFormatedAmount(aaa)+ ".00");

                                count6 = new TextView(PreviewActivity.this);
                                count6.setTypeface(myFont.setFont());
                                count6.setTextColor(Color.parseColor("#000000"));
                                count6.setTextSize(16);
                                count6.setText("บาท");

                                layout_payment_change.addView(text_change, lp);
                                layout_payment_change.addView(change, lp1);
                                layout_payment_change.addView(count6, lp2);
                                layout_head.addView(layout_payment_change);
                            }


                            setImg(arr_product, arr_service, arr_price, arr_total, arr_count);

                        }
                    });
        }else {
            new MyToast(PreviewActivity.this, "ไม่มีการเชื่อมต่อ Internet", 0);
        }
    }

    public static String formatPhoneNumber(String number) {
        number = number.substring(0, number.length() - 4) + "-" + number.substring(number.length() - 4, number.length());
        number = number.substring(0, number.length() - 8) + "-" + number.substring(number.length() - 8, number.length());
        return number;
    }

    private String getFormatedAmount(int amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }

    public String getItem(){
        return  ""+arr_text_index.size();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setImg(ArrayList<String>arr_pro,ArrayList<String>arr_service,ArrayList<String>arr_price,ArrayList<String>arr_total,ArrayList<String>arr_count) {
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String dates = "" + df.format("yyyy-MM-dd hh:mm", new java.util.Date());

        String data_branch= textBranchName.getText().toString().trim()+"\n"+
                textBranchPhone.getText().toString().trim();

        String data = "----------------------------------\n"+
                textOrderDate.getText().toString() + "\n" +
                textAppoint.getText().toString() + "\n" +
                textCustomerName.getText().toString() + "\n" +
                textCustomerPhone.getText().toString() + "\n" +
                "----------------------------------\n";
        for (int i = 0; i < arr_pro.size(); i++) {
            //data_test+=arr_text_service.get(i)+"\n";
            //int index=i+1;
            data += arr_count.get(i) + " " + arr_pro.get(i).trim() + " " + arr_service.get(i) + " " + arr_total.get(i) + ".00\n";
        }

        data += "----------------------------------\n" +
                index.getText().toString() + "\t\t\t\t\t" + product.getText().toString() + "\n" +
                index1.getText().toString() + "\t\t\t\t\t\t\t" + product1.getText().toString() + "\n" +
                index2.getText().toString() + "\t\t\t\t" + product2.getText().toString() + "\n" +
                index22.getText().toString() + "\t\t\t" + product22.getText().toString() + "\n" +
                index3.getText().toString() + "\t\t\t\t\t" + product3.getText().toString() + "\n";
        if(Integer.parseInt(arrSpecailDiscount.get(0))!=0){
            data +=index33.getText().toString() + "\t\t\t\t\t\t" + product33.getText().toString() + "\n";
        }
        data +=  index4.getText().toString() + "\t\t\t\t\t\t\t" + product4.getText().toString() + "\n" +
                /*index.getText().toString()+"\t\t\t\t\t"+product.getText().toString()+"\t\t\t\t\t"+count.getText().toString()+"\n"+
                index1.getText().toString()+"\t\t\t\t\t\t"+product1.getText().toString()+"\t\t\t"+count1.getText().toString()+"\n"+
                index2.getText().toString()+"\t\t\t"+product2.getText().toString()+"\t\t\t\t"+count2.getText().toString()+"\n"+
                index3.getText().toString()+"\t\t\t\t"+product3.getText().toString()+"\t\t\t\t"+count3.getText().toString()+"\n"+
                index4.getText().toString()+"\t\t\t\t\t\t"+product4.getText().toString()+"\t\t\t"+count4.getText().toString()+"\n"+*/
                "----------------------------------\n" +
                "สถานะการชำระเงิน : \t" + status.getText().toString() + "\n" +
                "----------------------------------\n";
        if(Integer.parseInt(arrIsPayment.get(0))==1&&Integer.parseInt(arrNetAmount.get(0))!=0){
            data +=text_paymentType.getText().toString() + "\t\t\t\t\t" + payment.getText().toString() + "\n" +
                    text_cash.getText().toString() + "\t\t\t\t\t\t" + cash.getText().toString() + "\n" +
                    text_change.getText().toString() + "\t\t\t\t\t\t" + change.getText().toString() + "\n" +
                    "----------------------------------\n";
        }
        data+=dates;

        String data1="\n\n\n";
        String data2=textOrderNo.getText().toString();

        Converter convert = new Converter();

        Bitmap bmp = convert.textAsBitmap(data, 18, 2, Color.GRAY,
                Typeface.MONOSPACE);

        Bitmap bmp1 = convert.textAsBitmap(data1, 18, 2, Color.GRAY,
                Typeface.MONOSPACE);

        Bitmap bmp2 = convert.textAsBitmap(data2, 24, 2, Color.GRAY,
                Typeface.MONOSPACE);

        Bitmap bmp_branch = convert.textAsBitmap(data_branch, 24, 2, Color.GRAY,
                Typeface.MONOSPACE);

        bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.icon_slip280);


        Bitmap bitmap1 = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            int widths=getResources().getDisplayMetrics().widthPixels;
            int heights=getResources().getDisplayMetrics().heightPixels;
            BitMatrix bitMatrix = multiFormatWriter.encode(arrOrderNo.get(0), BarcodeFormat.CODE_128, (widths*35)/100, (heights*5)/100);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap1 = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }

        FileOutputStream fileOutputStream = null;
        FileOutputStream fileOutputStream1 = null;
        FileOutputStream fileOutputStream2 = null;
        FileOutputStream fileOutputStream3 = null;
        FileOutputStream fileOutputStream4 = null;
        FileOutputStream fileOutputStream_brch = null;
        try {
            fileOutputStream = openFileOutput("slip.png", MODE_PRIVATE);
            fileOutputStream1 = openFileOutput("logo.png", MODE_PRIVATE);
            fileOutputStream2 = openFileOutput("barcode.png", MODE_PRIVATE);
            fileOutputStream3 = openFileOutput("blank.png", MODE_PRIVATE);
            fileOutputStream4 = openFileOutput("orderno.png", MODE_PRIVATE);

            fileOutputStream_brch = openFileOutput("branch.png", MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream1);
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream2);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        bmp1.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream3);
        bmp2.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream4);
        bmp_branch.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream_brch);
        try {
            fileOutputStream.flush();
            fileOutputStream1.flush();
            fileOutputStream2.flush();
            fileOutputStream3.flush();
            fileOutputStream4.flush();
            fileOutputStream_brch.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
            fileOutputStream1.close();
            fileOutputStream2.close();
            fileOutputStream3.close();
            fileOutputStream4.close();
            fileOutputStream_brch.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = getFilesDir();
        String path = file.getAbsolutePath() + "/slip.png";

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        mBitmapInsurance = BitmapFactory.decodeFile(path, options);

        try {
            bos = new ByteArrayOutputStream();
        } catch (Exception e) {
        }
    }
    public static String dateThai(String strDate)
    {
        String Months[] = {
                "ม.ค", "ก.พ", "มี.ค", "เม.ย",
                "พ.ค", "มิ.ย", "ก.ค", "ส.ค",
                "ก.ย", "ต.ค", "พ.ย", "ธ.ค"};

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        int year=0,month=0,day=0;
        try {
            Date date = df.parse(strDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DATE);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return String.format("%s %s %s", day,Months[month],year+543);
    }

    public static boolean isBluetoothHeadsetConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            new MyToast(PreviewActivity.this,"เชื่อมต่อสำเร็จ",2);
                            mPrint.setEnabled(true);
                            mPrint.setVisibility(View.VISIBLE);
                            mSearch.setVisibility(View.GONE);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d("1111", "22222.....");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d("22222", "11111.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    new MyToast(PreviewActivity.this,"ตัดการเชื่อมต่อกับอุปกรณ์",2);
                    mPrint.setEnabled(false);
                    mPrint.setVisibility(View.GONE);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    new MyToast(PreviewActivity.this,"ไม่สามารถเชื่อมต่ออุปกรณ์ได้",0);
                    break;
            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:

                if (resultCode == Activity.RESULT_OK) {
                    new MyToast(PreviewActivity.this,"เปิด Bluetooth สำเร็จ",2);

                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    con_dev = mService.getDevByMac(address);
                    //new MyToast(PreviewActivity.this,"Connect A",2);
                    mService.connect(con_dev);
                }
                break;
        }
    }

    @SuppressLint("SdCardPath")
    private void printImage() {

        byte[] sendData_brch = null;
        byte[] sendData = null;
        byte[] sendData1 = null;
        byte[] sendData2 = null;
        byte[] sendData3 = null;
        byte[] sendData4 = null;
        PrintPic pg_brch = new PrintPic();
        PrintPic pg = new PrintPic();
        PrintPic pg1 = new PrintPic();
        PrintPic pg2 = new PrintPic();
        PrintPic pg3 = new PrintPic();
        PrintPic pg4 = new PrintPic();
        pg_brch.initCanvas(500);
        pg.initCanvas(500);
        pg1.initCanvas(500);
        pg2.initCanvas(576);
        pg3.initCanvas(500);
        pg4.initCanvas(500);
        pg_brch.initPaint();
        pg.initPaint();
        pg1.initPaint();
        pg2.initPaint();
        pg3.initPaint();
        pg4.initPaint();
        File file_brch = PreviewActivity.this.getFilesDir();
        File file = PreviewActivity.this.getFilesDir();
        File file1 = PreviewActivity.this.getFilesDir();
        File file2 = PreviewActivity.this.getFilesDir();
        File file3 = PreviewActivity.this.getFilesDir();
        File file4 = PreviewActivity.this.getFilesDir();
        String path_brch = file_brch.getAbsolutePath() + "/branch.png";
        String path = file.getAbsolutePath() + "/logo.png";
        String path1 = file1.getAbsolutePath() + "/slip.png";
        String path2 = file2.getAbsolutePath() + "/barcode.png";
        String path3 = file3.getAbsolutePath() + "/blank.png";
        String path4 = file4.getAbsolutePath() + "/orderno.png";
        pg_brch.drawImage(0, 0, path_brch);
        pg.drawImage(0, 0, path);
        pg1.drawImage(0, 0, path1);
        pg2.drawImage(0, 0, path2);
        pg3.drawImage(0, 0, path3);
        pg4.drawImage(0, 0, path4);
        sendData_brch = pg_brch.printDraw();
        sendData = pg.printDraw();
        sendData1 = pg1.printDraw();
        sendData2 = pg2.printDraw();
        sendData3 = pg3.printDraw();
        sendData4 = pg4.printDraw();
        mService.write(sendData);
        mService.write(sendData2);
        mService.write(sendData4);
        mService.write(sendData_brch);
        mService.write(sendData1);
        mService.write(sendData3);

        File[]arrFile={file,file1,file3,file4};
        for (File f:arrFile
             ) {
            deleteFiles(f);
        }

        System.out.println("TAG : " + sendData1.length);
        System.out.println("State : " + mService.getState());
        Log.d("123", "" + sendData1.length);
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d("TAG", "SocketClosed");
        } catch (IOException ex) {
            Log.d("TAG", "CouldNotCloseSocket");
        }
    }

    public void reverseTimer(int Seconds){
        new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
            }

            public void onFinish() {
                tv.setText("Completed");
            }
        }.start();
    }

    @Override
    public void run() {

    }
    private void deleteFiles(File file) {
        if (file.isDirectory())
            for (File f : file.listFiles())
                deleteFiles(f);
        else
            file.delete();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
