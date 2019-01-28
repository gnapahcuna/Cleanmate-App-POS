package com.techlogn.cleanmate_pos_v36;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by anucha on 2/10/2018.
 */

public class FragmentManageLevel2 extends Fragment {

    private View myView;
    private ArrayList<MyItemFactoryProductList> items;
    private ArrayList<MyItemFactoryOrder> items1;
    private ArrayList<MyItemBarcode> itemsPK;
    private String orderNo,dataID,branchID;
    private TextView t,Text1,Text2;
    private MyFont myFont;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private EditText edit;
    private ProgressDialog dialog;
    private String search,type;
    private String TYPE;
    private int maximumunit;
    String contents="";
    int mCount=0;
    ArrayList<String>arrCheckmCount;

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_manage_level2, container, false);


        getIPAPI=new GetIPAPI();

        arrCheckmCount=new ArrayList<>();
        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/
        LinearLayout ln_frag3=myView.findViewById(R.id.ln_frag3);
        LinearLayout.LayoutParams rl =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        final int height=getResources().getDisplayMetrics().heightPixels;
        rl.setMargins(0, 0, 0, 0);
        ln_frag3.setLayoutParams(rl);
        ln_frag3.getLayoutParams().height=(height*60)/100;

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            items = new ArrayList<>();
            items1 = new ArrayList<>();
            itemsPK = new ArrayList<>();
            t = myView.findViewById(R.id.text_orderno);
            myFont = new MyFont(getActivity());
            Text1 = myView.findViewById(R.id.textTitle1);
            Text2 = myView.findViewById(R.id.textTitle2);
            Text1.setTypeface(myFont.setFont());
            Text2.setTypeface(myFont.setFont());

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                orderNo = bundle.getString("orderNo");
            }

            myView.setFocusableInTouchMode(true);
            myView.requestFocus();
            myView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            return true;
                        }
                    }
                    return false;
                }
            });

            SharedPreferences sharedPreferences3=getActivity().getSharedPreferences("SearchManage", Activity.MODE_PRIVATE);
            Map<String,?> entries3 = sharedPreferences3.getAll();
            Set<String> keys3 = entries3.keySet();
            String[] getData3;
            List<String> list3 = new ArrayList<String>(keys3);
            for (String temp : list3) {
                //System.out.println(temp+" = "+sharedPreferences3.getStringSet(temp,null));
                for (int i = 0; i < sharedPreferences3.getStringSet(temp, null).size(); i++) {
                    getData3 = sharedPreferences3.getStringSet(temp, null).toArray(new String[sharedPreferences3.getStringSet(temp, null).size()]);
                    //System.out.println(temp + " : " + getData3[i]);
                    char chk=getData3[i].charAt(1);
                    if(chk=='a'){
                        type=getData3[i].substring(3);
                    }else if(chk=='b'){
                        search=getData3[i].substring(3);
                    }
                }
            }
            int width=getResources().getDisplayMetrics().widthPixels;

            final RelativeLayout scan = myView.findViewById(R.id.layoutClick_addcart);
            scan.getLayoutParams().width=(width*45)/100;
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scan();
                }
            });


            RelativeLayout back = myView.findViewById(R.id.btn_scan);
            back.getLayoutParams().width=(width*45)/100;
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //System.out.println(type + " : " + search);
                    if(Integer.parseInt(type)==1){
                        android.text.format.DateFormat df = new android.text.format.DateFormat();
                        String dates = "" + df.format("yyyy-MM-dd", new Date());
                        String url = getIPAPI.IPAddress+"/CleanmatePOS/factoryOrderNo.php?branchID=" + branchID + "&Date=" + dates;
                        new MyAsyncTask().execute(url,"", "search","");
                    }else if(Integer.parseInt(type)==2){
                        String url = getIPAPI.IPAddress+"/CleanmatePOS/searchFactory.php?branchID=" + branchID + "&search=" + search;
                        new MyAsyncTask().execute(url,"", "search","");
                    }
                }
            });

            TextView t5 = myView.findViewById(R.id.textView5);
            t5.setTypeface(myFont.setFont());

            SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("ID", Activity.MODE_PRIVATE);
            Map<String, ?> entries2 = sharedPreferences2.getAll();
            Set<String> keys2 = entries2.keySet();
            String[] getData2;
            List<String> list2 = new ArrayList<String>(keys2);
            for (String temp : list2) {
                for (int i = 0; i < sharedPreferences2.getStringSet(temp, null).size(); i++) {
                    getData2 = sharedPreferences2.getStringSet(temp, null).toArray(new String[sharedPreferences2.getStringSet(temp, null).size()]);
                    char chk = getData2[i].charAt(1);
                    if (chk == 'a') {
                        dataID = getData2[i].substring(3);
                    } else if (chk == 'b') {
                        branchID = getData2[i].substring(3);
                    }
                }
            }
            String url=getIPAPI.IPAddress+"/CleanmatePOS/factoryOrderProductList1.php?orderNo="+orderNo;
            String url1=getIPAPI.IPAddress+"/CleanmatePOS/factoryOrderProductList_getbarcode.php?orderNo="+orderNo;
            String url2=getIPAPI.IPAddress+"/CleanmatePOS/GetCountBarcodePK.php?OrderNo="+orderNo;
            new MyAsyncTask().execute(url,url1,"select",url2);
        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return myView;
    }
    public void scan(){
        int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.CAMERA},
                    123);
            //scanBarcode();
            return;
        }else{
            scanBarcode();
        }

    }
    private void scanBarcode() {
        Intent it = new Intent(getActivity(), BarcodeScanner.class);
        it.putExtra("orderNo",orderNo);
        it.putExtra("branchID",branchID);
        it.putExtra("ID", "" + 0);
        it.putExtra("IsBasket", "IsBasket");
        it.putExtra("IsScan", "Package");
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

            SharedPreferences sharedPreferences4=getContext().getSharedPreferences("ID", Activity.MODE_PRIVATE);
            Map<String, ?> entries4 = sharedPreferences4.getAll();
            Set<String> keys4 = entries4.keySet();
            String[] getData4;
            List<String> list4 = new ArrayList<String>(keys4);
            for (String temp : list4) {
                //System.out.println(temp+" = "+sharedPreferences2.getStringSet(temp,null));
                for (int i = 0; i < sharedPreferences4.getStringSet(temp, null).size(); i++) {
                    getData4 = sharedPreferences4.getStringSet(temp, null).toArray(new String[sharedPreferences4.getStringSet(temp, null).size()]);
                    //System.out.println(temp + " : " + getData4[i]);
                    char chk = getData4[i].charAt(1);
                    if (chk == 'a') {
                        dataID = getData4[i].substring(3);
                    } else if (chk == 'b') {
                        branchID = getData4[i].substring(3);
                    }
                }
            }

            if (requestCode == 1010 && resultCode == getActivity().RESULT_OK) {
                contents = data.getStringExtra("barcode");
                //System.out.println(orderNo + " : " + branchID + " ,len : " + orderNo.length());

                String url = getIPAPI.IPAddress+"/CleanmatePOS/Transport1.php?Data1=" + branchID + "&Data3=" + orderNo.trim() + "&Data4=" + contents + "&packageType=" + 0;
                new MyAsyncTask().execute(url,"", "transport","");

            }/* else if (requestCode == 123) {

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
                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setIcon(R.mipmap.loading);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("กรุณารอสักครู่....");
                    dialog.setIndeterminate(true);
                    dialog.show();
                    Ion.with(getActivity())
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
                                    new MyToast(getActivity(), result, 2);
                                }
                            });


                    //String query_order = "UPDATE ops_transportpackage SET ImageFile='"+mEncode+"' WHERE Barcode='"+mBarcode+"'";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }catch (Exception e) {

        }
        //img.setImageBitmap(bitmap);
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
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
            String response1="";
            String response2="";
            if(strings[2]=="select"){
                try {
                    URL url = new URL(strings[0]);
                    URL url1 = new URL(strings[1]);
                    URL url2 = new URL(strings[3]);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) url2.openConnection();

                    httpURLConnection.setDoOutput(true);
                    httpURLConnection1.setDoOutput(true);
                    httpURLConnection2.setDoOutput(true);

                    httpURLConnection.connect();
                    httpURLConnection1.connect();
                    httpURLConnection2.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStream inputStream1 = httpURLConnection1.getInputStream();
                    InputStream inputStream2 = httpURLConnection2.getInputStream();

                    Scanner scanner = new Scanner(inputStream, "UTF-8");
                    Scanner scanner1 = new Scanner(inputStream1, "UTF-8");
                    Scanner scanner2 = new Scanner(inputStream2, "UTF-8");

                    response = scanner.useDelimiter("\\A").next();
                    response1 = scanner1.useDelimiter("\\A").next();
                    response2 = scanner2.useDelimiter("\\A").next();



                }catch (Exception ex){
                    System.out.println("Error1");
                }
            }else{
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
            }


            String output="";
            int index,num;
            try {
                if(strings[2]=="search"){
                    TYPE=strings[2];
                    JSONArray jsonArray=new JSONArray(response);
                    output=""+jsonArray.length();
                    //System.out.println(jsonArray.length());
                    for(int i=0;i<jsonArray.length();i++) {
                        index = i + 1;
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        try {
                            items1.add(new MyItemFactoryOrder("" + index,jsonObj.getString("OrderNo"), dateThai(jsonObj.getString("OrderDate")),
                                    jsonObj.getString("Barcode"),jsonObj.getString("BarcodePackage")));
                        } catch (Exception ex) {
                            items1.add(new MyItemFactoryOrder("" + index, jsonObj.getString("OrderNo"), dateThai(jsonObj.getString("OrderDate")),
                                    "0", "0"));
                        }
                    }
                }else if(strings[2]=="select") {
                    TYPE=strings[2];
                    // System.out.println("response : "+response);
                    // System.out.println("response1 : "+response1);
                    JSONArray jsonArray2 = new JSONArray(response2);
                    itemsPK.clear();
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObj = jsonArray2.getJSONObject(i);
                        itemsPK.add(new MyItemBarcode(jsonObj.getString("OrderNo"), jsonObj.getString("Barcode")));
                    }
                    mCount=0;
                    mCount=itemsPK.size();
                    System.out.println("mCount = "+response2);

                    JSONArray jsonArray = new JSONArray(response);
                    JSONArray jsonArray1 = new JSONArray(response1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        JSONObject jsonObj1 = jsonArray1.getJSONObject(i);
                        index = i + 1;
                        num = 0;
                        items.add(new MyItemFactoryProductList("" + index, jsonObj.getString("ProductNameTH"), jsonObj.getString("ProductNameEN"), "" + num, jsonObj.getString("Num")
                                , jsonObj.getString("ProductID"), jsonObj1.getString("bar1"),jsonObj.getString("Num")));
                    }
                }else if(strings[2]=="transport") {
                    TYPE=strings[2];
                    output = response;

                    if(response.trim().equals("\uFEFFบาร์โค้ดนี้ถูกใช้งานแล้ว")){
                        //output = response;
                    }else{
                        JSONArray jsonArray = new JSONArray(response);
                        itemsPK.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            itemsPK.add(new MyItemBarcode(jsonObj.getString("OrderNo"), jsonObj.getString("Barcode")));
                        }
                        mCount=0;
                        mCount+=itemsPK.size();
                    }
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

            final NotificationBadge badge = myView.findViewById(R.id.badge);
            badge.setNumber(mCount);
            ImageView bages = myView.findViewById(R.id.img_bage);
            bages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCount > 0) {
                        //Toast.makeText(getActivity(), "Show", Toast.LENGTH_SHORT).show();

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.custom_vew_barcode_pk);
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView orderNo = dialog.findViewById(R.id.text_orderno);
                        TextView barcode = dialog.findViewById(R.id.text_barcode);
                        ListView list = dialog.findViewById(R.id.list_scan);
                        RelativeLayout rl=dialog.findViewById(R.id.rl_pk);
                        rl.getLayoutParams().height=(getResources().getDisplayMetrics().heightPixels*40)/100;
                        orderNo.setText("เลขที่ออเดอร์");
                        barcode.setText("เลขบาร์โค้ดถุงบรรจุภัณฑ์");
                        //itemsPK = new ArrayList<>();
                        //itemsPK.clear();
                        MyAdapterBarcode myAdapter = new MyAdapterBarcode(getActivity(), R.layout.list_barcode, itemsPK);
                        list.setAdapter(myAdapter);
                        Button okButton = dialog.findViewById(R.id.btn_ok);
                        okButton.setText("ปิดลง");
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        MyFont myFont = new MyFont(getActivity());
                        okButton.setTypeface(myFont.setFont());
                        //title.setTypeface(myFont.setFont());
                        //des.setTypeface(myFont.setFont());*/
                    } else {
                        new MyToast(getActivity(), "ยังไม่มีการสแกนถุง", 0);
                    }
                }

            });

            System.out.println("TYPE : " + TYPE+" , "+s);
            if(TYPE.equals("transport")){
                if(s.trim().equals("\uFEFFบาร์โค้ดนี้ถูกใช้งานแล้ว")){
                    new MyToast(getActivity(), s, 0);
                    scan();
                }else{
                    new MyToast(getActivity(), "สแกนบาร์โค้ดถุงสำเร็จ", 2);
                }
            }

            if (TYPE.equals("search")) {
                FragmentManageLevel1 fragmentManageLevel1 = new FragmentManageLevel1(items1);
                //fragmentManageLevel1.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel1)
                        .commit();
            } else if(TYPE.equals("select")) {
                t.setText("เลขที่ออเดอร์ : " + orderNo);
                t.setTypeface(myFont.setFont());
                ListView listView = myView.findViewById(R.id.list);
                MyAdapterFactoryProductList myAdapter = new MyAdapterFactoryProductList(getActivity(), R.layout.factory_productlist, items);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        System.out.println("2 Num : " + items.get(i).getNum2());
                        System.out.println("2 bar1 : " + items.get(i).getNum2());

                        TextView proTH = view.findViewById(R.id.textTH);
                        TextView proEN = view.findViewById(R.id.textEN);
                        TextView proID = view.findViewById(R.id.textID);
                        String nameTH = proTH.getText().toString();
                        String nameEN = proEN.getText().toString();
                        String ID = proID.getText().toString();
                        maximumunit = Integer.parseInt(items.get(i).getMaximum());
                        Bundle bundle = new Bundle();
                        bundle.putString("orderNo", orderNo);
                        bundle.putString("productTH", nameTH);
                        bundle.putString("productEN", nameEN);
                        bundle.putString("productID", ID);
                        bundle.putString("Num", "" + items.get(i).getNum2());
                        bundle.putString("bar1", "" + items.get(i).getBar1());

                        FragmentManageLevel3 fragment2_2 = new FragmentManageLevel3();
                        fragment2_2.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment2_2)
                                .commit();

                    }
                });
                listView.setAdapter(myAdapter);
            }/*else if(type.trim().equals("transport")) {
                boolean ss;
                if(s.trim().equals("\uFEFFบาร์โค้ดนี้ถููกใช้งานแล้ว")){
                    new MyToast(getActivity(), s, 0);
                    scan();
                }else{
                    new MyToast(getActivity(), "สแกนบาร์โค้ดถุงสำเร็จ", 2);
                    *//*myAdapter = new MyAdapterBarcode(getBaseContext(), R.layout.list_barcode, items);
                    listView.setAdapter(myAdapter);

                    SharedPreferences.Editor editor1 = sp_bcpk.edit();
                    StringBuilder sb1 = new StringBuilder();
                    HashSet<String> mSet1 = new HashSet<>();
                    for (int j = 0; j < getOrderNos.size(); j++) {
                        mSet1.add("<a>" + getOrderNos.get(j));
                        mSet1.add("<b>" + getContents.get(j));
                    }
                    editor1.putStringSet(getOrderNos.get(0), mSet1);
                    editor1.apply();*//*
                }

            }*/


        }
    }
    public static String dateThai(String strDate) {
        String Months[] = {
                "ม.ค", "ก.พ", "มี.ค", "เม.ย",
                "พ.ค", "มิ.ย", "ก.ค", "ส.ค",
                "ก.ย", "ต.ค", "พ.ย", "ธ.ค"};

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        int year = 0, month = 0, day = 0;
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

        return String.format("%s %s %s", day, Months[month], year + 543);
    }
}

