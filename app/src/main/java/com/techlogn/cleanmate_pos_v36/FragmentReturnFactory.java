package com.techlogn.cleanmate_pos_v36;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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

public class FragmentReturnFactory extends Fragment {
    private View myView;
    private EditText edit;
    private MyFont myFont;
    private RelativeLayout layout_order,layout_item,layout_sav;
    private EditText getOrder,getItem;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private String dataID,branchID,IsCheck;
    private RequestPermissionHandler mRequestPermissionHandler;
    private MyAdapterBarcodeOrderDetail myAdapter;
    private ArrayList<MyItemBarcodeOrderDetail> items;
    //private ListView listView;
    private SwipeMenuListView mSwipeListView;
    private TextView t5;
    private ImageView imageView;
    private ProgressDialog dialog;
    private String type,getOrderNo,chk,Barcode,ProductNameTH;

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
        mRequestPermissionHandler = new RequestPermissionHandler();

        myView = inflater.inflate(R.layout.fragment_return_factory, container, false);
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

        items=new ArrayList<>();

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

        if (isInternetPresent) {
            myFont=new MyFont(getActivity());
            TextView t1=myView.findViewById(R.id.textViewScanOrder);
            TextView t2=myView.findViewById(R.id.textViewAddItem);
            TextView t3=myView.findViewById(R.id.textViewSave);
            TextView t4=myView.findViewById(R.id.textView5);
            t5=myView.findViewById(R.id.textView8);
            imageView=myView.findViewById(R.id.imageView8);
            t1.setTypeface(myFont.setFont());
            t2.setTypeface(myFont.setFont());
            t3.setTypeface(myFont.setFont());
            t4.setTypeface(myFont.setFont());
            t5.setTypeface(myFont.setFont());


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
            layout_order=myView.findViewById(R.id.layout_search);
            layout_item=myView.findViewById(R.id.layout_scan);
            layout_sav=myView.findViewById(R.id.layout_save);
            getOrder=myView.findViewById(R.id.edt_order);
            getOrder.setHint("เลขที่ใบรับผ้า");
            int width=getResources().getDisplayMetrics().widthPixels;
            getOrder.getLayoutParams().width=(width*50)/100;
            mSwipeListView = myView.findViewById(R.id.listItem);
            getOrder.setTypeface(myFont.setFont());

            layout_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanOrder();
                }
            });
            layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getOrder.getText().toString().isEmpty()){
                        new MyToast(getActivity(),"กรุณากรอกหรือสแกนหมายเลขออเดอร์ก่อน",0);
                    }else{
                        t5.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        scanItem();
                        //myAdapter.notifyDataSetChanged();
                    }
                }
            });
            layout_sav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getOrder.getText().toString().isEmpty()){
                        new MyToast(getActivity(),"กรุณาสแกนบาร์โค้ดสินค้าก่อน",0);
                    }else if(items.size()==0){
                        new MyToast(getActivity(),"กรุณากรอกเลขออเดอร์หรือสแกนบาร์โค้ดถุงผ้าก่อน",0);
                    }else{
                        scanNewPackage(IsCheck);
                        new MyToast(getActivity(),"สแกนถุงใหม่",2);
                    }


                }
            });
        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return myView;
    }
    private void scanBarcodeItem() {
        Intent it = new Intent(getActivity(), BarcodeScanner.class);
        it.putExtra("IsBasket", "ReturnFactory");
        it.putExtra("IsScan", "Product");
        startActivityForResult(it, 200);
        //getActivity().finish();
    }
    private void scanBarcodeOrder() {
        Intent it = new Intent(getActivity(), BarcodeScanner.class);
        it.putExtra("IsBasket", "ReturnFactory");
        it.putExtra("IsScan", "PackagePL");
        startActivityForResult(it, 100);
        //getActivity().finish();
    }
    private void scanBarcodeNewPackage(String comment) {
        Intent it = new Intent(getActivity(), BarcodeScanner.class);
        it.putExtra("IsBasket", "ReturnFactory");
        it.putExtra("IsScan", "Package");
        startActivityForResult(it, 300);
        //getActivity().finish();
    }

    public void scanItem() {
        mRequestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "request permission success", Toast.LENGTH_SHORT).show();
                scanBarcodeItem();
            }

            @Override
            public void onFailed() {
                return;
            }
        });

    }
    public void scanOrder() {
        mRequestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "request permission success", Toast.LENGTH_SHORT).show();
                scanBarcodeOrder();
            }

            @Override
            public void onFailed() {
                return;
            }
        });

    }
    public void scanNewPackage(final String comment) {
        mRequestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "request permission success", Toast.LENGTH_SHORT).show();
                scanBarcodeNewPackage(comment);
            }

            @Override
            public void onFailed() {
                return;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            final String contents = data.getStringExtra("barcode");
            String url=getIPAPI.IPAddress+"/CleanmatePOS/ReturnFactoryGetOrder.php?Content="+contents+"&BranchID="+branchID;
            new MyAsyncTask().execute(url,"ReturnFactoryGetOrder");

        }else if(requestCode == 200 && resultCode == Activity.RESULT_OK){
            if(items.size()>0) {
                for (int i = 0; i < items.size(); i++) {
                    System.out.println("items : " + items.get(i).getBarcode());
                }
            }

            final String contents = data.getStringExtra("barcode");
            String url=getIPAPI.IPAddress+"/CleanmatePOS/ReturnFactoryGetItem.php?Content="+contents+"&OrderNo="+getOrder.getText().toString();
            new MyAsyncTask().execute(url,"ReturnFactoryGetItem");


        }else if(requestCode == 300 && resultCode == Activity.RESULT_OK) {

            final String contents = data.getStringExtra("barcode");
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setIcon(R.mipmap.loading);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กรุณารอสักครู่....");
            dialog.setIndeterminate(true);
            dialog.show();
            if (items.size() > 0) {
                for (int i = 0; i < items.size(); i++) {
                    System.out.println(getOrder.getText().toString() + " , " + items.get(i).getBarcode());
                    Ion.with(getActivity())
                            .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/IsReturnFactory.php")
                            .setBodyParameter("OrderNo", "" + getOrder.getText().toString())
                            .setBodyParameter("Barcode", "" + items.get(i).getBarcode())
                            .setBodyParameter("content", "" + contents)
                            .setBodyParameter("comment", "" + items.get(i).getIsCheck())
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    new MyToast(getActivity(), "ทำรายการสำเร็จ", 2);
                                    dialog.dismiss();
                                }
                            });


                }
                String url1=getIPAPI.IPAddress+"/CleanmatePOS/Transport1.php?Data1="+branchID;
                url1+="&Data3="+getOrder.getText().toString();
                url1+="&Data4="+contents;
                url1+="&packageType="+0;
                new MyAsyncTask().execute(url1,"Transport1");

            }else{
                new MyToast(getActivity(), "ยังไม่มีรายการสินค้า", 0);
                dialog.dismiss();
            }
        }
    }
    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem menuCall = new SwipeMenuItem(getActivity());
                menuCall.setBackground(new ColorDrawable(Color.parseColor("#ff4d4d")));
                menuCall.setWidth(dp2px(80));
                menuCall.setIcon(R.drawable.ic_do_not_disturb_alt_black_24dp);
                menu.addMenuItem(menuCall);

            }
        };

        mSwipeListView.setMenuCreator(creator);
    }

    private int dp2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setArguments(EditText edt) {
        edit=edt;
        edit.setHint("\tค้นหารายการสินค้าคืนโรงงาน");
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
            String response3="";
            String output="";

            if(strings[1].equals("ReturnFactoryGetOrder")){
                type=strings[1];
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

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    output=""+jsonArray.length();
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        getOrderNo=jsonObj.getString("OrderNo");
                    }


                }catch (Exception ex){
                    System.out.println("Error2 : "+ex);
                }
            }else if(strings[1].equals("ReturnFactoryGetItem")){
                type=strings[1];
                try {
                    URL url=new URL(strings[0]);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    //httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    Scanner scanner=new Scanner(inputStream,"UTF-8");
                    response1=scanner.useDelimiter("\\A").next();


                }catch (Exception ex){
                    System.out.println("Error1");
                }

                int index;
                try {
                    JSONArray jsonArray=new JSONArray(response1);
                    output=""+jsonArray.length();
                    System.out.println(jsonArray.length());
                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        index = items.size()+1;
                        Barcode=jsonObj.getString("Barcode");
                        ProductNameTH=jsonObj.getString("ProductNameTH");

                    }

                }catch (Exception ex){
                    System.out.println("Error2 : "+ex);
                }
            }else if(strings[1].equals("Transport1")){
                type=strings[1];
                try {
                    URL url=new URL(strings[0]);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    //httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    Scanner scanner=new Scanner(inputStream,"UTF-8");
                    response2=scanner.useDelimiter("\\A").next();


                }catch (Exception ex){
                    System.out.println("ErrorTransport1");
                }

                try {
                    System.out.println(response2);
                    output=""+1010;
                    new MyToast(getActivity(), response2, 2);
                    /*JSONObject jsonObject1=new JSONObject(response);
                    out+=jsonObject1.optString("firstname");
                    JSONArray jsonArray=new JSONArray(response1);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        out += jsonObject.optString("OrderNo");
                    }*/
                }catch (Exception ex){
                    System.out.println("ErrorTransport2 : "+ex);
                }
            }

            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(Integer.parseInt(s)==0){
                new MyToast(getActivity(), "ไม่พบข้อมูลออเดอร์ที่ต้องส่งคืนโรงงาน", 0);
            }else {
                if (type.equals("ReturnFactoryGetOrder")) {
                    getOrder.setText("" + getOrderNo);
                } else if (type.equals("ReturnFactoryGetItem")) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_alert_return_factory);
                    dialog.show();
                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                    title.setText("ระบุเหตุผลในการยกเลิก");
                    final RadioButton chk1 = dialog.findViewById(R.id.radioButton1);
                    final RadioButton chk2 = dialog.findViewById(R.id.radioButton2);
                    final RadioButton chk3 = dialog.findViewById(R.id.radioButton3);
                    final RadioButton[] arrRadioButton = new RadioButton[]{chk1, chk2, chk3};
                    final MultiAutoCompleteTextView edt = dialog.findViewById(R.id.multiAutoCompleteTextView);
                    edt.setVisibility(View.GONE);
                    for (RadioButton rb : arrRadioButton
                            ) {
                        rb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v.getId() == R.id.radioButton1) {
                                    IsCheck = chk1.getText().toString().trim();
                                    edt.setVisibility(View.GONE);
                                } else if (v.getId() == R.id.radioButton2) {
                                    IsCheck = chk2.getText().toString().trim();
                                    edt.setVisibility(View.GONE);
                                } else if (v.getId() == R.id.radioButton3) {
                                    IsCheck = edt.getText().toString().trim();
                                    edt.setVisibility(View.VISIBLE);
                                    edt.requestFocus();
                                }


                            }
                        });
                    }
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

                            if (chk3.isChecked() == true) {
                                IsCheck = edt.getText().toString().trim();
                            }
                            items.add(new MyItemBarcodeOrderDetail(Barcode,ProductNameTH,IsCheck));
                            System.out.println("out : "+items.size());
                            myAdapter = new MyAdapterBarcodeOrderDetail(getActivity(), R.layout.list_barcode, items);
                            mSwipeListView.setAdapter(myAdapter);

                        }
                    });

                    MyFont myFont = new MyFont(getActivity());
                    okButton.setTypeface(myFont.setFont());
                    declineButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    chk1.setTypeface(myFont.setFont());
                    chk2.setTypeface(myFont.setFont());
                    chk3.setTypeface(myFont.setFont());
                    edt.setTypeface(myFont.setFont());



                    mSwipeListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

                    createSwipeMenu();

                    mSwipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            mSwipeListView.smoothOpenMenu(i);
                        }
                    });

                    mSwipeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                            items.remove(position);
                            myAdapter = new MyAdapterBarcodeOrderDetail(getActivity(), R.layout.list_barcode, items);
                            myAdapter.notifyDataSetChanged();
                            mSwipeListView.setAdapter(myAdapter);
                            return true;
                        }
                    });
                } else if (type.equals("ReturnFactoryGetItem")) {
                    if (Integer.parseInt(s) == 0) {
                        new MyToast(getActivity(), "ข้อมูลไม่ตรงกับออเดอร์", 0);
                    }
                } else {
                    new MyToast(getActivity(), "ทำรายการสำเร็จ", 2);
                    getOrder.setText("");
                    items.clear();
                    myAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                    t5.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
