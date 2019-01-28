package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.luseen.simplepermission.permissions.Permission;
import com.luseen.simplepermission.permissions.PermissionActivity;
import com.luseen.simplepermission.permissions.PermissionUtils;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static maes.tech.intentanim.CustomIntent.customType;

public class BarcodePayment extends PermissionActivity {

    int total,count;
    ImageView imageView;
    int orderNo,branchID,ID;
    String mPrice,text;
    RelativeLayout preview;

    MyFont myFont;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    ProgressDialog dialog;
    String type;
    SharedPreferences sharedPreferences,sharedPreferences1,sharedPreferences2,sharedPreferences3,sharedPreferences4,sharedPreferences5,
            sharedPreferences6,sharedPreferences7,sharedPreferences8,sp_bcpk,sharedPreferences9;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    private GetIPAPI getIPAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_payment);

        getIPAPI=new GetIPAPI();

        mSQLite = SQLiteHelper.getInstance(this);

        sp_bcpk=getBaseContext().getSharedPreferences("BarcodePK", Activity.MODE_PRIVATE);
        sharedPreferences=getBaseContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
        sharedPreferences1=getBaseContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
        sharedPreferences2=getBaseContext().getSharedPreferences("Coupon", Activity.MODE_PRIVATE);
        sharedPreferences3=getBaseContext().getSharedPreferences("Appoint", Activity.MODE_PRIVATE);
        sharedPreferences4=getBaseContext().getSharedPreferences("CouponValue", Activity.MODE_PRIVATE);
        sharedPreferences5=getBaseContext().getSharedPreferences("Privilage", Activity.MODE_PRIVATE);
        sharedPreferences6=getBaseContext().getSharedPreferences("CouponPoint", Activity.MODE_PRIVATE);
        sharedPreferences7=getBaseContext().getSharedPreferences("Express", Activity.MODE_PRIVATE);

        /*sharedPreferences.edit().clear().apply();
        sharedPreferences1.edit().clear().apply();
        sharedPreferences2.edit().clear().apply();
        sharedPreferences3.edit().clear().apply();
        sharedPreferences4.edit().clear().apply();
        sharedPreferences5.edit().clear().apply();
        sharedPreferences6.edit().clear().apply();
        sharedPreferences7.edit().clear().apply();*/

        //Log
        /*Bugfender.init(this, "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getApplication());*/

        cd = new ConnectionDetector(BarcodePayment.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            try{
                ID = Integer.parseInt(getIntent().getExtras().get("ID").toString());
                if (ID == 0) {
                    String s = getIntent().getExtras().get("orderNo").toString();
                    orderNo = Integer.parseInt(s);
                    branchID = Integer.parseInt(getIntent().getExtras().get("branchID").toString());
                } else if (ID == 1) {
                    orderNo = Integer.parseInt(getIntent().getExtras().get("orderNo").toString());
                    branchID = Integer.parseInt(getIntent().getExtras().get("branchID").toString());
                }
                //System.out.println("Order ID " + orderNo);
            }catch (Exception ex){
                final Dialog dialog = new Dialog(BarcodePayment.this);
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                TextView des = dialog.findViewById(R.id.tv_description);
                title.setText("แจ้งเตือน");
                des.setText("เกิดข้อผิดพลาด กรุณาทำรายการใหม่อีกครั้ง");
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                declineButton.setVisibility(View.GONE);
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url=getIPAPI.IPAddress+"/CleanmatePOS/CancelOrderAll.php?OrderNo="+orderNo+"&BranchID="+branchID;
                        new MyAsyncTask().execute(url,"error");
                        dialog.dismiss();
                    }
                });
                MyFont myFont = new MyFont(BarcodePayment.this);
                declineButton.setTypeface(myFont.setFont());
                okButton.setTypeface(myFont.setFont());
                title.setTypeface(myFont.setFont());
                des.setTypeface(myFont.setFont());
            }


            myFont = new MyFont(getBaseContext());


            String url=getIPAPI.IPAddress+"/CleanmatePOS/Barcode_Display.php?orderNo="+orderNo+"&branchID="+branchID;
            new MyAsyncTask().execute(url,"payment");

            RelativeLayout scan = findViewById(R.id.btn_scan);
            int width=getResources().getDisplayMetrics().widthPixels;
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //scan();
                    /*Intent i = new Intent(getBaseContext(), ManageScanBarcodeProductActivity.class);
                    i.putExtra("orderNo", orderNo);
                    i.putExtra("branchID", branchID);
                    i.putExtra("ID", "" + 0);
                    i.putExtra("total", total);
                    i.putExtra("back", "back1");
                    finish();
                    startActivity(i);
                    overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);*/

                    final Dialog dialog = new Dialog(BarcodePayment.this);
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
                            sharedPreferences9 = getBaseContext().getSharedPreferences("CouponPoint_1", Activity.MODE_PRIVATE);

                            sharedPreferences.edit().clear().apply();
                            sharedPreferences1.edit().clear().apply();
                            sharedPreferences2.edit().clear().apply();
                            sharedPreferences3.edit().clear().apply();
                            sharedPreferences4.edit().clear().apply();
                            sharedPreferences5.edit().clear().apply();
                            sharedPreferences6.edit().clear().apply();
                            sharedPreferences7.edit().clear().apply();
                            sharedPreferences8.edit().clear().apply();
                            sharedPreferences9.edit().clear().apply();
                            sp_bcpk.edit().clear().apply();

                            Intent intent = new Intent(getBaseContext(), MenuActivity.class);
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

                            new MyToast(BarcodePayment.this, "เสร็จสิ้นการทำรายการเรียบร้อย", 1);
                        }
                    });
                    okButton.setTypeface(myFont.setFont());
                    declineButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    des.setTypeface(myFont.setFont());
                }
            });
            RelativeLayout cancel =findViewById(R.id.btn_cancel);
            //cancel.setVisibility(View.GONE);
            scan.getLayoutParams().width=(width*45)/100;
            cancel.getLayoutParams().width=(width*45)/100;
            TextView t7=findViewById(R.id.textView7);
            t7.setText("ไปหน้าตะกร้าสินค้า");
            TextView t71=findViewById(R.id.textView71);
            //t71.setText("ดำเนินการต่อ");
            t71.setText("เสร็จสิ้น");

            TextView title_pv=findViewById(R.id.textView);
            title_pv.setText("ใบรับฝากผ้า");

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = getIPAPI.IPAddress+"/CleanmatePOS/CancelOrderAll.php?OrderNo=" + orderNo + "&BranchID=" + branchID;
                    new MyAsyncTask().execute(url, "cancel");
                    dialog.dismiss();

                }
            });

            preview = findViewById(R.id.btn_preview);
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(BarcodePayment.this, PreviewActivity.class);
                    in.putExtra("key", "order");
                    in.putExtra("orderNo", orderNo);
                    in.putExtra("branchID", branchID);
                    finish();
                    startActivity(in);
                    overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);

                }
            });
            TextView t = findViewById(R.id.textView);
            t.setTypeface(myFont.setFont());
            t7.setTypeface(myFont.setFont());

        }else {
            new MyToast(BarcodePayment.this, "ไม่มีการเชื่อมต่อ Internet", 0);
        }


    }
    private void scanBarcode() {
        Intent it = new Intent(this, BarcodeScanner.class);
        it.putExtra("orderNo",orderNo);
        it.putExtra("branchID",branchID);
        it.putExtra("ID", "" + 0);
        it.putExtra("IsBasket", "IsBasket1");
        it.putExtra("IsScan", "Package");
        //finish();
        startActivityForResult(it, 1010);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*String contents = data.getStringExtra("barcode");
        Intent i = new Intent(getBaseContext(), ManageScanBarcodeProductActivity.class);
        i.putExtra("orderNo", orderNo);
        i.putExtra("branchID", branchID);
        i.putExtra("ID", "" + 0);
        i.putExtra("total", total);
        i.putExtra("barcode", "" + contents);
        i.putExtra("back", "back1");
        finish();
        startActivity(i);
        overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        //System.out.println("STOP");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //System.out.println("START");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //System.out.println("RESUME");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //System.out.println("DESTROY");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            new MyToast(BarcodePayment.this,"ข้อมูลรายการถูกบันทึกแล้ว ไม่สามารถย้อนกลับไปทำรายการได้",0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BarcodePayment.this);
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

            if(strings[1].equals("payment")) {
                type=strings[1];
                String output = "";
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    int specail=0;
                    int netAmount=0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        try{
                            netAmount= (int)Double.parseDouble(jsonObject.optString("NetAmount"));
                        }catch (Exception ex){
                            netAmount= 0;
                        }
                        try{
                            specail= (int)Double.parseDouble(jsonObject.optString("SpecialDiscount"));
                        }catch (Exception ex){
                            specail= 0;
                        }
                        int totals = netAmount-specail;
                        mPrice = ""+totals+".00";
                    }
                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex.getMessage());
                }
            }if(strings[1].equals("cancel")||strings[1].equals("error")) {
                type=strings[1];
                String output = "";
                try {
                    //System.out.println(response);
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

            if (type.trim().equals("payment")) {
                TextView text_orderNo = findViewById(R.id.id_bill);
                TextView textPrice = findViewById(R.id.text_catTH);
                text_orderNo.setText("เลขที่ใบรับผ้า : " + orderNo);
                try {
                    text = mPrice.substring(0, mPrice.indexOf('.'));
                } catch (Exception e) {
                    String url = getIPAPI.IPAddress+"/CleanmatePOS/Barcode_Display.php?orderNo=" + orderNo + "&branchID=" + branchID;
                    new MyAsyncTask().execute(url);
                }

                try {
                    if (text.isEmpty()) {
                        textPrice.setText(text + "0.00");
                    } else {
                        textPrice.setText(text + ".00");
                    }
                }catch (Exception ex){
                    final Dialog dialog = new Dialog(BarcodePayment.this);
                    dialog.setContentView(R.layout.custon_alert_dialog);
                    dialog.show();
                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                    TextView des = dialog.findViewById(R.id.tv_description);
                    title.setText("แจ้งเตือน");
                    des.setText("เกิดข้อผิดพลาด กรุณาทำรายการใหม่อีกครั้ง");
                    Button declineButton = dialog.findViewById(R.id.btn_cancel);
                    declineButton.setVisibility(View.GONE);
                    Button okButton = dialog.findViewById(R.id.btn_ok);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url=getIPAPI.IPAddress+"/CleanmatePOS/CancelOrderAll.php?OrderNo="+orderNo+"&BranchID="+branchID;
                            new MyAsyncTask().execute(url,"error");
                            dialog.dismiss();
                        }
                    });
                    MyFont myFont = new MyFont(BarcodePayment.this);
                    declineButton.setTypeface(myFont.setFont());
                    okButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    des.setTypeface(myFont.setFont());
                }


                //System.out.println("text : " + text);

                text_orderNo.setTypeface(myFont.setFont());
                textPrice.setTypeface(myFont.setFont());

                imageView = findViewById(R.id.image_barcode);
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix;
                    try {
                        bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODE_128, 500, 200);
                    } catch (Exception e) {
                        text = "0.00";
                        bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODE_128, 500, 200);
                    }
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException ex) {
                    ex.printStackTrace();
                }

            } else if (type.trim().equals("error")) {
                /*Intent intent = new Intent(getBaseContext(), BasketActivity.class);
                intent.putExtra("key","error");
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);*/
                Intent in =new Intent(BarcodePayment.this,BasketActivity.class);
                in.putExtra("key","1");
                customType(BarcodePayment.this,"fadein-to-fadeout");
                startActivity(in);
            } else {
                /*new MyToast(BarcodePayment.this, "ยกเลิกรายการทั้งหมดเรียบร้อย", 2);

                sharedPreferences.edit().clear().apply();
                sharedPreferences1.edit().clear().apply();
                sharedPreferences2.edit().clear().apply();
                sharedPreferences3.edit().clear().apply();
                sharedPreferences4.edit().clear().apply();
                sharedPreferences5.edit().clear().apply();
                sharedPreferences6.edit().clear().apply();
                sharedPreferences7.edit().clear().apply();*/

                /*Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);*/

                Intent in =new Intent(BarcodePayment.this,BasketActivity.class);
                in.putExtra("key","1");
                startActivity(in);
                customType(BarcodePayment.this,"fadein-to-fadeout");
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
