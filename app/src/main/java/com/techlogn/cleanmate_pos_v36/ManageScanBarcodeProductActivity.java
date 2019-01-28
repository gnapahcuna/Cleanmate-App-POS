package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.Permission;
import com.luseen.simplepermission.permissions.PermissionActivity;
import com.luseen.simplepermission.permissions.PermissionUtils;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ManageScanBarcodeProductActivity extends PermissionActivity {

    ArrayList<String> arr;
    ListView listView;
    String branchID,orderNo,mBarcode;
    MyAdapterBarcode myAdapter;
    ArrayList<MyItemBarcode> items;
    SharedPreferences sharedPreferences,sharedPreferences1,sharedPreferences2,sharedPreferences3,sharedPreferences4,sharedPreferences5,
            sharedPreferences6,sharedPreferences7,sharedPreferences8,sp_bcpk;
    String contents="";
    ArrayList<String>getContents=new ArrayList<>();
    ArrayList<String>getOrderNos=new ArrayList<>();

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    MyFont myFont;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    ProgressDialog dialog;
    String type;

    private GetIPAPI getIPAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        branchID = getIntent().getExtras().get("branchID").toString();
        orderNo = getIntent().getExtras().get("orderNo").toString();

        getIPAPI=new GetIPAPI();

        sp_bcpk=getBaseContext().getSharedPreferences("BarcodePK", Activity.MODE_PRIVATE);
        //Log
        /*Bugfender.init(this, "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getApplication());*/


        setContentView(R.layout.activity_scanbarcode_product);
        cd = new ConnectionDetector(ManageScanBarcodeProductActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            TextView t1 = findViewById(R.id.text_orderno);
            t1.setText("เลขที่ใบรับผ้า");
            TextView t2 = findViewById(R.id.text_barcode);
            t1.setText("เลขที่ออเดอร์");
            t2.setText("เลขบาร์โค้ดถุงบรรจุภัณฑ์");
            myFont = new MyFont(ManageScanBarcodeProductActivity.this);
            t1.setTypeface(myFont.setFont());
            t2.setTypeface(myFont.setFont());

            listView = findViewById(R.id.list_scan);
            arr = new ArrayList<>();

            RelativeLayout b = findViewById(R.id.btn_next);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getBaseContext(), "Scan Agian", Toast.LENGTH_SHORT).show();
                    scan();
                    //myAdapter.notifyDataSetChanged();

                }
            });

            items = new ArrayList<>();
            items.clear();

            Map<String, ?> entries22 = sp_bcpk.getAll();
            Set<String> keys22 = entries22.keySet();
            String[] getData22;
            List<String> list22 = new ArrayList<String>(keys22);
            for (String temp : list22) {
                for (int i = 0; i < sp_bcpk.getStringSet(temp, null).size(); i++) {
                    getData22 = sp_bcpk.getStringSet(temp, null).toArray(new String[sp_bcpk.getStringSet(temp, null).size()]);
                    char chk = getData22[i].charAt(1);
                    if (chk == 'a') {
                        getOrderNos.add((getData22[i].substring(3)));
                    }else if(chk == 'b'){
                        getContents.add((getData22[i].substring(3)));
                    }
                }
            }
            //System.out.println(getOrderNos+" , "+getContents);
            if(sp_bcpk.getAll().size()>0) {
                for (int i = 0; i < getContents.size(); i++) {
                    items.add(new MyItemBarcode(getOrderNos.get(0), getContents.get(i)));
                }
                myAdapter = new MyAdapterBarcode(getBaseContext(), R.layout.list_barcode, items);
                listView.setAdapter(myAdapter);
            }

            /*if(getIntent().getExtras().getString("back").equals("back1")) {
                items.clear();
                myAdapter = new MyAdapterBarcode(getBaseContext(), R.layout.list_barcode, items);
                listView.setAdapter(myAdapter);
                mBarcode=getIntent().getExtras().getString("barcode");
                String url = getIPAPI.IPAddress+"/CleanmatePOS/Transport1.php?Data1=" + branchID + "&Data3=" + orderNo.trim() + "&Data4=" + mBarcode + "&packageType=" + 0;
                new MyAsyncTask().execute(url, "transport");

            }else if(getIntent().getExtras().getString("back").equals("back2")){
                items.clear();
                myAdapter = new MyAdapterBarcode(getBaseContext(), R.layout.list_barcode, items);
                listView.setAdapter(myAdapter);
                mBarcode=getIntent().getExtras().getString("barcode");
                String url = getIPAPI.IPAddress+"/CleanmatePOS/Transport2.php?Data1=" + branchID + "&Data3=" + orderNo.trim();
                new MyAsyncTask().execute(url, "transport1");
            }*/

            if((this.getIntent().getExtras().getString("back") != null)){

            }else{
                scan();
            }


            mSQLite = SQLiteHelper.getInstance(this);



            TextView t = findViewById(R.id.textView);
            TextView t_ = findViewById(R.id.textView_1);
            TextView title = findViewById(R.id.text_title);
            TextView text_next = findViewById(R.id.textView_next);
            text_next.setText("สแกนถุง");
            t.setTypeface(myFont.setFont());
            t_.setTypeface(myFont.setFont());
            text_next.setTypeface(myFont.setFont());
            title.setTypeface(myFont.setFont());

            t_.setText("ย้อนกลับ");


            //
            RelativeLayout cancel =findViewById(R.id.btn_cancel);
            cancel.getLayoutParams().width=(getResources().getDisplayMetrics().widthPixels*45)/100;
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ManageScanBarcodeProductActivity.this, BarcodePayment.class);
                    i.putExtra("ID",""+0);
                    i.putExtra("orderNo", orderNo.trim());
                    i.putExtra("branchID", branchID);
                    i.putExtra("total", getIntent().getExtras().getString("total"));
                    startActivity(i);
                    overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);

                }
            });

            final RelativeLayout finish=findViewById(R.id.btn_finish);
            finish.getLayoutParams().width=(getResources().getDisplayMetrics().widthPixels*45)/100;
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sp_bcpk.getAll().size() > 0) {
                        final Dialog dialog = new Dialog(ManageScanBarcodeProductActivity.this);
                        dialog.setContentView(R.layout.custon_alert_dialog);
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView title = dialog.findViewById(R.id.tv_quit_learning);
                        TextView des = dialog.findViewById(R.id.tv_description);
                        title.setText("แจ้งเตือน");
                        des.setText("ทำรายการสำเร็จเรียบร้อยแล้ว");
                        Button declineButton = dialog.findViewById(R.id.btn_cancel);
                        declineButton.setVisibility(View.GONE);
                        Button okButton = dialog.findViewById(R.id.btn_ok);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sharedPreferences = getBaseContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
                                sharedPreferences1 = getBaseContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
                                sharedPreferences2 = getBaseContext().getSharedPreferences("Coupon", Activity.MODE_PRIVATE);
                                sharedPreferences3 = getBaseContext().getSharedPreferences("Appoint", Activity.MODE_PRIVATE);
                                sharedPreferences4 = getBaseContext().getSharedPreferences("CouponValue", Activity.MODE_PRIVATE);
                                sharedPreferences5 = getBaseContext().getSharedPreferences("Privilage", Activity.MODE_PRIVATE);
                                sharedPreferences6 = getBaseContext().getSharedPreferences("CouponPoint", Activity.MODE_PRIVATE);
                                sharedPreferences7 = getBaseContext().getSharedPreferences("Express", Activity.MODE_PRIVATE);
                                sharedPreferences8 = getBaseContext().getSharedPreferences("Special", Activity.MODE_PRIVATE);

                                sharedPreferences.edit().clear().apply();
                                sharedPreferences1.edit().clear().apply();
                                sharedPreferences2.edit().clear().apply();
                                sharedPreferences3.edit().clear().apply();
                                sharedPreferences4.edit().clear().apply();
                                sharedPreferences5.edit().clear().apply();
                                sharedPreferences6.edit().clear().apply();
                                sharedPreferences7.edit().clear().apply();
                                sharedPreferences8.edit().clear().apply();
                                sp_bcpk.edit().clear().apply();

                                Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                                dialog.dismiss();

                                mDb = mSQLite.getReadableDatabase();
                                String sql2 = "Delete FROM promotion_sale";
                                mDb.execSQL(sql2);

                                String sql3 = "Delete FROM tb_coupon";
                                mDb.execSQL(sql3);

                                String sqlDeletePrivilae = "Delete FROM privilage";
                                mDb.execSQL(sqlDeletePrivilae);

                                new MyToast(ManageScanBarcodeProductActivity.this, "เสร็จสิ้นการทำรายการเรียบร้อย", 1);
                            }
                        });
                        okButton.setTypeface(myFont.setFont());
                        declineButton.setTypeface(myFont.setFont());
                        title.setTypeface(myFont.setFont());
                        des.setTypeface(myFont.setFont());

                    }else{
                        new MyToast(ManageScanBarcodeProductActivity.this, "ยังไม่ได้สแกนถุง", 0);
                    }
                }
            });
            ImageView capture=findViewById(R.id.btn_cap);
            capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i,123);
                }
            });
            //
        }else {
            new MyToast(ManageScanBarcodeProductActivity.this, "ไม่มีการเชื่อมต่อ Internet", 0);
        }
    }
    private void scanBarcode() {
        Intent it = new Intent(this, BarcodeScanner.class);
        it.putExtra("orderNo",orderNo);
        it.putExtra("branchID",branchID);
        it.putExtra("ID", "" + 0);
        it.putExtra("IsBasket", "IsBasket");
        it.putExtra("IsScan", "Package");
        it.putExtra("barcode", mBarcode);
        //finish();
        startActivityForResult(it, 1010);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = bundle.getParcelable("data");
            Bitmap bmp = null;
            ByteArrayOutputStream bos = null;
            byte[] bt = null;
            String encodeString = null;
            if (requestCode == 1010 && resultCode == RESULT_OK) {
                contents = data.getStringExtra("barcode");
                branchID = getIntent().getExtras().get("branchID").toString();
                orderNo = getIntent().getExtras().get("orderNo").toString();
                //System.out.println(orderNo + " : " + branchID + " ,len : " + orderNo.length());

                String url = getIPAPI.IPAddress+"/CleanmatePOS/Transport1.php?Data1=" + branchID + "&Data3=" + orderNo.trim() + "&Data4=" + contents + "&packageType=" + 0;
                new MyAsyncTask().execute(url, "transport");

            } else if (requestCode == 123) {

                try {
                    FileOutputStream fileOutputStream = openFileOutput("myImage.jpg", MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    final File file = getFilesDir();
                    String path = file.getAbsolutePath() + "/myImage.jpg";

                    bmp = BitmapFactory.decodeFile(path);
                    bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bt = bos.toByteArray();
                    encodeString = Base64.encodeToString(bt, Base64.DEFAULT);

                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    String dates = orderNo + df.format("yyyyMMddhhmmssa", new java.util.Date());
                    final ProgressDialog dialog = new ProgressDialog(ManageScanBarcodeProductActivity.this);
                    dialog.setIcon(R.mipmap.loading);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("กรุณารอสักครู่....");
                    dialog.setIndeterminate(true);
                    dialog.show();
                    Ion.with(ManageScanBarcodeProductActivity.this)
                            .load(getIPAPI.IPAddress+"/CleanmatePOS/ImageTransport1.php")
                            .setBodyParameter("Data1", encodeString)
                            .setBodyParameter("Data2", orderNo)
                            .setBodyParameter("Data3", dates)
                            .setBodyParameter("Date", orderNo + dates)
                            .setBodyParameter("OrderNo", "" + orderNo)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {

                                    dialog.dismiss();
                                    new MyToast(getBaseContext(), result, 2);
                                }
                            });


                    //String query_order = "UPDATE ops_transportpackage SET ImageFile='"+mEncode+"' WHERE Barcode='"+mBarcode+"'";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){

        }

        //img.setImageBitmap(bitmap);


    }
    public void scan(){
        if (PermissionUtils.isMarshmallowOrHigher()) {
            requestPermission(Permission.CAMERA, new SinglePermissionCallback() {
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
            dialog = new ProgressDialog(ManageScanBarcodeProductActivity.this);
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กำลังตรวจสอบข้อมูล");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            String output = "";
            type=strings[1];
            try {
                //type=strings[1];
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream, "UTF-8");
                response = scanner.useDelimiter("\\A").next();

            } catch (Exception ex) {
                System.out.println("Error1");
            }
            if(strings[1].equals("transport")) {
                //type=strings[1];
                try {
                    output = response;
                    //System.out.println(response);
                    if(response.trim().equals("\uFEFFบาร์โค้ดนี้ถููกใช้งานแล้ว")){
                        //output = response;
                    }else{
                        JSONArray jsonArray = new JSONArray(response);
                        items.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            getOrderNos.add(jsonObj.getString("OrderNo"));
                            getContents.add(jsonObj.getString("Barcode"));
                            items.add(new MyItemBarcode(jsonObj.getString("OrderNo"), jsonObj.getString("Barcode")));
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex);
                }
            }else if(strings[1].equals("transport1")) {
                //type=strings[1];
                try {
                    if(response.trim().equals("\uFEFFบาร์โค้ดนี้ถููกใช้งานแล้ว")){
                    }else {
                        JSONArray jsonArray = new JSONArray(response);
                        items.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            items.add(new MyItemBarcode(jsonObj.getString("OrderNo"), jsonObj.getString("Barcode")));
                        }
                    }

                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex);
                }
            }
            else if(strings[1].equals("cancel")) {
                //type=strings[1];
                try {
                    System.out.println(response);
                } catch (Exception ex) {
                    System.out.println("Error3 : " + ex);
                }
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(type.trim().equals("transport")) {
                if(s.trim().equals("\uFEFFบาร์โค้ดนี้ถููกใช้งานแล้ว")){
                    new MyToast(getBaseContext(), s, 0);
                    scan();
                }else{
                    new MyToast(getBaseContext(), "สแกนบาร์โค้ดถุงสำเร็จ", 2);
                    myAdapter = new MyAdapterBarcode(getBaseContext(), R.layout.list_barcode, items);
                    listView.setAdapter(myAdapter);

                    SharedPreferences.Editor editor1 = sp_bcpk.edit();
                    StringBuilder sb1 = new StringBuilder();
                    HashSet<String> mSet1 = new HashSet<>();
                    for (int j = 0; j < getOrderNos.size(); j++) {
                        mSet1.add("<a>" + getOrderNos.get(j));
                        mSet1.add("<b>" + getContents.get(j));
                    }
                    editor1.putStringSet(getOrderNos.get(0), mSet1);
                    editor1.apply();
                }
            }else if(type.trim().equals("transport1")) {
                myAdapter = new MyAdapterBarcode(getBaseContext(), R.layout.list_barcode, items);
                listView.setAdapter(myAdapter);
            }else if(type.trim().equals("cancel")){

            }
            else{
                new MyToast(ManageScanBarcodeProductActivity.this,"ยกเลิกรายการทั้งหมดเรียบร้อย",2);
                sharedPreferences=getBaseContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
                sharedPreferences1=getBaseContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
                sharedPreferences2=getBaseContext().getSharedPreferences("Coupon", Activity.MODE_PRIVATE);
                sharedPreferences3=getBaseContext().getSharedPreferences("Appoint", Activity.MODE_PRIVATE);
                sharedPreferences4=getBaseContext().getSharedPreferences("CouponValue", Activity.MODE_PRIVATE);
                sharedPreferences5=getBaseContext().getSharedPreferences("Privilage", Activity.MODE_PRIVATE);
                sharedPreferences6=getBaseContext().getSharedPreferences("CouponPoint", Activity.MODE_PRIVATE);
                sharedPreferences7=getBaseContext().getSharedPreferences("Express", Activity.MODE_PRIVATE);
                sharedPreferences8=getBaseContext().getSharedPreferences("Special", Activity.MODE_PRIVATE);

                sharedPreferences.edit().clear().apply();
                sharedPreferences1.edit().clear().apply();
                sharedPreferences2.edit().clear().apply();
                sharedPreferences3.edit().clear().apply();
                sharedPreferences4.edit().clear().apply();
                sharedPreferences5.edit().clear().apply();
                sharedPreferences6.edit().clear().apply();
                sharedPreferences7.edit().clear().apply();
                sharedPreferences8.edit().clear().apply();

                Intent intent=new Intent(getBaseContext(),MenuActivity.class);
                /*intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);*/
                startActivity(intent);
                overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
