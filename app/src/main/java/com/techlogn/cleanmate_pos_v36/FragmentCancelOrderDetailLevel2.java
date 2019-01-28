package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by anucha on 1/3/2018.
 */

public class FragmentCancelOrderDetailLevel2 extends Fragment {

    View myView;
    TextView Text1,Text2,Text,TextTitle;
    MyFont myFont;
    ArrayList<MyItemCancelOrderdetailLevel2> items;
    String dataID,branchID,orderNo,productID,productName;

    private GetIPAPI getIPAPI;

    private SwipeMenuListView mSwipeListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_cancel_orederdetail_level2, container, false);


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/

        getIPAPI=new GetIPAPI();


        myFont=new MyFont(getActivity());
        items=new ArrayList<>();
        TextTitle=myView.findViewById(R.id.textTitle);
        Text=myView.findViewById(R.id.textTitle5);
        Text1=myView.findViewById(R.id.textTitle6);
        Text2=myView.findViewById(R.id.textTitle7);
        Text.setTypeface(myFont.setFont());
        Text1.setTypeface(myFont.setFont());
        Text2.setTypeface(myFont.setFont());
        TextTitle.setTypeface(myFont.setFont());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderNo = bundle.getString("orderNo");
            productID=bundle.getString("productID");
            productName=bundle.getString("productName");
        }

        TextTitle.setText(productName);

        TextView text5=myView.findViewById(R.id.textView5);
        text5.setTypeface(myFont.setFont());

        RelativeLayout btn_back=myView.findViewById(R.id.btn_scan);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("orderNo", ""+orderNo);
                FragmentCancelOrderDetail fragmentCancelOrderDetail = new FragmentCancelOrderDetail();
                fragmentCancelOrderDetail.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentCancelOrderDetail)
                        .commit();
            }
        });
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

        mSwipeListView = myView.findViewById(R.id.listView);

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setIcon(R.mipmap.loading);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กรุณารอสักครู่....");
            dialog.setIndeterminate(true);
            dialog.show();
            Ion.with(getActivity())
                    .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/CancelOrderDetailProductList.php")
                    .setBodyParameter("OrderNo", ""+orderNo)
                    .setBodyParameter("ProductID", productID)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            dialog.dismiss();
                            int index;
                            if(result.size()==0){
                                new MyToast(getActivity(),"ข้อมูลยังไม่ถูกจัดการ",2);
                            }else {
                                for (int i = 0; i < result.size(); i++) {

                                    JsonObject jsonObject = (JsonObject) result.get(i);
                                    index = i + 1;
                                    items.add(new MyItemCancelOrderdetailLevel2("" + index, jsonObject.get("Barcode").getAsString()));

                                }


                                MyAdapterCancelOrderdetailLevel2 myAdapter = new MyAdapterCancelOrderdetailLevel2(getActivity(), R.layout.list_cancel_orderdetail_level2, items);
                                mSwipeListView.setAdapter(myAdapter);

                                mSwipeListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

                                createSwipeMenu();

                                mSwipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        mSwipeListView.smoothOpenMenu(i);
                                    }
                                });
                            }


                        }
                    });

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
}
