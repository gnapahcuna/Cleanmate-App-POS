package com.techlogn.cleanmate_pos_v36;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by anucha on 1/3/2018.
 */

public class FragmentServiceProduct extends Fragment{

    View myView;
    ArrayList<CustomItemProduct> items;
    int ID;
    String dataID, branchID;
    ArrayList<String>arrMenuTH,arrMenuEN,arrImg,arr1,arr2;
    SharedPreferences sharedPreferences,sharedPreferences_pro;

    //HorizontalScrollMenuView menu;

    int num=0;
    int c=0;
    ListView listView;
    MyAdapterProduct myAdapter;

    SharedPreferences sp;
    int totalCount=0;
    int count_pro = 1,total_price=0;
    TextView text_result1,text_result2,text_result3,textNum ;


    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    int i_num=0;

    private int mScreenWidth;
    private int mScreenHeight;

    private ImageView mImgBall;
    private int mImgHeight;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    double sqm=0.00;
    int position=0;

    TextView tv1;
    ArrayList<TextView>arr_text_menu;
    LinearLayout ln_menu;
    TextView nameService;

    private GetIPAPI getIPAPI;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_service_product, container, false);
        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/



        items = new ArrayList<>();
        arrMenuTH=new ArrayList<>();
        arrMenuEN=new ArrayList<>();
        arrImg=new ArrayList<>();
        arr1=new ArrayList<>();
        arr2=new ArrayList<>();
        //menu=myView.findViewById(R.id.menuSlide);


        //for (int i = 0; i < menu.geti; i++) {
            //noinspection ConstantConditions

        sharedPreferences = getContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
        sharedPreferences_pro = getContext().getSharedPreferences("Promotion", Activity.MODE_PRIVATE);


        listView = myView.findViewById(R.id.list_1);

        text_result1=myView.findViewById(R.id.text_result1);
        text_result2=myView.findViewById(R.id.text_result2);
        text_result3=myView.findViewById(R.id.text_result3);

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

        DisplayMetrics display = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(display);
        mScreenWidth = display.widthPixels;
        mScreenHeight = display.heightPixels;

        mImgBall = myView.findViewById(R.id.img_sale);
        mImgBall.setVisibility(View.GONE);
        mImgBall.post(new Runnable() {
            @Override
            public void run() {
                mImgHeight = mImgBall.getHeight();
                setStartPosition(100);
            }
        });

        ln_menu=myView.findViewById(R.id.lv_menu);
        ln_menu.setBackgroundColor(Color.parseColor("#336699"));
        arr_text_menu=new ArrayList<>();
        nameService = myView.findViewById(R.id.textService);


        return myView;
    }

    private void setStartPosition(int y) {
        mImgBall.setX(mScreenWidth - 400);
        mImgBall.setY(y);
    }
    private void animate(TimeInterpolator interpol, float yBy) {
        mImgBall.animate()
                .setInterpolator(interpol)
                .translationYBy(yBy)
                .setDuration(1000)
                .setStartDelay(50)
                .start();
    }
    @Override
    public void onStart() {
        super.onStart();

        mDb = mSQLite.getReadableDatabase();
        String sql="";
        String key="",sub="";
        Bundle bundle = this.getArguments();


        MyFont myFont = new MyFont(getActivity());
        if (bundle != null) {
            key = bundle.getString("key");
            sub = bundle.getString("sub");
            System.out.println("key : "+key);
            if(key!=null) {
                if (key.endsWith("อื่นๆ")) {
                    sql = "SELECT product.ProductID,product.ServiceNameTH,product.ServiceNameEN,product.ProductNameTH,product.ProductNameEN,CouponPriceList.CouponPrice,product.ImageFile,product.ColorCode,product.ServiceType,product.CategoryID,product.Num FROM product left join CouponPriceList on product.ProductID=CouponPriceList.ProductID where product.ServiceNameTH='" + key + "'";
                } else {
                    sql = "SELECT * FROM product where ServiceNameTH='" + key + "'";
                }
            }

            nameService.setText(key + " (" + sub + ")");
            nameService.setTypeface(myFont.setFont());
        }
        if(key==null){
            sql = "SELECT * FROM product where ServiceNameTH='ซักแห้ง'";
            //sql = "SELECT * FROM product where CategoryID='6'";
            String sql1 = "SELECT * FROM tb_service where ServiceType='1'";
            Cursor cursor = mDb.rawQuery(sql1, null);
            while (cursor.moveToNext()) {
                nameService.setText(cursor.getString(1) + " (" + cursor.getString(2) + ")");
                nameService.setTypeface(myFont.setFont());
            }

        }
        Cursor cursor = mDb.rawQuery(sql, null);
        System.out.println("cursor : "+cursor.getColumnCount());
        String str = "";
        while (cursor.moveToNext()) {

            /*if (cursor.getInt(0) < 10) {
                str += "0";

            } else {*/
            try{
                items.add(new CustomItemProduct(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        ""+(int)Double.parseDouble(cursor.getString(5)),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(10)));
            }catch (Exception e){
                System.out.println("exception : "+e.getMessage());
            }
            //}
        }

        //System.out.println(str);

        String sql1 = "SELECT * FROM tb_service";
        cursor = mDb.rawQuery(sql1, null);
        //myFont =new MyFont(getActivity());
        while (cursor.moveToNext()) {
            //menu.addItem(cursor.getString(1) + " ("+cursor.getString(2)+")", R.drawable.ic_arrow_drop_down_black_24dp);

            LinearLayout.LayoutParams rl =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            int height=getResources().getDisplayMetrics().heightPixels;
            //System.out.println((height*3)/100);
            rl.setMargins(20, (height*2)/100, 20, (height*2)/100);
            tv1 = new TextView(getActivity());
            tv1.setTextColor(Color.parseColor("#ffffff"));
            DisplayMetrics displayMetrics=new DisplayMetrics();

            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int wpx=displayMetrics.widthPixels;
            int hpx=displayMetrics.heightPixels;
            float xdpi=displayMetrics.xdpi;
            float ydpi=displayMetrics.ydpi;
            int densityDpi=displayMetrics.densityDpi;
            float w=wpx/xdpi;
            float h=hpx/ydpi;
            double screen=Math.sqrt(w*w+h*h);
            //System.out.println("densityDpi : "+densityDpi);
            //System.out.println("screen size : "+screen);
            Configuration config = getActivity().getResources().getConfiguration();
            if (config.smallestScreenWidthDp >= 720) {
                tv1.setTextSize(28);
            }
            else if (config.smallestScreenWidthDp >= 600) {
                tv1.setTextSize(26);
            }else if (config.smallestScreenWidthDp >= 480) {
                if(screen>=5.95){
                    tv1.setTextSize(20);
                }else{
                    tv1.setTextSize(20);
                }
            }else if (config.smallestScreenWidthDp >= 440) {
                tv1.setTextSize(24);
            }else if (config.smallestScreenWidthDp >= 320) {
                if(screen>=5.95){
                    tv1.setTextSize(24);
                }else{
                    tv1.setTextSize(18);
                }
            }
            /*if(densityDpi>=720){
                tv1.setTextSize(28);
            }else if(densityDpi>=600){
                tv1.setTextSize(26);
            }else if(densityDpi>=480){
                if(screen>=5.95){
                    tv1.setTextSize(26);
                }else{
                    tv1.setTextSize(24);
                }
            }else if(densityDpi>=440){
                tv1.setTextSize(24);
            }else if(densityDpi>=320){
                if(screen>=5.95){
                    tv1.setTextSize(24);
                }else{
                    tv1.setTextSize(18);
                }
            }*/
            tv1.setText(cursor.getString(1) + " ("+cursor.getString(2)+")");
            //System.out.println(nameService.getText().toString().trim()+" == "+tv1.getText().toString().trim());
            if(nameService.getText().toString().trim().equals(tv1.getText().toString().trim())){
                //tv1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                String mystring=cursor.getString(1) + " ("+cursor.getString(2)+")";
                String ss=cursor.getString(1);
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
                tv1.setText(content);
            }
            tv1.setLayoutParams(rl);
            arr_text_menu.add(tv1);
            ln_menu.addView(tv1);
            tv1.setTypeface(myFont.setFont());
        }


        String sql2 = "SELECT * FROM promotion_sale";
        cursor = mDb.rawQuery(sql2, null);






        setCart();
        listView = myView.findViewById(R.id.list_1);
        myAdapter = new MyAdapterProduct(getActivity(), R.layout.product_list, items, dataID, R.layout.fragment_service_product, listView, text_result1, text_result2, text_result3);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String subImg = getIPAPI.IPAddressImage+"/" + items.get(i).mImage.substring(3).trim();
                Picasso.with(getContext()).load(subImg).into(mImgBall);
                mImgBall.setVisibility(View.VISIBLE);
                setStartPosition(100);
                animate(new AccelerateInterpolator(), mScreenHeight - 100);

                textNum = view.findViewById(R.id.text_num);
                final CustomItemProduct item = items.get(i);
                //getItem(i,Integer.parseInt(item.mServiceType),Integer.parseInt(item.mTextProductID),Integer.parseInt(item.mTextPrice));

                if(Integer.parseInt(item.mTextProductID)==54||Integer.parseInt(item.mTextProductID)==55||Integer.parseInt(item.mTextProductID)==56||
                        Integer.parseInt(item.mTextProductID)==57||Integer.parseInt(item.mTextProductID)==58){
                    //Toast.makeText(getActivity(),"ตรม. : "+item.mTextProductTH,Toast.LENGTH_LONG).show();

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_select_sqm);
                    dialog.show();
                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                    title.setText("โปรดระบุ ตรม./ชิ้น");
                    Button okButton = dialog.findViewById(R.id.btn_ok);
                    final NumberPicker numberPicker1 = dialog.findViewById(R.id.number_picker1);
                    final NumberPicker numberPicker2 = dialog.findViewById(R.id.number_picker2);
                    numberPicker1.setMinValue(1);
                    numberPicker1.setMaxValue(99);
                    numberPicker1.setValue(0);
                    numberPicker2.setMinValue(1);
                    numberPicker2.setMaxValue(99);
                    numberPicker2.setValue(0);
                    position=i;
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String s=numberPicker1.getValue()+"."+numberPicker2.getValue();
                            //sqm=Math.rint(Double.parseDouble(s));

                            String pattern = "0.0";
                            float get=Float.parseFloat(numberPicker1.getValue()+"."+numberPicker2.getValue());
                            DecimalFormat df = new DecimalFormat(pattern);
                            String output=df.format(get);
                            sqm=Double.parseDouble(output);

                            //Toast.makeText(getActivity(),""+String.format("%1$,.2f", sqm)+" ตรม.,"+s+item.mTextProductTH,Toast.LENGTH_LONG).show();

                            getItem(sqm,position,Integer.parseInt(item.mServiceType),Integer.parseInt(item.mTextProductID),Integer.parseInt(item.mTextPrice));

                            dialog.dismiss();
                        }
                    });
                    MyFont myFont=new MyFont(getActivity());
                    okButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    numberPicker1.setTypeface(myFont.setFont());
                    numberPicker2.setTypeface(myFont.setFont());

                }else{
                    getItem((double) 0,i,Integer.parseInt(item.mServiceType),Integer.parseInt(item.mTextProductID),Integer.parseInt(item.mTextPrice));
                }

                //onStart();

            }
        });
        listView.setAdapter(myAdapter);

        cursor.close();


        /*menu.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {
            @Override
            public void onHSMClick(com.darwindeveloper.horizontalscrollmenulibrary.extras.MenuItem menuItem, int position) {

                items.clear();
                items = new ArrayList<>();
                final String sub = menuItem.getText().toString().substring(0, menuItem.getText().toString().indexOf('(')).trim();
                final String sub1 = menuItem.getText().toString().substring(menuItem.getText().toString().indexOf('(') + 1, menuItem.getText().indexOf(')')).trim();
                Bundle bundle = new Bundle();
                bundle.putString("key",sub);
                bundle.putString("sub",sub1);
                FragmentServiceProduct fragment1_1 = new FragmentServiceProduct();
                fragment1_1.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,fragment1_1)
                        .commit();

                //onStart();

                *//*setCart();
                listView = myView.findViewById(R.id.list_1);
                myAdapter = new MyAdapterProduct(getActivity(), R.layout.product_list, items, dataID, R.layout.fragment_service_product, listView, text_result1, text_result2, text_result3);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        textNum = view.findViewById(R.id.text_num);
                        final CustomItemProduct item = items.get(i);
                        Toast.makeText(getActivity(),item.mTextProductID,Toast.LENGTH_LONG).show();
                        getItem(i,Integer.parseInt(item.mServiceType),Integer.parseInt(item.mTextProductID),Integer.parseInt(item.mTextPrice));
                        //onStart();

                    }
                });
                listView.setAdapter(myAdapter);*//*
            }
        });*/
        for(TextView tv : arr_text_menu){
            final TextView t =tv;
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //System.out.println("v1 : "+v.getId());
                    items.clear();
                    items = new ArrayList<>();
                    final String sub = t.getText().toString().substring(0, t.getText().toString().indexOf('(')).trim();
                    final String sub1 = t.getText().toString().substring(t.getText().toString().indexOf('(') + 1, t.getText().toString().indexOf(')')).trim();
                    Bundle bundle = new Bundle();
                    bundle.putString("key",sub);
                    bundle.putString("sub",sub1);
                    FragmentServiceProduct fragment1_1 = new FragmentServiceProduct();
                    fragment1_1.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame,fragment1_1)
                            .commit();

                }
            });
        }
    }


    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
    public void getItem(double sqm,int i,int service,int productID,int price) {

        if(sqm>0){
            //System.out.println(productID+" : "+String.format("%1$,.2f", sqm)+" ตรม.");
            /*ContentValues cv=new ContentValues();
            cv.put("ProductID",productID);
            cv.put("Sqm",sqm);
            cv.put("ProductPrice",price);
            mDb.insert("tb_sqm",null,cv);*/
        }else{
            //Toast.makeText(getActivity(),"Not tarangmet",Toast.LENGTH_SHORT).show();
        }

        /*SharedPreferences.Editor editor_promotion = sharedPreferences_pro.edit();
        HashSet<String> mSet_pro = new HashSet<>();
        if(service==1) {
            mSet_pro.add("<a>" + service);
            mSet_pro.add("<b>" + productID);
            editor_promotion.putStringSet("Promotion", mSet_pro);
            editor_promotion.apply();
        }
        ArrayList<Integer>arr_proService=new ArrayList<>();
        ArrayList<Integer>arr_proProduct=new ArrayList<>();
        Map<String, ?> entries_pro = sharedPreferences_pro.getAll();
        Set<String> keys_pro = entries_pro.keySet();
        String[] getData_pro;
        List<String> list_pro = new ArrayList<String>(keys_pro);
        for (String temp : list_pro) {
            System.out.println(temp+" = "+sharedPreferences_pro.getStringSet(temp,null));
            for (int k = 0; k < sharedPreferences_pro.getStringSet(temp, null).size(); k++) {
                getData_pro = sharedPreferences_pro.getStringSet(temp, null).toArray(new String[sharedPreferences_pro.getStringSet(temp, null).size()]);
                char chk = getData_pro[k].charAt(1);
                if (chk == 'g') {
                    arr_proService.add(Integer.parseInt(getData_pro[k].substring(3)));
                } else if (chk == 'c') {
                    arr_proProduct.add(Integer.parseInt(getData_pro[k].substring(3)));
                }

            }
        }*/
        if(service==1) {
            ContentValues cv = new ContentValues();
            cv.put("ServiceType", service);
            cv.put("ProductID", productID);
            cv.put("Price", price);
            mDb.insert("promotion_sale", null, cv);
        }


        int results = 0;
        int counts = 0;

        Map<String, ?> entries2 = sharedPreferences.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        double test_count_pro=0.00;
        int sum_counts=0,sum_result=0;

        ArrayList<Double> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();
        for (String temp : list2) {
            for (int k = 0; k < sharedPreferences.getStringSet(temp, null).size(); k++) {
                getData2 = sharedPreferences.getStringSet(temp, null).toArray(new String[sharedPreferences.getStringSet(temp, null).size()]);
                char chk = getData2[k].charAt(1);
                if (chk == 'g') {
                    arr1.add(Double.parseDouble(getData2[k].substring(3)));
                } else if (chk == 'c') {
                    arr2.add(Integer.parseInt(getData2[k].substring(3)));
                }

            }
        }
        for (int k = 0; k < arr1.size(); k++) {
            results += arr1.get(k) * arr2.get(k);
            counts += arr1.get(k);
        }

        count_pro = 1;
        sum_counts = counts + 1;
        sum_result = results + Integer.parseInt(items.get(i).mTextPrice);
        Map<String, ?> entries3 = sharedPreferences.getAll();
        Set<String> keys3 = entries3.keySet();
        String[] getData3;
        List<String> list3 = new ArrayList<String>(keys3);
        for (String temp : list3) {
            for (int k = 0; k < sharedPreferences.getStringSet(temp, null).size(); k++) {
                getData3 = sharedPreferences.getStringSet(temp, null).toArray(new String[sharedPreferences.getStringSet(temp, null).size()]);
                char chk = getData3[k].charAt(1);
                if (temp.trim().equals(items.get(i).mTextProductID.trim())) {
                    if (chk == 'c') {
                    } else if (chk == 'g') {
                        count_pro = Integer.parseInt(getData3[k].substring(3)) + 1;
                    }

                } else {
                }
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<String> arr_proTH = new ArrayList<>();
        arr_proTH.add(items.get(i).mTextProductID);
        StringBuilder sb = new StringBuilder();
        HashSet<String> mSet = new HashSet<>();
        for (int j = 0; j < arr_proTH.size(); j++) {
            mSet.add("<a>" + items.get(i).mTextProductTH);
            mSet.add("<b>" + items.get(i).mTextProductEN);
            mSet.add("<c>" + items.get(i).mTextPrice);
            mSet.add("<d>" + items.get(i).mServiceNameTH);
            mSet.add("<e>" + items.get(i).mServiceNameEN);
            mSet.add("<f>" + getIPAPI.IPAddressImage+"/" + items.get(i).mImage.substring(3).trim());
            mSet.add("<g>" + count_pro);
            mSet.add("<h>" + items.get(i).mTextProductID);
            mSet.add("<i>" + items.get(i).mColor);
            mSet.add("<j>" + items.get(i).mServiceType);
        }
        editor.putStringSet(items.get(i).mTextProductID, mSet);
        editor.apply();

        textNum.setText("" + count_pro);

        total_price += Integer.parseInt(items.get(i).mTextPrice);

        text_result1.setText(" " + sharedPreferences.getAll().size() + " ");
        text_result2.setText(" " + sum_counts + " ");
        text_result3.setText(" " + getFormatedAmount(sum_result) + " ");
    }

    public  void setCart(){
        total_price=0;
        sharedPreferences = getContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
        ConstraintLayout cart=myView.findViewById(R.id.layout_click_cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new AlertDialog.Builder(getActivity())
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("ยืนยันการลบ")
                        .setMessage("ท่านต้องการลบรายการนี้จริงหรือไม่?")
                        .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String sql = "DELETE FROM product";
                                mDb = mSQLite.getWritableDatabase();
                                mDb.execSQL(sql);
                                sql = "DELETE FROM tb_service";
                                SQLiteDatabase mDb = mSQLite.getWritableDatabase();
                                mDb.execSQL(sql);
                                onStart();
                            }
                        })
                        .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        })
                        .show();*/
                if(sharedPreferences.getAll().size()>0) {
                    SharedPreferences SP = getActivity().getSharedPreferences("K", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorSP = SP.edit();
                    HashSet<String> mSet = new HashSet<>();
                    mSet.add("<a>" + "2");
                    mSet.add("<b>" + ID);
                    editorSP.putStringSet("K", mSet);
                    editorSP.apply();

                    SharedPreferences sharedPreferences1=getContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
                    sharedPreferences1.edit().clear().apply();

                    Intent i = new Intent(getActivity(), BasketActivity.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                    getActivity().finish();
                    //sharedPreferences.edit().clear().apply();
                }else{
                    new MyToast(getActivity(),"ยังไม่มีสินค้าในตะกร้าสินค้า",0);
                }
            }
        });

        ArrayList<String>arrPrice=new ArrayList<>();
        ArrayList<String>arrCount=new ArrayList<>();
        Map<String,?> entries2 = sharedPreferences.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            //System.out.println("A : "+temp+" = "+sp.getStringSet(temp,null));
            for (int k = 0; k < sharedPreferences.getStringSet(temp, null).size(); k++) {
                getData2 = sharedPreferences.getStringSet(temp, null).toArray(new String[sharedPreferences.getStringSet(temp, null).size()]);
                char chk=getData2[k].charAt(1);
                if(chk=='c'){
                    arrPrice.add(getData2[k].substring(3));
                    //total_price+=Integer.parseInt(getData2[k].substring(3));
                }else if(chk=='g'){
                    totalCount+=Integer.parseInt(getData2[k].substring(3));
                    arrCount.add(getData2[k].substring(3));
                }

            }
        }
        for(int i=0;i<arrPrice.size();i++){
            total_price+=Integer.parseInt(arrPrice.get(i))*Integer.parseInt(arrCount.get(i));
        }

        final TextView text1=myView.findViewById(R.id.text_data1);
        final TextView text2=myView.findViewById(R.id.text_data2);
        final TextView text3=myView.findViewById(R.id.text_data3);
        final TextView text4=myView.findViewById(R.id.text_data4);
        final TextView text5=myView.findViewById(R.id.text_data5);

        text1.setText("สินค้า");
        text2.setText("ชนิด");
        text3.setText("ชิ้น");
        text4.setText(" ราคารวม");
        text5.setText("บาท");


        text_result1.setText(" "+ sharedPreferences.getAll().size()+" ");
        text_result2.setText(" "+  totalCount+" ");
        text_result3.setText(" "+ getFormatedAmount(total_price)+" ");

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
}
