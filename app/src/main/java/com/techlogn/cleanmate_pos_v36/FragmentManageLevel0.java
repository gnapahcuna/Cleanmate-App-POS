package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by anucha on 1/3/2018.
 */

public class FragmentManageLevel0 extends Fragment {

    private View myView;
    private MyFont myFont;
    private CardView layout_map, layout_cancel, layout_order_in, layout_return_fac;
    private EditText edt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_manage_level0, container, false);
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


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/



        Bundle bundle = this.getArguments();
        if (bundle != null) {
           if(bundle.get("IsReturnFactory").equals("ReturnFactory")){
               FragmentReturnFactory fragmentReturnFactory = new FragmentReturnFactory();
               //fragmentReturnFactory.setArguments(edt);
               getFragmentManager()
                       .beginTransaction()
                       .replace(R.id.content_frame, fragmentReturnFactory)
                       .commit();
           }else if(bundle.get("IsReturnFactory").equals("Manage")){
               Bundle bundle1 = new Bundle();
               bundle1.putString("IsSearch","1");
               bundle1.putString("searchtext",bundle.get("searchtext").toString());
               FragmentManageLevel1Search fragmentManageLevel1Search = new FragmentManageLevel1Search();
               fragmentManageLevel1Search.setArguments(bundle1);
               getFragmentManager()
                       .beginTransaction()
                       .replace(R.id.content_frame, fragmentManageLevel1Search)
                       .commit();
           }
        }

        myFont = new MyFont(getActivity());
        TextView textMap = myView.findViewById(R.id.textMap);
        TextView textCancel = myView.findViewById(R.id.textCancel);
        //TextView textOrderIn=myView.findViewById(R.id.textOrderIn);
        TextView textReturnFactory = myView.findViewById(R.id.textReturnFactory);
        textMap.setTypeface(myFont.setFont());
        textCancel.setTypeface(myFont.setFont());
        //textOrderIn.setTypeface(myFont.setFont());
        textReturnFactory.setTypeface(myFont.setFont());

        layout_map = myView.findViewById(R.id.layout_today);
        layout_cancel = myView.findViewById(R.id.layout_mounth);
        //layout_order_in=myView.findViewById(R.id.layout_order_in);
        layout_return_fac = myView.findViewById(R.id.layout_return_fac);

        layout_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                FragmentManageLevel1Search fragmentManageLevel1 = new FragmentManageLevel1Search();
                //fragmentManageLevel1.setArguments(edt);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel1)
                        .commit();
            }
        });

        layout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentCancel fragmentCancel = new FragmentCancel();
                //fragmentCancelOrder.setArguments(edt);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentCancel)
                        .commit();
            }
        });

        /*layout_order_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentReuturnStore fragmentReuturnStore = new FragmentReuturnStore();
                //fragmentReuturnStore.setArguments(edt);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,fragmentReuturnStore)
                        .commit();
            }
        });*/

        layout_return_fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentReturnFactory fragmentReturnFactory = new FragmentReturnFactory();
                //fragmentReturnFactory.setArguments(edt);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentReturnFactory)
                        .commit();
            }
        });

        return myView;
    }

    /*public void setArguments(EditText edit) {
        try {
            edt = edit;
            edit.setHint("\tค้นหารานการ map สินค้า");
        } catch (Exception ex) {

        }
    }*/
}
