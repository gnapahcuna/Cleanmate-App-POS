package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by anucha on 2/12/2018.
 */

public class FragmentManageLevel3 extends Fragment {

    View myView;
    ArrayList<MyItemFactoryScan> items;
    String orderNo,nameTH,nameEN,proID,maximumunit,Num,bar1;
    TextView t;
    MyFont myFont;
    String dataID,branchID,orderID;
    ArrayList<String>orderDetail;
    boolean chk=false;
    int num=0;
    ArrayList<String>packageType;
    TextView textScan;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private ProgressDialog dialog;
    String nameTh = "", nameEn = "";

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_manage_3, container, false);


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/

        getIPAPI=new GetIPAPI();


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            items = new ArrayList<>();
            myFont = new MyFont(getActivity());
            MyFont myFont = new MyFont(getActivity());
            TextView title = myView.findViewById(R.id.text_title);
            title.setTypeface(myFont.setFont());

            TextView t5 = myView.findViewById(R.id.textView5);
            TextView title1 = myView.findViewById(R.id.textTitle1);
            TextView title2 = myView.findViewById(R.id.textTitle2);

            t5.setTypeface(myFont.setFont());
            title1.setTypeface(myFont.setFont());
            title2.setTypeface(myFont.setFont());

            textScan = myView.findViewById(R.id.text_scan);
            textScan.setTypeface(myFont.setFont());

            packageType = new ArrayList<>();

            orderDetail = new ArrayList<>();

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                orderNo = bundle.getString("orderNo");
                nameTH = bundle.getString("productTH");
                nameEN = bundle.getString("productEN");
                proID = bundle.getString("productID");
                maximumunit=bundle.getString("MaximumUnit");
                Num = bundle.getString("Num");
                bar1=bundle.getString("bar1");


            }
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
            //System.out.println("branchID : " + branchID);
            //System.out.println("OrderNo : " + orderNo);
            //System.out.println("OrderNo : " + proID);

            String url=getIPAPI.IPAddress+"/CleanmatePOS/CountNumOrderDetail1.php?orderNo="+orderNo+"&branchID="+branchID+"&productID="+proID;
            String url1=getIPAPI.IPAddress+"/CleanmatePOS/IDorderDetail1.php?orderNo="+orderNo+"&branchID="+branchID+"&productID="+proID;
            String url2=getIPAPI.IPAddress+"/CleanmatePOS/factoryscanlist1.php?orderNo="+orderNo+"&branchID="+branchID+"&productID="+proID;
            new MyAsyncTask().execute(url,url1,url2);

            myView.setFocusableInTouchMode(true);
            myView.requestFocus();
            myView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                        }
                    }
                    return false;
                }
            });


            final RelativeLayout scan = myView.findViewById(R.id.layoutClick_addcart);
            int width=getResources().getDisplayMetrics().widthPixels;
            scan.getLayoutParams().width=(width*45)/100;
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (num <= 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderNo", orderNo);

                        FragmentManageLevel2 fragment2_1 = new FragmentManageLevel2();
                        fragment2_1.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment2_1)
                                .commit();
                    } else {
                        try {
                            Intent i = new Intent(getActivity(), FactoryList.class);
                            i.putExtra("orderNo", orderNo);
                            i.putExtra("orderDetail", orderDetail.get(0));
                            i.putExtra("branchID", branchID);
                            i.putExtra("orderID", orderNo);
                            i.putExtra("productID", proID);
                            i.putExtra("productTH", nameTH);
                            i.putExtra("productEN", nameEN);
                            i.putExtra("MaximumUnit", maximumunit);
                            i.putExtra("Num", Num);
                            i.putExtra("bar1", items.size());
                            startActivity(i);
                            getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                        } catch (Exception e) {
                            new MyToast(getActivity(), "ทำรายการแล้วเรียบร้อย", 2);
                        }
                    }
                }
            });

            RelativeLayout back = myView.findViewById(R.id.btn_scan);
            back.getLayoutParams().width=(width*45)/100;
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNo", orderNo);

                    FragmentManageLevel2 fragment2_1 = new FragmentManageLevel2();
                    fragment2_1.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment2_1)
                            .commit();
                }
            });
        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }
        return myView;
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
            try {
                URL url = new URL(strings[0]);
                URL url1 = new URL(strings[1]);
                URL url2 = new URL(strings[2]);

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

            String output="";
            int index;
            try {
                num=Integer.parseInt(response.substring(response.indexOf('.')+1));
                /*JSONArray jsonArray=new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    num = Integer.parseInt(jsonObj.getString("num"));

                }*/
                JSONArray jsonArray1=new JSONArray(response1);
                for(int i=0;i<jsonArray1.length();i++) {
                    JSONObject jsonObj = jsonArray1.getJSONObject(i);
                    orderDetail.add(jsonObj.getString("OrderDetailID"));
                }

                JSONArray jsonArray2=new JSONArray(response2);
                for(int i=0;i<jsonArray2.length();i++) {
                    JSONObject jsonObj = jsonArray2.getJSONObject(i);
                    System.out.println(jsonObj.getString("Barcode"));
                    index = i + 1;
                    nameTh = jsonObj.getString("ProductNameTH");
                    nameEn = jsonObj.getString("ProductNameEN");
                    orderID = jsonObj.getString("OrderNo");

                    if(jsonObj.getString("Barcode")!=null){
                        items.add(new MyItemFactoryScan("" + index, "สินค้า : " + jsonObj.getString("Barcode")
                                , jsonObj.getString("OrderDetailID"), jsonObj.getString("IsImage")));
                    } else {
                        items.add(new MyItemFactoryScan("" + index, "บาร์โค้ดสินค้า", jsonObj.getString("OrderDetailID"), "0"));
                    }
                }


            }catch (Exception ex){
                System.out.println("Error2 : "+ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if (num <= 0) {
                textScan.setText("ต่อไป");
            } else {
                textScan.setText("สแกนบาร์โค้ดสินค้า");
            }
            System.out.println("3 Num : "+Num);
            System.out.println("3 bar1 : "+bar1);
            System.out.println("3 num : "+num);

            TextView t = myView.findViewById(R.id.text_title);
            TextView des = myView.findViewById(R.id.text_description);
            t.setText(nameTH + " (" + nameEN + ")");
            des.setText("จัดการแล้ว ("+(Integer.parseInt(Num)-num)+"/"+Num+")");
            t.setTypeface(myFont.setFont());
            des.setTypeface(myFont.setFont());

            ListView listView = myView.findViewById(R.id.list);
            MyAdapterFactoryScan myAdapter = new MyAdapterFactoryScan(getActivity(), R.layout.factory_scan_list, items, proID, orderNo,nameTh,nameEn,Num,bar1);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
            listView.setAdapter(myAdapter);
        }
    }
}
