package com.techlogn.cleanmate_pos_v36;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.libRG.CustomTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ThirdFragmentBasket extends Fragment {

    View v,v1,v2;
    EditText search;
    ImageView chk;
    String dataID,branchID;
    TabLayout tabLayout;
    int total=0;
    MyAdapterCoupon myAdapter;
    ArrayList<CustomItemCoupon> items;
    ListView listView;
    CustomTextView text_total;
    ListView lv;
    String str1,str2,test;
    ArrayList<String> arrTest,arrTest1,arrTest2,arrTest3;
    int aa=0,id_mem;
    int a=0,ii;
    String bookID;
    String price,num;
    ArrayList<String>arr;
    SharedPreferences sp1,sp2,sp3,spp1,spp2,spp3;
    SharedPreferences.Editor editor1;

    private ProgressDialog dialog;
    private GetIPAPI getIPAPI;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    private boolean isComplete=false;

    private int mSum1=0,mSum2=0,mSum3=0,mNum1=0,mNum2=0,mNum3=0;
    private ArrayList<Integer>arrSum1,arrSum2,arrSum3,arrNum1,arrNum2,arrNum3;
    public ThirdFragmentBasket() {
    }

    public static ThirdFragmentBasket newInstance() {

        Bundle args = new Bundle();
        ThirdFragmentBasket fragment = new ThirdFragmentBasket();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDb = mSQLite.getReadableDatabase();
        String sql = "SELECT * FROM privilage";
        Cursor cursor = mDb.rawQuery(sql, null);
        int i=0;
        total=0;

        HashSet<String> mSet = new HashSet<>();
        while (cursor.moveToNext()) {
            i++;
            items.add(new CustomItemCoupon(cursor.getString(1),cursor.getString(2),""+i));
            //System.out.println(cursor.getString(1));
            //System.out.println(cursor.getString(2));
            total+=Integer.parseInt(cursor.getString(2));

            mSet.add("<a>" + cursor.getString(1));
            mSet.add("<b>" + cursor.getString(2));
            editor1.putStringSet(cursor.getString(1), mSet);
            editor1.apply();
        }
        myAdapter = new MyAdapterCoupon(getActivity(), R.layout.list_coupon, items, listView,text_total,total);
        listView.setAdapter(myAdapter);
        text_total.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+total+" บาท");

        int width=getResources().getDisplayMetrics().widthPixels;
        RelativeLayout cancel = v.findViewById(R.id.btn_scan);
        cancel.getLayoutParams().width = (width*45)/100;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                TextView des = dialog.findViewById(R.id.tv_description);
                title.setText("แจ้งเตือน");
                des.setText("ต้องการยกเลิกข้อมูลคูปองเงินสด");
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
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
                        sp1.edit().clear().apply();
                        String sql = "SELECT * FROM privilage";
                        final Cursor cursor = mDb.rawQuery(sql, null);
                        final ProgressDialog dialog = new ProgressDialog(getActivity());
                        dialog.setIcon(R.mipmap.loading);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage("กรุณารอสักครู่....");
                        dialog.setIndeterminate(true);
                        dialog.setCancelable(false);
                        while (cursor.moveToNext()) {
                            /*String url=getIPAPI.IPAddress+"/CleanmatePOS/CancelCoupon.php?CouponNo="+cursor.getString(1);
                            new MyAsyncTask().execute(url,"Cancel");*/
                            dialog.show();
                            String sqlDeletePrivilae = "Delete FROM privilage";
                            mDb.execSQL(sqlDeletePrivilae);
                            sp1.edit().clear().apply();
                            ii=0;
                            Ion.with(getActivity())
                                    .load(getIPAPI.IPAddress+"/CleanmatePOS/CancelCoupon.php?CouponNo="+cursor.getString(1))
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            dialog.dismiss();
                                            ii++;
                                            if(ii==cursor.getCount()){
                                                new MyToast(getActivity(),result,2);
                                            }

                                        }
                                    });
                            onStart();
                            items.clear();
                            myAdapter = new MyAdapterCoupon(getActivity(), R.layout.list_coupon, items, listView,text_total,total);
                            listView.setAdapter(myAdapter);

                        }
                    }
                });
                MyFont myFont = new MyFont(getActivity());
                okButton.setTypeface(myFont.setFont());
                declineButton.setTypeface(myFont.setFont());
                title.setTypeface(myFont.setFont());
                des.setTypeface(myFont.setFont());
            }
        });
        Map<String, ?> entries_sp1 = sp1.getAll();
        Set<String> keys_sp1 = entries_sp1.keySet();
        String[] getData_sp1;
        List<String> list_sp1 = new ArrayList<String>(keys_sp1);
        for (String temp : list_sp1) {
            for (int k = 0; k < sp1.getStringSet(temp, null).size(); k++) {
                getData_sp1 = sp1.getStringSet(temp, null).toArray(new String[sp1.getStringSet(temp, null).size()]);
                char chk = getData_sp1[k].charAt(1);
                if (chk == 'a') {
                    //System.out.println("CouponNo : "+getData_sp1[k].substring(3));
                } else if (chk == 'b') {
                   // System.out.println("Price : "+getData_sp1[k].substring(3));
                }

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sp2 = getContext().getSharedPreferences("ID_MEM", Context.MODE_PRIVATE);
        sp3=getContext().getSharedPreferences("Member", Context.MODE_PRIVATE);

        spp1 = getContext().getSharedPreferences("ORDER", Context.MODE_PRIVATE);
        spp2=getContext().getSharedPreferences("CouponPoint", Context.MODE_PRIVATE);
        spp3=getContext().getSharedPreferences("Privilage", Context.MODE_PRIVATE);

        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/

        arrNum1=new ArrayList<>();
        arrNum2=new ArrayList<>();
        arrNum3=new ArrayList<>();
        arrSum1=new ArrayList<>();
        arrSum2=new ArrayList<>();
        arrSum3=new ArrayList<>();


        mSQLite = SQLiteHelper.getInstance(getActivity());

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            int width=getResources().getDisplayMetrics().widthPixels;

            arrTest = new ArrayList<>();
            arrTest1 = new ArrayList<>();
            arrTest2 = new ArrayList<>();

            //System.out.println("ThirdFrag");
            v = inflater.inflate(R.layout.fragment_third, container, false);
            v2 = inflater.inflate(R.layout.fragment_fourth, container, false);

            v.setFocusableInTouchMode(true);
            v.requestFocus();
            v.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                        /*FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame
                                        , new Fragment1())
                                .commit();*/
                            return true;
                        }
                    }
                    return false;
                }
            });
            MyFont myFont = new MyFont(getActivity());
            TextView t5 = v.findViewById(R.id.textView5);
            TextView t7 = v.findViewById(R.id.textView7);
            TextView text1 = v.findViewById(R.id.textTitle1);
            TextView text2 = v.findViewById(R.id.textTitle2);
            TextView text3 = v.findViewById(R.id.textTitle3);
            t7.setTypeface(myFont.setFont());
            t5.setTypeface(myFont.setFont());
            text1.setTypeface(myFont.setFont());
            text2.setTypeface(myFont.setFont());
            text3.setTypeface(myFont.setFont());

            tabLayout = getActivity().findViewById(R.id.tablayout);

            search = v.findViewById(R.id.editText_search);
            search.setInputType(InputType.TYPE_CLASS_NUMBER);
            search.getLayoutParams().width=(width*95)/100;
            chk = v.findViewById(R.id.imgchk_coupon);

            search.setTypeface(myFont.setFont());

            text_total = v.findViewById(R.id.total_price);

            listView = v.findViewById(R.id.list_coupon);

            items = new ArrayList<>();

            SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("ID", Activity.MODE_PRIVATE);

            sp1 = getContext().getSharedPreferences("CouponValue", Context.MODE_PRIVATE);
            editor1 = sp1.edit();


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

            Map<String, ?> entries = sp2.getAll();
            Set<String> keys = entries.keySet();
            String[] getData;
            List<String> list = new ArrayList<String>(keys);
            for (String temp : list) {
                for (int k = 0; k < sp2.getStringSet(temp, null).size(); k++) {
                    getData = sp2.getStringSet(temp, null).toArray(new String[sp2.getStringSet(temp, null).size()]);
                    char chk = getData[k].charAt(1);
                    if (chk == 'a') {
                        id_mem = Integer.parseInt(getData[k].substring(3));
                    }

                }
            }
            //System.out.println("CustomerID : " + id_mem);

            Map<String, ?> entries21 = spp1.getAll();
            Set<String> keys21 = entries21.keySet();
            String[] getData21;
            List<String> list21 = new ArrayList<String>(keys21);
            for (String temp : list21) {
                for (int i = 0; i < spp1.getStringSet(temp, null).size(); i++) {
                    getData21 = spp1.getStringSet(temp, null).toArray(new String[spp1.getStringSet(temp, null).size()]);
                    char chk = getData21[i].charAt(1);
                    if (chk == 'c') {
                        arrSum1.add(Integer.parseInt(getData21[i].substring(3)));
                    }else if(chk == 'g'){
                        arrNum1.add(Integer.parseInt(getData21[i].substring(3)));
                    }
                }
            }
            Map<String, ?> entries22 = spp2.getAll();
            Set<String> keys22 = entries22.keySet();
            String[] getData22;
            List<String> list22 = new ArrayList<String>(keys22);
            for (String temp : list22) {
                for (int i = 0; i < spp2.getStringSet(temp, null).size(); i++) {
                    getData22 = spp2.getStringSet(temp, null).toArray(new String[spp2.getStringSet(temp, null).size()]);
                    char chk = getData22[i].charAt(1);
                    if (chk == 'c') {
                        arrSum2.add(Integer.parseInt(getData22[i].substring(3)));
                    }else if(chk == 'b'){
                        arrNum2.add(Integer.parseInt(getData22[i].substring(3)));
                    }
                }
            }
            Map<String, ?> entries23 = spp3.getAll();
            Set<String> keys23 = entries23.keySet();
            String[] getData23;
            List<String> list23 = new ArrayList<String>(keys23);
            for (String temp : list23) {
                for (int i = 0; i < spp3.getStringSet(temp, null).size(); i++) {
                    getData23 = spp3.getStringSet(temp, null).toArray(new String[spp3.getStringSet(temp, null).size()]);
                    char chk = getData23[i].charAt(1);
                    if (chk == 'c') {
                        arrSum3.add(Integer.parseInt(getData23[i].substring(3)));
                    }else if(chk == 'b'){
                        arrNum3.add(Integer.parseInt(getData23[i].substring(3)));
                    }
                }
            }
            for(int i=0;i<arrSum1.size();i++){
                mSum1+=arrSum1.get(i)*arrNum1.get(i);
            }
            for(int i=0;i<arrSum2.size();i++){
                mSum2+=arrSum2.get(i)*arrNum2.get(i);
            }
            for(int i=0;i<arrSum3.size();i++){
                mSum3+=arrSum3.get(i)*arrNum3.get(i);
            }
            System.out.println((mSum1)+", "+(mSum2)+", "+(mSum3));

            chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mSum1-(mSum2+mSum3)==0){
                        new MyToast(getActivity(),"ยอดที่ต้องชำระเป็นศูนย์ ไม่สามารถใช้เมนูนี้ได้",0);
                    }else {
                        if (sp3.getAll().size() == 0) {
                            new MyToast(getActivity(), "ยังไม่มีข้อมูลลูกค้า", 0);
                        } else if (search.getText().toString().isEmpty()) {
                            new MyToast(getActivity(), "กรุณากรอกหมายเลขคูปองก่อน", 0);
                        } else {
                            android.text.format.DateFormat df = new android.text.format.DateFormat();
                            String date_today = "" + df.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());

                            String url = getIPAPI.IPAddress + "/CleanmatePOS/Coupon.php?couponNo=" + search.getText().toString();
                            url += "&BranchID=" + branchID;
                            url += "&CustomerID=" + id_mem;
                            url += "&date=" + date_today;
                            new MyAsyncTask().execute(url);
                        }
                    }
                }
            });

            search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            search.setImeActionLabel("ค้นหา", EditorInfo.IME_ACTION_SEARCH);
            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //Toast.ma003000054keText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
                        if (search.getText().toString().trim().isEmpty()) {
                            new MyToast(getActivity(), "ยังไม่มีหมายเลขสำหรับการค้นหา", 0);
                        } else {
                            //method
                            if(sp3.getAll().size()==0){
                                new MyToast(getActivity(),"ยังไม่มีข้อมูลลูกค้า",0);
                            }else if(search.getText().toString().isEmpty())
                            {
                                new MyToast(getActivity(),"กรุณากรอกหมายเลขคูปองก่อน",0);
                            } else{
                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                String date_today = "" + df.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());

                                String url=getIPAPI.IPAddress+"/CleanmatePOS/Coupon.php?couponNo="+search.getText().toString();
                                url+="&BranchID="+branchID;
                                url+="&CustomerID="+id_mem;
                                url+="&date="+date_today;
                                new MyAsyncTask().execute(url);
                            }
                        }

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        return true;
                    }
                    return false;
                }
            });





            RelativeLayout next = v.findViewById(R.id.layoutClick_addcart);
            next.getLayoutParams().width = (width*45)/100;
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*sp1.edit().clear().apply();
                    for (int i = 0; i < items.size(); i++) {
                        HashSet<String> mSet = new HashSet<>();
                        mSet.add("<a>" + items.get(i).mTextCouponNo);
                        mSet.add("<b>" + items.get(i).mTextPrice);
                        mSet.add("<c>" + items.get(i).mNum);
                        editor1.putStringSet(items.get(i).mNum, mSet);
                        editor1.apply();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, FourthFragmentBasket.newInstance());
                        fragmentTransaction.commit();

                        TabLayout.Tab tab = tabLayout.getTabAt(3);
                        tab.select();
                    }*/

                    /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, FourthFragmentBasket.newInstance());
                    fragmentTransaction.commit();*/
                    TabLayout.Tab tab = tabLayout.getTabAt(3);
                    tab.select();
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, FourthFragmentBasket.newInstance()).commit();

                }
            });

        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return v;
    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
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
            String response = "";
            try {
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

            String output = "";
            try {
                //System.out.println(response);
                JSONArray jsonArray = new JSONArray(response);
                output = "" + jsonArray.length();
                ContentValues cv = new ContentValues();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    cv.put("CouponNo", jsonObj.getString("ConponNo"));
                    cv.put("Value", jsonObj.getString("DiscountRate"));
                    mDb.insert("privilage", null, cv);
                }
                isComplete = true;
                output = response;
            } catch (Exception ex) {
                isComplete = false;
                output = response;
                System.out.println("Error2 : " + ex);
            }

            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(isComplete==true){
                items.clear();
                onStart();
                search.setText("");
            }else{
                new MyToast(getActivity(),s,0);
            }

        }
    }

}
