package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by anucha on 1/3/2018.
 */

public class FragmentSupplies extends Fragment{

    View myView;
    ArrayList<CustomItemSupplies> items;
    ListView listView;
    TextView textTitle;
    MyAdapterSupplies myAdapter;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    private GetIPAPI getIPAPI;

    private TextView text_result1,text_result2,text_result3;
    SharedPreferences SP;
    int num=0,count=0;
    double price=0.00;
    ArrayList<String> textTH,textPrice,textNum;
    ConstraintLayout next;
    private int mCount = 0;
    ProgressDialog dialog;
    String dataID="",branchID="";

    ArrayList items1;
    ArrayList<CustomItemSuppliesOrder>items2;

    boolean chk=false;

    @Override
    public void onStart() {
        super.onStart();

        mDb = mSQLite.getReadableDatabase();
        String sql = "SELECT * FROM supplies";
        Cursor cursor = mDb.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            //System.out.println(cursor.getString(1)+" = "+cursor.getString(2));
            items.add(new CustomItemSupplies(cursor.getString(1),cursor.getString(2),cursor.getString(3),""+0));
        }



        listView = myView.findViewById(R.id.list_1);
        myAdapter = new MyAdapterSupplies(getActivity(), R.layout.list_supplies, items,listView, text_result1, text_result2, text_result3);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomItemSupplies item = items.get(position);

                num=1;
                Map<String, ?> entries2 = SP.getAll();
                Set<String> keys2 = entries2.keySet();
                String[] getData2;
                List<String> list2 = new ArrayList<String>(keys2);
                for (String temp : list2) {
                    for (int k = 0; k < SP.getStringSet(temp, null).size(); k++) {
                        getData2 = SP.getStringSet(temp, null).toArray(new String[SP.getStringSet(temp, null).size()]);
                        char chk = getData2[k].charAt(1);
                        if (temp.trim().equals(items.get(position).mSuppliesNameTH.trim())) {
                            if (chk == 'c') {
                                num=Integer.parseInt(getData2[k].substring(3))+1;
                            }
                        }

                    }
                }
                textTH=new ArrayList<>();
                textPrice=new ArrayList<>();
                textNum=new ArrayList<>();

                Map<String, ?> entries = SP.getAll();
                Set<String> keys = entries.keySet();
                String[] getData;
                List<String> list = new ArrayList<String>(keys);
                for (String temp : list) {
                    System.out.println(temp+" = "+SP.getStringSet(temp,null));
                    for (int k = 0; k < SP.getStringSet(temp, null).size(); k++) {
                        getData = SP.getStringSet(temp, null).toArray(new String[SP.getStringSet(temp, null).size()]);
                        char chk = getData[k].charAt(1);
                        if (chk == 'a') {
                            textTH.add(getData[k].substring(3));
                        } else if (chk == 'b') {
                            textPrice.add(getData[k].substring(3));
                        } else if (chk == 'c') {
                            textNum.add(getData[k].substring(3));
                        }

                    }
                }
                int counts=0;
                double prices=0.00;
                for(int i=0;i<textTH.size();i++){
                    prices+=Double.parseDouble(textPrice.get(i))*Integer.parseInt(textNum.get(i));
                    counts+=Integer.parseInt(textNum.get(i));
                }
                price=prices+Double.parseDouble(item.mTextPrice);
                count=counts+1;

                SharedPreferences.Editor editorSP = SP.edit();
                HashSet<String> mSet = new HashSet<>();
                ArrayList<String> arr_proTH = new ArrayList<>();
                arr_proTH.add(items.get(position).mSuppliesNameTH);
                for (int j = 0; j < arr_proTH.size(); j++) {
                    mSet.add("<a>" + item.mSuppliesNameTH);
                    mSet.add("<b>" + item.mTextPrice);
                    mSet.add("<c>" + num);
                    mSet.add("<d>" + item.mSuppliesID);
                }
                editorSP.putStringSet(items.get(position).mSuppliesNameTH, mSet);
                editorSP.apply();

                TextView TextNum = view.findViewById(R.id.text_num);
                TextNum.setText(""+num);

                text_result1.setText(" "+SP.getAll().size()+" ");
                text_result2.setText(" "+count+" ");
                text_result3.setText(" "+String.format("%,.2f", price)+" ");
            }
        });
        listView.setAdapter(myAdapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SP.getAll().size()!=0) {
                    FragmentSuppliesOrder fragmentSuppliesOrder = new FragmentSuppliesOrder();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentSuppliesOrder)
                            .commit();
                }else{
                    new MyToast(getActivity(),"ยังไม่ได้เลือกรายการ",0);
                }
            }
        });
    }
    private void setCart(){
        final TextView text1=myView.findViewById(R.id.text_data1);
        final TextView text2=myView.findViewById(R.id.text_data2);
        final TextView text3=myView.findViewById(R.id.text_data3);
        final TextView text4=myView.findViewById(R.id.text_data4);
        final TextView text5=myView.findViewById(R.id.text_data5);

        textTH=new ArrayList<>();
        textPrice=new ArrayList<>();
        textNum=new ArrayList<>();
        Map<String, ?> entries = SP.getAll();
        Set<String> keys = entries.keySet();
        String[] getData;
        List<String> list = new ArrayList<String>(keys);
        for (String temp : list) {
            System.out.println(temp+" = "+SP.getStringSet(temp,null));
            for (int k = 0; k < SP.getStringSet(temp, null).size(); k++) {
                getData = SP.getStringSet(temp, null).toArray(new String[SP.getStringSet(temp, null).size()]);
                char chk = getData[k].charAt(1);
                if (chk == 'a') {
                    textTH.add(getData[k].substring(3));
                } else if (chk == 'b') {
                    textPrice.add(getData[k].substring(3));
                } else if (chk == 'c') {
                    textNum.add(getData[k].substring(3));
                }

            }
        }
        int counts=0;
        for(int i=0;i<textTH.size();i++){
            price+=Double.parseDouble(textPrice.get(i))*Integer.parseInt(textNum.get(i));
            counts+=Integer.parseInt(textNum.get(i));
        }
        count=counts;


        text1.setText("สินค้า");
        text2.setText("ชนิด");
        text3.setText("ชิ้น");
        text4.setText(" ราคารวม");
        text5.setText("บาท");

        text_result1.setText(" "+ SP.getAll().size()+" ");
        text_result2.setText(" "+  count+" ");
        text_result3.setText(" "+ String.format("%,.2f", price)+" ");

        MyFont myFont=new MyFont(getActivity());
        text_result1.setTypeface(myFont.setFont());
        text_result2.setTypeface(myFont.setFont());
        text_result3.setTypeface(myFont.setFont());
        text1.setTypeface(myFont.setFont());
        text2.setTypeface(myFont.setFont());
        text3.setTypeface(myFont.setFont());
        text4.setTypeface(myFont.setFont());
        text5.setTypeface(myFont.setFont());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_supplies, container, false);
        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }
        getIPAPI=new GetIPAPI();
        items = new ArrayList<>();
        listView = myView.findViewById(R.id.list_1);

        mSQLite=SQLiteHelper.getInstance(getActivity());

        myView.setFocusableInTouchMode(true);
        myView.requestFocus();
        myView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                }
                return false;
            }
        });

        textTitle=myView.findViewById(R.id.textService);
        textTitle.setText("รายการวัสดุสิ้นเปลือง");

        text_result1=myView.findViewById(R.id.text_result1);
        text_result2=myView.findViewById(R.id.text_result2);
        text_result3=myView.findViewById(R.id.text_result3);

        LinearLayout ln = myView.findViewById(R.id.ln_list);
        ln.getLayoutParams().height=(getResources().getDisplayMetrics().heightPixels*80)/100;

        SP = getActivity().getSharedPreferences("Supplies", Context.MODE_PRIVATE);

        next =myView.findViewById(R.id.layout_click_cart);

        setCart();


        SharedPreferences sharedPreferences4=getContext().getSharedPreferences("ID", Activity.MODE_PRIVATE);
        Map<String, ?> entries4 = sharedPreferences4.getAll();
        Set<String> keys4 = entries4.keySet();
        String[] getData4;
        List<String> list4 = new ArrayList<String>(keys4);
        for (String temp : list4) {
            //System.out.println(temp+" = "+sharedPreferences2.getStringSet(temp,null));
            for (int i = 0; i < sharedPreferences4.getStringSet(temp, null).size(); i++) {
                getData4 = sharedPreferences4.getStringSet(temp, null).toArray(new String[sharedPreferences4.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData4[i]);
                char chk = getData4[i].charAt(1);
                if (chk == 'a') {
                    dataID = getData4[i].substring(3);
                } else if (chk == 'b') {
                    branchID = getData4[i].substring(3);
                }
            }
        }
        items1 = new ArrayList();
        items2=new ArrayList<>();

        String url1 = getIPAPI.IPAddress+"/CleanmatePOS/CheckSuppliesOrder.php?BranchID="+branchID;
        new MyAsyncTask().execute(url1,""+0);


        ImageView img_bage=myView.findViewById(R.id.img_bage);
        img_bage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loaddata
                System.out.println(branchID);
                String url1 = getIPAPI.IPAddress+"/CleanmatePOS/CheckSuppliesOrder.php?BranchID="+branchID;
                new MyAsyncTask().execute(url1,""+1);
            }
        });

        return myView;
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

            String response = "";
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
                System.out.println("Error1 : " + ex);
            }

            String output = "";
            try {
                output = strings[1];
                System.out.println(response);
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() == 0) {
                    chk = true;
                }

                //if(Integer.parseInt(output)==1) {
                ArrayList<String> OrderNo = new ArrayList<>();
                ArrayList<String> OrderNo1 = new ArrayList<>();
                ArrayList<String> SuppliesNameTH = new ArrayList<>();
                ArrayList<String> Counts = new ArrayList<>();
                ArrayList<String> Price = new ArrayList<>();
                ArrayList<String> Prices = new ArrayList<>();
                ArrayList<String> CreateDate = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    //JSONObject jsonObj1 = jsonArray.getJSONObject(i);

                    JSONObject jsonObject1 = new JSONObject(jsonObj.getString("CreateDate"));

                    OrderNo.add(jsonObj.getString("OrderNo") + "," + jsonObject1.getString("date") + "," + jsonObj.getString("IsChecker") + "," + jsonObj.getString("IsBranchEmp"));
                    OrderNo1.add(jsonObj.getString("OrderNo"));
                    SuppliesNameTH.add(jsonObj.getString("SuppliesNameTH"));
                    Counts.add(jsonObj.getString("counts"));
                    Price.add(jsonObj.getString("Price"));
                    Prices.add(jsonObj.getString("prices"));
                    //CreateDate.add(jsonObject1.getString("date"));

                    /*items2.add(new CustomItemSuppliesOrder(jsonObj1.getString("SuppliesNameTH"),jsonObj1.getString("Price"),jsonObj1.getString("counts")));
                    items1.addAll(Arrays.asList(
                            new CustomItemSuppliesOrderList(jsonObj.getString("OrderNo"),jsonObject1.getString("date"), items2,"รอตอบรับจากแอดมิน", Double.parseDouble(jsonObj.getString("prices")))
                    ));*/

                }
                HashSet<String> hashSet = new HashSet<>();
                hashSet.addAll(OrderNo);
                OrderNo.clear();
                OrderNo.addAll(hashSet);
                double total = 0.00;
                for (int i = 0; i < OrderNo.size(); i++) {
                    String[] splits = OrderNo.get(i).split(",");
                    items2 = new ArrayList<>();
                    for (int j = 0; j < OrderNo1.size(); j++) {
                        if (splits[0].equals(OrderNo1.get(j))) {
                            total += Double.parseDouble(Price.get(j))* Double.parseDouble(Counts.get(j));
                            //System.out.println(SuppliesNameTH.get(j)+"\t\t"+Price.get(j)+"\t\t"+Price.get(j)+"\t\t"+Counts.get(j));
                            items2.add(new CustomItemSuppliesOrder(SuppliesNameTH.get(j), Price.get(j), Counts.get(j)));
                        }
                    }
                    if (Integer.parseInt(splits[2]) == 0 && Integer.parseInt(splits[3]) == 0) {
                        //System.out.println(splits[0]+"\t\t"+splits[1]+"\t\tรอตรวจสอบ");
                        items1.addAll(Arrays.asList(
                                new CustomItemSuppliesOrderList(splits[0], splits[1], items2, "รอตรวจสอบ", total, 0)
                        ));
                    } else {
                        //System.out.println(splits[0]+"\t\t"+splits[1]+"\t\tกำลังตรวจสอบและเตรียมจัดส่ง");
                        items1.addAll(Arrays.asList(
                                new CustomItemSuppliesOrderList(splits[0], splits[1], items2, "กำลังตรวจสอบและเตรียมจัดส่ง", total, 1)
                        ));
                        mCount++;
                    }
                    //System.out.println("Total : "+total);
                    total = 0;
                }
                //}


            } catch (Exception ex) {
                System.out.println("Error2 : " + ex);
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            final NotificationBadge badge = myView.findViewById(R.id.badge);
            badge.setNumber(mCount);

            if(chk==true){
                new MyToast(getActivity(),"ไม่มีรายการ",0);
            }else {
                if (Integer.parseInt(s) == 1) {
                    try {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.custom_supplies_order);
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView title = dialog.findViewById(R.id.tv_quit_learning);
                        title.setText("รายการสั่งพัสดุ");
                        TextView close = dialog.findViewById(R.id.textClose);
                        SpannableString contents = new SpannableString("ปิด");
                        contents.setSpan(new UnderlineSpan(), 0, 3, 0);
                        close.setText(contents);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        RecyclerView rcv = dialog.findViewById(R.id.recyclerView);
                        CustomAdapterSuppliesOrder adapter = new CustomAdapterSuppliesOrder(getActivity(), items1,rcv,dialog,badge,mCount);

                        rcv.setAdapter(adapter);
                        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
                        MyFont myFont = new MyFont(getActivity());
                        title.setTypeface(myFont.setFont());
                        items1=new ArrayList();
                        items2=new ArrayList<>();
                        mCount=0;
                    } catch (Exception e) {
                        new MyToast(getActivity(), "การเชื่อมต่อมีปัญหา", 0);
                    }
                }else{
                    items1=new ArrayList();
                    items2=new ArrayList<>();
                    mCount=0;
                }
            }
        }
    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

}
