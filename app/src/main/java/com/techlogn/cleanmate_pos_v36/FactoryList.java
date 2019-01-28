package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;

import com.luseen.simplepermission.permissions.PermissionActivity;
import com.luseen.simplepermission.permissions.PermissionUtils;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FactoryList extends PermissionActivity {

    String dataID,branchID;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private ProgressDialog dialog;

    private GetIPAPI getIPAPI;
    private String numscan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_list);

        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(this, "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getApplication());*/


        cd = new ConnectionDetector(FactoryList.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            scan();
        }else {
            new MyToast(FactoryList.this, "ไม่มีการเชื่อมต่อ Internet", 0);
        }
    }
    private void scanBarcode() {
        Intent it = new Intent(this, BarcodeScanner.class);
        it.putExtra("IsBasket", "MapBarcode");
        it.putExtra("orderNo",getIntent().getExtras().get("orderNo").toString());
        it.putExtra("productTH",getIntent().getExtras().get("productTH").toString());
        it.putExtra("productEN",getIntent().getExtras().get("productEN").toString());
        it.putExtra("productID",getIntent().getExtras().get("productID").toString());
        it.putExtra("IsScan", "Product");
        it.putExtra("Num",getIntent().getExtras().get("Num").toString());
        it.putExtra("bar1",getIntent().getExtras().get("bar1").toString());
        startActivityForResult(it, 1010);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        final String contents;
        if (requestCode == 1010 && resultCode == RESULT_OK) {
            contents = data.getStringExtra("barcode");


            SharedPreferences sharedPreferences2 = getSharedPreferences("ID", Activity.MODE_PRIVATE);
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
            String url = getIPAPI.IPAddress+"/CleanmatePOS/AddBarcode1.php?orderDetail=" + getIntent().getExtras().get("orderDetail").toString() + "&barcode=" + contents;
            url+="&orderNo="+getIntent().getExtras().get("orderNo").toString();
            new MyAsyncTask().execute(url);
        }
    }
    public void scan(){
        if (PermissionUtils.isMarshmallowOrHigher()) {
            requestPermission(com.luseen.simplepermission.permissions.Permission.CAMERA, new SinglePermissionCallback() {
                @Override
                public void onPermissionResult(boolean permissionGranted,
                                               boolean isPermissionDeniedForever) {

                    if (!permissionGranted) {
                        return;
                    }else{
                        scanBarcode();
                    }
                }
            });
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(FactoryList.this);
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
                System.out.println("Error1");
            }

            String output="";
            try {
                output=response;
                System.out.println(response);
                //String[] splits=response.split(response);

                if(response.trim().equals("\uFEFFบาร์โค้ดนี้ถูกใช้งานแล้ว")){
                    output=response;
                    numscan=""+0;
                }else{
                    output="สแกนบาร์โค้ดสำเร็จ";
                    numscan=response.substring(response.indexOf('.')+1);
                }
            }catch (Exception ex){
                System.out.println("Error2 : "+ex);
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(Integer.parseInt(numscan)>0){
                new MyToast(getBaseContext(), s, 2);
                scan();
            }else if(s.equals("\uFEFFบาร์โค้ดนี้ถูกใช้งานแล้ว")){
                new MyToast(getBaseContext(), s, 0);
                scan();
            } else {
                new MyToast(getBaseContext(), s, 2);
                Intent intent = new Intent(getBaseContext(), ManageCaptureActivity.class);
                intent.putExtra("send", "345");
                intent.putExtra("orderNo", getIntent().getExtras().get("orderNo").toString());
                intent.putExtra("branchID", branchID);
                intent.putExtra("orderID", getIntent().getExtras().get("orderID").toString());
                intent.putExtra("productID", getIntent().getExtras().get("productID").toString());
                intent.putExtra("orderDetail", getIntent().getExtras().get("orderDetail").toString());
                intent.putExtra("packageType", 1);
                intent.putExtra("productTH",getIntent().getExtras().get("productTH").toString());
                intent.putExtra("productEN",getIntent().getExtras().get("productEN").toString());
                intent.putExtra("Num",getIntent().getExtras().get("Num").toString());
                intent.putExtra("bar1",getIntent().getExtras().get("bar1").toString());
                startActivity(intent);
            }
        }
    }
}
