package com.techlogn.cleanmate_pos_v36;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.libRG.CustomTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FirstFragmentBasket extends Fragment {

    View v,v1;
    ArrayList<CustomItemOrder> items;
    String dataID,branchID;
    private ProgressDialog dialog;

    SharedPreferences sharedPreferences,sharedPreferences1,sharedPreferences2,sharedPreferences3,sp,sp1,sharedPreferences9,
            sharedPreferences4,sharedPreferences5,sharedPreferences8;
    ArrayList<String>arr_proTH,arr_proEN,arr_serTH,arr_serEN,arr_price,arr_id,arr_img,arr_count,arr_color,arr_serType;

    LinearLayout layout;
    ListView listView;
    MyAdapterOrder myAdapter;
    int sum;
    TabLayout tabLayout;
    ViewPager viewPager;
    CustomTextView total;

    ImageView bin,delivery_express;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    int check_express=0;
    int express=0,expressTotal=0;
    String expressDate="",expressDay="",appoint="";

    MyFont myFont;

    private com.fourmob.datetimepicker.date.DatePickerDialog mDatePicker1;
    private Calendar mCalendar;
    String dd="",mm="",dd4="",mm4="";
    String dates;
    TextView textDate;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    ArrayList<String> arrServiceType;
    ArrayList<String> arrExpressRate;
    ArrayList<String> arrIsExpressLevel;

    private GetIPAPI getIPAPI;
    public FirstFragmentBasket() {
        // Required empty public constructor
    }


    public static FirstFragmentBasket newInstance() {

        Bundle args = new Bundle();
        FirstFragmentBasket fragment = new FirstFragmentBasket();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_first, container, false);
        v1 = inflater.inflate(R.layout.activity_menu_basket, container, false);

        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            v.setFocusableInTouchMode(true);
            v.requestFocus();
            v.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            new MyToast(getActivity(), "เลือกสินค้าเพิ่ม กดปุ่ม -เลือกสินค้าเพิ่ม- เท่านั้น", 2);
                            return true;
                        }
                    }
                    return false;
                }
            });

            sp1 = getContext().getSharedPreferences("Appoint", Context.MODE_PRIVATE);
            //sp1.edit().clear().apply();

            mSQLite = SQLiteHelper.getInstance(getActivity());

            textDate = v.findViewById(R.id.textDate);
            textDate.setText("");
            //textDate.setVisibility(View.GONE);
            mCalendar = Calendar.getInstance();
            dtpicker();

            ImageView imgDate = v.findViewById(R.id.img_appoint);
            imgDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sharedPreferences.getAll().size() == 0) {
                        new MyToast(getActivity(), "ยังไม่มีรายการสินค้า", 0);
                    } else {
                        mDatePicker1.setYearRange(2018, 2030);
                        mDatePicker1.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "datePicker");
                    }
                }
            });

            myFont = new MyFont(getActivity());
            TextView title = v.findViewById(R.id.text_title);
            title.setTypeface(myFont.setFont());

            tabLayout = v1.findViewById(R.id.tablayout);
            viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);

            dialog = new ProgressDialog(getActivity());
            items = new ArrayList<>();

            total = v.findViewById(R.id.total_price);

            TextView t5 = v.findViewById(R.id.textView5);
            TextView t7 = v.findViewById(R.id.textView7);
            t7.setTypeface(myFont.setFont());
            t5.setTypeface(myFont.setFont());

            //Preferrence
            arr_proTH = new ArrayList<>();
            arr_proEN = new ArrayList<>();
            arr_serTH = new ArrayList<>();
            arr_serEN = new ArrayList<>();
            arr_price = new ArrayList<>();
            arr_id = new ArrayList<>();
            arr_img = new ArrayList<>();
            arr_count = new ArrayList<>();
            arr_color = new ArrayList<>();
            arr_serType = new ArrayList<>();

            sharedPreferences = getContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
            sharedPreferences1 = getContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
            sharedPreferences2 = getContext().getSharedPreferences("CouponValue", Activity.MODE_PRIVATE);
            //sp1 = getContext().getSharedPreferences("Appoint", Activity.MODE_PRIVATE);
            sharedPreferences4 = getContext().getSharedPreferences("Privilage", Activity.MODE_PRIVATE);
            sharedPreferences5 = getContext().getSharedPreferences("CouponPoint", Activity.MODE_PRIVATE);
            sp = getContext().getSharedPreferences("Express", Context.MODE_PRIVATE);
            sharedPreferences8=getContext().getSharedPreferences("Special", Activity.MODE_PRIVATE);
            sharedPreferences9=getContext().getSharedPreferences("CouponPoint_1", Activity.MODE_PRIVATE);
            // h sharedPreferences1.edit().clear().apply();
            //sharedPreferences.edit().clear().apply();
            Map<String, ?> entries = sharedPreferences.getAll();
            Set<String> keys = entries.keySet();
            String[] getData;
            List<String> list = new ArrayList<String>(keys);
            for (String temp : list) {
                for (int i = 0; i < sharedPreferences.getStringSet(temp, null).size(); i++) {
                    getData = sharedPreferences.getStringSet(temp, null).toArray(new String[sharedPreferences.getStringSet(temp, null).size()]);
                    char chk = getData[i].charAt(1);
                    if (chk == 'a') {
                        arr_proTH.add(getData[i].substring(3));
                    } else if (chk == 'b') {
                        arr_proEN.add(getData[i].substring(3));
                    } else if (chk == 'c') {
                        arr_price.add(getData[i].substring(3));
                    } else if (chk == 'd') {
                        arr_serTH.add(getData[i].substring(3));
                    } else if (chk == 'e') {
                        arr_serEN.add(getData[i].substring(3));
                    } else if (chk == 'f') {
                        arr_img.add(getData[i].substring(3));
                    } else if (chk == 'g') {
                        arr_count.add(getData[i].substring(3).trim());
                    } else if (chk == 'h') {
                        arr_id.add(getData[i].substring(3));
                    } else if (chk == 'i') {
                        arr_color.add(getData[i].substring(3));
                    } else if (chk == 'j') {
                        arr_serType.add(getData[i].substring(3));
                    }
                }
            }
            sum = 0;
            for (int i = 0; i < arr_id.size(); i++) {
                sum += Integer.parseInt(arr_price.get(i)) * Double.parseDouble(arr_count.get(i));
                items.add(new CustomItemOrder(arr_id.get(i), arr_serTH.get(i), arr_serEN.get(i), arr_proTH.get(i), arr_proEN.get(i),
                        arr_price.get(i), arr_img.get(i), arr_color.get(i), arr_count.get(i), arr_serType.get(i), false, 0));
            }


            Map<String, ?> entries1 = sp.getAll();
            Set<String> keys1 = entries1.keySet();
            String[] getData1;
            List<String> list1 = new ArrayList<String>(keys1);
            for (String temp : list1) {
                for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                    getData1 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                    char chk = getData1[i].charAt(1);
                    if (chk == 'a') {
                        check_express = Integer.parseInt(getData1[i].substring(3));
                    } else if (chk == 'b') {
                        expressTotal = Integer.parseInt(getData1[i].substring(3));
                    } else if (chk == 'c') {
                        expressDate = getData1[i].substring(3);
                    } else if (chk == 'd') {
                        expressDay = getData1[i].substring(3);
                    }
                }
            }
            Map<String, ?> entries2 = sp1.getAll();
            Set<String> keys2 = entries2.keySet();
            String[] getData2;
            List<String> list2 = new ArrayList<String>(keys2);
            for (String temp : list2) {
                for (int i = 0; i < sp1.getStringSet(temp, null).size(); i++) {
                    getData2 = sp1.getStringSet(temp, null).toArray(new String[sp1.getStringSet(temp, null).size()]);
                    char chk = getData2[i].charAt(1);
                    if (chk == 'a') {
                        appoint = getData2[i].substring(3);
                    }
                }
            }

            bin = v.findViewById(R.id.imageView_bin);
            delivery_express = v.findViewById(R.id.imageView_delivery);

            RelativeLayout addition = v.findViewById(R.id.layoutClick_addcart);
            int width=getResources().getDisplayMetrics().widthPixels;
            addition.getLayoutParams().width = (width*45)/100;
            addition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = getContext().getSharedPreferences("K", Context.MODE_PRIVATE);
                    String F = "";
                    String dataID = "";
                    Map<String, ?> entries2 = sp.getAll();
                    Set<String> keys2 = entries2.keySet();
                    String[] getData2;
                    List<String> list2 = new ArrayList<String>(keys2);
                    for (String temp : list2) {
                        for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                            getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);


                            char chk = getData2[i].charAt(1);
                            if (chk == 'a') {
                                F = getData2[i].substring(3);
                            } else if (chk == 'b') {
                                dataID = getData2[i].substring(3);
                            }
                        }
                    }
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("F", F);
                    intent.putExtra("dataID", dataID);
                    getActivity().finish();
                    startActivity(intent);

                }
            });

            listView = v.findViewById(R.id.list_order);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                    return false;
                }
            });

            myAdapter = new MyAdapterOrder(getActivity(), R.layout.order_list1, items, listView, dataID, sum, total, tabLayout, viewPager, bin);
            listView.setAdapter(myAdapter);


            total.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + getFormatedAmount(sum) + " บาท");
            total.setTypeface(myFont.setFont());

            if (check_express == 2) {
                delivery_express.setImageDrawable(getResources().getDrawable(R.drawable.express_select));
                total.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + getFormatedAmount(expressTotal) + " บาท");
                textDate.setText(expressDay);
            } else if (check_express == 1) {
                delivery_express.setImageDrawable(getResources().getDrawable(R.drawable.delivery_express));
                total.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + getFormatedAmount(expressTotal) + " บาท");
                textDate.setText(expressDay);
            } else {
                delivery_express.setImageDrawable(getResources().getDrawable(R.drawable.express_unselect));
            }


            if (appoint.trim().isEmpty()) {

            } else {
                textDate.setText(appoint.substring(8, appoint.length()));
                total.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + getFormatedAmount(sum) + " บาท");
            }



            delivery_express.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sharedPreferences.getAll().size() == 0) {
                        new MyToast(getActivity(), "ยังไม่มีรายการสินค้า", 0);
                    } else {
                        PopupMenu popupMenu = new PopupMenu(getContext(), view);
                        popupMenu.inflate(R.menu.popup_express);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                expressTotal = 0;
                                if (menuItem.getItemId() == R.id.express1) {
                                    delivery_express.setImageDrawable(getResources().getDrawable(R.drawable.express_select));
                                    new MyToast(getActivity(), "ซักด่วนพิเศษ", 2);
                                    express = 2;
                                } else if (menuItem.getItemId() == R.id.express2) {
                                    delivery_express.setImageDrawable(getResources().getDrawable(R.drawable.delivery_express));
                                    new MyToast(getActivity(), "ซักด่วน", 2);
                                    express = 1;
                                }
                                sum = 0;


                                String url=getIPAPI.IPAddress+"/CleanmatePOS/IsExpression.php";
                                new MyAsyncTask().execute(url);

                                return true;
                            }
                        });
                        popupMenu.show();
                    }
                }

            });

        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }
        return v;
    }
    private void dtpicker() {
        mDatePicker1 = DatePickerDialog.newInstance(onDateSetListener1,
                mCalendar.get(Calendar.YEAR),       // ปี
                mCalendar.get(Calendar.MONTH),      // เดือน
                mCalendar.get(Calendar.DAY_OF_MONTH),// วัน (1-31)
                false);
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener1 =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, final int day) {

                    dd = "" + day;
                    mm = "" + (month + 1);

                    if (dd.length() == 1) {
                        dd4 = "0" + dd;
                    }
                    if (mm.length() == 1) {
                        mm4 = "0" + mm;
                    }
                    if (dd.length() == 2) {
                        dd4 = dd;
                    }
                    if (mm.length() == 2) {
                        mm4 = mm;
                    }
                    dates = year + "-" + mm4 + "-" + dd4;
                    String dd = dd4;

                    textDate.setText(dd);

                    SharedPreferences.Editor editor = sp1.edit();
                    ArrayList<String> arr_proTH = new ArrayList<>();
                    arr_proTH.add("appoint");
                    StringBuilder sb = new StringBuilder();
                    HashSet<String> mSet = new HashSet<>();
                    for (int i = 0; i < arr_proTH.size(); i++) {
                        mSet.add("<a>" + dates);
                        mSet.add("<b>" + "");
                    }
                    editor.putStringSet("appoint", mSet);
                    editor.apply();


                }
            };
    @Override
    public void onStart() {
        super.onStart();
        RelativeLayout cancel = v.findViewById(R.id.btn_scan);
        int width=getResources().getDisplayMetrics().widthPixels;
        cancel.getLayoutParams().width = (width*45)/100;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myAdapter.getCount() == 0) {
                    /*final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.custon_alert_dialog);
                    dialog.show();
                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                    TextView des = dialog.findViewById(R.id.tv_description);
                    title.setText("แจ้งเตือน");
                    des.setText("ยังไม่มีรายการในตะกร้าสินค้า");
                    Button declineButton = dialog.findViewById(R.id.btn_cancel);
                    declineButton.setVisibility(View.GONE);
                    Button okButton = dialog.findViewById(R.id.btn_ok);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    MyFont myFont = new MyFont(getActivity());
                    declineButton.setTypeface(myFont.setFont());
                    okButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    des.setTypeface(myFont.setFont());*/
                    new MyToast(getActivity(), "ยังไม่มีรายการสินค้า", 0);
                } else {


                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.custon_alert_dialog);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                    TextView des = dialog.findViewById(R.id.tv_description);
                    title.setText("แจ้งเตือน");
                    des.setText("รายการทั้งหมดจะถูกลบและไม่สามารถเรียกกลับคืนได้");
                    Button okButton = dialog.findViewById(R.id.btn_ok);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharedPreferences.edit().clear().apply();
                            sharedPreferences1.edit().clear().apply();
                            sharedPreferences2.edit().clear().apply();
                            sp1.edit().clear().apply();
                            sharedPreferences4.edit().clear().apply();
                            sharedPreferences5.edit().clear().apply();
                            sharedPreferences8.edit().clear().apply();
                            sharedPreferences9.edit().clear().apply();
                            sp.edit().clear().apply();
                            SharedPreferences sp_num = getContext().getSharedPreferences("NUM", Context.MODE_PRIVATE);
                            sp_num.edit().clear().apply();
                            SharedPreferences sp_bcpk=getActivity().getSharedPreferences("BarcodePK", Activity.MODE_PRIVATE);
                            sp_bcpk.edit().clear().apply();
                            reloadAllData();
                            total.setText("");
                            dialog.dismiss();
                            mDb = mSQLite.getReadableDatabase();
                            String sql2 = "Delete FROM promotion_sale";
                            mDb.execSQL(sql2);

                            String sql3 = "Delete FROM tb_coupon";
                            mDb.execSQL(sql3);
                            new MyToast(getActivity(), "ลบข้อมูลเรียบร้อย", 1);

                            delivery_express.setImageDrawable(getResources().getDrawable(R.drawable.express_unselect));
                            textDate.setText("");
                            bin.setImageDrawable(getResources().getDrawable(R.drawable.recycling_bin2));
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


            }
        });

    }
    public void reloadAllData(){
        myAdapter.clear();
        myAdapter.addAll(items);
        myAdapter.notifyDataSetChanged();
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


            String response="";
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();


                InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner=new Scanner(inputStream,"UTF-8");
                response=scanner.useDelimiter("\\A").next();

            }catch (Exception ex){
                System.out.println("Error1");
            }

            String output="";
            try {
                arrServiceType = new ArrayList<>();
                arrExpressRate = new ArrayList<>();
                arrIsExpressLevel = new ArrayList<>();
                JSONArray jsonArray=new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    arrServiceType.add(jsonObject.optString("ServiceType"));
                    arrExpressRate.add(jsonObject.optString("ExpressRate"));
                    arrIsExpressLevel.add(jsonObject.optString("IsExpressLevel"));
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
            for (int i = 0; i < arr_count.size(); i++) {
                for (int j = 0; j < Integer.parseInt(arr_count.get(i)); j++) {
                    for (int k = 0; k < arrExpressRate.size(); k++) {
                        if (Integer.parseInt(arrServiceType.get(k)) == Integer.parseInt(arr_serType.get(i))
                                && express == Integer.parseInt(arrIsExpressLevel.get(k))) {
                            expressTotal += Integer.parseInt(arr_price.get(i)) + ((Integer.parseInt(arr_price.get(i)) * Integer.parseInt(arrExpressRate.get(k))) / 100);

                        }
                    }
                }
            }
            total.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + getFormatedAmount(expressTotal) + " บาท");


            android.text.format.DateFormat df = new android.text.format.DateFormat();
            String date_today = "" + df.format("yyyy-MM-dd", new java.util.Date());
            Calendar calendar = Calendar.getInstance();


            if (express == 1) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            } else if (express == 2) {
                calendar.add(Calendar.DAY_OF_YEAR, 0);
            }
            Date tomorrow = calendar.getTime();
            String tomorrowAsString = "" + df.format("yyyy-MM-dd", tomorrow);
            String dd = "" + df.format("dd", tomorrow);
            String date = tomorrowAsString;


            SharedPreferences.Editor editor = sp.edit();
            ArrayList<String> arr = new ArrayList<>();
            arr.add("Express");
            HashSet<String> mSet = new HashSet<>();
            mSet.add("<a>" + express);
            mSet.add("<b>" + expressTotal);
            mSet.add("<c>" + date);
            mSet.add("<d>" + dd);
            editor.putStringSet("Express", mSet);
            editor.apply();

            textDate.setText(dd);

            SharedPreferences.Editor editor1 = sp1.edit();
            ArrayList<String> arr_proTH = new ArrayList<>();
            arr_proTH.add("appoint");
            StringBuilder sb = new StringBuilder();
            HashSet<String> mSet1 = new HashSet<>();
            for (int i = 0; i < arr_proTH.size(); i++) {
                mSet1.add("<a>" + date_today);
                mSet1.add("<b>" + "");
            }
            editor1.putStringSet("appoint", mSet1);
            editor1.apply();
        }
    }

}
