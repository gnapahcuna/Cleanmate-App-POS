package com.techlogn.cleanmate_pos_v36;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static maes.tech.intentanim.CustomIntent.customType;

public class TypePaymentActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cash,credit;
    CardView[] cards;
    TextView tcash,tcredit;
    int selected=0,orderNo,branchID,total,isBack=0;
    ImageView back;
    ProgressDialog dialog;
    ImageView imgCash;
    ImageView imgCredit;
    private GetIPAPI getIPAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_payment);

        getIPAPI=new GetIPAPI();

        ImageView back=findViewById(R.id.img_black);
        back.setOnClickListener(this);

        RelativeLayout counti=findViewById(R.id.layoutClick_addcart);
        counti.getLayoutParams().width = (getResources().getDisplayMetrics().widthPixels*50)/100;
        counti.setOnClickListener(this);

        cash=findViewById(R.id.card_cash);
        credit=findViewById(R.id.card_credit);
        tcash=findViewById(R.id.txt_cash);
        tcredit=findViewById(R.id.txt_credit);

        cards=new CardView[]{cash,credit};

        for (CardView c:cards
             ) {
            c.setOnClickListener(this);
        }

        MyFont myFont=new MyFont(TypePaymentActivity.this);

        orderNo = Integer.parseInt(getIntent().getExtras().get("orderNo").toString());
        branchID = Integer.parseInt(getIntent().getExtras().get("branchID").toString());
        total = Integer.parseInt(getIntent().getExtras().get("total").toString());

        try{
            isBack=Integer.parseInt(getIntent().getExtras().get("back").toString());
        }catch (Exception ex){
            //isBack=0;
        }
        System.out.print("isBack1 = "+isBack);

        TextView t=findViewById(R.id.textTotal);
        t.setText("ยอดชำระ : "+getFormatedAmount(total)+" บาท");
        t.setTypeface(myFont.setFont());

        tcash.setTypeface(myFont.setFont());
        tcredit.setTypeface(myFont.setFont());

        tcredit.setText("บัตรเครดิต");

        System.out.println("orderNo "+orderNo);
        System.out.println("branchID "+branchID);
        System.out.println("total "+total);

        imgCash=findViewById(R.id.img_cash);
        imgCredit=findViewById(R.id.img_credit);

        Picasso.with(TypePaymentActivity.this).load(R.drawable.payment_method_cash).into(imgCash);
        Picasso.with(TypePaymentActivity.this).load(R.drawable.payment_method_creadit).into(imgCredit);

        back=findViewById(R.id.img_black);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBack==1){
                    /*FragmentReturnCustomer fragmentReturnCustomer = new FragmentReturnCustomer();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentReturnCustomer)
                            .commit();*/
                    Intent in=new Intent(TypePaymentActivity.this,MenuActivity.class);
                    in.putExtra("F","ReturnCust");
                    in.putExtra("OrderNo",orderNo);
                    startActivity(in);

                }else {
                    String url = getIPAPI.IPAddress+"/CleanmatePOS/CancelOrderAll.php?OrderNo=" + orderNo + "&BranchID=" + branchID;
                    new MyAsyncTask().execute(url, "cancel");
                    SharedPreferences sp = getSharedPreferences("Cash", Context.MODE_PRIVATE);
                    sp.edit().clear().commit();
                }
                //Toast.makeText(getBaseContext(),"Back Bastket Menu",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_cash:
                //
                selected=1;
                cash.setBackgroundColor(Color.parseColor("#2eb8b8"));
                credit.setBackgroundColor(Color.parseColor("#ffffff"));
                tcash.setTextColor(Color.parseColor("#ffffff"));
                tcredit.setTextColor(Color.parseColor("#000000"));
                Picasso.with(TypePaymentActivity.this).load(R.drawable.payment_method_cash_select).into(imgCash);
                Picasso.with(TypePaymentActivity.this).load(R.drawable.payment_method_creadit).into(imgCredit);
                break;
            case R.id.card_credit:
                //
                selected=2;
                credit.setBackgroundColor(Color.parseColor("#2eb8b8"));
                cash.setBackgroundColor(Color.parseColor("#ffffff"));
                tcredit.setTextColor(Color.parseColor("#ffffff"));
                tcash.setTextColor(Color.parseColor("#000000"));
                Picasso.with(TypePaymentActivity.this).load(R.drawable.payment_method_cash).into(imgCash);
                Picasso.with(TypePaymentActivity.this).load(R.drawable.payment_method_credit_select).into(imgCredit);
                break;
            case R.id.layoutClick_addcart:
                //
                if(selected==0){
                    new MyToast(TypePaymentActivity.this,"เลือกรูปแบบการชำระเงินก่อน",0);
                }else if(selected==1){
                    Intent intent = new Intent(getBaseContext(), RecieveMoneyActivity.class);
                    intent.putExtra("select",""+2);
                    intent.putExtra("ID",""+0);
                    intent.putExtra("orderNo", orderNo);
                    intent.putExtra("branchID", branchID);
                    intent.putExtra("total", total);
                    intent.putExtra("back", isBack);
                    startActivity(intent);
                    customType(TypePaymentActivity.this,"fadein-to-fadeout");
                    finish();
                }else if(selected==2){
                    Intent intent = new Intent(getBaseContext(), ResultPaymentActivity.class);
                    intent.putExtra("select",""+2);
                    intent.putExtra("ID",""+0);
                    intent.putExtra("orderNo", orderNo);
                    intent.putExtra("branchID", branchID);
                    intent.putExtra("total", total);
                    intent.putExtra("back", isBack);
                    startActivity(intent);
                    customType(TypePaymentActivity.this,"fadein-to-fadeout");
                    finish();
                }
                break;
        }
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
            dialog = new ProgressDialog(TypePaymentActivity.this);
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
                //httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner=new Scanner(inputStream,"UTF-8");
                response=scanner.useDelimiter("\\A").next();


            }catch (Exception ex){
                System.out.println("Error1 : ");
            }
            if(strings[1].equals("cancel")||strings[1].equals("error")) {
                String output = "";
                try {
                    System.out.println(response);
                } catch (Exception ex) {
                    System.out.println("Error3 : " + ex);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            Intent in =new Intent(TypePaymentActivity.this,BasketActivity.class);
            in.putExtra("key","1");
            customType(TypePaymentActivity.this,"fadein-to-fadeout");
            startActivity(in);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
