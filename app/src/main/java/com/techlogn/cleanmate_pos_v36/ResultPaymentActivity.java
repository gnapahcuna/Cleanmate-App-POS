package com.techlogn.cleanmate_pos_v36;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static maes.tech.intentanim.CustomIntent.customType;

public class ResultPaymentActivity extends AppCompatActivity {

    ImageView line1,line2,line3,back;
    LinearLayout layout;
    TextView t1,t2,t3,t4;
    int cash=0;
    int money=0,orderNo,branchID,totals,isBack=0;
    ProgressDialog dialog;
    private GetIPAPI getIPAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_payment);

        getIPAPI=new GetIPAPI();

        t1=findViewById(R.id.textTotal1);
        t2=findViewById(R.id.textTotal2);
        t3=findViewById(R.id.textTotal3);
        t4=findViewById(R.id.textTotal4);

        line1=findViewById(R.id.line1);
        line2=findViewById(R.id.line2);
        line3=findViewById(R.id.line3);

        layout=findViewById(R.id.layout);

        int width=getResources().getDisplayMetrics().widthPixels;
        /*layout.getLayoutParams().width=(width*60)/100;
        LinearLayout.LayoutParams rl =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        rl.setMargins((width*25)/100, 0, 0, 0);
        layout.setLayoutParams(rl);*/

        line1.getLayoutParams().width=(width*80)/100;
        line2.getLayoutParams().width=(width*80)/100;
        line3.getLayoutParams().width=(width*80)/100;

        orderNo = Integer.parseInt(getIntent().getExtras().get("orderNo").toString());
        branchID = Integer.parseInt(getIntent().getExtras().get("branchID").toString());
        totals = Integer.parseInt(getIntent().getExtras().get("total").toString());

        try{
            isBack=Integer.parseInt(getIntent().getExtras().get("back").toString());
        }catch (Exception ex){
            //isBack=0;
        }
        System.out.print("isBack3 = "+isBack);


        money=totals;

        RelativeLayout counti=findViewById(R.id.layoutClick_addcart);
        counti.getLayoutParams().width=(width*50)/100;
        counti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(isBack==1) {
                    final Dialog dialog1 = new Dialog(ResultPaymentActivity.this);
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
                            if (Integer.parseInt(getIntent().getExtras().getString("select")) == 2) {
                                cash=0;
                                cash = money;
                            }else {
                                cash = 0;
                                SharedPreferences sp = getSharedPreferences("Cash", Context.MODE_PRIVATE);
                                Map<String, ?> entries2 = sp.getAll();
                                Set<String> keys2 = entries2.keySet();
                                String[] getData2;
                                List<String> list2 = new ArrayList<String>(keys2);
                                for (String temp : list2) {
                                    for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                                        getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                                        char chk = getData2[i].charAt(1);
                                        if (chk == 'j') {
                                            cash = Integer.parseInt(getData2[i].substring(3));
                                        }
                                    }
                                }
                            }
                            System.out.println("CASH : "+cash);
                            final ProgressDialog dialog = new ProgressDialog(ResultPaymentActivity.this);
                            dialog.setIcon(R.mipmap.loading);
                            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            dialog.setMessage("กรุณารอสักครู่....");
                            dialog.setIndeterminate(true);
                            dialog.setCancelable(false);
                            dialog.show();
                            android.text.format.DateFormat df = new android.text.format.DateFormat();
                            String datesCreate = "" + df.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());
                            Ion.with(ResultPaymentActivity.this)
                                    .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/IsReturnCustomer1.php")
                                    .setBodyParameter("IsReturnCustomer", "" + 1)
                                    .setBodyParameter("OrderNo", ""+orderNo)
                                    .setBodyParameter("Date", ""+datesCreate)
                                    .setBodyParameter("IsPayment", ""+0)
                                    .setBodyParameter("PaymentType", ""+Integer.parseInt(getIntent().getExtras().getString("select")))
                                    .setBodyParameter("PaymentCash", ""+cash)
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            dialog1.dismiss();
                                            dialog.dismiss();
                                            new MyToast(ResultPaymentActivity.this, result, 1);
                                            /*FragmentReturnCustomer fragmentReturnCustomer = new FragmentReturnCustomer();
                                            getFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.content_frame, fragmentReturnCustomer)
                                                    .commit();*/
                                            SharedPreferences sp = getSharedPreferences("Cash", Context.MODE_PRIVATE);
                                            sp.edit().clear().apply();

                                            Intent in=new Intent(ResultPaymentActivity.this,MenuActivity.class);
                                            in.putExtra("F","ReturnCust1");
                                            in.putExtra("OrderNo",orderNo);
                                            startActivity(in);

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
                    MyFont myFont=new MyFont(ResultPaymentActivity.this);
                    okButton.setTypeface(myFont.setFont());
                    declineButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    des.setTypeface(myFont.setFont());

                }else{
                    if (Integer.parseInt(getIntent().getExtras().getString("select")) == 2) {
                        cash = money;
                    } else {
                        cash = 0;
                        SharedPreferences sp = getSharedPreferences("Cash", Context.MODE_PRIVATE);
                        Map<String, ?> entries2 = sp.getAll();
                        Set<String> keys2 = entries2.keySet();
                        String[] getData2;
                        List<String> list2 = new ArrayList<String>(keys2);
                        for (String temp : list2) {
                            for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                                getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                                char chk = getData2[i].charAt(1);
                                if (chk == 'j') {
                                    cash = Integer.parseInt(getData2[i].substring(3));
                                }
                            }
                        }
                    }

                    String url = getIPAPI.IPAddress+"/CleanmatePOS/Payment.php?PaymentType=" + Integer.parseInt(getIntent().getExtras().getString("select"))
                            + "&Cash=" + cash + "&OrderNo=" + orderNo;
                    new MyAsyncTask().execute(url);
                }
            }
        });

        if(Integer.parseInt(getIntent().getExtras().getString("select"))==1){
            SharedPreferences sp=getSharedPreferences("Cash", Context.MODE_PRIVATE);
            if(sp.getAll().size()>0){
                cash=0;
                Map<String, ?> entries2 = sp.getAll();
                Set<String> keys2 = entries2.keySet();
                String[] getData2;
                List<String> list2 = new ArrayList<String>(keys2);
                for (String temp : list2) {
                    for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                        getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                        char chk = getData2[i].charAt(1);
                        if (chk == 'j') {
                            cash=Integer.parseInt(getData2[i].substring(3));
                        }
                    }
                }
                t1.setText(""+getFormatedAmount(money)+" บาท");
                t2.setText("เงินสด");
                t3.setText(""+getFormatedAmount(cash)+" บาท");
                int total=cash-money;
                t4.setText(""+getFormatedAmount(total)+" บาท");
            }
        }else if(Integer.parseInt(getIntent().getExtras().getString("select"))==2){
            t1.setText(""+getFormatedAmount(money)+" บาท");
            t2.setText("บัตรเครดิต");
            t3.setText(""+getFormatedAmount(money)+" บาท");
            t4.setText(""+getFormatedAmount(0)+" บาท");
        }

        back=findViewById(R.id.img_black);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(getIntent().getExtras().getString("select"))==1){
                    Intent intent = new Intent(getBaseContext(), RecieveMoneyActivity.class);
                    intent.putExtra("ID",""+0);
                    intent.putExtra("orderNo", orderNo);
                    intent.putExtra("branchID", branchID);
                    intent.putExtra("total", totals);
                    intent.putExtra("back", isBack);
                    startActivity(intent);
                    customType(ResultPaymentActivity.this,"fadein-to-fadeout");
                    finish();
                }else if(Integer.parseInt(getIntent().getExtras().getString("select"))==2){
                    Intent intent = new Intent(getBaseContext(), TypePaymentActivity.class);
                    intent.putExtra("ID",""+0);
                    intent.putExtra("orderNo", orderNo);
                    intent.putExtra("branchID", branchID);
                    intent.putExtra("total", totals);
                    intent.putExtra("back", isBack);
                    startActivity(intent);
                    customType(ResultPaymentActivity.this,"fadein-to-fadeout");
                    finish();
                }
            }
        });
    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ResultPaymentActivity.this);
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กำลังตรวจสอบข้อมูล");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected  String doInBackground(String... strings) {
            String response="";
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner=new Scanner(inputStream,"UTF-8");
                response=scanner.useDelimiter("\\A").next();

            }catch (Exception ex){
                System.out.println("Error1 : ");
            }
            try {
                System.out.println(response);
            } catch (Exception ex) {
                System.out.println("Error2 : " + ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            Intent intent = new Intent(ResultPaymentActivity.this, BarcodePayment.class);
            intent.putExtra("ID",""+0);
            intent.putExtra("orderNo", orderNo);
            intent.putExtra("branchID", branchID);
            intent.putExtra("total", totals);
            startActivity(intent);
            overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);

            SharedPreferences sp=getSharedPreferences("Cash", Context.MODE_PRIVATE);
            sp.edit().clear().commit();
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
