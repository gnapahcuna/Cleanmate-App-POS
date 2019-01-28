package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.libRG.CustomTextView;

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

public class FragmentSuppliesOrder extends Fragment{

    View myView;
    ArrayList<CustomItemSuppliesOrder> items;
    ListView listView;
    TextView textTitle;
    MyAdapterSuppliesOrder myAdapter;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    SharedPreferences SP;
    int num=0,count=0;
    double price=0.0;
    ArrayList<String> textTH,textPrice,textNum,textID;
    ImageView imgback;
    CustomTextView total;
    ProgressDialog dialog,dialogupload;
    GetIPAPI getIPAPI;
    String dataID="",branchID="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_supplies_order, container, false);
        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();

        SP = getActivity().getSharedPreferences("Supplies", Context.MODE_PRIVATE);
        getIPAPI=new GetIPAPI();

        if (isInternetPresent) {

        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        items = new ArrayList<>();
        listView = myView.findViewById(R.id.list_1);

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

        textTitle=myView.findViewById(R.id.txtTitle);
        textTitle.setText("สรุปรายการสั่งวัสดุสิ้นเปลือง");
        imgback=myView.findViewById(R.id.img_black);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSupplies fragmentSupplies = new FragmentSupplies();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentSupplies)
                        .commit();
            }
        });

        LinearLayout ln = myView.findViewById(R.id.ln_list);
        ln.getLayoutParams().height=(getResources().getDisplayMetrics().heightPixels*70)/100;

        RelativeLayout addition = myView.findViewById(R.id.layoutClick_addcart);
        int width=getResources().getDisplayMetrics().widthPixels;
        addition.getLayoutParams().width = (width*45)/100;



        total=myView.findViewById(R.id.total_price);
        total.getLayoutParams().width=(width*90)/100;

        textTH=new ArrayList<>();
        textPrice=new ArrayList<>();
        textNum=new ArrayList<>();
        textID=new ArrayList<>();
        Map<String, ?> entries = SP.getAll();
        Set<String> keys = entries.keySet();
        String[] getData;
        List<String> list = new ArrayList<String>(keys);
        for (String temp : list) {
            System.out.println(temp+" = "+SP.getStringSet(temp,null));
            for (int k = 0; k < SP.getStringSet(temp, null).size(); k++) {
                getData = SP.getStringSet(temp, null).toArray(new String[SP.getStringSet(temp, null).size()]);
                char chk = getData[k].charAt(1);
                if (chk == 'a') {
                    textTH.add(getData[k].substring(3));
                } else if (chk == 'b') {
                    textPrice.add(getData[k].substring(3));
                } else if (chk == 'c') {
                    textNum.add(getData[k].substring(3));
                }else if (chk == 'd') {
                    textID.add(getData[k].substring(3));
                }

            }
        }
        double price=0.00;
        for(int i=0;i<textTH.size();i++) {
            items.add(new CustomItemSuppliesOrder(textTH.get(i), textPrice.get(i), "" + textNum.get(i)));
            price+=Double.parseDouble(textPrice.get(i))*Integer.parseInt(textNum.get(i));
        }
        listView = myView.findViewById(R.id.list_1);
        myAdapter = new MyAdapterSuppliesOrder(getActivity(), R.layout.list_supplies, items,listView, total);
        listView.setAdapter(myAdapter);

        total.setText("ราคารวม "+String.format("%,.2f", price)+" บาท");


        RelativeLayout cancel = myView.findViewById(R.id.btn_scan);
        cancel.getLayoutParams().width = (width*45)/100;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                TextView des = dialog.findViewById(R.id.tv_description);
                title.setText("แจ้งเตือน");
                des.setText("ยกเลิกรายการทั้งหมด?");
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        total.setText("ราคารวม "+0+" บาท");
                        SP.edit().clear().apply();
                        items.clear();
                        myAdapter = new MyAdapterSuppliesOrder(getActivity(), R.layout.list_supplies, items,listView, total);
                        listView.setAdapter(myAdapter);

                        FragmentSupplies fragmentSupplies = new FragmentSupplies();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragmentSupplies)
                                .commit();
                    }
                });
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                MyFont myFont = new MyFont(getActivity());
                okButton.setTypeface(myFont.setFont());
                declineButton.setTypeface(myFont.setFont());
                title.setTypeface(myFont.setFont());
                des.setTypeface(myFont.setFont());
            }
        });

        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                TextView des = dialog.findViewById(R.id.tv_description);
                title.setText("แจ้งเตือน");
                des.setText("ยืนยันการสั่งวัสดุสิ้นเปลือง?");
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        dialogupload = new ProgressDialog(getActivity());
                        dialogupload.setIcon(R.mipmap.ic_launcher);
                        dialogupload.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialogupload.setMessage("กำลังตรวจสอบข้อมูล");
                        dialogupload.setIndeterminate(true);
                        dialogupload.setCancelable(false);
                        dialogupload.show();
                        final Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {

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
                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                String dates = "" + df.format("yyyy-MM-dd", new java.util.Date());
                                String datesCreate = "" + df.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());

                                String url = getIPAPI.IPAddress+"/CleanmatePOS/AddSuppliesOrder.php?BranchID="+branchID;
                                url+="&OrderDate="+dates;
                                url+="&Date2="+datesCreate;
                                url+="&CreateBy="+dataID;
                                new MyAsyncTask().execute(url);

                                for(int i=0;i<textTH.size();i++){
                                    for(int j=0;j<Integer.parseInt(textNum.get(i));j++){
                                        String url1 = getIPAPI.IPAddress+"/CleanmatePOS/AddSuppliesOrderDetail.php?SuppliesID="+textID.get(i)+"&BranchID="+branchID;
                                        new MyAsyncTask().execute(url1);
                                    }
                                }
                                dialog.dismiss();
                                handler.removeCallbacks(this);
                                dialogupload.dismiss();
                                new MyToast(getActivity(), "ข้อมูลการทำรายการถูกบันทึกเรียบร้อย", 1);

                                total.setText("ราคารวม "+0+" บาท");
                                SP.edit().clear().apply();
                                items.clear();
                                myAdapter = new MyAdapterSuppliesOrder(getActivity(), R.layout.list_supplies, items,listView, total);
                                listView.setAdapter(myAdapter);

                                FragmentSupplies fragmentSupplies = new FragmentSupplies();
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, fragmentSupplies)
                                        .commit();
                            }
                        };
                        handler.postDelayed(runnable, 1000);

                    }
                });
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                MyFont myFont = new MyFont(getActivity());
                okButton.setTypeface(myFont.setFont());
                declineButton.setTypeface(myFont.setFont());
                title.setTypeface(myFont.setFont());
                des.setTypeface(myFont.setFont());
            }
        });

        return myView;
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(getActivity());
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กำลังตรวจสอบข้อมูล");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();*/
        }

        @Override
        protected String doInBackground(String... strings) {


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
                System.out.println("Error1 : " + ex);
            }

            String output = "";
            try {
                output = response;
                System.out.println(response);
            } catch (Exception ex) {
                System.out.println("Error2 : " + ex);
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //dialog.dismiss();
        }
    }

}
