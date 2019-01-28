package com.techlogn.cleanmate_pos_v36;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by anucha on 1/3/2018.
 */

public class FragmentReturnCustomer extends Fragment {

    private View myView;
    private String dataID,branchID;
    private MyFont myFont;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private EditText search;
    private ArrayList<MyItemReturnCustomerList> items;
    private ProgressDialog dialog;
    private RequestPermissionHandler mRequestPermissionHandler;

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater=inflater;
        mContainer=container;
        myView = mLayoutInflater.inflate(R.layout.fragment_return_customer, mContainer, false);


        mRequestPermissionHandler = new RequestPermissionHandler();
        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/

        getIPAPI=new GetIPAPI();

        myFont = new MyFont(getActivity());
        items = new ArrayList<>();

        search=myView.findViewById(R.id.editText_search);
        search.setHint("ค้นหาเลขที่ใบรับผ้า");
        search.setTypeface(myFont.setFont());
        TextView t=myView.findViewById(R.id.textView6);
        t.setTypeface(myFont.setFont());
        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("ID", Activity.MODE_PRIVATE);
            Map<String, ?> entries2 = sharedPreferences2.getAll();
            Set<String> keys2 = entries2.keySet();
            String[] getData2;
            List<String> list2 = new ArrayList<String>(keys2);
            for (String temp : list2) {
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
            ImageView scan =myView.findViewById(R.id.imageView7);
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scan();
                }
            });

            search.setInputType(InputType.TYPE_CLASS_PHONE);
            search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        String url=getIPAPI.IPAddress+"/CleanmatePOS/searchReturnCustomer.php?search="+search.getText().toString();
                        url+="&branchID="+branchID;
                        new MyAsyncTask().execute(url);

                    }
                    return false;
                }
            });

        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }
        return myView;
    }
    private void scanBarcode() {
        Intent it = new Intent(getActivity(), BarcodeScanner.class);
        it.putExtra("IsBasket", "ReturnCustomer");
        it.putExtra("IsScan", "Order1");
        startActivityForResult(it, 200);
        //getActivity().finish();
    }
    public void scan() {
        mRequestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "request permission success", Toast.LENGTH_SHORT).show();
                scanBarcode();
            }

            @Override
            public void onFailed() {
                return;
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 200 && resultCode == Activity.RESULT_OK){
            final String contents = data.getStringExtra("barcode");
            String url=getIPAPI.IPAddress+"/CleanmatePOS/searchReturnCustomer.php?search="+contents;
            url+="&branchID="+branchID;
            new MyAsyncTask().execute(url);
        }
    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
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
                System.out.println(jsonArray.length());
                for(int i=0;i<jsonArray.length();i++) {
                    index = i + 1;
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    items.add(new MyItemReturnCustomerList("" + index, jsonObj.getString("OrderNo"),
                            jsonObj.getString("FirstName")+ " " + jsonObj.getString("LastName")));
                    //" ( คุณ" + jsonObject.get("NickName").getAsString() + " )"));

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
                new MyToast(getActivity(), "ไม่พบข้อมูลออเดอร์ที่ต้องคืนลูกค้า", 0);
            }else {
                Bundle bundle = new Bundle();
                bundle.putString("key", search.getText().toString());
                FragmentReturnCustomerOrder fragmentReturnCustomer = new FragmentReturnCustomerOrder(items);
                fragmentReturnCustomer.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentReturnCustomer)
                        .commit();
            }
        }
    }
}
