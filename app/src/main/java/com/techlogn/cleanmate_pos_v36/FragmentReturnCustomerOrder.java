package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anucha on 1/3/2018.
 */

public class FragmentReturnCustomerOrder extends Fragment {

    private View myView;
    private TextView Text1,Text2,TextTitle,Text3,Text5;
    private MyFont myFont;
    private ArrayList<MyItemReturnCustomerList> items;
    private String dataID,branchID,orderNo,key;
    private ListView listView;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    public FragmentReturnCustomerOrder(ArrayList<MyItemReturnCustomerList> items){
        this.items=items;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_return_customer_order, container, false);


        //Log
       /* Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/



        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            myFont = new MyFont(getActivity());
            TextTitle = myView.findViewById(R.id.textTitle);
            Text1 = myView.findViewById(R.id.textTitle1);
            Text2 = myView.findViewById(R.id.textTitle2);
            Text2.setText("เลขที่ใบรับผ้า");
            Text3 = myView.findViewById(R.id.textTitle3);
            Text1.setTypeface(myFont.setFont());
            Text2.setTypeface(myFont.setFont());
            Text3.setTypeface(myFont.setFont());
            TextTitle.setTypeface(myFont.setFont());

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
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                key = bundle.getString("key");
            }

            listView = myView.findViewById(R.id.list);
            MyAdapterReturnCustomerList myAdapter = new MyAdapterReturnCustomerList(getActivity(), R.layout.list_return_customer, items);
            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNo", "" + items.get(i).getOrderNo());
                    bundle.putString("key", "" + key);
                    FragmentReturnCustomerList fragmentReturnCustomer = new FragmentReturnCustomerList();
                    fragmentReturnCustomer.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentReturnCustomer)
                            .commit();
                }
            });

            RelativeLayout back = myView.findViewById(R.id.btn_scan);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentReturnCustomer fragmentReturnCustomer = new FragmentReturnCustomer();
                    getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentReturnCustomer)
                            .commit();

                }
            });

        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return myView;
    }
}
