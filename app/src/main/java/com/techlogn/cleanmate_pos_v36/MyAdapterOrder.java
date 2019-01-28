package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.libRG.CustomTextView;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by anucha on 1/6/2018.
 */

public class MyAdapterOrder extends ArrayAdapter {


    private Context mContext;
    private ArrayList<CustomItemOrder> mArrayList;
    private int mLayout;

    private int totalPrice=0;
    ArrayList<String>listPrice,listNum;
    private int num=1,aa=0,aaa=0;
    private ArrayList<CustomItemOrder> items;

    private String mProductTH,mProductEN,mServiceTH,mServiceEN,mProductID,mPrice,mImage,mColor,mSertype;
    private ArrayList<String> arrProductTH,arrProductEN,arrServiceTH,arrServiceEN,arrProductID,arrPrice,arrImage,arrColor,arrCount,arrSerType;
    private int g;

    ViewHolderOrder vHolder;
    View v;
    int mPosition;
    LayoutInflater inflater;
    ListView mListView;
    int result=0;
    String mDataID;
    private MyFont myFont;
    int mSum=0;
    TabLayout tabLayout,mTablayout;
    ViewPager viewPager,mViewPager;
    CustomTextView textTotal,mTextTotal;
    SharedPreferences sp,sp1,sp2,sp3,sp4;
    SharedPreferences.Editor editor1,editor2,editor3,editor4;
    HashSet<String> mSet1,mSet2,mSet3,mSet4;
    ArrayList<String>arrNum;
    int count_pro=1;
    EditText edt;
    String c="";

    ImageView mBin;

    ViewGroup mParent;
    View mConvertview;
    int bin_chk=0;

    int cc=0;


    int isActive=0;
    int coupon_count=0,coupon_count1,serviceType=0;
    int num_coupon=0;

    boolean isDisplay=false;
    CheckBox checkBox;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    //test privilage
    int numPrivilage = 0;
    int aaaa=0;

    int numCounpoun=0;
    int couponAmount=0;

    private GetIPAPI getIPAPI;

    public MyAdapterOrder(Context context, int layout, ArrayList<CustomItemOrder> arrayList,ListView listview,String dataID,
                          int sum,CustomTextView total,TabLayout tab,ViewPager viewPage,ImageView bin) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        mArrayList = arrayList;
        mListView=listview;
        mDataID=dataID;
        mSum=sum;
        textTotal=total;
        tabLayout=tab;
        viewPager=viewPage;

        items = arrayList;

        mBin=bin;


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        if(convertView == null) {

            mParent=parent;
            mConvertview=convertView;

            this.mPosition=position;

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayout, parent, false);
            vHolder = new ViewHolderOrder(convertView);
            convertView.setTag(vHolder);

            //v=inflater.inflate(mLayout1,parent,false);
        } else {
            vHolder = (ViewHolderOrder)convertView.getTag();
        }
        getIPAPI =new GetIPAPI();

        myFont=new MyFont(getContext());

        mSQLite=SQLiteHelper.getInstance(mContext);

        listPrice=new ArrayList<>();
        listNum=new ArrayList<>();

        final CustomItemOrder item = items.get(position);
        vHolder.textProductTH.setText(item.mTextProductTH);
        vHolder.textProductEN.setText(item.mTextProductEN);
        vHolder.textprice.setText(item.mTextPrice);
        vHolder.textCount.setText(item.mCount);
        Picasso.with(getContext()).load(item.mImage).into(vHolder.image);
        vHolder.viewColor.setBackgroundColor(Color.parseColor(item.mColor));
        vHolder.thb.setText("บาท");

        myFont=new MyFont(getContext());
        vHolder.textProductTH.setTypeface(myFont.setFont());
        vHolder.textProductEN.setTypeface(myFont.setFont());
        vHolder.textprice.setTypeface(myFont.setFont());
        vHolder.textCount.setTypeface(myFont.setFont());
        vHolder.thb.setTypeface(myFont.setFont());

        sp=getContext().getSharedPreferences("ORDER", Context.MODE_PRIVATE);
        sp1=mContext.getSharedPreferences("CouponPoint", Activity.MODE_PRIVATE);
        sp4=mContext.getSharedPreferences("CouponPoint_1", Activity.MODE_PRIVATE);
        sp2=mContext.getSharedPreferences("Privilage", Activity.MODE_PRIVATE);
        sp3=mContext.getSharedPreferences("Express", Activity.MODE_PRIVATE);

        editor1 = sp1.edit();
        mSet1 = new HashSet<>();

        editor2 = sp2.edit();
        mSet2 = new HashSet<>();

        editor3 = sp3.edit();
        mSet3 = new HashSet<>();

        editor4 = sp4.edit();
        mSet4 = new HashSet<>();

        vHolder.checkBox.setChecked(item.chk=false);
        if(item.mStatus==0){
            vHolder.checkBox.setVisibility(View.GONE);
        }else if(item.mStatus==1){
            vHolder.checkBox.setVisibility(View.VISIBLE);
        }
        arrNum = new ArrayList<>();
        Map<String, ?> entries2 = sp.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                if (temp.trim().equals(item.mTextProductID.trim())) {
                    char chk = getData2[i].charAt(1);
                    if (chk == 'h') {
                        arrNum.add(getData2[i].substring(3));
                    } else if (chk == 'g') {
                        count_pro = Integer.parseInt(getData2[i].substring(3));
                    }

                }
            }
        }
        vHolder.textCount.setText("");
        for (int i = 0; i < arrNum.size(); i++) {
            if (items.get(position).mTextProductID.trim().equals(arrNum.get(i).trim())) {
                vHolder.textCount.setText("" + count_pro);
            }
        }



        vHolder.more.setImageResource(R.drawable.ic_more_vert_black_24dp);
        vHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view,item.mTextProductID,item.mTextProductTH,item.mTextProductEN,item.mServiceNameTH,item.mServiceNameEN,item.mCount,
                        position,item.mTextPrice,item.mImage,item.mColor,mArrayList,position,tabLayout,viewPager,textTotal,vHolder.textCount,item.mSerType);

            }
        });

        mBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.size() == 0) {
                    new MyToast(mContext, "ยังไม่มีรายการสินค้า", 0);
                } else {
                    bin_chk++;
                    if (bin_chk == 2) {
                        int count = mParent.getChildCount();
                        for (int i = 0; i < count; i++) {
                            View childAt = mParent.getChildAt(i);
                            CheckBox checkBox = childAt.findViewById(R.id.check2);
                            if (checkBox.isChecked() == true) {
                                cc++;
                            }
                        }
                        if(cc<=0){
                            new MyToast(mContext,"ยังไม่ได้เลือกรายการลบ",0);
                            for (int i = 0; i < count; i++) {
                                View childAt = mParent.getChildAt(i);
                                CheckBox checkBox = childAt.findViewById(R.id.check2);
                                checkBox.setVisibility(View.GONE);
                                mBin.setImageResource(R.drawable.recycling_bin2);
                            }
                        }else {
                            final Dialog dialog = new Dialog(mContext);
                            dialog.setContentView(R.layout.custon_alert_dialog);
                            dialog.show();
                            Window window = dialog.getWindow();
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(false);
                            TextView title = dialog.findViewById(R.id.tv_quit_learning);
                            TextView des = dialog.findViewById(R.id.tv_description);
                            title.setText("แจ้งเตือน");
                            des.setText("ตัองการลบ " + cc + " รายการ");
                            Button declineButton = dialog.findViewById(R.id.btn_cancel);
                            declineButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    bin_chk = 0;
                                }
                            });
                            Button okButton = dialog.findViewById(R.id.btn_ok);
                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    SharedPreferences sharedPreferences;
                                    sharedPreferences = getContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
                                    ArrayList<Integer> arr = new ArrayList<>();
                                    ArrayList<Integer> arr1 = new ArrayList<>();
                                    int count = parent.getChildCount();
                                    for (int i = 0; i < count; i++) {
                                        View childAt = parent.getChildAt(i);
                                        checkBox = childAt.findViewById(R.id.check2);
                                        checkBox.setVisibility(View.GONE);
                                        mBin.setImageResource(R.drawable.recycling_bin1);

                                        if (checkBox.isChecked() == true) {
                                            arr.add(Integer.parseInt(items.get(i).mTextProductID));
                                            arr1.add(i);
                                        }

                                    }
                                    for (int i = 0; i < arr.size(); i++) {
                                        sharedPreferences.edit().remove(String.valueOf(arr.get(i))).commit();
                                        MyAdapterOrder.this.remove(arr1.get(i));
                                        MyAdapterOrder.this.notifyDataSetChanged();
                                    }

                                    Intent in = new Intent(mContext, BasketActivity.class);
                                    in.putExtra("key", "" + 1);
                                    mContext.startActivity(in);

                                    MyAdapterOrder.this.notifyDataSetChanged();
                                    mListView.setAdapter(MyAdapterOrder.this);
                                    laodData();
                                    textTotal.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t " + getFormatedAmount(totalPrice) + " THB");

                                    sp1.edit().clear().commit();
                                    sp3.edit().clear().commit();

                                    mBin.setImageResource(R.drawable.recycling_bin2);
                                    bin_chk = 0;
                                    dialog.dismiss();


                                    final ProgressDialog dialog1 = new ProgressDialog(mContext);
                                    dialog1.setIcon(R.mipmap.loading);
                                    dialog1.setTitle("กำลังดำเนินการ");
                                    dialog1.setTitle("Deleting....");
                                    dialog1.setIndeterminate(false);
                                    dialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    dialog1.setMax(cc);
                                    dialog1.setCancelable(false);
                                    dialog1.show();

                                    final Handler handler = new Handler();
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            int v = dialog1.getProgress();
                                            v++;
                                            if (v <= dialog1.getMax()) {
                                                dialog1.setProgress(v);
                                                handler.postDelayed(this, 200);
                                            } else {
                                                handler.removeCallbacks(this);
                                                dialog1.dismiss();
                                                new MyToast(mContext, "ลบข้อมูลเรียบร้อย", 1);
                                                return;
                                            }
                                        }
                                    };
                                    handler.postDelayed(runnable, 1000);

                                }
                            });
                            MyFont myFont = new MyFont(mContext);
                            okButton.setTypeface(myFont.setFont());
                            declineButton.setTypeface(myFont.setFont());
                            title.setTypeface(myFont.setFont());
                            des.setTypeface(myFont.setFont());
                        }


                    } else {
                        int count = mParent.getChildCount();
                        for (int i = 0; i < count; i++) {
                            View childAt = mParent.getChildAt(i);
                            CheckBox checkBox = childAt.findViewById(R.id.check2);
                            checkBox.setVisibility(View.VISIBLE);
                            mBin.setImageResource(R.drawable.recycling_bin1);
                        }
                    }
                }
            }
        });
        return convertView;
    }

        private void showPopupMenu(final View v, final String productID, final String productTH, String productEN, String serviceTH, String serviceEN,
                               final String count, final int position, String price, String img, final String color, ArrayList<CustomItemOrder> arrList,
                               final int positions, TabLayout t, ViewPager view, CustomTextView total, final TextView textCount, final String serType){
        mProductID=productID;
        mProductTH=productTH;
        mProductEN=productEN;
        mServiceTH=serviceTH;
        mServiceEN=serviceEN;
        aa = Integer.parseInt(count);
        g=position;
        mImage=img;
        mColor=color;
        mPrice=price;
        mTablayout=t;
        mViewPager=view;
        mTextTotal=total;
        mSertype=serType;

        final ArrayList<CustomItemOrder> arr=arrList;


        arrProductTH=new ArrayList<>();
        arrProductEN=new ArrayList<>();
        arrServiceTH=new ArrayList<>();
        arrServiceEN=new ArrayList<>();
        arrProductID=new ArrayList<>();
        arrPrice=new ArrayList<>();
        arrImage=new ArrayList<>();
        arrColor=new ArrayList<>();
        arrCount=new ArrayList<>();
        arrSerType=new ArrayList<>();


        Map<String,?> entries2 = sp.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                char chk=getData2[i].charAt(1);
                if(temp.trim().equals(mProductID.trim())){
                    if(chk=='g'){
                        aa=Integer.parseInt(getData2[i].substring(3));
                        aaa=Integer.parseInt(getData2[i].substring(3));
                    }
                }
            }
        }

        PopupMenu popupMenu=new PopupMenu(getContext(),v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.edit){

                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.custom_select_num);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView title=dialog.findViewById(R.id.tv_quit_learning);
                    title.setText("เลือกจำนวนตามต้องการ");
                    Button okButton =  dialog.findViewById(R.id.btn_ok);
                    final NumberPicker numberPicker = dialog.findViewById(R.id.number_picker);
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(50);
                    numberPicker.setValue(aaa);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor = sp.edit();
                            ArrayList<String>arr_proTH=new ArrayList<>();
                            arr_proTH.add(mProductID);
                            StringBuilder sb = new StringBuilder();
                            HashSet<String> mSet = new HashSet<>();
                            for (int i = 0; i < arr_proTH.size(); i++) {
                                mSet.add("<a>" + mProductTH);
                                mSet.add("<b>" + mProductEN);
                                mSet.add("<c>" + mPrice);
                                mSet.add("<d>" + mServiceTH);
                                mSet.add("<e>" + mServiceEN);
                                mSet.add("<f>" + mImage);
                                mSet.add("<g>" + numberPicker.getValue());
                                mSet.add("<h>" + mProductID);
                                mSet.add("<i>" + mColor);
                                mSet.add("<j>" + mSertype);
                            }
                            editor.putStringSet(mProductID, mSet);
                            editor.apply();

                            vHolder.textCount.setText(items.get(position).mCount);
                            MyAdapterOrder.this.notifyDataSetChanged();
                            mListView.setAdapter(MyAdapterOrder.this);
                            laodData();
                            mTextTotal.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t "+getFormatedAmount(totalPrice)+" THB");
                            dialog.dismiss();
                        }
                    });
                    MyFont myFont=new MyFont(mContext);
                    okButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    numberPicker.setTypeface(myFont.setFont());

                }else if(menuItem.getItemId()==R.id.coupon) {
                    numCounpoun=0;
                    numPrivilage=0;
                    couponAmount=0;
                    //coupon
                    Map<String, ?> entries1 = sp1.getAll();
                    Set<String> keys1 = entries1.keySet();
                    String[] getData1;
                    List<String> list1 = new ArrayList<String>(keys1);
                    for (String temp : list1) {
                        if (temp.trim().equals(mProductID.trim())) {
                            for (int i = 0; i < sp1.getStringSet(temp, null).size(); i++) {
                                getData1 = sp1.getStringSet(temp, null).toArray(new String[sp1.getStringSet(temp, null).size()]);
                                char chk = getData1[i].charAt(1);
                                if (chk == 'f') {
                                    numCounpoun = Integer.parseInt(getData1[i].substring(3));
                                }
                            }
                        }
                    }
                    //privilage
                    Map<String, ?> entries2 = sp2.getAll();
                    Set<String> keys2 = entries2.keySet();
                    String[] getData2;
                    List<String> list2 = new ArrayList<String>(keys2);
                    for (String temp : list2) {
                        if (temp.trim().equals(mProductID.trim())) {
                            for (int i = 0; i < sp2.getStringSet(temp, null).size(); i++) {
                                getData2 = sp2.getStringSet(temp, null).toArray(new String[sp2.getStringSet(temp, null).size()]);
                                char chk = getData2[i].charAt(1);
                                if (chk == 'f') {
                                    numPrivilage = Integer.parseInt(getData2[i].substring(3));
                                }
                            }
                        }
                    }

                    int chk=0;
                    if(numCounpoun+numPrivilage>=aa){
                        new MyToast(mContext, "ใช้บริการครบแล้ว", 0);
                        chk=1;
                    }else if(numCounpoun==0&&numPrivilage!=0){
                        couponAmount=aa - (numCounpoun+numPrivilage);
                    }else if(numPrivilage==0&&numCounpoun!=0){
                        couponAmount = aa - (numCounpoun+numPrivilage);
                    }
                    else {
                        couponAmount = aa;

                    }
                    if(chk!=1){
                        final ProgressDialog dialog = new ProgressDialog(mContext);
                        dialog.setIcon(R.mipmap.loading);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage("กรุณารอสักครู่....");
                        dialog.setIndeterminate(true);
                        dialog.show();
                        Ion.with(mContext)
                                .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/CoupounUse.php")
                                .setBodyParameter("ProductID", "" + items.get(g).mTextProductID)
                                .asJsonArray()
                                .setCallback(new FutureCallback<JsonArray>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonArray result) {
                                        dialog.dismiss();
                                        serviceType=0;
                                        for (int i = 0; i < result.size(); i++) {
                                            JsonObject jsonObject = (JsonObject) result.get(i);
                                            isActive = Integer.parseInt(jsonObject.get("IsActive").getAsString());
                                            coupon_count = Integer.parseInt(jsonObject.get("CouponCount").getAsString());
                                            serviceType = Integer.parseInt(jsonObject.get("ServiceType").getAsString());
                                        }
                                        System.out.println("coupon : "+serType+", "+serviceType);
                                        if (Integer.parseInt(mSertype) != serviceType) {
                                            new MyToast(mContext, "ใช้ไม่ได้กับสินค้าประเภทนี้", 0);
                                        } else {
                                            final Dialog dialog = new Dialog(mContext);
                                            dialog.setContentView(R.layout.custom_privilage);
                                            dialog.show();
                                            Window window = dialog.getWindow();
                                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                            int id = Integer.parseInt(mProductID);
                                            Button b = dialog.findViewById(R.id.btn_ok);
                                            TextView title = dialog.findViewById(R.id.tv_quit_learning);

                                            final NumberPicker numberPicker = dialog.findViewById(R.id.number_picker);
                                            numberPicker.setMinValue(1);
                                            numberPicker.setMaxValue(couponAmount * coupon_count);
                                            numberPicker.setValue(1);
                                            numberPicker.setTypeface(myFont.setFont());

                                            title.setText("ใช้สำหรับ " + mProductTH + " " + coupon_count + " ใบ ต่อ 1 ชิ้น");

                                            b.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if (numberPicker.getValue() % coupon_count == 0) {
                                                        int i=(numberPicker.getValue()/coupon_count)+numCounpoun;
                                                        editor1 = sp1.edit();
                                                        mSet1 = new HashSet<>();
                                                        mSet1.add("<a>" + mProductID);
                                                        mSet1.add("<b>" + numberPicker.getValue());
                                                        mSet1.add("<c>" + mPrice);
                                                        mSet1.add("<d>" + mProductTH);
                                                        mSet1.add("<e>" + "0");
                                                        mSet1.add("<f>" + i);
                                                        editor1.putStringSet(mProductID, mSet1);
                                                        editor1.apply();
                                                        dialog.dismiss();
                                                        new MyToast(mContext, "บันทึกการใช้บริการแล้ว", 2);
                                                    } else {
                                                        final Dialog dialog1 = new Dialog(mContext);
                                                        dialog1.setContentView(R.layout.custon_alert_dialog);
                                                        dialog1.show();
                                                        TextView title = dialog1.findViewById(R.id.tv_quit_learning);
                                                        TextView des = dialog1.findViewById(R.id.tv_description);
                                                        title.setText("แจ้งเตือน");
                                                        des.setText("ใส่จำนวนคูปองไม่ตรงกันกับจำนวนสินค้า เช่น สินค้าที่ใช้คูปอง 2 ใบ จะไม่สามารถใส่คูปอง 1 ใบต่อสินค้านั้นได้");
                                                        Button declineButton = dialog1.findViewById(R.id.btn_cancel);
                                                        declineButton.setVisibility(View.GONE);
                                                        Button okButton = dialog1.findViewById(R.id.btn_ok);
                                                        okButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog1.dismiss();
                                                            }
                                                        });

                                                        MyFont myFont = new MyFont(mContext);
                                                        declineButton.setTypeface(myFont.setFont());
                                                        okButton.setTypeface(myFont.setFont());
                                                        title.setTypeface(myFont.setFont());
                                                        des.setTypeface(myFont.setFont());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                    }


                }else if(menuItem.getItemId()==R.id.coupon_1) {
                    numCounpoun=0;
                    numPrivilage=0;
                    couponAmount=0;
                    //coupon
                    Map<String, ?> entries1 = sp4.getAll();
                    Set<String> keys1 = entries1.keySet();
                    String[] getData1;
                    List<String> list1 = new ArrayList<String>(keys1);
                    for (String temp : list1) {
                        if (temp.trim().equals(mProductID.trim())) {
                            for (int i = 0; i < sp4.getStringSet(temp, null).size(); i++) {
                                getData1 = sp4.getStringSet(temp, null).toArray(new String[sp4.getStringSet(temp, null).size()]);
                                char chk = getData1[i].charAt(1);
                                if (chk == 'f') {
                                    numCounpoun = Integer.parseInt(getData1[i].substring(3));
                                }
                            }
                        }
                    }
                    /*//privilage
                    Map<String, ?> entries2 = sp2.getAll();
                    Set<String> keys2 = entries2.keySet();
                    String[] getData2;
                    List<String> list2 = new ArrayList<String>(keys2);
                    for (String temp : list2) {
                        if (temp.trim().equals(mProductID.trim())) {
                            for (int i = 0; i < sp2.getStringSet(temp, null).size(); i++) {
                                getData2 = sp2.getStringSet(temp, null).toArray(new String[sp2.getStringSet(temp, null).size()]);
                                char chk = getData2[i].charAt(1);
                                if (chk == 'f') {
                                    numPrivilage = Integer.parseInt(getData2[i].substring(3));
                                }
                            }
                        }
                    }*/

                    int chk=0;
                    if(numCounpoun+numPrivilage>=aa){
                        new MyToast(mContext, "ใช้บริการครบแล้ว", 0);
                        chk=1;
                    }else if(numCounpoun==0&&numPrivilage!=0){
                        couponAmount=aa - (numCounpoun+numPrivilage);
                    }else if(numPrivilage==0&&numCounpoun!=0){
                        couponAmount = aa - (numCounpoun+numPrivilage);
                    }
                    else {
                        couponAmount = aa;

                    }
                    if(chk!=1){
                        final ProgressDialog dialog = new ProgressDialog(mContext);
                        dialog.setIcon(R.mipmap.loading);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage("กรุณารอสักครู่....");
                        dialog.setIndeterminate(true);
                        dialog.show();
                        Ion.with(mContext)
                                .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/CoupounUse_1.php")
                                .setBodyParameter("ProductID", "" + items.get(g).mTextProductID)
                                .asJsonArray()
                                .setCallback(new FutureCallback<JsonArray>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonArray result) {
                                        dialog.dismiss();
                                        serviceType=0;
                                        for (int i = 0; i < result.size(); i++) {
                                            JsonObject jsonObject = (JsonObject) result.get(i);
                                            isActive = Integer.parseInt(jsonObject.get("IsActive").getAsString());
                                            coupon_count = Integer.parseInt(jsonObject.get("CouponCount").getAsString());
                                            serviceType = Integer.parseInt(jsonObject.get("ServiceType").getAsString());
                                        }
                                        System.out.println("coupon_1 : "+serType+", "+serviceType);
                                        if (Integer.parseInt(mSertype) != serviceType) {
                                            new MyToast(mContext, "ใช้ไม่ได้กับสินค้าประเภทนี้", 0);
                                        } else {
                                            final Dialog dialog = new Dialog(mContext);
                                            dialog.setContentView(R.layout.custom_privilage);
                                            dialog.show();
                                            Window window = dialog.getWindow();
                                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                            int id = Integer.parseInt(mProductID);
                                            Button b = dialog.findViewById(R.id.btn_ok);
                                            TextView title = dialog.findViewById(R.id.tv_quit_learning);

                                            final NumberPicker numberPicker = dialog.findViewById(R.id.number_picker);
                                            numberPicker.setMinValue(1);
                                            numberPicker.setMaxValue(couponAmount * coupon_count);
                                            numberPicker.setValue(1);
                                            numberPicker.setTypeface(myFont.setFont());

                                            title.setText("ใช้สำหรับ " + mProductTH + " " + coupon_count + " ใบ ต่อ 1 ชิ้น");

                                            b.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if (numberPicker.getValue() % coupon_count == 0) {
                                                        int i=(numberPicker.getValue()/coupon_count)+numCounpoun;
                                                        editor1 = sp4.edit();
                                                        mSet1 = new HashSet<>();
                                                        mSet1.add("<a>" + mProductID);
                                                        mSet1.add("<b>" + numberPicker.getValue());
                                                        mSet1.add("<c>" + mPrice);
                                                        mSet1.add("<d>" + mProductTH);
                                                        mSet1.add("<e>" + "0");
                                                        mSet1.add("<f>" + i);
                                                        editor1.putStringSet(mProductID, mSet1);
                                                        editor1.apply();
                                                        dialog.dismiss();
                                                        new MyToast(mContext, "บันทึกการใช้บริการแล้ว", 2);
                                                    } else {
                                                        final Dialog dialog1 = new Dialog(mContext);
                                                        dialog1.setContentView(R.layout.custon_alert_dialog);
                                                        dialog1.show();
                                                        TextView title = dialog1.findViewById(R.id.tv_quit_learning);
                                                        TextView des = dialog1.findViewById(R.id.tv_description);
                                                        title.setText("แจ้งเตือน");
                                                        des.setText("ใส่จำนวนคูปองไม่ตรงกันกับจำนวนสินค้า เช่น สินค้าที่ใช้คูปอง 2 ใบ จะไม่สามารถใส่คูปอง 1 ใบต่อสินค้านั้นได้");
                                                        Button declineButton = dialog1.findViewById(R.id.btn_cancel);
                                                        declineButton.setVisibility(View.GONE);
                                                        Button okButton = dialog1.findViewById(R.id.btn_ok);
                                                        okButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog1.dismiss();
                                                            }
                                                        });

                                                        MyFont myFont = new MyFont(mContext);
                                                        declineButton.setTypeface(myFont.setFont());
                                                        okButton.setTypeface(myFont.setFont());
                                                        title.setTypeface(myFont.setFont());
                                                        des.setTypeface(myFont.setFont());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                    }


                }else if(menuItem.getItemId()==R.id.privilege) {
                    numCounpoun=0;
                    numPrivilage=0;
                    couponAmount=0;
                    //coupon
                    Map<String, ?> entries1 = sp1.getAll();
                    Set<String> keys1 = entries1.keySet();
                    String[] getData1;
                    List<String> list1 = new ArrayList<String>(keys1);
                    for (String temp : list1) {
                        if (temp.trim().equals(mProductID.trim())) {
                            for (int i = 0; i < sp1.getStringSet(temp, null).size(); i++) {
                                getData1 = sp1.getStringSet(temp, null).toArray(new String[sp1.getStringSet(temp, null).size()]);
                                char chk = getData1[i].charAt(1);
                                if (chk == 'f') {
                                    numCounpoun = Integer.parseInt(getData1[i].substring(3));
                                }
                            }
                        }
                    }
                    //privilage
                    Map<String, ?> entries2 = sp2.getAll();
                    Set<String> keys2 = entries2.keySet();
                    String[] getData2;
                    List<String> list2 = new ArrayList<String>(keys2);
                    for (String temp : list2) {
                        if (temp.trim().equals(mProductID.trim())) {
                            for (int i = 0; i < sp2.getStringSet(temp, null).size(); i++) {
                                getData2 = sp2.getStringSet(temp, null).toArray(new String[sp2.getStringSet(temp, null).size()]);
                                char chk = getData2[i].charAt(1);
                                if (chk == 'f') {
                                    numPrivilage = Integer.parseInt(getData2[i].substring(3));
                                }
                            }
                        }
                    }

                    int chk=0;
                    if(numCounpoun+numPrivilage>=aa){
                        new MyToast(mContext, "ใช้บริการครบแล้ว", 0);
                        chk=1;
                    }else if(numCounpoun==0&&numPrivilage!=0){
                        couponAmount=aa - (numCounpoun+numPrivilage);
                    }else if(numPrivilage==0&&numCounpoun!=0){
                        couponAmount = aa - (numCounpoun+numPrivilage);
                    }
                    else {
                        couponAmount = aa;

                    }
                    if(chk!=1){
                        final ProgressDialog dialog = new ProgressDialog(mContext);
                        dialog.setIcon(R.mipmap.loading);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage("กรุณารอสักครู่....");
                        dialog.setIndeterminate(true);
                        dialog.show();
                        Ion.with(mContext)
                                .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/CoupounUse.php")
                                .setBodyParameter("ProductID", "" + items.get(g).mTextProductID)
                                .asJsonArray()
                                .setCallback(new FutureCallback<JsonArray>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonArray result) {
                                        dialog.dismiss();
                                        serviceType=0;
                                        for (int i = 0; i < result.size(); i++) {
                                            JsonObject jsonObject = (JsonObject) result.get(i);
                                            isActive = Integer.parseInt(jsonObject.get("IsActive").getAsString());
                                            coupon_count = Integer.parseInt(jsonObject.get("CouponCount").getAsString());
                                            serviceType = Integer.parseInt(jsonObject.get("ServiceType").getAsString());
                                        }
                                        if (Integer.parseInt(mSertype) != serviceType) {
                                            new MyToast(mContext, "ใช้ไม่ได้กับสินค้าประเภทนี้", 0);
                                        } else {
                                            final Dialog dialog = new Dialog(mContext);
                                            dialog.setContentView(R.layout.custom_privilage);
                                            dialog.show();
                                            Window window = dialog.getWindow();
                                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                            int id = Integer.parseInt(mProductID);
                                            Button b = dialog.findViewById(R.id.btn_ok);
                                            TextView title = dialog.findViewById(R.id.tv_quit_learning);

                                            final NumberPicker numberPicker = dialog.findViewById(R.id.number_picker);
                                            numberPicker.setMinValue(1);
                                            numberPicker.setMaxValue(couponAmount);
                                            numberPicker.setValue(1);
                                            numberPicker.setTypeface(myFont.setFont());

                                            int pri=20*coupon_count;
                                            title.setText("ใช้แลก 1 ชิ้น/ไม้แขวน "+pri+" อัน");

                                            b.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (numberPicker.getValue() % coupon_count == 0) {
                                                        int i = numberPicker.getValue() + numPrivilage;
                                                        editor2 = sp2.edit();
                                                        mSet2 = new HashSet<>();
                                                        mSet2.add("<a>" + mProductID);
                                                        mSet2.add("<b>" + numberPicker.getValue());
                                                        mSet2.add("<c>" + mPrice);
                                                        mSet2.add("<d>" + mProductTH);
                                                        mSet2.add("<e>" + "1");
                                                        mSet2.add("<f>" + i);
                                                        editor2.putStringSet(mProductID, mSet2);
                                                        editor2.apply();
                                                        dialog.dismiss();
                                                        new MyToast(mContext, "บันทึกการใช้บริการแล้ว", 2);
                                                    } else {
                                                        final Dialog dialog1 = new Dialog(mContext);
                                                        dialog1.setContentView(R.layout.custon_alert_dialog);
                                                        dialog1.show();
                                                        TextView title = dialog1.findViewById(R.id.tv_quit_learning);
                                                        TextView des = dialog1.findViewById(R.id.tv_description);
                                                        title.setText("แจ้งเตือน");
                                                        des.setText("ใส่จำนวนคูปองไม่ตรงกันกับจำนวนสินค้า เช่น สินค้าที่ใช้คูปอง 2 ใบ จะไม่สามารถใส่คูปอง 1 ใบต่อสินค้านั้นได้");
                                                        Button declineButton = dialog1.findViewById(R.id.btn_cancel);
                                                        declineButton.setVisibility(View.GONE);
                                                        Button okButton = dialog1.findViewById(R.id.btn_ok);
                                                        okButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog1.dismiss();
                                                            }
                                                        });

                                                        MyFont myFont = new MyFont(mContext);
                                                        declineButton.setTypeface(myFont.setFont());
                                                        okButton.setTypeface(myFont.setFont());
                                                        title.setTypeface(myFont.setFont());
                                                        des.setTypeface(myFont.setFont());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                    }

                }else if(menuItem.getItemId()==R.id.delete){

                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.custon_alert_dialog);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView title=dialog.findViewById(R.id.tv_quit_learning);
                    TextView des=dialog.findViewById(R.id.tv_description);
                    title.setText("แจ้งเตือน");
                    des.setText("คุณต้องการยกเลิกรายการนี้หรือไม่?");
                    Button declineButton = dialog.findViewById(R.id.btn_cancel);
                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button okButton =  dialog.findViewById(R.id.btn_ok);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyAdapterOrder.this.remove(MyAdapterOrder.this.getItem(g));
                            SharedPreferences sharedPreferences;
                            sharedPreferences=getContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
                            sharedPreferences.edit().remove(mProductID).commit();
                            sp1.edit().remove(mProductID).commit();

                            MyAdapterOrder.this.remove(position);
                            MyAdapterOrder.this.notifyDataSetChanged();
                            mListView.setAdapter(MyAdapterOrder.this);

                            laodData();
                            mTextTotal.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t "+getFormatedAmount(totalPrice)+" THB");

                            sp1.edit().clear().commit();
                            sp2.edit().clear().commit();

                            dialog.dismiss();
                        }
                    });
                    okButton.setTypeface(myFont.setFont());
                    declineButton.setTypeface(myFont.setFont());
                    title.setTypeface(myFont.setFont());
                    des.setTypeface(myFont.setFont());

                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void insert_coupon(String productID,String counponNum1,String productPrice,String productTH,int counponNum) {
        mDb = mSQLite.getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("ProductID",productID);
        cv.put("CouponNumber",counponNum1);
        cv.put("ProductPrice",productPrice);
        cv.put("ProductNameTH",productTH);
        cv.put("CouponNum",counponNum);
        mDb.insert("tb_coupon",null,cv);

        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
    public void laodData(){
        totalPrice=0;
        SharedPreferences sp=getContext().getSharedPreferences("ORDER", Context.MODE_PRIVATE);
        Map<String,?> entries2 = sp.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            for (int k = 0; k < sp.getStringSet(temp, null).size(); k++) {
                getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                char chk=getData2[k].charAt(1);
                if(chk=='c'){
                    listPrice.add(getData2[k].substring(3));
                }if(chk=='g'){
                    listNum.add(getData2[k].substring(3));
                }
            }
        }
        for(int i=0;i<listPrice.size();i++){
            totalPrice+=Integer.parseInt(listPrice.get(i))*Integer.parseInt(listNum.get(i));
        }
    }

}
