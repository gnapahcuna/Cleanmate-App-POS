package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BarcodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String branchID,dataID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        //Log
        /*Bugfender.init(this, "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getApplication());*/

        mScannerView = findViewById(R.id.zxscan);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

        ImageView back=findViewById(R.id.btn_flash);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getExtras().get("IsBasket").toString().equals("IsBasket")) {
                    Intent in = new Intent(BarcodeScanner.this, MenuActivity.class);
                    in.putExtra("orderNo", getIntent().getExtras().get("orderNo").toString());
                    in.putExtra("branchID", getIntent().getExtras().get("branchID").toString());
                    in.putExtra("ID", getIntent().getExtras().get("ID").toString());
                    in.putExtra("back", "back1");
                    in.putExtra("F", "MapBarcodePK");
                    finish();
                    startActivity(in);
                    overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                }
                else if(getIntent().getExtras().get("IsBasket").toString().equals("IsBasket1")) {
                    Intent in = new Intent(BarcodeScanner.this, MenuActivity.class);
                    in.putExtra("orderNo", getIntent().getExtras().get("orderNo").toString());
                    in.putExtra("branchID", getIntent().getExtras().get("branchID").toString());
                    in.putExtra("ID", getIntent().getExtras().get("ID").toString());
                    in.putExtra("back", "back1");
                    in.putExtra("F", "MapBarcodePK");
                    finish();
                    startActivity(in);
                }
                else if(getIntent().getExtras().get("IsBasket").toString().equals("MapBarcode")) {
                    Intent in = new Intent(BarcodeScanner.this, MenuActivity.class);
                    in.putExtra("F", "MapBarcode");
                    in.putExtra("orderNo",getIntent().getExtras().get("orderNo").toString());
                    in.putExtra("productTH",getIntent().getExtras().get("productTH").toString());
                    in.putExtra("productEN",getIntent().getExtras().get("productEN").toString());
                    in.putExtra("productID",getIntent().getExtras().get("productID").toString());
                    in.putExtra("Num",getIntent().getExtras().get("Num").toString());
                    in.putExtra("bar1",getIntent().getExtras().get("bar1").toString());
                    finish();
                    startActivity(in);
                    overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                }
                else if(getIntent().getExtras().get("IsBasket").toString().equals("ReturnFactory")) {
                    Intent in = new Intent(BarcodeScanner.this, MenuActivity.class);
                    in.putExtra("F", "ReturnFactory");
                    finish();
                    startActivity(in);
                    overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                }
                else if(getIntent().getExtras().get("IsBasket").toString().equals("ReturnCustomer")) {
                    Intent in = new Intent(BarcodeScanner.this, MenuActivity.class);
                    in.putExtra("F", "ReturnCustomer");
                    finish();
                    startActivity(in);
                    overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                }
            }
        });



    }
    @Override
    public void handleResult(Result rawResult) {
        SharedPreferences sharedPreferences2=getSharedPreferences("ID", Activity.MODE_PRIVATE);
        Map<String,?> entries2 = sharedPreferences2.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            //System.out.println(temp+" = "+sharedPreferences.getStringSet(temp,null));
            for (int i = 0; i < sharedPreferences2.getStringSet(temp, null).size(); i++) {
                getData2 = sharedPreferences2.getStringSet(temp, null).toArray(new String[sharedPreferences2.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk=getData2[i].charAt(1);
                if(chk=='a'){
                    dataID=getData2[i].substring(3);
                }else if(chk=='b'){
                    branchID=getData2[i].substring(3);
                }
            }
        }
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String dates = "" + df.format("yy", new java.util.Date());


        if(getIntent().getExtras().getString("IsScan").equals("Product")) {
            int digit1 = Integer.parseInt("" + rawResult.getText().charAt(3) + rawResult.getText().charAt(4));
            int branch = Integer.parseInt("" + rawResult.getText().charAt(0) + rawResult.getText().charAt(1) + rawResult.getText().charAt(2));
            if (rawResult.getText().length() != 10 || Integer.parseInt(dates) != digit1) {
                new MyToast(BarcodeScanner.this, "รูปแบบบาร์โค้ดไม่ถูกต้อง", 0);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();

            } else if (Integer.parseInt(branchID) != branch) {
                new MyToast(BarcodeScanner.this, "รูปแบบบาร์โค้ดไม่ตรงกับสาขาของท่าน", 0);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();
            } else {
                mScannerView.stopCamera();
                Intent itent = new Intent();
                itent.putExtra("barcode", rawResult.getText());
                setResult(RESULT_OK, itent);
                finish();
            }
        }else if(getIntent().getExtras().getString("IsScan").equals("Package")){
            int branch = Integer.parseInt("" + rawResult.getText().charAt(0) + rawResult.getText().charAt(1) + rawResult.getText().charAt(2));
            System.out.println("branchID : "+branchID+" : "+branch);
            if (rawResult.getText().length() != 9) {
                new MyToast(BarcodeScanner.this, "รูปแบบบาร์โค้ดไม่ถูกต้อง", 0);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();

            } else if ((Integer.parseInt(branchID)) != branch) {
                new MyToast(BarcodeScanner.this, "รูปแบบบาร์โค้ดไม่ตรงกับสาขาของท่าน", 0);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();
            } else {
                mScannerView.stopCamera();
                Intent itent = new Intent();
                itent.putExtra("barcode", rawResult.getText());
                setResult(RESULT_OK, itent);
                finish();
            }
        }else if(getIntent().getExtras().getString("IsScan").equals("PackagePL")){
            //int branch = Integer.parseInt("" + rawResult.getText().charAt(0) + rawResult.getText().charAt(1) + rawResult.getText().charAt(2));
            String digit=""+rawResult.getText().charAt(0)+rawResult.getText().charAt(1)+rawResult.getText().charAt(2)+rawResult.getText().charAt(3);

            if (rawResult.getText().length() != 11||!digit.equals("PL"+dates)) {
                new MyToast(BarcodeScanner.this, "รูปแบบบาร์โค้ดไม่ถูกต้อง", 0);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();

            } else {
                mScannerView.stopCamera();
                Intent itent = new Intent();
                itent.putExtra("barcode", rawResult.getText());
                setResult(RESULT_OK, itent);
                finish();
            }
        }else if(getIntent().getExtras().getString("IsScan").equals("Order")){
            if (rawResult.getText().length() != 10) {
                new MyToast(BarcodeScanner.this, "รูปแบบบาร์โค้ดไม่ถูกต้อง", 0);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();

            } else {
                mScannerView.stopCamera();
                Intent itent = new Intent();
                itent.putExtra("barcode", rawResult.getText());
                setResult(RESULT_OK, itent);
                finish();
            }
        }else if(getIntent().getExtras().getString("IsScan").equals("Order1")) {
            if (rawResult.getText().length() != 10) {
                new MyToast(BarcodeScanner.this, "รูปแบบบาร์โค้ดไม่ถูกต้อง", 0);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();

            } else {
                mScannerView.stopCamera();
                Intent itent = new Intent();
                itent.putExtra("barcode", rawResult.getText());
                setResult(RESULT_OK, itent);
                finish();
            }
        }


    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}