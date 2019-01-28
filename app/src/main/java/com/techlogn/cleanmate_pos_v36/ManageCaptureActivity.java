package com.techlogn.cleanmate_pos_v36;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import br.com.felix.imagezoom.ImageZoom;
import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ManageCaptureActivity extends AppCompatActivity {
    ArrayList<String>arrImg;
    private static ViewPager mPager;
    private static int currentPage = 0;
    int z=0;

    private ImageView picFrame;
    private Camera camera;
    String proID,orderDetailID,branchID,orderNo,orderID,packageType,send;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private ProgressDialog dialog;
    private String type;

    private GetIPAPI getIPAPI;
    private int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_product);


        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(this, "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getApplication());*/

        RelativeLayout ln_frag3=findViewById(R.id.rl_slide);
        width=getResources().getDisplayMetrics().widthPixels;
        ln_frag3.getLayoutParams().height=(width*100)/100;
        ln_frag3.getLayoutParams().width=(width*90)/100;


        cd = new ConnectionDetector(ManageCaptureActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {


            send = getIntent().getExtras().get("send").toString();
            String chk = "";

            if (Integer.parseInt(send) == 123) {
                orderDetailID = getIntent().getExtras().getString("AA");
                chk = "123";

            } else if (Integer.parseInt(send) == 345) {
                orderDetailID = getIntent().getExtras().getString("orderDetail");
                TextView text = findViewById(R.id.text_scan);
                text.setText("เสร็จสิ้น");
                chk = "345";
            }

            //System.out.println("orderDetailID : " + orderDetailID + "send : " + send + "chk : " + chk);


            loadImage();

            RelativeLayout cap = findViewById(R.id.btn_scan);
            //int width=getResources().getDisplayMetrics().widthPixels;
            int heigth=getResources().getDisplayMetrics().heightPixels;
            cap.getLayoutParams().width = (width*50)/100;
            cap.getLayoutParams().height = (heigth*8)/100;
            cap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Capture();

                    camera = new Camera.Builder()
                            .setDirectory("pics")
                            .setName("ali_" + System.currentTimeMillis())
                            .setImageFormat(Camera.IMAGE_JPEG)
                            .setCompression(75)
                            .setImageHeight(1000)
                            .build(ManageCaptureActivity.this);
                    try {
                        camera.takePicture();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            final RelativeLayout finish = findViewById(R.id.layoutClick_addcart);
            finish.getLayoutParams().width = (width*50)/100;
            finish.getLayoutParams().height = (heigth*8)/100;
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (z == 1) {
                        new MyToast(ManageCaptureActivity.this, "กรุณาถ่ายรูปสินค้าก่อน", 0);
                    } else {
                        if (Integer.parseInt(send) == 123) {
                            Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                            intent.putExtra("F", "1010");
                            intent.putExtra("orderNo", getIntent().getExtras().get("orderNo").toString());
                            intent.putExtra("productID", getIntent().getExtras().get("proID").toString());
                            intent.putExtra("productTH",getIntent().getExtras().get("productTH").toString());
                            intent.putExtra("productEN",getIntent().getExtras().get("productEN").toString());
                            intent.putExtra("Num",getIntent().getExtras().get("Num").toString());
                            intent.putExtra("bar1",getIntent().getExtras().get("bar1").toString());
                            startActivity(intent);
                        } else if (Integer.parseInt(send) == 345) {
                            Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                            intent.putExtra("F", "1010");
                            intent.putExtra("orderNo", getIntent().getExtras().get("orderNo").toString());
                            intent.putExtra("productID", getIntent().getExtras().get("productID").toString());
                            intent.putExtra("productTH",getIntent().getExtras().get("productTH").toString());
                            intent.putExtra("productEN",getIntent().getExtras().get("productEN").toString());
                            intent.putExtra("Num",getIntent().getExtras().get("Num").toString());
                            intent.putExtra("bar1",getIntent().getExtras().get("bar1").toString());
                            startActivity(intent);
                        }
                    }
                }
            });

            ImageView btn_comment = findViewById(R.id.btn_comment);
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(ManageCaptureActivity.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_comment);
                    dialog.show();
                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                    title.setText("ระบุรายละเอียดพิเศษ");
                    final RadioButton chk1 = dialog.findViewById(R.id.radioButton1);
                    final RadioButton chk2 = dialog.findViewById(R.id.radioButton2);
                    final RadioButton chk3 = dialog.findViewById(R.id.radioButton3);
                    final RadioButton chk4 = dialog.findViewById(R.id.radioButton4);
                    final RadioButton chk5 = dialog.findViewById(R.id.radioButton5);
                    Button declineButton = dialog.findViewById(R.id.btn_cancel);
                    final RadioButton[] arrs = new RadioButton[]{chk1, chk2, chk3, chk4, chk5};
                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button okButton = dialog.findViewById(R.id.btn_ok);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            int i = 0;
                            String commnet = "";
                            for (RadioButton ch : arrs
                                    ) {
                                i++;
                                if (ch.isChecked() == true) {
                                    commnet = ch.getText().toString().trim();
                                }
                            }

                            android.text.format.DateFormat df = new android.text.format.DateFormat();
                            String dates = "" + df.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date());

                            System.out.println("orderDetailID : "+orderDetailID );
                            String url=getIPAPI.IPAddress+"/CleanmatePOS/SpecialDetial.php?SpecialDetial="+commnet+"&OrderDetailID="+orderDetailID;
                            url+="&OrderDetailID="+orderDetailID;
                            new MyAsyncTask().execute(url,"special");


                        }
                    });
                    MyFont myFont = new MyFont(ManageCaptureActivity.this);
                    okButton.setTypeface(myFont.setFont());
                    declineButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    chk1.setTypeface(myFont.setFont());
                    chk2.setTypeface(myFont.setFont());
                    chk3.setTypeface(myFont.setFont());
                    chk4.setTypeface(myFont.setFont());
                    chk5.setTypeface(myFont.setFont());
                }
            });
        }else {
            new MyToast(ManageCaptureActivity.this, "ไม่มีการเชื่อมต่อ Internet", 0);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String SEND=getIntent().getExtras().getString("send");
        String ID;

        ID=getIntent().getExtras().getString("orderDetail");

        System.out.println(orderDetailID);
        System.out.println("ID : "+ID+", SEND : "+SEND);

        //Bundle bundle =data.getExtras();
        Bitmap mBitmapInsurance=null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;

        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap=null;
            try {
                bitmap= camera.getCameraBitmap();
            }catch (Exception e){

            }

            if (bitmap != null) {
                //picFrame.setImageBitmap(bitmap);

                try
                {
                    DisplayMetrics displayMetrics=new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int wpx=displayMetrics.widthPixels;
                    int hpx=displayMetrics.heightPixels;
                    float xdpi=displayMetrics.xdpi;
                    float ydpi=displayMetrics.ydpi;
                    float w=wpx/xdpi;
                    float h=hpx/ydpi;
                    double screen=Math.sqrt(w*w+h*h);
                    Configuration config = getResources().getConfiguration();


                    FileOutputStream fileOutputStream=openFileOutput("myImage.jpg",MODE_PRIVATE);
                    if (config.smallestScreenWidthDp >= 480) {
                        if(screen>=5.95){
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        }else{
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        }
                    }else{
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    }

                    fileOutputStream.flush();
                    fileOutputStream.close();
                    File file=getFilesDir();
                    String path=file.getAbsolutePath()+"/myImage.jpg";

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;

                    mBitmapInsurance = BitmapFactory.decodeFile(path,options);

                    //bmp = BitmapFactory.decodeFile(path);
                    try{
                        bos = new ByteArrayOutputStream();
                    }catch (Exception e){
                        new MyToast(ManageCaptureActivity.this,"เกิดข้อผิดพลาด กรุณาลองอีกครั้ง",0);
                    }

                    if (config.smallestScreenWidthDp >= 480) {
                        if(screen>=5.95){
                            mBitmapInsurance.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        }else{
                            mBitmapInsurance.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        }
                    }else{
                        mBitmapInsurance.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    }

                    //bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bt = bos.toByteArray();
                    encodeString = Base64.encodeToString(bt, Base64.DEFAULT);

                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    String dates = orderDetailID + df.format("yyyyMMddhhmmss", new java.util.Date());

                    System.out.println("Date : "+ID+dates);

                    final ProgressDialog dialog = new ProgressDialog(ManageCaptureActivity.this);
                    dialog.setIcon(R.mipmap.loading);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("กรุณารอสักครู่....");
                    dialog.setIndeterminate(true);
                    dialog.show();
                    Ion.with(ManageCaptureActivity.this)
                            .load(getIPAPI.IPAddress+"/CleanmatePOS/ImageProduct.php")
                            .setBodyParameter("Data1", encodeString)
                            .setBodyParameter("Data2", orderDetailID)
                            .setBodyParameter("Data3", dates)
                            .setBodyParameter("Date", orderDetailID+dates)
                            .setBodyParameter("OrderNo", ""+getIntent().getExtras().get("orderNo").toString())
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {

                                    dialog.dismiss();
                                    new MyToast(getBaseContext(), result, 2);

                                    Intent intent = ManageCaptureActivity.this.getIntent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    ManageCaptureActivity.this.finish();
                                    ManageCaptureActivity.this.overridePendingTransition(0, 0);
                                    ManageCaptureActivity.this.startActivity(intent);
                                    ManageCaptureActivity.this.overridePendingTransition(0, 0);
                                }
                            });

                    freeMemory();
                    //String query_order = "UPDATE ops_transportpackage SET ImageFile='"+mEncode+"' WHERE Barcode='"+mBarcode+"'";
                }catch (Exception e){
                    e.printStackTrace();
                    new MyToast(ManageCaptureActivity.this,"เกิดข้อผิดพลาด กรุณาลองอีกครั้ง",0);
                }

            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }
    public void loadImage(){
        arrImg=new ArrayList<>();

        final ProgressDialog dialog=new ProgressDialog(ManageCaptureActivity.this);
        dialog.setIcon(R.mipmap.loading);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("กรุณารอสักครู่....");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        Ion.with(ManageCaptureActivity.this)
                .load(getIPAPI.IPAddress+"/CleanmatePOS/ViewImageMobile.php")
                .setBodyParameter("Data1",orderDetailID)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        dialog.dismiss();

                        try {


                            if (result.size() <= 0) {
                                new MyToast(ManageCaptureActivity.this, "ยังไม่มีรูปถ่ายสินค้านี้", 0);
                                z = 1;
                            }
                            for (int i = 0; i < result.size(); i++) {

                                JsonObject jsonObject = (JsonObject) result.get(i);
                                //System.out.println(jsonObject.get("ContentImage").getAsString());
                                arrImg.add(jsonObject.get("ImageFile").getAsString());
                            }

                            ImageZoom imageZoom1 = findViewById(R.id.img1);
                            ImageZoom imageZoom2 = findViewById(R.id.img2);
                            ImageZoom imageZoom3 = findViewById(R.id.img3);
                            ImageZoom imageZoom4 = findViewById(R.id.img4);

                            TextView textView1 = findViewById(R.id.text1);
                            TextView textView2 = findViewById(R.id.text2);
                            TextView textView3 = findViewById(R.id.text3);
                            TextView textView4 = findViewById(R.id.text4);

                            imageZoom1.setVisibility(View.GONE);
                            imageZoom2.setVisibility(View.GONE);
                            imageZoom3.setVisibility(View.GONE);
                            imageZoom4.setVisibility(View.GONE);

                            textView1.setVisibility(View.GONE);
                            textView2.setVisibility(View.GONE);
                            textView3.setVisibility(View.GONE);
                            textView4.setVisibility(View.GONE);
                            ImageZoom[] imgs=new ImageZoom[]{imageZoom1,imageZoom2,imageZoom3,imageZoom4};

                            TextView[] text=new TextView[]{textView1,textView2,textView3,textView4};

                            for(int i=0;i<arrImg.size();i++) {
                                if (i == 4) {
                                    break;
                                } else {
                                    text[i].setVisibility(View.VISIBLE);
                                    imgs[i].setVisibility(View.VISIBLE);
                                    int index=i+1;
                                    text[i].setText("รูปที่ "+index);
                                    Picasso.with(ManageCaptureActivity.this).load(arrImg.get(i)).into(imgs[i]);
                                }
                            }

                            /*mPager = (ViewPager) findViewById(R.id.pager);
                            mPager.setAdapter(new MyAdapterSlideImage_product(getBaseContext(), arrImg));
                            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                            indicator.setViewPager(mPager);

                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == arrImg.size()) {
                                        currentPage = 0;
                                    }
                                    mPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 4000, 4000);*/


                        } catch (Exception ex) {
                            new MyToast(ManageCaptureActivity.this, "การเชื่อมต่อมีปัญหา", 0);
                        }
                    }
                });
        freeMemory();
    }
    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
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
            dialog = new ProgressDialog(ManageCaptureActivity.this);
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
            String output="";
            if(strings[1].equals("special")){
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
                    System.out.println("Error11");
                }


                try {
                    type=strings[1];
                    output=response;
                }catch (Exception ex){
                    System.out.println("Error21 : "+ex);
                }
            }/*else if(strings[1].equals("ImageProduct")){
                try {
                    URL url=new URL(strings[0]);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    Scanner scanner=new Scanner(inputStream,"UTF-8");
                    response=scanner.useDelimiter("\\A").next();


                }catch (Exception ex){
                    System.out.println("Error12");
                }

                try {
                    type=strings[1];
                    output=response;
                }catch (Exception ex){
                    System.out.println("Error22 : "+ex);
                }

            }*/else if(strings[1].equals("ViewImageMobile")){
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
                    System.out.println("Error13");
                }

                try {
                    type=strings[1];
                    output=response;
                    JSONArray jsonArray2=new JSONArray(response);
                    for(int i=0;i<jsonArray2.length();i++) {
                        JSONObject jsonObj = jsonArray2.getJSONObject(i);
                        arrImg.add(jsonObj.getString("ImageFile"));
                    }
                }catch (Exception ex){
                    System.out.println("Error23 : "+ex);
                }
            }

            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(type.equals("special")){
                new MyToast(ManageCaptureActivity.this, s, 2);
            }else if(type.equals("ImageProduct")){
                new MyToast(ManageCaptureActivity.this, s, 2);
                Intent intent = ManageCaptureActivity.this.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ManageCaptureActivity.this.finish();
                ManageCaptureActivity.this.overridePendingTransition(0, 0);
                ManageCaptureActivity.this.startActivity(intent);
                ManageCaptureActivity.this.overridePendingTransition(0, 0);
            }else if(type.equals("ViewImageMobile")){
                //new MyToast(ManageCaptureActivity.this, s, 2);
                mPager = (ViewPager) findViewById(R.id.pager);
                mPager.setAdapter(new MyAdapterSlideImage_product(getBaseContext(), arrImg));
                CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                indicator.setViewPager(mPager);

                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == arrImg.size()) {
                            currentPage = 0;
                        }
                        mPager.setCurrentItem(currentPage++, true);
                    }
                };
                Timer swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 4000, 4000);
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
