package com.techlogn.cleanmate_pos_v36;

import android.Manifest;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by anucha on 1/3/2018.
 */

public class FragmentReuturnStore extends Fragment {

    private View myView;
    private EditText edit;
    private MyFont myFont;
    private RelativeLayout layout_scan,layout_scan_bottom;
    private LinearLayout layout_title;
    private LinearLayout layout_detail;
    private LinearLayout layout_order;
    private TextView t10,textCountOrder,textCount;
    private ImageView icon_scan;
    private RequestPermissionHandler mRequestPermissionHandler;
    private MyAdapterReturnStore myAdapter;
    private ArrayList<MyItemReturnStore> items;
    private ListView listView;
    private String dataID,branchID;
    private ProgressDialog dialog;
    private String count,type;
    int index;

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_return_store, container, false);
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

        getIPAPI=new GetIPAPI();
        //Log
       /* Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/


        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("ID", Activity.MODE_PRIVATE);
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

        items=new ArrayList<>();

        mRequestPermissionHandler = new RequestPermissionHandler();
        myFont=new MyFont(getActivity());
        TextView t1=myView.findViewById(R.id.textTitle1);
        TextView t2=myView.findViewById(R.id.textTitle2);
        TextView t4=myView.findViewById(R.id.textView5);
        TextView t5=myView.findViewById(R.id.textViewScanOrder);
        textCount=myView.findViewById(R.id.textView11);

        t10=myView.findViewById(R.id.textView10);
        icon_scan=myView.findViewById(R.id.imageView9);
        layout_scan=myView.findViewById(R.id.layout_scan);
        layout_scan_bottom=myView.findViewById(R.id.layout_scan_bottom);

        layout_order=myView.findViewById(R.id.layout_order);
        layout_title=myView.findViewById(R.id.linearLayout6);
        layout_detail=myView.findViewById(R.id.ln_frag3);

        listView=myView.findViewById(R.id.list);

        textCountOrder=myView.findViewById(R.id.text_orderno);

        t1.setTypeface(myFont.setFont());
        t2.setTypeface(myFont.setFont());
        t4.setTypeface(myFont.setFont());
        t5.setTypeface(myFont.setFont());
        t10.setTypeface(myFont.setFont());
        textCountOrder.setTypeface(myFont.setFont());
        textCount.setTypeface(myFont.setFont());

        layout_order.setVisibility(View.GONE);
        layout_title.setVisibility(View.GONE);
        layout_detail.setVisibility(View.GONE);
        layout_scan_bottom.setVisibility(View.GONE);

        RelativeLayout back = myView.findViewById(R.id.btn_scan);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManageLevel0 fragmentManageLevel0 = new FragmentManageLevel0();
                //fragmentManageLevel0.setArguments(edit);
                getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel0)
                        .commit();

            }
        });
        String url=getIPAPI.IPAddress+"/CleanmatePOS/CountOrderReturnStore.php?branchID="+branchID;
        new MyAsyncTask().execute(url,"CountOrderReturnStore","");

        layout_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(textCount.getText().toString().endsWith("ออเดอร์ที่ต้องรับ : ("+0+"/"+0+")")){
                    new MyToast(getActivity(),"ยังไม่มีออเดอร์ที่ต้องรับคืน",0);
                }else {
                    //scan code
                    scan();

                    t10.setVisibility(View.GONE);
                    icon_scan.setVisibility(View.GONE);
                    layout_scan.setVisibility(View.GONE);
                    textCount.setVisibility(View.GONE);


                    layout_order.setVisibility(View.VISIBLE);
                    layout_title.setVisibility(View.VISIBLE);
                    layout_detail.setVisibility(View.VISIBLE);
                    layout_scan_bottom.setVisibility(View.VISIBLE);
                }


            }
        });

        layout_scan_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scan code
                scan();
            }
        });

        return myView;
    }
    private void scanBarcode() {
        Intent it = new Intent(getActivity(), Barcode.class);
        startActivityForResult(it, 100);
        getActivity().finish();
    }

    public void scan() {
        /*if (PermissionUtils.isMarshmallowOrHigher()) {
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
        }*/
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            final String contents = data.getStringExtra("barcode");
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            String dates = "" + df.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date());
            String url=getIPAPI.IPAddress+"/CleanmatePOS/IsBranchEmpVerify.php?verify="+1;
            url+="&date="+dates;
            url+="&OrderNo="+contents;
            url+="&branchID="+branchID;
            new MyAsyncTask().execute(url,"IsBranchEmpVerify",contents);

        }
    }

    public void setArguments(EditText edit) {
        this.edit=edit;
        this.edit.setHint("\tค้นหารายการรับสินค้าเข้าร้าน");
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
            if(strings[1].equals("CountOrderReturnStore")){
                type=strings[1];
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
                try {

                    JSONArray jsonArray=new JSONArray(response);
                    System.out.println(jsonArray.length());
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        count="ออเดอร์ที่ต้องรับ : (" + items.size() + "/" + jsonObj.getString("Count") + ")";

                    }

                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex);
                }
            }else if(strings[1].equals("IsBranchEmpVerify")) {
                type=strings[1];

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

                    index = items.size() + 1;
                    items.add(new MyItemReturnStore(items.size() + 1, strings[2], response));
                    new MyToast(getActivity(), response, 2);

                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(type.equals("CountOrderReturnStore")) {
                textCount.setText(count);
                textCountOrder.setText(count);
            }else if(type.equals("IsBranchEmpVerify")){

                textCountOrder.setText("ออเดอร์ที่ต้องรับ : (" + index + textCountOrder.getText().toString().substring(textCountOrder.getText().toString().indexOf('/'),
                        textCountOrder.getText().toString().length()));
                myAdapter = new MyAdapterReturnStore(getActivity(), R.layout.list_return_store, items);
                listView.setAdapter(myAdapter);
            }

        }
    }
}
