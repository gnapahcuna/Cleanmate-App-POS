package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FragmentCancel extends Fragment {
    private View myView;
    private ArrayList<CustomCancelOrder> items;
    private MyFont myFont;
    private String dataID, branchID;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private EditText search;
    private ProgressDialog dialog;

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_cancel_orders_search, container, false);

        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/

        getIPAPI=new GetIPAPI();

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            myFont = new MyFont(getActivity());
            items = new ArrayList<>();
            TextView t1 = myView.findViewById(R.id.textView6);
            t1.setTypeface(myFont.setFont());
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

            search = myView.findViewById(R.id.editText_search);

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
            t1.setText("Search For Cancel");
            /*t1.setPaintFlags(t1.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    String dates = "" + df.format("yyyy-MM-dd", new java.util.Date());
                    String url=getIPAPI.IPAddress+"/CleanmatePOS/CancelOrder.php?branchID="+branchID+"&Date="+dates;
                    new MyAsyncTask().execute(url);


                }
            });*/
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
            search.setHint("ค้นหาเลขที่ใบรับผ้า,เบอร์ลูกค้า");
            int width=getResources().getDisplayMetrics().widthPixels;
            search.getLayoutParams().width=(70*width)/100;
            search.setTypeface(myFont.setFont());
            search.setInputType(InputType.TYPE_CLASS_PHONE);
            search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        String url=getIPAPI.IPAddress+"/CleanmatePOS/searchCancelOrder.php?branchID="+branchID+"&search="+search.getText().toString();
                        new MyAsyncTask().execute(url);
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


            String response="";
            String response1="";
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
                output=""+jsonArray.length();
                System.out.println(jsonArray.length());
                for(int i=0;i<jsonArray.length();i++) {
                    index = i + 1;
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    items.add(new CustomCancelOrder("" + index,  jsonObj.getString("OrderNo"),
                            dateThai( jsonObj.getString("OrderDate")),  jsonObj.getString("TelephoneNo"),
                            jsonObj.getString("IsAddition"),  jsonObj.getString("AdditionNetAmount"),
                            jsonObj.getString("NetAmount"),  jsonObj.getString("DeliveryStatus"),
                            jsonObj.getString("IsDriverVerify"),  jsonObj.getString("IsCheckerVerify"),
                            jsonObj.getString("IsReturnCustomer"),jsonObj.getString("IsBranchEmpVerify"),
                            jsonObj.getString("IsCustomerCancel")));
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

            if(Integer.parseInt(s)==0){
                new MyToast(getActivity(), "ไม่พบข้อมูลออเดอร์", 0);
            }else{
                FragmentCancelOrder fragmentCancelOrder = new FragmentCancelOrder(items);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentCancelOrder)
                        .commit();
            }
        }
    }
}
