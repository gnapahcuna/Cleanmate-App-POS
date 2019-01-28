package com.techlogn.cleanmate_pos_v36;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by anucha on 3/8/2018.
 */

public class SecondFragmentBasketCustomer extends Fragment {

    View v;
    EditText firstname,lastname,nickname,phone,email,exp_date;
    Spinner title;
    String t,f,l,n,p,e,r,c="",exp;

    String date_ex="",dates;
    int a=0;
    SpinnerAdapter adapter;
    TabLayout tabLayout;

    CheckBox chk1,chk2;
    ArrayList<CheckBox>arrCheck;
    int isMemberType=0;

    private Calendar mCalendar;
    private com.fourmob.datetimepicker.date.DatePickerDialog mDatePicker1;
    private String Datetime1;
    String dd="",mm="",dd4="",mm4="";
    String tomorrowAsString;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private GetIPAPI getIPAPI;

    ProgressDialog dialog;

    public SecondFragmentBasketCustomer() {
    }

    public static SecondFragmentBasketCustomer newInstance() {

        Bundle args = new Bundle();
        SecondFragmentBasketCustomer fragment = new SecondFragmentBasketCustomer();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_regis_member, container, false);

        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            MyFont myFont = new MyFont(getActivity());
            TextView t5 = v.findViewById(R.id.textView5);
            TextView t3 = v.findViewById(R.id.textView3);
            TextView t4 = v.findViewById(R.id.textView4);
            TextView title2 = v.findViewById(R.id.text_title2);

            t5.setTypeface(myFont.setFont());
            t3.setTypeface(myFont.setFont());
            t4.setTypeface(myFont.setFont());
            title2.setTypeface(myFont.setFont());


            tabLayout = getActivity().findViewById(R.id.tablayout);

            firstname = v.findViewById(R.id.edt_firstname);
            lastname = v.findViewById(R.id.edt_latname);
            nickname = v.findViewById(R.id.edt_nickname);
            phone = v.findViewById(R.id.edt_phone);
            email = v.findViewById(R.id.edt_email);
            title = v.findViewById(R.id.spinner_title_name);
            exp_date = v.findViewById(R.id.edt_ExpDate);

            firstname.setTypeface(myFont.setFont());
            lastname.setTypeface(myFont.setFont());
            nickname.setTypeface(myFont.setFont());
            phone.setTypeface(myFont.setFont());
            email.setTypeface(myFont.setFont());
            exp_date.setTypeface(myFont.setFont());


            firstname.setInputType(InputType.TYPE_CLASS_TEXT);
            firstname.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            lastname.setInputType(InputType.TYPE_CLASS_TEXT);
            lastname.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            nickname.setInputType(InputType.TYPE_CLASS_TEXT);
            nickname.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            phone.setInputType(InputType.TYPE_CLASS_PHONE);
            phone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            email.setInputType(InputType.TYPE_CLASS_TEXT);
            email.setImeOptions(EditorInfo.IME_ACTION_NEXT);


            String[] list = new String[]{"นาย", "นาง", "นางสาว", "ไม่ระบุ"};
            adapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item, list);
            adapter.addAll(list);
            adapter.add("--เลือกคำนำหน้า--");
            title.setAdapter(adapter);
            title.setSelection(adapter.getCount());


            chk1 = v.findViewById(R.id.checkBox1);
            chk2 = v.findViewById(R.id.checkBox2);

            chk1.setTypeface(myFont.setFont());
            chk2.setTypeface(myFont.setFont());


            arrCheck = new ArrayList<>();
            arrCheck.add(chk1);
            arrCheck.add(chk2);

            mCalendar = Calendar.getInstance();
            dtpicker();

            for (CheckBox chk : arrCheck
                    ) {
                chk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.checkBox1) {
                            isMemberType = 1;
                            chk2.setChecked(false);
                            exp_date.requestFocus();
                            mDatePicker1.setYearRange(2018, 2030);
                            mDatePicker1.show(getActivity().getSupportFragmentManager(), "datePicker");
                        } else if (v.getId() == R.id.checkBox2) {
                            isMemberType = 0;
                            chk1.setChecked(false);
                            email.requestFocus();
                            exp_date.setText("");
                        }
                    }
                });
            }


            android.text.format.DateFormat df = new android.text.format.DateFormat();
            dates = "" + df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());
            String dt = dates;  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar ca = Calendar.getInstance();
            try {
                ca.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ca.add(Calendar.DATE, 365);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            date_ex = sdf1.format(ca.getTime());

            android.text.format.DateFormat df_create = new android.text.format.DateFormat();
            String dates_create = "" + df_create.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());
            String dt_create = dates_create;  // Start date
            SimpleDateFormat sdf_create = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar cc = Calendar.getInstance();
            try {
                cc.setTime(sdf_create.parse(dt_create));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cc.add(Calendar.DATE, 365);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf_create1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            date_ex = sdf_create1.format(cc.getTime());

            LinearLayout accept = v.findViewById(R.id.layoutClick_addcart);
            int width =getResources().getDisplayMetrics().widthPixels;
            accept.getLayoutParams().width = (width*45)/100;
            RelativeLayout cancel = v.findViewById(R.id.btn_scan);
            cancel.getLayoutParams().width = (width*45)/100;
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t = "" + title.getSelectedItemPosition();
                    f = firstname.getText().toString();
                    l = lastname.getText().toString();
                    n = nickname.getText().toString();
                    p = phone.getText().toString();
                    e = email.getText().toString();
                    if (f.isEmpty() && l.isEmpty() && n.isEmpty() && p.isEmpty()) {
                        new MyToast(getActivity(), "ยังไม่มีข้อมูลลูค้า", 0);
                    } else {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.custon_alert_dialog);
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView title = dialog.findViewById(R.id.tv_quit_learning);
                        TextView des = dialog.findViewById(R.id.tv_description);
                        title.setText("แจ้งเตือน");
                        des.setText("ยกเลิกการทำรายการนี้");
                        Button okButton = dialog.findViewById(R.id.btn_ok);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                firstname.setText("");
                                lastname.setText("");
                                nickname.setText("");
                                phone.setText("");
                                email.setText("");
                                exp_date.setText("");
                                chk1.setChecked(false);
                                chk2.setChecked(false);
                                new MyToast(getActivity(), "ยกเลิกข้อมูลเรียบร้อย", 1);
                                dialog.dismiss();
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
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    t = "" + title.getSelectedItemPosition();
                    f = firstname.getText().toString();
                    l = lastname.getText().toString();
                    n = nickname.getText().toString();
                    p = phone.getText().toString();
                    e = email.getText().toString();

                    exp = exp_date.getText().toString();

                    if (f.isEmpty() && l.isEmpty() && n.isEmpty() && p.isEmpty()) {
                        new MyToast(getActivity(), "กรุณากรอกข้อมูลในช่อง ชื่อ, สกุล, ชื่อเล่น, เบอร์โทรศัพท์", 0);

                    } else if (t.endsWith("4")) {
                        new MyToast(getActivity(), "กรุณาเลือกคำนำหน้า", 0);
                    } else if (p.length() < 9) {
                        new MyToast(getActivity(), "ใส่หมายเลขโทรศัพท์ไม่ถูกต้อง", 0);
                    } else {
                        if (isMemberType == 0) {
                            register(isMemberType, "", "", "", getActivity());

                        } else if (isMemberType == 1) {
                            register(isMemberType, dates, exp, p, getActivity());
                        }

                        /*SharedPreferences sp1 = getContext().getSharedPreferences("Appoint", Context.MODE_PRIVATE);
                        sp1.edit().clear().apply();
                        android.text.format.DateFormat df = new android.text.format.DateFormat();
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_YEAR, 3);
                        Date tomorrow = calendar.getTime();
                        tomorrowAsString = "" + df.format("yyyy-MM-dd", tomorrow);

                        sp1 = getContext().getSharedPreferences("Appoint", Context.MODE_PRIVATE);
                        sp1.edit().clear().apply();

                        SharedPreferences.Editor editor = sp1.edit();
                        ArrayList<String> arr_proTH = new ArrayList<>();
                        arr_proTH.add("appoint");
                        StringBuilder sb = new StringBuilder();
                        HashSet<String> mSet = new HashSet<>();
                        for (int i = 0; i < arr_proTH.size(); i++) {
                            mSet.add("<a>" + tomorrowAsString);
                            mSet.add("<b>" + "");
                        }
                        editor.putStringSet("appoint", mSet);
                        editor.apply();*/


                        int id_mem = 0;
                        SharedPreferences sp2 = getContext().getSharedPreferences("Member", Context.MODE_PRIVATE);
                        SharedPreferences sp3 = getContext().getSharedPreferences("ID_MEM", Context.MODE_PRIVATE);
                            /*Map<String, ?> entries2 = sp3.getAll();
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
                                        System.out.println("ID Member : " + id_mem);
                                    }

                                }
                            }*/
                        SharedPreferences.Editor editor1 = sp2.edit();
                        ArrayList<String> arr_proTH1 = new ArrayList<>();
                        arr_proTH1.add("members");
                        StringBuilder sb1 = new StringBuilder();
                        HashSet<String> mSet1 = new HashSet<>();
                        for (int j = 0; j < arr_proTH1.size(); j++) {
                            mSet1.add("<a>" + p);
                            mSet1.add("<b>" + f + " " + l);
                            mSet1.add("<c>" + n);
                            mSet1.add("<d>" + isMemberType);
                            mSet1.add("<e>" + id_mem);
                        }
                        editor1.putStringSet("members", mSet1);
                        editor1.apply();


                    }


                /*final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                TextView title1=dialog.findViewById(R.id.tv_quit_learning);
                TextView des=dialog.findViewById(R.id.tv_description);
                title1.setText("แจ้งเตือน");
                des.setText("ลูกค้าท่านต้องการลงทะเบียนแบบสมาชิกหรือไม่");
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                declineButton.setText("ไม่ต้องการ");
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        t = "" + title.getSelectedItemPosition();
                        f = firstname.getText().toString();
                        l = lastname.getText().toString();
                        n = nickname.getText().toString();
                        p = phone.getText().toString();
                        e = email.getText().toString();
                        if (f.isEmpty() && l.isEmpty() && n.isEmpty() && p.isEmpty()) {
                            new MyToast(getActivity(),"กรุณากรอกข้อมูลในช่อง ชื่อ, สกุล, ชื่อเล่น, เบอร์โทรศัพท์",0);

                        }else if(t.endsWith("4")){
                            new MyToast(getActivity(),"กรุณาเลือกคำนำหน้า",0);
                        }
                        else {
                            register(1,"","","");
                            dialog.dismiss();
                        }

                    }
                });
                Button okButton =  dialog.findViewById(R.id.btn_ok);
                okButton.setText("ต้องการ");
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        t = "" + title.getSelectedItemPosition();
                        f = firstname.getText().toString();
                        l = lastname.getText().toString();
                        n = nickname.getText().toString();
                        p = phone.getText().toString();
                        e = email.getText().toString();
                        if (f.isEmpty() && l.isEmpty() && n.isEmpty() && p.isEmpty()) {
                            new MyToast(getActivity(),"กรุณากรอกข้อมูลในช่อง ชื่อ, สกุล, ชื่อเล่น, เบอร์โทรศัพท์",0);

                        }else if(t.endsWith("4")){
                            new MyToast(getActivity(),"กรุณาเลือกคำนำหน้า",0);
                        }
                        else {
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_member);
                            dialog.show();
                            TextView title1=dialog.findViewById(R.id.tv_quit_learning);
                            title1.setText("ลงทะเบียนสมาชิก");
                            Button declineButton = dialog.findViewById(R.id.btn_cancel);
                            Button okButton =  dialog.findViewById(R.id.btn_ok);
                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText code=dialog.findViewById(R.id.text_code);
                                    String getCode=code.getText().toString();
                                    register(0,dates,date_ex,getCode);
                                    dialog.dismiss();
                                }
                            });
                            declineButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        }

                    }
                });*/

                }
            });
        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return v;
    }
    public void register(int type, String start, String end, String code, final Context context){

        t = "" + title.getSelectedItemPosition();
        f = firstname.getText().toString();
        l = lastname.getText().toString();
        n = nickname.getText().toString();
        p = phone.getText().toString();
        e = email.getText().toString();

        if(type==0){
            String url=getIPAPI.IPAddress+"/CleanmatePOS/Register_No_Member.php?Data1="+t;
            url+="&Data2="+f;
            url+="&Data3="+l;
            url+="&Data4="+n;
            url+="&Data5="+p;
            url+="&Data6="+e;
            url+="&Data7="+type;
            url+="&Data8="+c;
            url+="&Data9="+dates;
            new MyAsyncTask().execute(url);

        }else{
            String url=getIPAPI.IPAddress+"/CleanmatePOS/Register.php?Data1="+t;
            url+="&Data2="+f;
            url+="&Data3="+l;
            url+="&Data4="+n;
            url+="&Data5="+p;
            url+="&Data6="+e;
            url+="&Data7="+type;
            url+="&Data8="+c;
            url+="&Data9="+dates;
            url+="&Data10="+start;
            url+="&Data11="+end;
            url+="&Data12="+code;
            new MyAsyncTask().execute(url);

        }


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
                    exp_date.setText(year+"-"+mm4+"-"+dd4);
                }
            };

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
            try {

                System.out.println(response);
                output=response;
                //new MyToast(getActivity(),response,1);
                /*JSONObject jsonObject1=new JSONObject(response);
                out+=jsonObject1.optString("firstname");
                JSONArray jsonArray=new JSONArray(response1);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    out += jsonObject.optString("OrderNo");
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
            System.out.println(s);
            //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            if (s.endsWith("#1")) {
                new MyToast(getActivity(),"หมายเลขโทรศัพท์นี้ถูกลงทะเบียนแล้ว",0);
            }else if(s.endsWith("#0")){
                new MyToast(getActivity(),"เกิดข้อผิดพลาด, ไม่สามารถทำรายการได้",0);
            }
            else if(s.equals("สมัครสมาชิกเรียบร้อยแล้ว")||s.equals("\uFEFFสมัครสมาชิกเรียบร้อยแล้ว")){
                new MyToast(getActivity(),s,2);

                title.setSelection(adapter.getCount());
                firstname.setText("");
                lastname.setText("");
                nickname.setText("");
                email.setText("");
                phone.setText("");

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, ThirdFragmentBasket.newInstance());
                fragmentTransaction.commit();
                TabLayout.Tab tab = tabLayout.getTabAt(2);
                tab.select();
            }else{
                new MyToast(getActivity(),"เกิดข้อผิดพลาด บันทึกข้อมูลไม่สำเร็จ",0);
            }
        }
    }

}
