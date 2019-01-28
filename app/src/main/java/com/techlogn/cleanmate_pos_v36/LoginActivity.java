package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Scanner;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText user,pass;
    Button signIn;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    TextView mVersion;
    ProgressDialog dialog;
    String firstname,lastname,id_control,id_branch,branchGroup,nameTitle,branchName,IsSignOn,ipAddress;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    private GetIPAPI getIPAPI;

    private GuideView mGuideView;
    private GuideView.Builder builder,builder1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getIPAPI=new GetIPAPI();
        String ip =getIPAPI.IPAddress;


        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 720) {
            //System.out.println("720dp");
        }
        else if (config.smallestScreenWidthDp >= 600) {
            //System.out.println("600dp");
        }
        else if (config.smallestScreenWidthDp >= 480) {
            //System.out.println("480dp");
        }
        else if (config.smallestScreenWidthDp >= 320) {
            //System.out.println("320dp");
        }

        mSQLite=SQLiteHelper.getInstance(LoginActivity.this);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();


        SharedPreferences sharedPreferences = getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences("Member", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences("CouponValue", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences3 = getSharedPreferences("Appoint", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences4 = getSharedPreferences("Privilage", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences5 = getSharedPreferences("CouponPoint", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences6 = getSharedPreferences("Express", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences7 = getSharedPreferences("searchtext", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences8 = getSharedPreferences("ID", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences9=getSharedPreferences("Special", Activity.MODE_PRIVATE);
        SharedPreferences sharedPreferences10=getSharedPreferences("CouponPoint_1", Activity.MODE_PRIVATE);
        SharedPreferences sp_bcpk=getBaseContext().getSharedPreferences("BarcodePK", Activity.MODE_PRIVATE);
        SharedPreferences sp_supplies=getBaseContext().getSharedPreferences("Supplies", Activity.MODE_PRIVATE);
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
        sharedPreferences10.edit().clear().apply();
        sp_bcpk.edit().clear().apply();
        sp_supplies.edit().clear().apply();

        if (isInternetPresent) {

            mVersion = findViewById(R.id.text_version);
            mVersion.setText("V.0.0.0.38");
            MyFont myFont = new MyFont(LoginActivity.this);
            mVersion.setTypeface(myFont.setFont());

            user = findViewById(R.id.edUsername);
            user.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            user.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        pass.requestFocus();
                        return true;
                    }
                    return false;
                }
            });
            pass = findViewById(R.id.edPassword);
            pass.setImeOptions(EditorInfo.IME_ACTION_GO);
            pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );
                        if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                            final Dialog dialog = new Dialog(LoginActivity.this);
                            dialog.setContentView(R.layout.custon_alert_dialog);
                            dialog.show();
                            Window window = dialog.getWindow();
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            TextView title = dialog.findViewById(R.id.tv_quit_learning);
                            TextView des = dialog.findViewById(R.id.tv_description);
                            title.setText("แจ้งเตือน");
                            des.setText("กรุณากรอก Username และ Password");
                            Button okButton = dialog.findViewById(R.id.btn_ok);
                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            Button declineButton = dialog.findViewById(R.id.btn_cancel);
                            declineButton.setVisibility(View.GONE);
                            MyFont myFont = new MyFont(LoginActivity.this);
                            okButton.setTypeface(myFont.setFont());
                            declineButton.setTypeface(myFont.setFont());
                            title.setTypeface(myFont.setFont());
                            des.setTypeface(myFont.setFont());
                        } else {
                            String url=getIPAPI.IPAddress+"/CleanmatePOS/Login.php?user="+user.getText().toString().trim();
                            url+="&pass="+pass.getText().toString().trim()+"&ip="+getDeviceIpAddress()+"&IsSignOn="+1;
                            new MyAsyncTask().execute(url);
                            return true;
                        }
                    }
                    return false;
                }
            });
            user.setText("pang");
            pass.setText("1234");

            signIn = findViewById(R.id.btnSignin);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    //checkFile();
                    if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                        final Dialog dialog = new Dialog(LoginActivity.this);
                        dialog.setContentView(R.layout.custon_alert_dialog);
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView title = dialog.findViewById(R.id.tv_quit_learning);
                        TextView des = dialog.findViewById(R.id.tv_description);
                        title.setText("แจ้งเตือน");
                        des.setText("กรุณากรอก Username และ Password");
                        Button okButton = dialog.findViewById(R.id.btn_ok);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        Button declineButton = dialog.findViewById(R.id.btn_cancel);
                        declineButton.setVisibility(View.GONE);
                                                    /*declineButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                        }
                                                    });*/
                        MyFont myFont = new MyFont(LoginActivity.this);
                        okButton.setTypeface(myFont.setFont());
                        declineButton.setTypeface(myFont.setFont());
                        title.setTypeface(myFont.setFont());
                        des.setTypeface(myFont.setFont());
                    } else {
                        String url=getIPAPI.IPAddress+"/CleanmatePOS/Login.php?user="+user.getText().toString().trim();
                        url+="&pass="+pass.getText().toString().trim()+"&ip="+getDeviceIpAddress()+"&IsSignOn="+1;
                        new MyAsyncTask().execute(url);
                    }
                }
            });
            /*builder = new GuideView.Builder(LoginActivity.this)
                    .setTitle("Guide Title Text")
                    .setContentText("Guide Description Text\n .....Guide Description Text\n .....Guide Description Text .....")
                    .setGravity(GuideView.Gravity.center)
                    .setDismissType(GuideView.DismissType.outside)
                    .setTargetView(user)
                    .setGuideListener(new GuideView.GuideListener() {
                        @Override
                        public void onDismiss(View view) {
                            new GuideView.Builder(LoginActivity.this)
                                    .setTitle("Guide Title Text")
                                    .setContentText("Guide Description Text\n .....Guide Description Text\n .....Guide Description Text .....")
                                    .setGravity(GuideView.Gravity.center) //optional
                                    .setDismissType(GuideView.DismissType.outside) //optional - default GuideView.DismissType.targetView
                                    .setTargetView(signIn)
                                    .setContentTextSize(12)//optional
                                    .setTitleTextSize(14)//optional
                                    .build()
                                    .show();
                        }
                    });
            builder.setTargetView(pass).build();
            mGuideView = builder.build();
            mGuideView.show();*/



        }else {
            new MyToast(LoginActivity.this, "ไม่มีการเชื่อมต่อ Internet", 0);
        }

    }
    @NonNull
    private String getDeviceIpAddress() {
        String actualConnectedToNetwork = null;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi.isConnected()) {
                actualConnectedToNetwork = getWifiIp();
            }
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = getNetworkInterfaceIpAddress();
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = "127.0.0.1";
        }
        return actualConnectedToNetwork;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*if (this.getIntent().getExtras() == null){
                finish();
            }else{

            }*/
            final Dialog dialog = new Dialog(LoginActivity.this);
            dialog.setContentView(R.layout.custon_alert_dialog);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            TextView title = dialog.findViewById(R.id.tv_quit_learning);
            TextView des = dialog.findViewById(R.id.tv_description);
            title.setText("แจ้งเตือน");
            des.setText("ต้องการออกจากแอพพลิเคชั่น?");
            Button declineButton = dialog.findViewById(R.id.btn_cancel);
            declineButton.setVisibility(View.GONE);
            Button okButton = dialog.findViewById(R.id.btn_ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finishAffinity();
                    System.exit(0);
                }
            });
            MyFont myFont = new MyFont(LoginActivity.this);
            declineButton.setTypeface(myFont.setFont());
            okButton.setTypeface(myFont.setFont());
            title.setTypeface(myFont.setFont());
            des.setTypeface(myFont.setFont());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Nullable
    private String getWifiIp() {
        final WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
            int ip = mWifiManager.getConnectionInfo().getIpAddress();
            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                    + ((ip >> 24) & 0xFF);
        }
        return null;
    }


    @Nullable
    public String getNetworkInterfaceIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String host = inetAddress.getHostAddress();
                        if (!TextUtils.isEmpty(host)) {
                            return host;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", "getLocalIpAddress", ex);
        }
        return null;
    }
    class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กำลังตรวจสอบข้อมูล");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
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
                JSONObject jsonObject=new JSONObject(response);
                firstname=jsonObject.optString("firstname");
                lastname=jsonObject.optString("lastname");
                id_control=jsonObject.optString("ControlID");
                id_branch=jsonObject.optString("BranchID");
                branchGroup=jsonObject.optString("BranchGroupID");
                nameTitle=jsonObject.optString("NameTitle");
                branchName=jsonObject.optString("BranchNameTH");
                IsSignOn=jsonObject.optString("IsSignOn");
                ipAddress=jsonObject.optString("SignOnIPAddress");
            }catch (Exception ex){
                System.out.println("Error2");
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (IsSignOn.equals("1")&&!ipAddress.equals(getDeviceIpAddress())) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                TextView des = dialog.findViewById(R.id.tv_description);
                title.setText("แจ้งเตือน");
                des.setText("User นี้กำลัง Login อยู่");
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                declineButton.setVisibility(View.GONE);
                                                    /*declineButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                        }
                                                    });*/
                MyFont myFont = new MyFont(LoginActivity.this);
                okButton.setTypeface(myFont.setFont());
                declineButton.setTypeface(myFont.setFont());
                title.setTypeface(myFont.setFont());
                des.setTypeface(myFont.setFont());
            } else {
                if (firstname.trim().isEmpty() || id_branch.trim().isEmpty()) {
                    final Dialog dialog = new Dialog(LoginActivity.this);
                    dialog.setContentView(R.layout.custon_alert_dialog);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                    TextView des = dialog.findViewById(R.id.tv_description);
                    title.setText("แจ้งเตือน");
                    des.setText("Username หรือ Password ไม่ถูกต้อง");
                    Button okButton = dialog.findViewById(R.id.btn_ok);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button declineButton = dialog.findViewById(R.id.btn_cancel);
                    declineButton.setVisibility(View.GONE);
                                                    /*declineButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                        }
                                                    });*/
                    MyFont myFont = new MyFont(LoginActivity.this);
                    okButton.setTypeface(myFont.setFont());
                    declineButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    des.setTypeface(myFont.setFont());
                } else {
                    SharedPreferences sp = LoginActivity.this.getSharedPreferences("ID", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    sp.edit().clear().apply();
                    ArrayList<String> arr_proTH = new ArrayList<>();
                    arr_proTH.add("id");
                    StringBuilder sb = new StringBuilder();
                    HashSet<String> mSet = new HashSet<>();
                    for (int i = 0; i < arr_proTH.size(); i++) {
                        mSet.add("<a>" + id_control);
                        mSet.add("<b>" + id_branch);
                        mSet.add("<c>" + firstname);
                        mSet.add("<d>" + lastname);
                        mSet.add("<e>" + branchGroup);
                        mSet.add("<f>" + nameTitle);
                        mSet.add("<g>" + branchName);
                    }
                    editor.putStringSet("id", mSet);
                    editor.apply();

                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );

                    startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    Intent in=new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDb = mSQLite.getReadableDatabase();
        String sqlDeleteProduct = "Delete FROM product";
        mDb.execSQL(sqlDeleteProduct);

        String sqlDeleteService = "Delete FROM tb_service";
        mDb.execSQL(sqlDeleteService);

        String sqlDeleteCategory = "Delete FROM category";
        mDb.execSQL(sqlDeleteCategory);

        String sqlDeletePromotion = "Delete FROM promotion_sale";
        mDb.execSQL(sqlDeletePromotion);

        String sqlDeleteCoupon = "Delete FROM tb_coupon";
        mDb.execSQL(sqlDeleteCoupon);

        String sqlDeleteBrochure = "Delete FROM brochure";
        mDb.execSQL(sqlDeleteBrochure);

        String sqlDeletePrivilae = "Delete FROM privilage";
        mDb.execSQL(sqlDeletePrivilae);

        String sqlDeleteSupplies = "Delete FROM supplies";
        mDb.execSQL(sqlDeleteSupplies);

        String sqlDeleteCouponPriceList = "Delete FROM CouponPriceList";
        mDb.execSQL(sqlDeleteCouponPriceList);


        //System.out.println("Delete Data Success");
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
