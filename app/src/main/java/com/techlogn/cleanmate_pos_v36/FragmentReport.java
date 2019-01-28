package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by anucha on 5/8/2018.
 */

public class FragmentReport extends Fragment {

    private View myView;
    private ListView mListView;
    private TextView textToday,textMounth;
    //private ArrayList<CustomItemReport>mListReport;
    private LinearLayout layout_today,layoutmounth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_report, container, false);


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/


        MyFont myFont=new MyFont(getActivity());
        textToday=myView.findViewById(R.id.textDay);
        //textMounth=myView.findViewById(R.id.textMont);
        textToday.setTypeface(myFont.setFont());
        //textMounth.setTypeface(myFont.setFont());

        layout_today=myView.findViewById(R.id.layout_today);
        //layoutmounth=myView.findViewById(R.id.layout_mounth);

        layout_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentReportDetail fragmentReportDetail = new FragmentReportDetail();
                //fragmentReportDetail.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentReportDetail)
                        .commit();
            }
        });

       /* layoutmounth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyToast(getActivity(),"ยังไม่มีรายงาน",2);
                *//*FragmentCancelOrder fragmentCancelOrder = new FragmentCancelOrder();
                //fragmentCancelOrder.setArguments(edt);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,fragmentCancelOrder)
                        .commit();*//*
            }
        });*/

        /*mListReport=new ArrayList<>();
        mListReport.add(new CustomItemReport("รายงานการขายประจำวัน (สุทธิ)"));
        mListReport.add(new CustomItemReport("รายงานการขายประจำเดือน (สุทธิ)"));
        mListView=myView.findViewById(R.id.listReport);

        MyAdapterReport myAdapter = new MyAdapterReport(getActivity(), R.layout.list_report,mListReport);
        mListView.setAdapter(myAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("index", ""+position);
                FragmentReportDetail fragmentReportDetail = new FragmentReportDetail();
                fragmentReportDetail.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentReportDetail)
                        .commit();
            }
        });
*/
        return myView;
    }
}
