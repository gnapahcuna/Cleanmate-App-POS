package com.techlogn.cleanmate_pos_v36;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by anucha on 2/11/2018.
 */

public class FragmentSearchFactory extends Fragment {

    View myView;
    ArrayList<MyItemSearchFactory> items;
    String orderNo;
    TextView t,Text1,Text2;
    MyFont myFont;
    String dataSearch,dataID,branchID;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_search_factory, container, false);


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/

        getIPAPI=new GetIPAPI();


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            items = new ArrayList<>();
            t = myView.findViewById(R.id.text_orderno);
            myFont = new MyFont(getActivity());
            Text1 = myView.findViewById(R.id.textTitle1);
            Text2 = myView.findViewById(R.id.textTitle2);
            Text1.setTypeface(myFont.setFont());
            Text2.setTypeface(myFont.setFont());

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                dataSearch = bundle.getString("key");
                dataID = bundle.getString("ID");
                branchID = bundle.getString("branchID");
            }


            myView.setFocusableInTouchMode(true);
            myView.requestFocus();
            myView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            Intent intent = new Intent(getActivity(), MenuActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            return true;
                        }
                    }
                    return false;
                }
            });

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setIcon(R.mipmap.loading);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กรุณารอสักครู่....");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            Ion.with(getActivity())
                    .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/searchFactory.php")
                    .setBodyParameter("data", dataSearch)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            dialog.dismiss();
                            try {


                                int index, num;
                                TextView textView = myView.findViewById(R.id.textSearch);
                                if (dataSearch.isEmpty()) {
                                    textView.setText("\u2315 ค้นหา \u1366 " + "ไม่มีคำค้นหา");
                                } else {
                                    //textView.setText("\u2315 ค้นหาจาก \u1366 "+dataSearch);
                                    textView.setVisibility(View.GONE);
                                    String orderNo = "";
                                    if (result.size() == 0) {
                                        t.setText("ไม่พบข้อมูล");
                                        //new MyToast(getActivity(),"ไม่พบข้อมูล",2);
                                    }
                                    for (int i = 0; i < result.size(); i++) {

                                        JsonObject jsonObject = (JsonObject) result.get(i);
                                        orderNo = jsonObject.get("OrderNo").getAsString();
                                        index = i + 1;
                                        num = 0;
                                        items.add(new MyItemSearchFactory("" + index, jsonObject.get("ProductNameTH").getAsString(), jsonObject.get("ProductNameEN").getAsString(), "" + num, jsonObject.get("Num").getAsString()));
                                    }
                                    t.setText("เลขที่ออเดอร์ : " + orderNo);
                                    t.setTypeface(myFont.setFont());
                                    ListView listView = myView.findViewById(R.id.list);
                                    MyAdapterSearchFactory myAdapter = new MyAdapterSearchFactory(getActivity(), R.layout.list_search_factory, items);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                /*TextView temp = view.findViewById(R.id.orderNo);
                                String orderNo = temp.getText().toString();
                                Bundle bundle = new Bundle();
                                bundle.putString("orderNo",orderNo);

                                FragmentServiceProduct thirdFragment_1 = new FragmentServiceProduct();
                                thirdFragment_1.setArguments(bundle);
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame,thirdFragment_1)
                                        .commit();*/

                                        }
                                    });
                                    listView.setAdapter(myAdapter);
                                }
                            } catch (Exception ex) {
                                new MyToast(getActivity(), "การเชื่อมต่อมีปัญหา", 0);
                            }

                        }
                    });
        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        return myView;
    }

}
