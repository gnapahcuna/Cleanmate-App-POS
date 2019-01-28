package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by anucha on 1/3/2018.
 */

public class FragmentManageLevel1 extends Fragment {

    private View myView;
    private ArrayList<MyItemFactoryOrder> items;
    private TextView Text1,Text2,Text,Text3,Text4;
    private MyFont myFont;
    private String dataID,branchID,key,mBranchID;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private EditText edit_order;
    private Bundle bundle;
    private Context mContext;

    private ImageView searchOrder;
    private  String searchtext,type;

    public FragmentManageLevel1(){
    }
    public FragmentManageLevel1(ArrayList<MyItemFactoryOrder> items){

        this.items=items;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_manage_level1, container, false);


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/
        LinearLayout ln_frag3=myView.findViewById(R.id.ln_frag3);
        LinearLayout.LayoutParams rl =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        final int height=getResources().getDisplayMetrics().heightPixels;
        rl.setMargins(0, 0, 0, 0);
        ln_frag3.setLayoutParams(rl);
        ln_frag3.getLayoutParams().height=(height*60)/100;


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {


            //items = new ArrayList<>();
            myFont = new MyFont(getActivity());
            Text = myView.findViewById(R.id.textTitle);
            //Text.setText("เลขที่ใบรับผ้า");
            Text1 = myView.findViewById(R.id.textTitle1);
            Text2 = myView.findViewById(R.id.textTitle2);
            Text2.setText("เลขที่ใบรับผ้า");
            Text3 = myView.findViewById(R.id.textTitle3);
            Text4 = myView.findViewById(R.id.textTitle4);
            Text.setTypeface(myFont.setFont());
            Text1.setTypeface(myFont.setFont());
            Text2.setTypeface(myFont.setFont());
            Text3.setTypeface(myFont.setFont());
            Text4.setTypeface(myFont.setFont());

            TextView Text5 = myView.findViewById(R.id.textView5);
            Text5.setTypeface(myFont.setFont());

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


            ListView listView = myView.findViewById(R.id.list);
            LinearLayout.LayoutParams lv =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            lv.setMargins(0, 0, 0, 0);
            listView.setLayoutParams(lv);
            listView.getLayoutParams().height=(height*60)/100;

            MyAdapterFactoryOrder myAdapter = new MyAdapterFactoryOrder(getActivity(), R.layout.factory_orderlist, items, branchID,searchtext,type);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView temp = view.findViewById(R.id.orderNo);
                    String orderNo = temp.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNo", orderNo);

                    FragmentManageLevel2 fragment2_1 = new FragmentManageLevel2();
                    fragment2_1.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment2_1)
                            .commit();



                }
            });
            listView.setAdapter(myAdapter);

            RelativeLayout back = myView.findViewById(R.id.btn_scan);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManageLevel1Search fragmentManageLevel1 = new FragmentManageLevel1Search();
                    //fragmentManageLevel1.setArguments(edit);
                    getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentManageLevel1)
                            .commit();

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

    /*public void setArguments(EditText edt) {
        edit=edt;
        edt.setHint("\tค้นหารานการ map สินค้า");
    }*/
}
