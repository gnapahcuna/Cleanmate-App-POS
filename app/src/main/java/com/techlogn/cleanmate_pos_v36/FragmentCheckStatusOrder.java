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

/**
 * Created by anucha on 1/3/2018.
 */
public class FragmentCheckStatusOrder extends Fragment {

    private View myView;
    private MyFont myFont;
    private ArrayList<CustomItemFactoryOrderList> items;
    private String dataID,branchID;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private EditText search;
    private ProgressDialog dialog;

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


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
            myView = inflater.inflate(R.layout.fragment_checkstatus_order, container, false);
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
            t1.setText("Search For Check");


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
    public static String dateThai(String strDate)
    {
        String Months[] = {
                "ม.ค", "ก.พ", "มี.ค", "เม.ย",
                "พ.ค", "มิ.ย", "ก.ค", "ส.ค",
                "ก.ย", "ต.ค", "พ.ย", "ธ.ค"};

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        int year=0,month=0,day=0;
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

        return String.format("%s %s %s", day,Months[month],year+543);
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

                ArrayList<String>arr1_test1=new ArrayList<>();
                ArrayList<String>arr1_test2=new ArrayList<>();
                ArrayList<String>arr1_test3=new ArrayList<>();
                ArrayList<String>arr1_test4=new ArrayList<>();
                ArrayList<String>arr1_test5=new ArrayList<>();
                ArrayList<String>arr1_test6=new ArrayList<>();
                ArrayList<String>arr1_test7=new ArrayList<>();
                ArrayList<String>arr1_test8=new ArrayList<>();
                ArrayList<String>arr1_test9=new ArrayList<>();
                ArrayList<String>arr1_test10=new ArrayList<>();
                ArrayList<String>arr1_test11=new ArrayList<>();
                ArrayList<String>arr1_test12=new ArrayList<>();


                ArrayList<String>arr2_test1=new ArrayList<>();
                ArrayList<String>arr2_test2=new ArrayList<>();
                ArrayList<String>arr2_test3=new ArrayList<>();
                ArrayList<String>arr2_test4=new ArrayList<>();
                ArrayList<String>arr2_test5=new ArrayList<>();
                ArrayList<String>arr2_test6=new ArrayList<>();
                ArrayList<String>arr2_test7=new ArrayList<>();
                ArrayList<String>arr2_test8=new ArrayList<>();
                ArrayList<String>arr2_test9=new ArrayList<>();
                ArrayList<String>arr2_test10=new ArrayList<>();
                ArrayList<String>arr2_test11=new ArrayList<>();
                ArrayList<String>arr2_test12=new ArrayList<>();


                ArrayList<String>arr1=new ArrayList<>();
                ArrayList<String>arr2=new ArrayList<>();
                ArrayList<String>arr3=new ArrayList<>();
                ArrayList<String>arr4=new ArrayList<>();
                ArrayList<String>arr5=new ArrayList<>();
                ArrayList<String>arr6=new ArrayList<>();
                ArrayList<String>arr7=new ArrayList<>();
                ArrayList<String>arr8=new ArrayList<>();
                ArrayList<String>arr9=new ArrayList<>();
                ArrayList<String>arr10=new ArrayList<>();
                ArrayList<String>arr11=new ArrayList<>();
                ArrayList<String>arr12=new ArrayList<>();

                String chk="";
                for(int i=0;i<jsonArray.length();i++) {
                    index = i + 1;
                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    if(Integer.parseInt(jsonObj.getString("DeliveryStatus"))==1){
                        arr1_test1.add(jsonObj.getString("OrderNo"));
                        arr1_test2.add(jsonObj.getString("OrderDate"));
                        arr1_test3.add(jsonObj.getString("TelephoneNo"));
                        arr1_test4.add(jsonObj.getString("IsAddition"));
                        arr1_test5.add(jsonObj.getString("AdditionNetAmount"));
                        arr1_test6.add(jsonObj.getString("NetAmount"));
                        arr1_test7.add(jsonObj.getString("DeliveryStatus"));
                        arr1_test8.add(jsonObj.getString("IsDriverVerify"));
                        arr1_test9.add(jsonObj.getString("IsCheckerVerify"));
                        arr1_test10.add(jsonObj.getString("IsReturnCustomer"));
                        arr1_test11.add(jsonObj.getString("IsBranchEmpVerify"));
                        arr1_test12.add(jsonObj.getString("IsCustomerCancel"));

                    }else if(Integer.parseInt(jsonObj.getString("DeliveryStatus"))==0){
                        arr2_test1.add(jsonObj.getString("OrderNo"));
                        arr2_test2.add(jsonObj.getString("OrderDate"));
                        arr2_test3.add(jsonObj.getString("TelephoneNo"));
                        arr2_test4.add(jsonObj.getString("IsAddition"));
                        arr2_test5.add(jsonObj.getString("AdditionNetAmount"));
                        arr2_test6.add(jsonObj.getString("NetAmount"));
                        arr2_test7.add(jsonObj.getString("DeliveryStatus"));
                        arr2_test8.add(jsonObj.getString("IsDriverVerify"));
                        arr2_test9.add(jsonObj.getString("IsCheckerVerify"));
                        arr2_test10.add(jsonObj.getString("IsReturnCustomer"));
                        arr2_test11.add(jsonObj.getString("IsBranchEmpVerify"));
                        arr2_test12.add(jsonObj.getString("IsCustomerCancel"));
                    }
                    /*System.out.println(jsonObj.getString("DeliveryStatus")+
                            jsonObj.getString("IsDriverVerify")+  jsonObj.getString("IsCheckerVerify")+
                            jsonObj.getString("IsReturnCustomer")+jsonObj.getString("IsBranchEmpVerify"));*/
                    items.add(new CustomItemFactoryOrderList("" + index,  jsonObj.getString("OrderNo"),
                            dateThai( jsonObj.getString("OrderDate")),  jsonObj.getString("TelephoneNo"),
                            jsonObj.getString("IsAddition"),  jsonObj.getString("AdditionNetAmount"),
                            jsonObj.getString("NetAmount"),  jsonObj.getString("DeliveryStatus"),
                            jsonObj.getString("IsDriverVerify"),  jsonObj.getString("IsCheckerVerify"),
                            jsonObj.getString("IsReturnCustomer"),jsonObj.getString("IsBranchEmpVerify"),
                            jsonObj.getString("IsCustomerCancel")));
                }
                for(int i=0;i<arr1_test1.size();i++){
                    for(int j=0;j<arr2_test1.size();j++) {
                        if(Integer.parseInt(arr1_test1.get(i))==Integer.parseInt(arr2_test1.get(j))){
                            arr1.add(arr1_test1.get(i));
                        }
                    }
                }
                System.out.println(arr1_test1+", "+arr2_test1+" :: "+arr1);
                /*for(int i=0;i<arr1_test1.size();i++){
                    arr1.add(arr1_test1.get(0));
                    arr2.add(arr1_test2.get(i));
                    arr3.add(arr1_test3.get(i));
                    arr4.add(arr1_test4.get(i));
                    arr5.add(arr1_test5.get(i));
                    arr6.add(arr1_test6.get(i));
                    arr7.add(arr1_test7.get(i));
                    arr8.add(arr1_test8.get(i));
                    arr9.add(arr1_test9.get(i));
                    arr10.add(arr1_test10.get(i));
                    arr11.add(arr1_test11.get(i));
                    arr12.add(arr1_test12.get(i));
                }*/


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
                FragmentCheckStatusOrderList fragmentCheckStatusOrderList = new FragmentCheckStatusOrderList(items);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentCheckStatusOrderList)
                        .commit();
            }
        }
    }
}