package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragmentCheckStatusOrderList extends Fragment {

    View myView;
    TextView Text1,Text2,Text,Text3,Text4;
    MyFont myFont;
    ArrayList<CustomItemFactoryOrderList> items;
    String dataID,branchID;

    private SwipeMenuListView mSwipeListView;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private  String key;
    public FragmentCheckStatusOrderList(ArrayList<CustomItemFactoryOrderList> items){
        this.items=items;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/



        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            myFont = new MyFont(getActivity());
            //items = new ArrayList<>();
            myView = inflater.inflate(R.layout.fragment3_order_list, container, false);

            Text = myView.findViewById(R.id.textTitle);
            Text2 = myView.findViewById(R.id.textTitle2);
            Text2.setText("เลขที่ใบรับผ้า");
            Text3 = myView.findViewById(R.id.textTitle3);
            Text4 = myView.findViewById(R.id.textTitle4);
            Text.setTypeface(myFont.setFont());
            Text2.setTypeface(myFont.setFont());
            Text3.setTypeface(myFont.setFont());
            Text4.setTypeface(myFont.setFont());

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
            RelativeLayout back = myView.findViewById(R.id.btn_scan);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentCheckStatusOrder fragmentCheckStatusOrder = new FragmentCheckStatusOrder();
                    getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentCheckStatusOrder)
                            .commit();

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

            MyAdapterFactoryOrder1 myAdapter = new MyAdapterFactoryOrder1(getActivity(), R.layout.factory_orderlist1, items, branchID, dataID);
            mSwipeListView = myView.findViewById(R.id.listView);
            mSwipeListView.setAdapter(myAdapter);

        } else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return myView;
    }

}
