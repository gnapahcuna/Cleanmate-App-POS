package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

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

public class FragmentCancelOrder extends Fragment {

    private View myView;
    private TextView Text1,Text2,Text,Text3,Text4,Text5;
    private MyFont myFont;
    private ArrayList<CustomCancelOrder> items;
    private String key="",IsCheck,dataID,branchID;
    private SwipeMenuListView mSwipeListView;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private EditText edit;
    private ProgressDialog dialog;

    private GetIPAPI getIPAPI;

    public FragmentCancelOrder(ArrayList<CustomCancelOrder> items){
        this.items=items;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_cancel_orders, container, false);

        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/

        getIPAPI=new GetIPAPI();

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            myFont = new MyFont(getActivity());
            Text = myView.findViewById(R.id.textTitle);
            Text1 = myView.findViewById(R.id.textTitle1);
            Text2 = myView.findViewById(R.id.textTitle2);
            Text2.setText("เลขที่ใบรับผ้า");
            Text3 = myView.findViewById(R.id.textTitle3);
            Text4 = myView.findViewById(R.id.textTitle4);
            Text5 = myView.findViewById(R.id.textView5);
            Text.setTypeface(myFont.setFont());
            Text1.setTypeface(myFont.setFont());
            Text2.setTypeface(myFont.setFont());
            Text3.setTypeface(myFont.setFont());
            Text4.setTypeface(myFont.setFont());
            Text5.setTypeface(myFont.setFont());
            myView.setFocusableInTouchMode(true);
            myView.requestFocus();
            myView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                        /*Intent intent = new Intent(getActivity(),MenuActivity.class);
                        startActivity(intent);
                        return true;*/
                        }
                    }
                    return false;
                }
            });
            RelativeLayout back = myView.findViewById(R.id.btn_scan);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentCancel fragmentCancel = new FragmentCancel();
                    //fragmentManageLevel0.setArguments(edit);
                    getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentCancel)
                            .commit();

                }
            });

            mSwipeListView = myView.findViewById(R.id.listView);
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
            MyAdapterCancelOrder myAdapter = new MyAdapterCancelOrder(getActivity(), R.layout.list_cancel_order, items, branchID, dataID, mSwipeListView);
            mSwipeListView.setAdapter(myAdapter);


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
                    if (Integer.parseInt(items.get(position).mIsDriverVerify) == 1 && Integer.parseInt(items.get(position).mIsCheckerVerify) == 0 &&
                            Integer.parseInt(items.get(position).mIsDriverVerify) == 1 && Integer.parseInt(items.get(position).mIsReturnCustomer) == 0) {
                        new MyToast(getActivity(), "ออเดอร์กำลังจัดส่งไม่สามารถยกเลิกได้", 0);
                    }/*else if(Integer.parseInt(items.get(position).mIsDriverVerify) == 1 && Integer.parseInt(items.get(position).mIsCheckerVerify) == 1 &&
                                            Integer.parseInt(items.get(position).mIsReturnCustomer) == 1&& Integer.parseInt(items.get(position).mIsComplete) == 1) {
                                        new MyToast(getActivity(), "ออเดอร์ส่งมอบให้ลูกค้าแล้ว", 0);
                                    }*/ else {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.custom_alert_cancel);
                        dialog.show();
                        TextView title = dialog.findViewById(R.id.tv_quit_learning);
                        title.setText("ระบุเหตุผลในการยกเลิก");
                        final RadioButton chk1 = dialog.findViewById(R.id.radioButton1);
                        final RadioButton chk2 = dialog.findViewById(R.id.radioButton2);
                        final RadioButton chk3 = dialog.findViewById(R.id.radioButton3);
                        final RadioButton chk4 = dialog.findViewById(R.id.radioButton4);
                        final RadioButton[] arrRadioButton = new RadioButton[]{chk1, chk2, chk3, chk4};
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
                                        IsCheck = chk3.getText().toString().trim();
                                        edt.setVisibility(View.GONE);
                                    } else if (v.getId() == R.id.radioButton4) {
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

                                if (chk4.isChecked() == true) {
                                    IsCheck = edt.getText().toString().trim();
                                }
                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                String dates = "" + df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());

                                String url=getIPAPI.IPAddress+"/CleanmatePOS/IsActive.php?Date="+dates;
                                url+="&By="+dataID;
                                url+="&IsActive="+0;
                                url+="&orderNo="+items.get(position).mOrderNo;
                                url+="&Content="+IsCheck;
                                new MyAsyncTask().execute(url);


                                System.out.println(dates);
                                System.out.println(dataID);
                                System.out.println(0);
                                System.out.println(items.get(position).mOrderNo);
                                System.out.println(IsCheck);

                                items.remove(position);

                            }
                        });

                        MyFont myFont = new MyFont(getActivity());
                        okButton.setTypeface(myFont.setFont());
                        declineButton.setTypeface(myFont.setFont());
                        title.setTypeface(myFont.setFont());
                        chk1.setTypeface(myFont.setFont());
                        chk2.setTypeface(myFont.setFont());
                        chk3.setTypeface(myFont.setFont());
                        chk4.setTypeface(myFont.setFont());
                        edt.setTypeface(myFont.setFont());
                    }
                    return true;
                }
            });


        }else {
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

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem menuCall = new SwipeMenuItem(getActivity());
                menuCall.setBackground(new ColorDrawable(Color.parseColor("#ff4d4d")));
                menuCall.setWidth(dp2px(80));
                menuCall.setIcon(R.drawable.ic_cancel_black_24dp);
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
        edit.setHint("\tค้นหารายการสำหรับยกเลิก");
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
                new MyToast(getActivity(), response, 2);

                /*FragmentCancel fragment4 = new FragmentCancel();
                ((Activity) getActivity()).getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment4)
                        .commit();*/

            }catch (Exception ex){
                System.out.println("Error2 : "+ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            MyAdapterCancelOrder myAdapter = new MyAdapterCancelOrder(getActivity(), R.layout.list_cancel_order, items, branchID, dataID, mSwipeListView);
            mSwipeListView.setAdapter(myAdapter);

            FragmentCancelOrder fragment4 = new FragmentCancelOrder(items);
            ((Activity) getActivity()).getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment4)
                    .commit();
        }
    }
}
