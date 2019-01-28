package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by anucha on 1/3/2018.
 */

public class FragmentManageLevel1Search extends Fragment {

    private View myView;
    private ArrayList<MyItemFactoryOrder> items;
    private MyFont myFont;
    private String dataID, branchID;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private EditText search;
    private TextView searchToDay;
    private ProgressDialog dialog;
    private Bundle bundle;
    private String searchtext,type;

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_manage_level1_search, container, false);


        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            myFont = new MyFont(getActivity());
            items = new ArrayList<>();

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

            search = myView.findViewById(R.id.editText_search);
            search.setHint("ค้นหาเลขที่ใบรับผ้า");
            searchToDay = myView.findViewById(R.id.textView6);
            searchToDay.setPaintFlags(searchToDay.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            searchToDay.setTypeface(myFont.setFont());
            searchToDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    String dates = "" + df.format("yyyy-MM-dd", new java.util.Date());
                    String url = getIPAPI.IPAddress+"/CleanmatePOS/factoryOrderNo.php?branchID=" + branchID + "&Date=" + dates;
                    new MyAsyncTask().execute(url, "" + 1);

                    SharedPreferences sp = getActivity().getSharedPreferences("SearchManage", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    sp.edit().clear().apply();
                    HashSet<String> mSet = new HashSet<>();
                    mSet.add("<a>" + 1);
                    mSet.add("<b>" + dates);
                    editor.putStringSet("SearchManage", mSet);
                    editor.apply();


                }
            });
            TextView Text5 = myView.findViewById(R.id.textView5);
            Text5.setTypeface(myFont.setFont());
            RelativeLayout back = myView.findViewById(R.id.btn_scan);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManageLevel0 fragmentManageLevel0 = new FragmentManageLevel0();
                    //fragmentManageLevel1.setArguments(edit);
                    getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentManageLevel0)
                            .commit();

                }
            });

            search.setTypeface(myFont.setFont());
            search.setInputType(InputType.TYPE_CLASS_PHONE);
            search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_SEARCH) {

                        items.clear();
                        String url = getIPAPI.IPAddress+"/CleanmatePOS/searchFactory.php?branchID=" + branchID + "&search=" + search.getText().toString();
                        new MyAsyncTask().execute(url, "" + 2);

                        SharedPreferences sp = getActivity().getSharedPreferences("SearchManage", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        sp.edit().clear().apply();
                        HashSet<String> mSet = new HashSet<>();
                        mSet.add("<a>" + 2);
                        mSet.add("<b>" + search.getText().toString());
                        editor.putStringSet("SearchManage", mSet);
                        editor.apply();
                    }
                    return false;

                }
            });

        } else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return myView;
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

            type=strings[1];

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
            int index;
            try {
                JSONArray jsonArray=new JSONArray(response);
                System.out.println(jsonArray.length());
                output=""+jsonArray.length();
                for(int i=0;i<jsonArray.length();i++) {
                    index = i + 1;
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    try {
                        items.add(new MyItemFactoryOrder("" + index,jsonObj.getString("OrderNo"), dateThai(jsonObj.getString("OrderDate")),
                                jsonObj.getString("Barcode"),jsonObj.getString("BarcodePackage")));
                    } catch (Exception ex) {
                        items.add(new MyItemFactoryOrder("" + index, jsonObj.getString("OrderNo"), dateThai(jsonObj.getString("OrderDate")),
                                "0", "0"));
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

            if (Integer.parseInt(s) == 0) {
                new MyToast(getActivity(), "ไม่พบข้อมูลออเดอร์", 0);
            } else {
                System.out.println(items);
                FragmentManageLevel1 fragmentManageLevel1 = new FragmentManageLevel1(items);
                //fragmentManageLevel1.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel1)
                        .commit();

            /*}else{
                if(type.equals("1")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("searchtext", search.getText().toString());
                    bundle.putString("type", "1");
                    FragmentManageLevel1 fragmentManageLevel1 = new FragmentManageLevel1();
                    fragmentManageLevel1.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentManageLevel1)
                            .commit();
                }else if(type.equals("2")){
                    FragmentManageLevel1 fragmentManageLevel1 = new FragmentManageLevel1(items);
                    //fragmentManageLevel1.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentManageLevel1)
                            .commit();
                }
            }*/

            }
        }
    }

}
