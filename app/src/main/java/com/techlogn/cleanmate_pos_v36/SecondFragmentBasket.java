package com.techlogn.cleanmate_pos_v36;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;

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

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class SecondFragmentBasket extends Fragment {

    View v,v1;
    private EditText search_text,name_text,nickname_text,phone_text;
    ImageButton btn_chk;
    ImageView btn_edit_member,editView;
    String id;
    Spinner title;
    String f,l,e,r,c;
    TabLayout tabLayout;
    ViewPager viewPager;
    String dataID,branchID;
    int a=0;
    String date_today;
    SharedPreferences sp,sp1,sp2,sp3;
    String tomorrowAsString;

    private GetIPAPI getIPAPI;

    String memberType="",exp="",nickname="",name="",phone="",email="",getExp="",getMemberType=""+0,getexp1;

    TextView textType,textExp,getTextExp;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    String getphone="",getname="",getnickname="",getexp="",gettypemember="",getemail="",getID="";

    ProgressDialog dialog;
    String getsearch;
    String typeservice;

    private Calendar mCalendar;
    private com.fourmob.datetimepicker.date.DatePickerDialog mDatePicker1;
    private String Datetime1;
    String dd="",mm="",dd4="",mm4="";

    public SecondFragmentBasket() {
        // Required empty public constructor
    }

    public static SecondFragmentBasket newInstance() {

        Bundle args = new Bundle();
        SecondFragmentBasket fragment = new SecondFragmentBasket();
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
        v = inflater.inflate(R.layout.fragment_second, container, false);


        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/


        btn_edit_member=v.findViewById(R.id.btn_edit_member);
        editView=v.findViewById(R.id.imageViewEdit);

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            MyFont myFont = new MyFont(getActivity());
            TextView title = v.findViewById(R.id.text_title);
            title.setTypeface(myFont.setFont());

            TextView t5 = v.findViewById(R.id.textView5);
            TextView t7 = v.findViewById(R.id.textView7);
            t7.setTypeface(myFont.setFont());
            t5.setTypeface(myFont.setFont());

            final LayoutInflater finalInflater = inflater;

            tabLayout = getActivity().findViewById(R.id.tablayout);

            int width=getResources().getDisplayMetrics().widthPixels;
            phone_text = v.findViewById(R.id.edittext_phone);
            phone_text.setWidth((width*30)/100);
            name_text = v.findViewById(R.id.editText_Name);
            name_text.setWidth((width*30)/100);
            nickname_text = v.findViewById(R.id.editText_Nickname);
            nickname_text.setWidth((width*30)/100);
            phone_text.setEnabled(false);
            name_text.setEnabled(false);
            nickname_text.setEnabled(false);

            sp2 = getContext().getSharedPreferences("Member", Context.MODE_PRIVATE);
            sp3 = getContext().getSharedPreferences("ID_MEM", Context.MODE_PRIVATE);

            search_text = v.findViewById(R.id.editText_search);
            search_text.setWidth((width*30)/100);
            search_text.requestFocus();


            android.text.format.DateFormat df = new android.text.format.DateFormat();
            date_today = "" + df.format("yyyy-MM-dd", new java.util.Date());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 3);
            Date tomorrow = calendar.getTime();
            tomorrowAsString = "" + df.format("yyyy-MM-dd", tomorrow);

       /* sp1=getContext().getSharedPreferences("Appoint", Context.MODE_PRIVATE);
        sp1.edit().clear().apply();

        SharedPreferences.Editor editor = sp1.edit();
        ArrayList<String> arr_proTH=new ArrayList<>();
        arr_proTH.add("appoint");
        StringBuilder sb = new StringBuilder();
        HashSet<String> mSet = new HashSet<>();
        for (int i = 0; i < arr_proTH.size(); i++) {
            mSet.add("<a>"+tomorrowAsString);
            mSet.add("<b>"+"");
        }
        editor.putStringSet("appoint", mSet);
        editor.apply();*/


            textType = v.findViewById(R.id.textView_type);
            textExp = v.findViewById(R.id.textView_exp);

            textType.setTypeface(myFont.setFont());
            textExp.setTypeface(myFont.setFont());

            phone_text.setTypeface(myFont.setFont());
            name_text.setTypeface(myFont.setFont());
            nickname_text.setTypeface(myFont.setFont());
            search_text.setTypeface(myFont.setFont());


            search_text.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            search_text.setImeActionLabel("ค้นหา", EditorInfo.IME_ACTION_SEARCH);
            search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //Toast.ma003000054keText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
                        if (search_text.getText().toString().trim().isEmpty()) {
                            new MyToast(getActivity(), "ยังไม่มีหมายเลขสำหรับการค้นหา", 0);
                        } else {
                            //method
                            searchMember(search_text.getText().toString());
                        }

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        return true;
                    }
                    return false;
                }
            });
            btn_chk = v.findViewById(R.id.btn_add_customer);
            btn_chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, SecondFragmentBasketCustomer.newInstance());
                    fragmentTransaction.commit();

                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    tab.select();

                }
            });



            //load data
            initData();

            //edit member
            btn_edit_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //String getphone="",getname="",getnickname="",getexp="",gettypemember="",getemail="",getID="";
                    SharedPreferences sharedPreferences1=getContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
                    Map<String,?> entries1 = sharedPreferences1.getAll();
                    Set<String> keys1 = entries1.keySet();
                    String[] getData1;
                    List<String> list1 = new ArrayList<String>(keys1);
                    for (String temp : list1) {
                        //System.out.println(temp+" = "+sharedPreferences1.getStringSet(temp,null));
                        for (int i = 0; i < sharedPreferences1.getStringSet(temp, null).size(); i++) {
                            getData1 = sharedPreferences1.getStringSet(temp, null).toArray(new String[sharedPreferences1.getStringSet(temp, null).size()]);

                            char chk=getData1[i].charAt(1);
                            //System.out.println("Check : " + chk);
                            if(chk=='a'){
                                getphone=getData1[i].substring(3);
                            }else if(chk=='b'){
                                getname=getData1[i].substring(3);
                            }else if(chk=='c'){
                                getnickname=getData1[i].substring(3);
                            }else if(chk=='d'){
                                gettypemember=getData1[i].substring(3);
                            }else if(chk=='e'){
                                getID=getData1[i].substring(3);
                            }else if(chk=='f'){
                                getexp=getData1[i].substring(3);
                            }else if(chk=='g'){
                                getemail=getData1[i].substring(3);
                            }
                        }
                    }

                    final Dialog dialog1 = new Dialog(getActivity());
                    dialog1.setContentView(R.layout.custom_editcustomer);
                    dialog1.setCancelable(false);
                    dialog1.show();
                    Window window = dialog1.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView title = dialog1.findViewById(R.id.tv_quit_learning);
                    final EditText edt_firstname=dialog1.findViewById(R.id.edt_editfirstname);
                    final EditText edt_editlastname=dialog1.findViewById(R.id.edt_editlastnamr);
                    final EditText edt_editnickname=dialog1.findViewById(R.id.edt_editnickname);
                    final EditText edt_editphone=dialog1.findViewById(R.id.edt_editphone);
                    final EditText edt_editemail=dialog1.findViewById(R.id.edt_editemail);
                    final Button b_member =dialog1.findViewById(R.id.btn_edit_member);
                    getTextExp=dialog1.findViewById(R.id.text_exp);
                    getTextExp.setVisibility(View.GONE);

                    String []spits=getname.split(" ");
                    String f=spits[0];
                    String l;
                    try {
                       l =spits[1];
                    }catch (Exception e){
                        l="";
                    }

                    edt_firstname.setText(f);
                    edt_editlastname.setText(l);
                    edt_editnickname.setText(getnickname);
                    edt_editphone.setText(getphone);
                    edt_editemail.setText(getemail);

                    mCalendar = Calendar.getInstance();
                    dtpicker();

                    if(getexp.trim().isEmpty()&&getexp1.trim().isEmpty()){
                        b_member.setText("แก้ไขให้เป็นสมาชิก");
                    }else{
                        b_member.setText("ต่ออายุการเป็นสมาชิก");
                        getTextExp.setVisibility(View.VISIBLE);
                        getTextExp.setText("ครบกำหนดสมาชิก : "+dateThai(getexp));
                    }
                    getMemberType=memberType;
                    b_member.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(getexp.trim().isEmpty()&&getexp1.trim().isEmpty()) {
                                if (getTextExp.getText().toString().length() == 16) {
                                    b_member.setBackground(getResources().getDrawable(R.drawable.button_edit_member_se));
                                    mDatePicker1.setYearRange(2018, 2030);
                                    mDatePicker1.show(getActivity().getSupportFragmentManager(), "datePicker");
                                } else {
                                    new MyToast(getActivity(), "คุณได้เลือกแก้ไขให้เป็นสมาชิกแล้ว", 0);
                                }
                            }else{
                                if (getTextExp.getText().toString().length() == 27) {
                                    b_member.setBackground(getResources().getDrawable(R.drawable.button_edit_member_se));
                                    mDatePicker1.setYearRange(2018, 2030);
                                    mDatePicker1.show(getActivity().getSupportFragmentManager(), "datePicker");
                                } else {
                                    new MyToast(getActivity(), "คุณได้เลือกแก้ไขให้เป็นสมาชิกแล้ว", 0);
                                }
                            }

                        }
                    });

                    title.setText("แก้ไขข้อมูลลูกค้า");
                    Button declineButton = dialog1.findViewById(R.id.btn_cancel);
                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edt_firstname.setText("");
                            edt_editlastname.setText("");
                            edt_editnickname.setText("");
                            edt_editphone.setText("");
                            edt_editemail.setText("");
                            dialog1.dismiss();
                        }
                    });
                    Button okButton = dialog1.findViewById(R.id.btn_ok);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (edt_firstname.getText().toString().isEmpty() || edt_editlastname.getText().toString().isEmpty() ||
                                    edt_editnickname.getText().toString().isEmpty() || edt_editphone.getText().toString().isEmpty()) {
                                new MyToast(getActivity(), "ต้องกรอก ชื่อ,สกุล,ชื่อเล่น,เบอร์โทรศัพท์ให้ครบก่อน", 1);
                            } else {
                                String url=getIPAPI.IPAddress+"/CleanmatePOS/EditMember.php?FirstName="+edt_firstname.getText().toString();
                                url+="&LastName="+edt_editlastname.getText().toString();
                                url+="&NickName="+edt_editnickname.getText().toString();
                                url+="&TelephoneNo="+edt_editphone.getText().toString();
                                url+="&Email="+edt_editemail.getText().toString();
                                url+="&CustomerID="+getID;
                                url+="&Exp="+getExp;
                                url+="&MemberType="+getMemberType;
                                new MyAsyncTask().execute(url,"edit");
                                getsearch=edt_editphone.getText().toString();

                                dialog1.dismiss();
                            }
                        }
                    });
                    MyFont myFont = new MyFont(getActivity());
                    okButton.setTypeface(myFont.setFont());
                    declineButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    edt_firstname.setTypeface(myFont.setFont());
                    edt_editlastname.setTypeface(myFont.setFont());
                    edt_editnickname.setTypeface(myFont.setFont());
                    edt_editphone.setTypeface(myFont.setFont());
                    edt_editemail.setTypeface(myFont.setFont());
                }
            });

            RelativeLayout cancel = v.findViewById(R.id.btn_scan);
            cancel.getLayoutParams().width = (width*45)/100;
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (phone_text.getText().toString().isEmpty() || name_text.getText().toString().isEmpty() ||
                            name_text.getText().toString().isEmpty()) {
                        new MyToast(getActivity(), "ยังไม่มีข้อมูลลูกค้า", 0);
                    } else {
                        //System.out.println("CustomerID 1 : " + id);
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.custon_alert_dialog);
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView title = dialog.findViewById(R.id.tv_quit_learning);
                        TextView des = dialog.findViewById(R.id.tv_description);
                        title.setText("แจ้งเตือน");
                        des.setText("ต้องการยกเลิกข้อมูลลูกค้า");
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
                                clearAll();
                                dialog.dismiss();
                                SharedPreferences sharedPreferences1=getContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
                                sharedPreferences1.edit().clear().apply();
                                initData();
                                new MyToast(getActivity(), "ลบข้อมูลเรียบร้อย", 2);

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

            RelativeLayout next = v.findViewById(R.id.layoutClick_addcart);
            next.getLayoutParams().width = (width*45)/100;
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (name_text.getText().toString().isEmpty()) {
                        showTolltip(name_text, "ยังไม่มีข้อมูลลูกค้า", Gravity.BOTTOM);
                    } else {
                        //sp1.edit().clear().apply();

                   /* SharedPreferences.Editor editor = sp1.edit();
                    ArrayList<String> arr_proTH = new ArrayList<>();
                    arr_proTH.add("appoint");
                    StringBuilder sb = new StringBuilder();
                    HashSet<String> mSet = new HashSet<>();
                    for (int i = 0; i < arr_proTH.size(); i++) {
                        mSet.add("<a>" + tomorrowAsString);
                    }
                    editor.putStringSet("appoint", mSet);
                    editor.apply();*/

                        sp2.edit().clear().apply();
                        int id_mem = 0;

                        Map<String, ?> entries2 = sp3.getAll();
                        Set<String> keys2 = entries2.keySet();
                        String[] getData2;
                        List<String> list2 = new ArrayList<String>(keys2);
                        for (String temp : list2) {
                            //System.out.println("A : "+temp+" = "+sp3.getStringSet(temp,null));
                            for (int k = 0; k < sp3.getStringSet(temp, null).size(); k++) {
                                getData2 = sp3.getStringSet(temp, null).toArray(new String[sp3.getStringSet(temp, null).size()]);
                                char chk = getData2[k].charAt(1);
                                if (chk == 'a') {
                                    id_mem = Integer.parseInt(getData2[k].substring(3));
                                }

                            }
                        }
                        //System.out.println("CustomerID : " + id_mem);

                        SharedPreferences.Editor editor1 = sp2.edit();
                        ArrayList<String> arr_proTH1 = new ArrayList<>();
                        arr_proTH1.add("members");
                        StringBuilder sb1 = new StringBuilder();
                        HashSet<String> mSet1 = new HashSet<>();
                        for (int j = 0; j < arr_proTH1.size(); j++) {
                            mSet1.add("<a>" + phone_text.getText().toString());
                            mSet1.add("<b>" + name_text.getText().toString());
                            mSet1.add("<c>" + nickname_text.getText().toString());
                            mSet1.add("<d>" + memberType);
                            mSet1.add("<e>" + id_mem);
                            mSet1.add("<f>" + textExp.getText().toString().substring(textExp.getText().toString().indexOf(':') + 1, textExp.getText().toString().length()));
                            mSet1.add("<g>" + email);
                        }
                        editor1.putStringSet("members", mSet1);
                        editor1.apply();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, ThirdFragmentBasket.newInstance());
                        fragmentTransaction.commit();

                        TabLayout.Tab tab = tabLayout.getTabAt(2);
                        tab.select();
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
                public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                    dd = "" + day;
                    mm = "" + (month+1);

                    if (dd.length() == 1) {dd4 = "0" + dd;}
                    if (mm.length() == 1) {mm4 = "0" + mm;}
                    if (dd.length() == 2) {dd4 = dd;}
                    if (mm.length() == 2) {mm4 = mm;}
                    getTextExp.setVisibility(View.VISIBLE);
                    getExp=year+"-"+mm4+"-"+dd4;
                    getTextExp.setText("ครบกำหนดสมาชิก : "+dateThai(getExp));
                    getMemberType=""+1;
                }
            };

    public void searchMember(String search){
        String url=getIPAPI.IPAddress+"/CleanmatePOS/SearchMember.php?Data="+search;
        new MyAsyncTask().execute(url,"search");

    }
    private void showTolltip(View anchorView, String msg, int gravity){
        TextView tv=new TextView(getActivity());
        tv.setTextSize(24);
        tv.setBackgroundColor(Color.parseColor("#2C3E50"));
        tv.setTextColor(Color.WHITE);

        new SimpleTooltip.Builder(getActivity())
                .anchorView(anchorView)
                .text(msg)
                .gravity(gravity)
                .modal(true)
                .margin(0.5f)
                .arrowColor(Color.parseColor("#2C3E50"))
                .contentView(tv)
                .build()
                .show();
    }
    private void clearAll(){

        search_text.setText("");
        name_text.setText("");
        nickname_text.setText("");
        phone_text.setText("");
        textType.setText("ประเภทลูกค้า : -");
        textExp.setText("ครบกำหนดสมาชิก : -");
        sp2.edit().clear().apply();
    }

    public void initData(){
        name_text.setText("");
        String getphone="",getname="",getnickname="",getexp="",gettypemember="",getemail="",getID="";
        SharedPreferences sharedPreferences1=getContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
        Map<String,?> entries1 = sharedPreferences1.getAll();
        Set<String> keys1 = entries1.keySet();
        String[] getData1;
        List<String> list1 = new ArrayList<String>(keys1);
        for (String temp : list1) {
            //System.out.println(temp+" = "+sharedPreferences1.getStringSet(temp,null));
            for (int i = 0; i < sharedPreferences1.getStringSet(temp, null).size(); i++) {
                getData1 = sharedPreferences1.getStringSet(temp, null).toArray(new String[sharedPreferences1.getStringSet(temp, null).size()]);

                char chk=getData1[i].charAt(1);
                //System.out.println("Check : " + chk);
                if(chk=='a'){
                    getphone=getData1[i].substring(3);
                }else if(chk=='b'){
                    getname=getData1[i].substring(3);
                }else if(chk=='c'){
                    getnickname=getData1[i].substring(3);
                }else if(chk=='d'){
                    gettypemember=getData1[i].substring(3);
                }else if(chk=='f'){
                    getexp=getData1[i].substring(3);
                }else if(chk=='g'){
                    getemail=getData1[i].substring(3);
                }
            }
        }
        //search_text.setText(phone);
        if(getname.isEmpty()){
            btn_edit_member.setVisibility(View.GONE);
            editView.setVisibility(View.GONE);
        }else{
            btn_edit_member.setVisibility(View.VISIBLE);
            editView.setVisibility(View.VISIBLE);
        }

        //System.out.println("type member "+gettypemember);
        name_text.setText(getname);
        nickname_text.setText(getnickname);
        phone_text.setText(getphone);
        memberType=gettypemember;
        String s="";
        if(!gettypemember.isEmpty()) {
            if (Integer.parseInt(gettypemember.trim()) == 1) {
                s = "สมาชิก";
            } else if (Integer.parseInt(gettypemember.trim()) == 0) {
                s = "ลูกค้าทั่วไป";
            }
        }
        textType.setText("ประเภทลูกค้า : "+s);
        getexp1=getexp;
        System.out.println(getexp);
        if(getexp1.trim().isEmpty()){
            textExp.setText("ครบกำหนดสมาชิก : "+getexp1);
        }else{
            textExp.setText("ครบกำหนดสมาชิก : "+dateThai(getexp1));
        }


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
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
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
        protected String doInBackground(String... strings) {

            typeservice = strings[1];
            String response = "";
            String output = "";

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
            if (strings[1].equals("search")) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    output=""+jsonArray.length();
                    //System.out.println("output : "+jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        id = jsonObject.optString("CustomerID");

                        try {
                            name = jsonObject.optString("FirstName") + " " + jsonObject.optString("LastName");
                            //System.out.println(name);
                        } catch (Exception ex) {
                            name = "";
                        }
                        try {
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("MemberExpirationDate"));

                            nickname = jsonObject.optString("NickName");
                            memberType = jsonObject.optString("MemberTypeID");
                            phone = jsonObject.optString("TelephoneNo");
                            exp = jsonObject1.getString("date");
                            email = jsonObject.optString("Email");
                        } catch (Exception ex) {
                            nickname = jsonObject.optString("NickName");
                            memberType = jsonObject.optString("MemberTypeID");
                            phone = jsonObject.optString("TelephoneNo");
                            exp = "";
                            email = jsonObject.optString("Email");
                        }

                    }
                    /*} else {
                        btn_edit_member=v.findViewById(R.id.btn_edit_member);
                        editView=v.findViewById(R.id.imageViewEdit);
                        btn_edit_member.setVisibility(View.GONE);
                        editView.setVisibility(View.GONE);
                        id = "";
                        new MyToast(getActivity(), "ไม่พบข้อมูลลูกค้า", 0);
                        name_text.setText("");
                        nickname_text.setText("");
                        phone_text.setText("");
                        textType.setText("ประเภทลูกค้า : ");
                        textExp.setText("ครบกำหนดสมาชิก : ");
                    }*/
                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex);
                }
            } else if (strings[1].equals("edit")) {

                try {
                    new MyToast(getActivity(), response, 1);
                    System.out.println("response : " + response);


                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex);
                }
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if (typeservice.equals("edit")) {
                searchMember(getsearch);
            } else if (typeservice.equals("search")) {
                if (Integer.parseInt(s) == 0) {
                    btn_edit_member.setVisibility(View.GONE);
                    editView.setVisibility(View.GONE);
                    id = "";
                    new MyToast(getActivity(), "ไม่พบข้อมูลลูกค้า", 0);
                    name_text.setText("");
                    nickname_text.setText("");
                    phone_text.setText("");
                    textType.setText("ประเภทลูกค้า : ");
                    textExp.setText("ครบกำหนดสมาชิก : ");
                } else {
                    btn_edit_member.setVisibility(View.VISIBLE);
                    editView.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor editor = sp3.edit();
                    HashSet<String> mSet = new HashSet<>();
                    mSet.add("<a>" + id);
                    editor.putStringSet("ID_MEM", mSet);
                    editor.apply();

                    if (Integer.parseInt(memberType.trim()) == 1) {
                        textType.setText("ประเภทลูกค้า : ลูกค้าสมาชิก");
                        textExp.setText("ครบกำหนดสมาชิก : " + dateThai(exp));
                    } else if (Integer.parseInt(memberType.trim()) == 0) {
                        textType.setText("ประเภทลูกค้า : ลูกค้าทั่วไป");
                        textExp.setText("ครบกำหนดสมาชิก : -");
                    } else {
                        //dialog.dismiss();
                    }

                    if (name.trim().isEmpty()) {
                        name_text.setHint("ชื่อ - สกุล");
                        name_text.setText("");
                    } else {
                        name_text.setText(name);
                    }
                    nickname_text.setText(nickname);
                    phone_text.setText(phone);

                    SharedPreferences.Editor editor1 = sp2.edit();
                    ArrayList<String> arr_proTH1 = new ArrayList<>();
                    arr_proTH1.add("members");
                    StringBuilder sb1 = new StringBuilder();
                    HashSet<String> mSet1 = new HashSet<>();
                    for (int j = 0; j < arr_proTH1.size(); j++) {
                        mSet1.add("<a>" + phone);
                        mSet1.add("<b>" + name);
                        mSet1.add("<c>" + nickname);
                        mSet1.add("<d>" + memberType);
                        mSet1.add("<e>" + id);
                        mSet1.add("<f>" + exp);
                        mSet1.add("<g>" + email);
                    }
                    editor1.putStringSet("members", mSet1);
                    editor1.apply();
                }
            }

        }
    }
}
