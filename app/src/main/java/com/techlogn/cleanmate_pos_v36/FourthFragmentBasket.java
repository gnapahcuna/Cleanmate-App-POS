package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aniket.mutativefloatingactionbutton.MutativeFab;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.libRG.CustomTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by anucha on 2/21/2018.
 */

public class FourthFragmentBasket extends Fragment {

    View v,v1;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView textData1,textData2,textData3,textData4,textData5,t1,t2,t3,t4,t5,t6,textData_Coupon,textData_Privilage
            ,textDataPrice;
    CustomTextView txtTotal1,txtTotal2,text_link_coupon,text_link_privilage,textDataSpecail;
    String dataID,branchID,branchGroup;
    int count=0;

    ArrayList<String> namePro,branchGroupPro,idPro,servicePro1,servicePro,couponValuePro1,couponValuePro,conditionPricePro,conditionPricePro1;

    int sum1=0,total1=0,total2=0,percent=0,sum_coupon=0;
    int c=0,sum_type=0;

    SharedPreferences sharedPreferences,sharedPreferences1,sharedPreferences2,sharedPreferences3,
            sharedPreferences4,sharedPreferences5,sharedPreferences6,sharedPreferences7,sharedPreferences8,sharedPreferences9;
    ArrayList<String>arr_proTH,arr_proEN,arr_serTH,arr_serEN,arr_price,arr_id,arr_img,arr_count,
            arr_color,arr_serID_test,arr_serID,arrCouponID,arrCouponNo,arrCouponPrice,arr_coupon_count;
    ArrayList<String>arr_express_proID,arr_express_count;
    String phone,name,nickname,appoint,typemember,id_member,comment;

    String dates,sOrderNo;

    ArrayList<String>arr_privilage_num, arr_privilage_proID,arr_price_privilage,arr_privilage_proTH
            ,arr_coupon_num, arr_coupon_proID,arr_price_coupon,arr_coupon_proTH,arrTestCounponNumber1;
    int sum_price_coupon=0,sum_price_privilage=0;

    int test=0,total_test=0;
    /*int test=0,total_test=0;

    ArrayList<Integer>arrService_1;
    int test522018357=0;*/


    boolean chkIsSale=false;

    int check_express=0;
    int isExpress=0,expressTotal=0;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private ArrayList<String>arrCoupounDiscount;

    ProgressDialog dialogupload;
    int getorderno=0;
    double promotion_price=0.00;
    String promotion_desc="";

    int fab_count=0;
    int counting=0;

    private GetIPAPI getIPAPI;
    RelativeLayout next;

    public FourthFragmentBasket() {
        // Required empty public constructor
    }

    public static FourthFragmentBasket newInstance() {

        Bundle args = new Bundle();
        FourthFragmentBasket fragment = new FourthFragmentBasket();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fourth, container, false);

        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/


        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm != null) {
            List<Fragment> fragments = fm.getFragments();
            for(int i = fragments.size() - 1; i >= 0; i--){
                Fragment fragment = fragments.get(i);
                if(fragment != null) {
                    if(fragment instanceof FourthFragmentBasket) {
                    }
                    break;
                }
            }
        }
        tabLayout=getActivity().findViewById(R.id.tablayout);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);



        sharedPreferences=getContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
        sharedPreferences1=getContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
        sharedPreferences2=getContext().getSharedPreferences("CouponValue", Activity.MODE_PRIVATE);
        sharedPreferences3=getContext().getSharedPreferences("Appoint", Activity.MODE_PRIVATE);
        sharedPreferences4=getContext().getSharedPreferences("ID", Activity.MODE_PRIVATE);
        sharedPreferences5=getContext().getSharedPreferences("Privilage", Activity.MODE_PRIVATE);
        sharedPreferences6=getContext().getSharedPreferences("CouponPoint", Activity.MODE_PRIVATE);
        sharedPreferences7=getContext().getSharedPreferences("Express", Activity.MODE_PRIVATE);
        sharedPreferences8=getContext().getSharedPreferences("Special", Activity.MODE_PRIVATE);
        sharedPreferences9=getContext().getSharedPreferences("CouponPoint_1", Activity.MODE_PRIVATE);

        textData1=v.findViewById(R.id.text_data1);
        textData2=v.findViewById(R.id.text_data2);
        textData3=v.findViewById(R.id.text_data3);
        textData4=v.findViewById(R.id.text_data4);
        textData5=v.findViewById(R.id.text_data5);
        textDataSpecail=v.findViewById(R.id.total_spacail);
        textDataSpecail.setText("");

        textDataPrice=v.findViewById(R.id.text_data5_price);
        txtTotal1=v.findViewById(R.id.total_price_1);
        txtTotal2=v.findViewById(R.id.total_price_2);

        txtTotal1.setText("");
        txtTotal2.setText("");


        t1=v.findViewById(R.id.text_text1);
        t2=v.findViewById(R.id.text_text2);
        t3=v.findViewById(R.id.text_text3);
        t4=v.findViewById(R.id.text_text4);
        t5=v.findViewById(R.id.text_text_privilage);
        t6=v.findViewById(R.id.text_text_coupon);

        textDataSpecail.setVisibility(View.GONE);

        textData_Privilage=v.findViewById(R.id.text_data_privilage);
        textData_Privilage.setText("");

        textData_Coupon=v.findViewById(R.id.text_data_specail);
        textData_Coupon.setText("");
        text_link_coupon=v.findViewById(R.id.view_coupon);
        text_link_privilage=v.findViewById(R.id.view_privilage);
        text_link_privilage.setVisibility(View.GONE);
        text_link_coupon.setVisibility(View.GONE);

        //textData4.setText("\n\t\t\t\tส่วนลดโปรโมชั่น");


        arr_proTH=new ArrayList<>();
        arr_proEN=new ArrayList<>();
        arr_serTH=new ArrayList<>();
        arr_serEN=new ArrayList<>();
        arr_price=new ArrayList<>();
        arr_id=new ArrayList<>();
        arr_img=new ArrayList<>();
        arr_count=new ArrayList<>();
        arr_color=new ArrayList<>();
        arr_serID_test=new ArrayList<>();
        arr_serID=new ArrayList<>();

        //ไม้แขวนเสื้อ
        arr_privilage_num=new ArrayList<>();
        arr_privilage_proID=new ArrayList<>();
        arr_price_privilage=new ArrayList<>();
        arr_privilage_proTH=new ArrayList<>();
        //คูปอง
        arr_coupon_count=new ArrayList<>();
        arr_coupon_num=new ArrayList<>();
        arr_coupon_proID=new ArrayList<>();
        arr_price_coupon=new ArrayList<>();
        arr_coupon_proTH=new ArrayList<>();

        arrTestCounponNumber1=new ArrayList<>();


        arr_express_proID=new ArrayList<>();
        arr_express_count=new ArrayList<>();

        namePro=new ArrayList<>();
        branchGroupPro=new ArrayList<>();
        idPro=new ArrayList<>();
        servicePro=new ArrayList<>();
        servicePro1=new ArrayList<>();
        couponValuePro=new ArrayList<>();
        couponValuePro1=new ArrayList<>();
        conditionPricePro=new ArrayList<>();
        conditionPricePro1=new ArrayList<>();


        /*dataPromotion=new ArrayList<>();
        dataPromotionPrice=new ArrayList<>();


        arrService_1=new ArrayList<>();*/


        arrCoupounDiscount=new ArrayList<>();

        MyFont myFont=new MyFont(getActivity());
        TextView t5=v.findViewById(R.id.textView5);
        TextView t7=v.findViewById(R.id.textView7);
        t7.setTypeface(myFont.setFont());
        t5.setTypeface(myFont.setFont());


        final MutativeFab mFab=v.findViewById(R.id.fab);
        mFab.setFabText("Special");
        mFab.setFabTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        mFab.setFabIcon(R.drawable.ic_loyalty_black_24dp);
        mFab.setFabBackgroundColor(ContextCompat.getColor(getActivity(), R.color.fab));
        mFab.setFabTextVisibility(View.GONE);
        mFab.getFabTextVisibility();
        mFab.setFabTypeface(myFont.setFont());


        Map<String, ?> entries_specail = sharedPreferences8.getAll();
        Set<String> keys_specail = entries_specail.keySet();
        String[] getData_specail;
        List<String> list_specail = new ArrayList<String>(keys_specail);
        for (String temp : list_specail) {
            for (int i = 0; i < sharedPreferences8.getStringSet(temp, null).size(); i++) {
                getData_specail = sharedPreferences8.getStringSet(temp, null).toArray(new String[sharedPreferences8.getStringSet(temp, null).size()]);
                char chk = getData_specail[i].charAt(1);
                if (chk == 'a') {
                    fab_count = Integer.parseInt(getData_specail[i].substring(3));
                }
            }
        }

        if(fab_count==0){
            textDataSpecail.setVisibility(View.GONE);
        }else{
            textDataSpecail.setVisibility(View.VISIBLE);
            textDataSpecail.setText("ส่วนลดพิเศษ "+fab_count+" บาท");
        }


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            int width=getResources().getDisplayMetrics().widthPixels;

            RelativeLayout back = v.findViewById(R.id.btn_scan);
            back.getLayoutParams().width = (width*45)/100;
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sharedPreferences.getAll().size() == 0) {
                        new MyToast(getActivity(), "ยังไม่มีรายการสินค้า", 0);
                    } else {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.custon_alert_dialog);
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView title = dialog.findViewById(R.id.tv_quit_learning);
                        TextView des = dialog.findViewById(R.id.tv_description);
                        title.setText("แจ้งเตือน");
                        des.setText("รายการทั้งหมดจะถูกลบและไม่สามารถเรียกกลับคืนได้");
                        Button declineButton = dialog.findViewById(R.id.btn_cancel);
                        //declineButton.setVisibility(View.GONE);
                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        Button okButton = dialog.findViewById(R.id.btn_ok);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sharedPreferences.edit().clear().apply();
                                sharedPreferences.edit().clear().apply();
                                sharedPreferences1.edit().clear().apply();
                                sharedPreferences2.edit().clear().apply();
                                sharedPreferences3.edit().clear().apply();
                                sharedPreferences5.edit().clear().apply();
                                sharedPreferences6.edit().clear().apply();
                                sharedPreferences7.edit().clear().apply();
                                sharedPreferences8.edit().clear().apply();
                                sharedPreferences9.edit().clear().apply();
                                SharedPreferences sp_bcpk=getActivity().getSharedPreferences("BarcodePK", Activity.MODE_PRIVATE);
                                sp_bcpk.edit().clear().apply();

                                sum1 = 0;
                                total1 = 0;
                                total2 = 0;
                                sum_coupon = 0;
                                percent = 0;
                                textData1.setText("");
                                textData2.setText("");
                                textData3.setText("");
                                textData4.setText("");
                                textData5.setText("");
                                text_link_coupon.setVisibility(View.GONE);
                                text_link_privilage.setVisibility(View.GONE);
                                textData_Coupon.setText("");
                                textData_Privilage.setText("");
                                txtTotal1.setText("รวมเงิน\t\t\t\t\t\t\t\t\t\t\t\t" + getFormatedAmount(total1) + " บาท");
                                txtTotal2.setText("ยอดชำระ\t\t\t\t\t\t\t\t\t\t\t\t" + getFormatedAmount(total2) + " บาท");
                                dialog.dismiss();



                                new MyToast(getActivity(), "ลบข้อมูลเรียบร้อย", 1);

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, FirstFragmentBasket.newInstance());
                                fragmentTransaction.commit();

                                TabLayout.Tab tab = tabLayout.getTabAt(0);
                                tab.select();
                            }
                        });
                        MyFont myFont = new MyFont(getActivity());
                        okButton.setTypeface(myFont.setFont());
                        declineButton.setTypeface(myFont.setFont());
                        title.setTypeface(myFont.setFont());
                        des.setTypeface(myFont.setFont());


                    }
                }
            });

            //Branch
            Map<String, ?> entries = sharedPreferences.getAll();
            Set<String> keys = entries.keySet();
            String[] getData;
            List<String> list = new ArrayList<String>(keys);
            for (String temp : list) {
                for (int i = 0; i < sharedPreferences.getStringSet(temp, null).size(); i++) {
                    getData = sharedPreferences.getStringSet(temp, null).toArray(new String[sharedPreferences.getStringSet(temp, null).size()]);

                    char chk = getData[i].charAt(1);
                    if (chk == 'a') {
                        arr_proTH.add(getData[i].substring(3));
                    } else if (chk == 'b') {
                        arr_proEN.add(getData[i].substring(3));
                    } else if (chk == 'c') {
                        arr_price.add(getData[i].substring(3));
                    } else if (chk == 'd') {
                        arr_serTH.add(getData[i].substring(3));
                    } else if (chk == 'e') {
                        arr_serEN.add(getData[i].substring(3));
                    } else if (chk == 'f') {
                        arr_img.add(getData[i].substring(3));
                    } else if (chk == 'g') {
                        arr_count.add(getData[i].substring(3).trim());
                    } else if (chk == 'h') {
                        arr_id.add(getData[i].substring(3));
                    } else if (chk == 'i') {
                        arr_color.add(getData[i].substring(3));
                    } else if (chk == 'j') {
                        arr_serID.add(getData[i].substring(3));
                    }
                }
            }

            //Is Branch
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
                    } else if (chk == 'e') {
                        branchGroup = getData4[i].substring(3);
                    }
                }
            }

            //Is Privilage
            Map<String, ?> entries_priv = sharedPreferences5.getAll();
            Set<String> keys_priv = entries_priv.keySet();
            String[] getData_priv;
            List<String> list_priv = new ArrayList<String>(keys_priv);
            for (String temp : list_priv) {
                for (int i = 0; i < sharedPreferences5.getStringSet(temp, null).size(); i++) {
                    getData_priv = sharedPreferences5.getStringSet(temp, null).toArray(new String[sharedPreferences5.getStringSet(temp, null).size()]);
                    char chk = getData_priv[i].charAt(1);
                    if (chk == 'a') {
                        arr_privilage_proID.add(temp + "," + getData_priv[i].substring(3));
                    } else if (chk == 'f') {
                        arr_privilage_num.add(temp + "," + getData_priv[i].substring(3));
                    } else if (chk == 'c') {
                        arr_price_privilage.add(temp + "," + getData_priv[i].substring(3));
                    } else if (chk == 'd') {
                        arr_privilage_proTH.add(temp + "," + getData_priv[i].substring(3));
                    }
                }
            }

            for (int i = 0; i < arr_price_privilage.size(); i++) {
                sum_price_privilage += Integer.parseInt(arr_price_privilage.get(i).substring(arr_price_privilage.get(i).indexOf(',') + 1)) *
                        Integer.parseInt(arr_privilage_num.get(i).substring(arr_privilage_num.get(i).indexOf(',') + 1));
            }
            System.out.println("sum_price_privilage : "+sum_price_privilage);

            //Is Coupon
            Map<String, ?> entries_coup = sharedPreferences6.getAll();
            Set<String> keys_coup = entries_coup.keySet();
            String[] getData_coup;
            List<String> list_coup = new ArrayList<String>(keys_coup);
            for (String temp : list_coup) {
                //System.out.println("Coupon1 : "+temp + " = " + sharedPreferences6.getStringSet(temp, null));
                for (int i = 0; i < sharedPreferences6.getStringSet(temp, null).size(); i++) {
                    getData_coup = sharedPreferences6.getStringSet(temp, null).toArray(new String[sharedPreferences6.getStringSet(temp, null).size()]);
                    //System.out.println("ไม้แขวนเสื้อ : "+temp + " : " + getData_priv[i]);
                    char chk = getData_coup[i].charAt(1);
                    if (chk == 'a') {
                        arr_coupon_proID.add(temp + "," + getData_coup[i].substring(3)+","+1);
                    }else if (chk == 'b') {
                        arr_coupon_count.add(temp + "," + getData_coup[i].substring(3));
                    } else if (chk == 'f') {
                        arr_coupon_num.add(temp + "," + getData_coup[i].substring(3));
                    } else if (chk == 'c') {
                        arr_price_coupon.add(temp + "," + getData_coup[i].substring(3));
                    } else if (chk == 'd') {
                        arr_coupon_proTH.add(temp + "," + getData_coup[i].substring(3)+",#cc0000");
                    }
                }
            }

            //Is Coupon //1
            Map<String, ?> entries_coup1 = sharedPreferences9.getAll();
            Set<String> keys_coup1 = entries_coup1.keySet();
            String[] getData_coup1;
            List<String> list_coup1 = new ArrayList<String>(keys_coup1);
            for (String temp : list_coup1) {
                //System.out.println("Coupon2 : "+temp + " = " + sharedPreferences9.getStringSet(temp, null));
                for (int i = 0; i < sharedPreferences9.getStringSet(temp, null).size(); i++) {
                    getData_coup1 = sharedPreferences9.getStringSet(temp, null).toArray(new String[sharedPreferences9.getStringSet(temp, null).size()]);
                    //System.out.println("ไม้แขวนเสื้อ : "+temp + " : " + getData_priv[i]);
                    char chk = getData_coup1[i].charAt(1);
                    if (chk == 'a') {
                        arr_coupon_proID.add(temp + "," + getData_coup1[i].substring(3)+","+3);
                    }else if (chk == 'b') {
                        arr_coupon_count.add(temp + "," + getData_coup1[i].substring(3));
                    } else if (chk == 'f') {
                        arr_coupon_num.add(temp + "," + getData_coup1[i].substring(3));
                    } else if (chk == 'c') {
                        arr_price_coupon.add(temp + "," + getData_coup1[i].substring(3));
                    } else if (chk == 'd') {
                        arr_coupon_proTH.add(temp + "," + getData_coup1[i].substring(3)+",#00cc99");
                    }
                }
            }

            for(int i=0;i<arr_coupon_num.size();i++){
                String[] splits=arr_coupon_proID.get(i).split(",");
                String[] counts=arr_coupon_num.get(i).split(",");
                for(int j=0;j<Integer.parseInt(counts[1]);j++){
                    arrTestCounponNumber1.add("000000,"+splits[2]);
                }
            }

            for (int i = 0; i < arr_price_coupon.size(); i++) {
                sum_price_coupon += Integer.parseInt(arr_price_coupon.get(i).substring(arr_price_coupon.get(i).indexOf(',') + 1)) *
                        Integer.parseInt(arr_coupon_num.get(i).substring(arr_coupon_num.get(i).indexOf(',') + 1));
            }
            sum1 = 0;
            for (int i = 0; i < arr_id.size(); i++) {
                sum1 += Integer.parseInt(arr_price.get(i)) * Integer.parseInt(arr_count.get(i));
            }

            //ซักแห้ง
            /*for (int i = 0; i < arrService_1.size(); i++) {
                for (int j = 0; j < arr_serID.size(); j++) {
                    if (arrService_1.get(i) == Integer.parseInt(arr_id.get(j))) {
                        test522018357 += Integer.parseInt(arr_price.get(j).trim()) * Integer.parseInt(arr_count.get(j).trim());
                        //System.out.println("ID ser 1 : "+arr_id.get(j)+" , "+arr_price.get(j));
                    }
                }
            }
            sum1 = 0;
            sum_type = 0;
            int sum_total = 0;*/

            /*//Promotion 1เมษา-31พฤษภา61
            int total_price = 0;
            int a1 = 0;
            //Promotion June
            int june = 0;
            august_price = 0.00;
            double august1=0.00;
            double august2=0.00;
            int august3=0;
            String text_promotion1 = "";
            String text_promotion2 = "";
            boolean check1 = false, check2 = false;
            for (int i = 0; i < arr_id.size(); i++) {
                sum1 += Integer.parseInt(arr_price.get(i)) * Integer.parseInt(arr_count.get(i));
                //promotion เดือนมิถุนายน
                if (Integer.parseInt(arr_id.get(i)) == 1||Integer.parseInt(arr_id.get(i)) == 3||Integer.parseInt(arr_id.get(i)) == 16||Integer.parseInt(arr_id.get(i)) == 212) {
                    text_promotion1 = "ชุดสูท 199 บาท";
                }
                if(Integer.parseInt(arr_id.get(i)) == 5||Integer.parseInt(arr_id.get(i)) == 6||Integer.parseInt(arr_id.get(i)) == 7||
                        Integer.parseInt(arr_id.get(i)) == 8||Integer.parseInt(arr_id.get(i)) == 218){
                    text_promotion2 = "เสื้อแจ็กเก็ต 130 บาท";
                }

                if (Integer.parseInt(arr_id.get(i)) == 1||Integer.parseInt(arr_id.get(i)) == 3||Integer.parseInt(arr_id.get(i)) == 16||Integer.parseInt(arr_id.get(i)) == 212) {
                    august1 += 199*Integer.parseInt(arr_count.get(i));
                    august3+=Integer.parseInt(arr_price.get(i)) * Integer.parseInt(arr_count.get(i));
                }
                if(Integer.parseInt(arr_id.get(i)) == 5||Integer.parseInt(arr_id.get(i)) == 6||Integer.parseInt(arr_id.get(i)) == 7||
                        Integer.parseInt(arr_id.get(i)) == 8||Integer.parseInt(arr_id.get(i)) == 218){
                    august2 += 130*Integer.parseInt(arr_count.get(i));
                    august3+=Integer.parseInt(arr_price.get(i)) * Integer.parseInt(arr_count.get(i));
                }

            }
            august_price=august3-(august1+august2);
            textDataPrice.setText((int) august1 + " บาท\n"+getFormatedAmount((int)august2)+ " บาท");
            textData5.setText(text_promotion1 + "\n"+text_promotion2);
            //System.out.println(sum1+", promotion เดือนสิงหา : " + august_price+", "+august3);


            if ((int) august_price == 0) {
                textDataPrice.setText("");
                textData5.setText("");
            } else {
                if((int)august2==0){
                    textDataPrice.setText((int) august1 + " บาท");
                    textData5.setText(text_promotion1);
                }else if((int)august1==0){
                    textData5.setText(text_promotion2);
                    textDataPrice.setText((int)august2+ " บาท");
                }else{
                    textDataPrice.setText((int) august1 + " บาท\n"+getFormatedAmount((int)august2)+ " บาท");
                    textData5.setText(text_promotion1 + "\n"+text_promotion2);
                }

            }*/


            //appoint

            Map<String, ?> entries3 = sharedPreferences3.getAll();
            Set<String> keys3 = entries3.keySet();
            String[] getData3;
            List<String> list3 = new ArrayList<String>(keys3);
            for (String temp : list3) {
                //System.out.println(temp+" = "+sharedPreferences.getStringSet(temp,null));
                for (int i = 0; i < sharedPreferences3.getStringSet(temp, null).size(); i++) {
                    getData3 = sharedPreferences3.getStringSet(temp, null).toArray(new String[sharedPreferences3.getStringSet(temp, null).size()]);
                    //System.out.println(temp + " : " + getData3[i]);
                    char chk = getData3[i].charAt(1);
                    //System.out.println("Check : " + chk);
                    if (chk == 'a') {
                        appoint = getData3[i].substring(3);
                    } else if (chk == 'b') {
                        comment = getData3[i].substring(3);
                    }
                }
            }

            //System.out.println("วันนัดรับ : "+appoint);


            final Map<String, ?> entries_express = sharedPreferences7.getAll();
            Set<String> keys_express = entries_express.keySet();
            String[] getData_express;
            List<String> list_express = new ArrayList<String>(keys_express);
            for (String temp : list_express) {
                for (int i = 0; i < sharedPreferences7.getStringSet(temp, null).size(); i++) {
                    getData_express = sharedPreferences7.getStringSet(temp, null).toArray(new String[sharedPreferences7.getStringSet(temp, null).size()]);
                    char chk = getData_express[i].charAt(1);
                    if (chk == 'a') {
                        check_express = Integer.parseInt(getData_express[i].substring(3));
                    } else if (chk == 'b') {
                        expressTotal = Integer.parseInt(getData_express[i].substring(3));
                    }
                }
            }
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            final String date_today = "" + df.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());
            Calendar calendar = Calendar.getInstance();


            if (check_express == 2) {
                isExpress = 1;
                sum1 = expressTotal;
                calendar.add(Calendar.DAY_OF_YEAR, 0);
            } else if (check_express == 1) {
                isExpress = 1;
                sum1 = expressTotal;
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            } else {
                isExpress = 0;
                sum1 = sum1;
                calendar.add(Calendar.DAY_OF_YEAR, 3);
            }

            //members
            Map<String, ?> entries1 = sharedPreferences1.getAll();
            Set<String> keys1 = entries1.keySet();
            String[] getData1;
            List<String> list1 = new ArrayList<String>(keys1);
            for (String temp : list1) {
                //System.out.println(temp+" = "+sharedPreferences.getStringSet(temp,null));
                for (int i = 0; i < sharedPreferences1.getStringSet(temp, null).size(); i++) {
                    getData1 = sharedPreferences1.getStringSet(temp, null).toArray(new String[sharedPreferences1.getStringSet(temp, null).size()]);
                    //System.out.println(temp + " : " + getData1[i]);
                    char chk = getData1[i].charAt(1);
                    //System.out.println("Check : " + chk);
                    if (chk == 'a') {
                        phone = getData1[i].substring(3);
                    } else if (chk == 'b') {
                        name = getData1[i].substring(3);
                    } else if (chk == 'c') {
                        nickname = getData1[i].substring(3);
                    } else if (chk == 'd') {
                        typemember = getData1[i].substring(3);
                    } else if (chk == 'e') {
                        id_member = getData1[i].substring(3);
                    }
                }
            }
            int aaSer1 = 0, aaSer3 = 0, per1, per2;
            if (typemember == null) {
                percent = 0;
            } else {
                if (typemember.trim().equals("1") && check_express != 2 && check_express != 1) {
                    //percent = (sum1 * 10) / 100;

                    for (int i = 0; i < arr_serTH.size(); i++) {
                        if (arr_serTH.get(i).trim().equals("ซักแห้ง")) {
                            aaSer1 += Integer.parseInt(arr_price.get(i)) * Integer.parseInt(arr_count.get(i));
                        } else if (arr_serTH.get(i).trim().equals("สปาเครื่องหนัง")) {
                            aaSer3 += Integer.parseInt(arr_price.get(i)) * Integer.parseInt(arr_count.get(i));
                        }
                    }

                    per1 = (aaSer1 * 10) / 100;
                    per2 = (aaSer3 * 10) / 100;

                    //System.out.println(aaSer1+", "+aaSer3+" = "+per1+", "+per2);

                    percent = per1 + per2;
                } else if (check_express == 2 || check_express == 1) {
                    percent = 0;
                }
            }
            if (chkIsSale == true) {
                percent = 0;
                chkIsSale = false;
            }


            arrCouponID = new ArrayList<>();
            arrCouponNo = new ArrayList<>();
            arrCouponPrice = new ArrayList<>();
            //coupon

            Map<String, ?> entries2 = sharedPreferences2.getAll();
            Set<String> keys2 = entries2.keySet();
            String[] getData2;
            List<String> list2 = new ArrayList<String>(keys2);
            for (String temp : list2) {
                // System.out.println(temp+" = "+sharedPreferences.getStringSet(temp,null));
                for (int i = 0; i < sharedPreferences2.getStringSet(temp, null).size(); i++) {
                    getData2 = sharedPreferences2.getStringSet(temp, null).toArray(new String[sharedPreferences2.getStringSet(temp, null).size()]);
                    //System.out.println(temp + " : " + getData2[i]);
                    char chk = getData2[i].charAt(1);
                    //System.out.println("Check : " + chk);
                    if (chk == 'a') {
                        arrCouponID.add(getData2[i].substring(3));
                    } else if (chk == 'b') {
                        //arrCouponPrice.add(getData2[i].substring(3));
                        sum_coupon+=Integer.parseInt(getData2[i].substring(3));
                    }
                }
            }
            TextView ti = v.findViewById(R.id.textTitle);
            textData1.setTypeface(myFont.setFont());
            textData2.setTypeface(myFont.setFont());
            textData3.setTypeface(myFont.setFont());
            textData4.setTypeface(myFont.setFont());
            textData5.setTypeface(myFont.setFont());
            textDataPrice.setTypeface(myFont.setFont());
            txtTotal1.setTypeface(myFont.setFont());
            txtTotal2.setTypeface(myFont.setFont());
            t1.setTypeface(myFont.setFont());
            t2.setTypeface(myFont.setFont());
            t3.setTypeface(myFont.setFont());
            t4.setTypeface(myFont.setFont());
            ti.setTypeface(myFont.setFont());
            t5.setTypeface(myFont.setFont());
            t6.setTypeface(myFont.setFont());
            textData_Privilage.setTypeface(myFont.setFont());
            textData_Coupon.setTypeface(myFont.setFont());
            text_link_coupon.setTypeface(myFont.setFont());
            text_link_privilage.setTypeface(myFont.setFont());

            //System.out.println((int)august_price);
            next = v.findViewById(R.id.layoutClick_addcart);
            next.getLayoutParams().width = (getResources().getDisplayMetrics().widthPixels * 45) / 100;

            List<Element> elements = new ArrayList<Element>();
            for (int i = 0; i < arr_price.size(); i++) {
                elements.add(new Element(i, Integer.parseInt(arr_price.get(i))));
            }

            ArrayList<String>arr_id_promo=new ArrayList<>();
            ArrayList<String>arr_serID_promo=new ArrayList<>();
            ArrayList<String>arr_count_promo=new ArrayList<>();
            ArrayList<String>arr_price_promo=new ArrayList<>();
            int Total_promo=0,Total_promo_price=0;
            // Sort and print
            Collections.sort(elements);
            Collections.reverse(elements); // If you want reverse order
            for (Element element : elements) {
                Total_promo+=Integer.parseInt(arr_count.get(element.index));
                Total_promo_price+=Integer.parseInt(arr_price.get(element.index))*Integer.parseInt(arr_count.get(element.index));
                arr_id_promo.add(arr_id.get(element.index));
                arr_serID_promo.add(arr_serID.get(element.index));
                arr_count_promo.add(arr_count.get(element.index));
                arr_price_promo.add(arr_price.get(element.index));

                //System.out.println("["+arr_id.get(element.index)+"] => "+arr_count.get(element.index));
            }
            final JsonArray jsonArray=new JsonArray();
            final String date_promo = "" + df.format("yyyy-MM-dd", new java.util.Date());
            for(int i=0;i<arr_id_promo.size();i++){
                for (int j = 0; j < Integer.parseInt(arr_count_promo.get(i)); j++) {
                    //System.out.println(arr_id_promo.get(i)+" == "+arr_serID_promo.get(i));
                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("ProductID",arr_id_promo.get(i));
                    jsonObject.addProperty("ServiceType",arr_serID_promo.get(i));
                    jsonObject.addProperty("BranchID",branchID);
                    jsonObject.addProperty("BranchGroupID",branchGroup);
                    jsonObject.addProperty("CustomerID",id_member);
                    jsonObject.addProperty("Total",Total_promo);
                    jsonObject.addProperty("TotalPrice",Total_promo_price);
                    jsonArray.add(jsonObject);
                }
            }

            //System.out.println("js : "+jsonArray);
            //getPromotion
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setIcon(R.mipmap.loading);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กรุณารอสักครู่....");
            dialog.setIndeterminate(true);
            dialog.show();
            Ion.with(getActivity())
                    .load("http://119.59.115.80/PromotionTest/Promotion.php")
                    .setJsonArrayBody(jsonArray)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            String sPro1 = "", sPro2 = "";

                            for (int k = 0; k < result.size(); k++) {
                                JsonObject jsonObject = (JsonObject) result.get(k);
                                System.out.println("promo : "+jsonObject.get("Price").getAsString());
                                System.out.println("desc : "+jsonObject.get("Description").getAsString());
                                if (Double.parseDouble(jsonObject.get("Price").getAsString().trim())!=100001) {
                                    promotion_desc = jsonObject.get("Description").getAsString();
                                    promotion_price += Double.parseDouble(jsonObject.get("Price").getAsString());
                                    //System.out.println(jsonObject.get("Description").getAsString());
                                    //System.out.println(jsonObject.get("Price").getAsString());
                                    sPro1 += promotion_desc + "\n";
                                    sPro2 += getFormatedAmount((int) Double.parseDouble(jsonObject.get("Price").getAsString())) + " บาท\n";
                                }
                            }

                            textDataPrice.setText(sPro2);
                            textData5.setText(sPro1);
                            try {
                                if (typemember == null || isExpress == 1) {
                                    textData5.setVisibility(View.GONE);
                                    textDataPrice.setVisibility(View.GONE);
                                    total_test = (sum1 - sum_coupon - sum_price_privilage) - (int) promotion_price;
                                    //System.out.println("total2 : "+1);
                                } else if (typemember.trim().equals("1")) {
                                    //System.out.println("ประเภทลูกค้า : "+typemember);
                                    //if (percent > (int) promotion_price) {
                                        //total_test = sum1 - sum_coupon - percent - sum_price_privilage;
                                    //} else {
                                        total_test = sum1 - sum_coupon - sum_price_privilage - (int) promotion_price;
                                    //}
                                    //System.out.println("total2 : "+2);
                                } else {
                                    total_test = sum1 - sum_coupon - sum_price_privilage - (int) promotion_price;
                                    // System.out.println("total2 : "+3);
                                }
                                total2 = total_test - sum_price_coupon - fab_count;

                                //total2=total_test-sum_coupon-totalCouponPrice-sum_price_privilage-(int)june_price;


                                textData1.setText(getFormatedAmount(sum1) + " บาท");
                                if (sum_coupon == 0) {
                                    textData2.setText(" - ");
                                } else {
                                    textData2.setText(getFormatedAmount(sum_coupon) + " บาท");
                                }
                                if (percent == 0) {
                                    textData3.setText(" - ");
                                    textData5.setVisibility(View.VISIBLE);
                                    textDataPrice.setVisibility(View.VISIBLE);
                                } else {
                                    /*if (percent > (int) promotion_price) {
                                        textData3.setText(getFormatedAmount(percent) + " บาท");
                                        textData5.setVisibility(View.GONE);
                                        textDataPrice.setVisibility(View.GONE);
                                    } else {*/
                                        textData3.setVisibility(View.GONE);
                                        textData5.setVisibility(View.VISIBLE);
                                        textDataPrice.setVisibility(View.VISIBLE);
                                        //textDataPrice.setText((int) june_price + " บาท");
                                        //textData5.append(text_promotion + "\n\n");
                                    //}
                                }
                                if (sum_price_privilage == 0) {
                                    textData_Privilage.setText(" - ");
                                    text_link_privilage.setVisibility(View.GONE);
                                } else {
                                    int countPrivilage = 0;
                                    for (int i = 0; i < arr_privilage_proID.size(); i++) {
                                        TextView textPro = new TextView(getActivity());
                                        textPro.setTextSize(18);
                                        countPrivilage += Integer.parseInt(arr_privilage_num.get(i).substring(arr_privilage_num.get(i).indexOf(',') + 1));

                                    }
                                    textData_Privilage.setText(countPrivilage + " ตัว");
                                    text_link_privilage.setVisibility(View.VISIBLE);

                                }

                                if (sum_price_coupon == 0) {
                                    textData_Coupon.setText(" - ");
                                    text_link_coupon.setVisibility(View.GONE);
                                    //txtTotal1.setVisibility(View.GONE);
                                } else {
                                    int countCoupon = 0;
                                    for (int i = 0; i < arr_coupon_proID.size(); i++) {
                                        TextView textPro = new TextView(getActivity());
                                        textPro.setTextSize(18);
                                        countCoupon += Integer.parseInt(arr_coupon_count.get(i).substring(arr_coupon_count.get(i).indexOf(',') + 1));
                                    }
                                    textData_Coupon.setText(countCoupon + " ใบ");
                                    text_link_coupon.setVisibility(View.VISIBLE);
                                    //txtTotal1.setVisibility(View.VISIBLE);

                                }
                                txtTotal1.setText("รวมเงิน\t\t\t\t\t\t\t" + getFormatedAmount(total_test) + " บาท");
                                textData4.setText(" - ");
                                txtTotal2.setText("เหลือจ่ายด้วยเงินสด\t\t\t\t\t\t" + getFormatedAmount(total2) + " บาท");


                            } catch (Exception ex) {
                                total1 = 0;
                                total2 = 0;
                                textData1.setText(getFormatedAmount(sum1) + " บาท");
                                textData2.setText(" - ");
                                textData3.setText(" - ");
                                textData4.setText(" - ");
                                textData_Privilage.setText(" - ");
                                txtTotal1.setText("รวมเงิน\t\t\t\t\t\t\t" + getFormatedAmount(total_test) + " บาท");
                                txtTotal2.setText("เหลือจ่ายด้วยเงินสด\t\t\t\t\t\t\t\t" + getFormatedAmount(total2) + " บาท");
                                int countCoupon = 0;
                                for (int i = 0; i < arr_coupon_proID.size(); i++) {
                                    TextView textPro = new TextView(v.getContext());
                                    textPro.setTextSize(18);
                                    countCoupon += Integer.parseInt(arr_coupon_num.get(i).substring(arr_coupon_num.get(i).indexOf(',') + 1));
                                }
                                textData_Coupon.setText(countCoupon + " ใบ");
                            }
                            //view Coupon
                            text_link_coupon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyFont myFont = new MyFont(getActivity());
                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.setContentView(R.layout.custom_view_coupon);
                                    dialog.show();
                                    Window window = dialog.getWindow();
                                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                    LinearLayout layout = dialog.findViewById(R.id.rl_view);
                                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams1.setMargins(0, 5, 0, 5);
                                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                                    title.setText("จ่ายด้วยคูปองชุด : " + sum_price_coupon + " บาท");
                                    for (int i = 0; i < arr_coupon_proTH.size(); i++) {
                                        TextView textPro = new TextView(getActivity());
                                        textPro.setTextSize(18);
                                        String[] splits = arr_coupon_proTH.get(i).split(",");
                                        textPro.setText("" + splits[1] +
                                                " " + arr_coupon_count.get(i).substring(arr_coupon_count.get(i).indexOf(',') + 1) + " ใบ");
                                        textPro.setTextColor(Color.parseColor(splits[2]));
                                        textPro.setGravity(Gravity.CENTER);
                                        layout.addView(textPro, layoutParams1);
                                        textPro.setTypeface(myFont.setFont());
                                        title.setTypeface(myFont.setFont());
                                    }
                                    title.setTypeface(myFont.setFont());

                                }
                            });
                            text_link_privilage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    MyFont myFont = new MyFont(getActivity());

                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.setContentView(R.layout.custom_view_coupon);
                                    dialog.show();
                                    Window window = dialog.getWindow();
                                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                    LinearLayout layout = dialog.findViewById(R.id.rl_view);
                                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams1.setMargins(0, 5, 0, 5);
                                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                                    title.setText("ใช้ไม้แขวนเสื้อบริการฟรี : " + sum_price_privilage + " บาท");
                                    for (int i = 0; i < arr_privilage_proTH.size(); i++) {
                                        TextView textPro = new TextView(getActivity());
                                        textPro.setTextSize(18);
                                        String[] splits = arr_privilage_proTH.get(i).split(",");
                                        textPro.setText(arr_privilage_proTH.get(i).substring(arr_privilage_proTH.get(i).indexOf(',') + 1) +
                                                " " + arr_privilage_num.get(i).substring(arr_privilage_num.get(i).indexOf(',') + 1) + " ตัว");
                                        textPro.setGravity(Gravity.CENTER);
                                        textPro.setTextColor(Color.parseColor("#cc0000"));
                                        layout.addView(textPro, layoutParams1);
                        /*if(i==arr_privilage_proID.size()-1){
                            Button b=new Button(getActivity());
                            b.setTextSize(18);
                            b.setText("ตกลง");
                            b.setTextColor(Color.parseColor("#FFFFFF"));
                            b.setTypeface(myFont.setFont());
                            b.setBackground(getResources().getDrawable(R.drawable.relative_layout_background));
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            layout.addView(b, layoutParams1);
                        }*/

                                        textPro.setTypeface(myFont.setFont());
                                        title.setTypeface(myFont.setFont());
                                    }

                                }
                            });


                            if (total2 == 0) {
                                mFab.setVisibility(View.GONE);
                            } else {
                                mFab.setVisibility(View.VISIBLE);
                            }
                            mFab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (total2 > 0) {

                                        mFab.setFabTextVisibility(View.VISIBLE);
                                        fab_count = 0;

                                        //if(counting==2){
                                        final Dialog dialog = new Dialog(getActivity());
                                        dialog.setContentView(R.layout.custom_special);
                                        dialog.show();
                                        Window window = dialog.getWindow();
                                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                        TextView title = dialog.findViewById(R.id.tv_quit_learning);
                                        TextView thb = dialog.findViewById(R.id.text_thb);
                                        thb.setVisibility(View.GONE);
                                        final EditText amount = dialog.findViewById(R.id.specail_amount);
                                        amount.setHint("ใส่จำนวนเงิน.");
                                        title.setText("ส่วนลดพิเศษ");
                                        Button declineButton = dialog.findViewById(R.id.btn_cancel);
                                        declineButton.setVisibility(View.GONE);
                                        declineButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                mFab.setFabTextVisibility(View.GONE);
                                            }
                                        });
                                        Button okButton = dialog.findViewById(R.id.btn_ok);
                                        okButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (amount.getText().toString().isEmpty()) {
                                                    //dialog.dismiss();
                                                    new MyToast(getActivity(), "กรุณากรอกจำนวนเงินส่วนลดก่อน", 0);
                                                } else {
                                                    dialog.dismiss();
                                                    mFab.setFabTextVisibility(View.GONE);

                                                    int total = Integer.parseInt(amount.getText().toString());

                                                    SharedPreferences.Editor editor1 = sharedPreferences8.edit();
                                                    HashSet<String> mSet1 = new HashSet<>();
                                                    mSet1.add("<a>" + total);
                                                    editor1.putStringSet("specail", mSet1);
                                                    editor1.apply();

                                                    fab_count = total;

                                                    if (fab_count != 0) {
                                                        textDataSpecail.setVisibility(View.VISIBLE);
                                                        total2 = total_test - sum_price_coupon - fab_count;
                                                        txtTotal2.setText("เหลือจ่ายด้วยเงินสด\t\t\t\t\t\t" + getFormatedAmount(total2) + " บาท");
                                                        textDataSpecail.setText("ส่วนลดพิเศษ " + fab_count + " บาท");
                                                    }
                                                }

                                            }
                                        });
                                        MyFont myFont = new MyFont(getActivity());
                                        okButton.setTypeface(myFont.setFont());
                                        declineButton.setTypeface(myFont.setFont());
                                        title.setTypeface(myFont.setFont());

                                        mFab.setFabTextVisibility(View.GONE);
                                        counting = 0;
                                    } else {
                                        new MyToast(getActivity(), "ยอดที่ต้องชำระเป็นศูนย์ ไม่สามารถใช้เมนูนี้ได้", 0);
                                    }
                                }
                            });

                            //System.out.println(name+","+phone+","+appoint+","+id_member);
                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sum1 > 0) {
                                        if (name != null && phone != null && id_member != null && appoint != null) {
                                            final Dialog dialog = new Dialog(getActivity());
                                            dialog.setContentView(R.layout.custon_alert_dialog);
                                            dialog.show();
                                            Window window = dialog.getWindow();
                                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                            TextView title = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView des = dialog.findViewById(R.id.tv_description);
                                            title.setText("แจ้งเตือน");
                                            des.setText("ลูกค้าต้องการชำระเงินในรูปแบบ");
                                            Button declineButton = dialog.findViewById(R.id.btn_cancel);
                                            Button okButton = dialog.findViewById(R.id.btn_ok);
                                            if (total2 == 0) {
                                                declineButton.setVisibility(View.GONE);
                                                okButton.setText("ชำระแล้ว");
                                                //CheckTypePayment=true;
                                            } else {
                                                okButton.setText("ชำระตอนนี้");
                                                declineButton.setVisibility(View.VISIBLE);
                                            }
                                            declineButton.setText("ค้างชำระ");
                                            declineButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogupload = new ProgressDialog(getActivity());
                                                    dialogupload.setIcon(R.mipmap.ic_launcher);
                                                    dialogupload.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    dialogupload.setMessage("กำลังตรวจสอบข้อมูล");
                                                    dialogupload.setIndeterminate(true);
                                                    dialogupload.setCancelable(false);
                                                    dialogupload.show();
                                                    final Handler handler = new Handler();
                                                    Runnable runnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (total_test > 0) {
                                                                if (name != null && phone != null && id_member != null) {
                                                                    int totals = total2 + fab_count;
                                                                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                                                                    dates = "" + df.format("yyyy-MM-dd", new java.util.Date());
                                                                    String datesCreate = "" + df.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());
                                                                    String url = getIPAPI.IPAddress + "/CleanmatePOS/AddOrder.php?OrderDate=" + dates;
                                                                    url += "&CustomerID=" + id_member;
                                                                    url += "&BranchID=" + branchID;
                                                                    url += "&AppointmentDate=" + appoint;
                                                                    url += "&CreatedDate=" + datesCreate;
                                                                    url += "&CreatedBy=" + dataID;
                                                                    url += "&NetAmount=" + totals;
                                                                    int a = 0;
                                                                    if (percent > (promotion_price) || isExpress == 1) {
                                                                        url += "&PromoDiscount=" + a;
                                                                    } else {
                                                                        url += "&PromoDiscount=" + (int) promotion_price;
                                                                    }
                                                                    url += "&CouponDiscount=" + sum_coupon;
                                                                    int b = 0;
                                                                    if (percent > (promotion_price)) {
                                                                        url += "&MemberDiscount=" + percent;
                                                                    } else {
                                                                        url += "&MemberDiscount=" + b;
                                                                    }
                                                                    url += "&IsPayment=" + 0;
                                                                    url += "&PaymentDate=" + "";
                                                                    url += "&IsExpress=" + isExpress;
                                                                    url += "&IsExpressLevel=" + check_express;
                                                                    url += "&SpecialDiscount=" + fab_count;
                                                                    String url2 = "";
                                                                    if (arrTestCounponNumber1.size() > 0) {

                                                                        //new MyAsyncTask().execute(url, url1, url2);
                                                                        new MyAsyncTask().execute(url, "add_order", "A");
                                                                        String url1 = "";
                                                                        for (int i = 0; i < arr_count.size(); i++) {
                                                                            for (int j = 0; j < Integer.parseInt(arr_count.get(i)); j++) {
                                                                                url1 = getIPAPI.IPAddress + "/CleanmatePOS/AddOrderDetailBeta.php?ProductID=" + arr_id.get(i);
                                                                                url1 += "&ProductNameTH=" + arr_proTH.get(i);
                                                                                url1 += "&ProductNameEN=" + arr_proEN.get(i);
                                                                                url1 += "&ServiceNameTH=" + arr_serTH.get(i);
                                                                                url1 += "&ServiceNameEN=" + arr_serEN.get(i);
                                                                                url1 += "&Amount=" + arr_price.get(i);
                                                                                url1 += "&SpecialDetial=" + "แขวน";
                                                                                url1 += "&CreatedDate=" + dates;
                                                                                url1 += "&CreatedBy=" + dataID.trim();
                                                                                url1 += "&AppointmentDate=" + appoint;
                                                                                url1 += "&BranchID=" + branchID;
                                                                                //url1 += "&Count=" + arr_count.get(i);
                                                                                new MyAsyncTask().execute(url1, "add_orderdetail", "A");
                                                                            }
                                                                        }
                                                                        System.out.println("คูปอง 1 : " + arrTestCounponNumber1.size());
                                                                        for (int i = 0; i < arrTestCounponNumber1.size(); i++) {
                                                                            String[] splits = arrTestCounponNumber1.get(i).split(",");
                                                                            url2 = getIPAPI.IPAddress + "/CleanmatePOS/CouponDiscount_1.php?CouponDiscountNo=" + splits[0] + "&BranchID=" + branchID + "&Type=" + splits[1];
                                                                            new MyAsyncTask().execute(url2, "add_coupon", "A");
                                                                        }
                                                                    } else {
                                                                        url2 = "not";
                                                                        new MyAsyncTask().execute(url, "add_order", "A");
                                                                        String url1 = "";
                                                                        for (int i = 0; i < arr_count.size(); i++) {
                                                                            for (int j = 0; j < Integer.parseInt(arr_count.get(i)); j++) {
                                                                                url1 = getIPAPI.IPAddress + "/CleanmatePOS/AddOrderDetailBeta.php?ProductID=" + arr_id.get(i);
                                                                                url1 += "&ProductNameTH=" + arr_proTH.get(i);
                                                                                url1 += "&ProductNameEN=" + arr_proEN.get(i);
                                                                                url1 += "&ServiceNameTH=" + arr_serTH.get(i);
                                                                                url1 += "&ServiceNameEN=" + arr_serEN.get(i);
                                                                                url1 += "&Amount=" + arr_price.get(i);
                                                                                url1 += "&SpecialDetial=" + "แขวน";
                                                                                url1 += "&CreatedDate=" + dates;
                                                                                url1 += "&CreatedBy=" + dataID.trim();
                                                                                url1 += "&AppointmentDate=" + appoint;
                                                                                url1 += "&BranchID=" + branchID;
                                                                                new MyAsyncTask().execute(url1, "add_orderdetail", "A");
                                                                            }
                                                                        }
                                                                        new MyAsyncTask().execute(url2, "add_coupon", "A");
                                                                        //new MyAsyncTask().execute(url, url1, url2);
                                                                    }
                                                                } else if (appoint == null) {
                                                                    new MyToast(getActivity(), "ยังไม่กำหนดวันรับสินค้า", 0);
                                                                } else {
                                                                    new MyToast(getActivity(), "ยังไม่มีข้อมูลลูกค้า", 0);
                                                                }
                                                            }
                                                            dialog.dismiss();
                                                            handler.removeCallbacks(this);
                                                            dialogupload.dismiss();
                                                            new MyToast(getActivity(), "ข้อมูลการทำรายการถูกบันทึกเรียบร้อย", 1);
                                                        }
                                                    };
                                                    handler.postDelayed(runnable, 1000);
                                                }
                                            });
                                            okButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();

                                                    dialogupload = new ProgressDialog(getActivity());
                                                    dialogupload.setIcon(R.mipmap.ic_launcher);
                                                    dialogupload.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    dialogupload.setMessage("กำลังตรวจสอบข้อมูล");
                                                    dialogupload.setIndeterminate(true);
                                                    dialogupload.setCancelable(false);
                                                    dialogupload.show();
                                                    final Handler handler = new Handler();
                                                    Runnable runnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            int totals = total2 + fab_count;
                                                            android.text.format.DateFormat df = new android.text.format.DateFormat();
                                                            dates = "" + df.format("yyyy-MM-dd", new java.util.Date());
                                                            String datesCreate = "" + df.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());
                                                            String url = getIPAPI.IPAddress + "/CleanmatePOS/AddOrder.php?OrderDate=" + dates;
                                                            url += "&CustomerID=" + id_member;
                                                            url += "&BranchID=" + branchID;
                                                            url += "&AppointmentDate=" + appoint;
                                                            url += "&CreatedDate=" + datesCreate;
                                                            url += "&CreatedBy=" + dataID;
                                                            url += "&NetAmount=" + totals;
                                                            int a = 0;
                                                            if (percent > (promotion_price) || isExpress == 1) {
                                                                url += "&PromoDiscount=" + a;
                                                            } else {
                                                                url += "&PromoDiscount=" + (int) promotion_price;
                                                            }
                                                            url += "&CouponDiscount=" + sum_coupon;
                                                            int b = 0;
                                                            if (percent > (promotion_price)) {
                                                                url += "&MemberDiscount=" + percent;
                                                            } else {
                                                                url += "&MemberDiscount=" + b;
                                                            }
                                                            url += "&IsPayment=" + 1;
                                                            url += "&PaymentDate=" + date_today;
                                                            url += "&IsExpress=" + isExpress;
                                                            url += "&IsExpressLevel=" + check_express;
                                                            url += "&SpecialDiscount=" + fab_count;
                                                            String url2 = "";
                                                            if (arrTestCounponNumber1.size() > 0) {

                                                                //new MyAsyncTask().execute(url, url1, url2);
                                                                new MyAsyncTask().execute(url, "add_order", "B");
                                                                String url1 = "";
                                                                for (int i = 0; i < arr_count.size(); i++) {
                                                                    for (int j = 0; j < Integer.parseInt(arr_count.get(i)); j++) {
                                                                        url1 = getIPAPI.IPAddress + "/CleanmatePOS/AddOrderDetailBeta.php?ProductID=" + arr_id.get(i);
                                                                        url1 += "&ProductNameTH=" + arr_proTH.get(i);
                                                                        url1 += "&ProductNameEN=" + arr_proEN.get(i);
                                                                        url1 += "&ServiceNameTH=" + arr_serTH.get(i);
                                                                        url1 += "&ServiceNameEN=" + arr_serEN.get(i);
                                                                        url1 += "&Amount=" + arr_price.get(i);
                                                                        url1 += "&SpecialDetial=" + "แขวน";
                                                                        url1 += "&CreatedDate=" + dates;
                                                                        url1 += "&CreatedBy=" + dataID.trim();
                                                                        url1 += "&AppointmentDate=" + appoint;
                                                                        url1 += "&BranchID=" + branchID;
                                                                        //url1 += "&Count=" + arr_count.get(i);
                                                                        new MyAsyncTask().execute(url1, "add_orderdetail", "B");
                                                                    }
                                                                }
                                                                System.out.println("คูปอง 1 : " + arrTestCounponNumber1.size());
                                                                for (int i = 0; i < arrTestCounponNumber1.size(); i++) {
                                                                    String[] splits = arrTestCounponNumber1.get(i).split(",");
                                                                    url2 = getIPAPI.IPAddress + "/CleanmatePOS/CouponDiscount_1.php?CouponDiscountNo=" + splits[0] + "&BranchID=" + branchID + "&Type=" + splits[1];
                                                                    new MyAsyncTask().execute(url2, "add_coupon", "B");
                                                                }
                                                            } else {
                                                                url2 = "not";
                                                                new MyAsyncTask().execute(url, "add_order", "B");
                                                                String url1 = "";
                                                                for (int i = 0; i < arr_count.size(); i++) {
                                                                    for (int j = 0; j < Integer.parseInt(arr_count.get(i)); j++) {
                                                                        url1 = getIPAPI.IPAddress + "/CleanmatePOS/AddOrderDetailBeta.php?ProductID=" + arr_id.get(i);
                                                                        url1 += "&ProductNameTH=" + arr_proTH.get(i);
                                                                        url1 += "&ProductNameEN=" + arr_proEN.get(i);
                                                                        url1 += "&ServiceNameTH=" + arr_serTH.get(i);
                                                                        url1 += "&ServiceNameEN=" + arr_serEN.get(i);
                                                                        url1 += "&Amount=" + arr_price.get(i);
                                                                        url1 += "&SpecialDetial=" + "แขวน";
                                                                        url1 += "&CreatedDate=" + dates;
                                                                        url1 += "&CreatedBy=" + dataID.trim();
                                                                        url1 += "&AppointmentDate=" + appoint;
                                                                        url1 += "&BranchID=" + branchID;
                                                                        new MyAsyncTask().execute(url1, "add_orderdetail", "B");
                                                                    }
                                                                }
                                                                new MyAsyncTask().execute(url2, "add_coupon", "B");
                                                                //new MyAsyncTask().execute(url, url1, url2);
                                                            }
                                                            handler.removeCallbacks(this);
                                                            dialogupload.dismiss();
                                                            new MyToast(getActivity(), "ข้อมูลการทำรายการถูกบันทึกเรียบร้อย", 1);
                                                        }
                                                    };
                                                    handler.postDelayed(runnable, 1000);
                                                }
                                            });
                                            MyFont myFont = new MyFont(getActivity());
                                            okButton.setTypeface(myFont.setFont());
                                            declineButton.setTypeface(myFont.setFont());
                                            title.setTypeface(myFont.setFont());
                                            des.setTypeface(myFont.setFont());
                                        } else {
                                            new MyToast(getActivity(), "ยังไม่มีข้อมูลลูกค้าหรือยังไม่กำหนดวันรับสินค้า", 0);
                                        }
                                    } else {
                                        new MyToast(getActivity(), "ยังไม่ได้เลือกรายการ", 0);
                                    }

                                }
                            });

                            dialog.dismiss();
                        }
                    });
            //end Promotion

        }else {
            new MyToast(getActivity(), "ไม่มีการเชื่อมต่อ Internet", 0);
        }


        return v;
    }

    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {
        public double Pro1=0.00;
        public String Pro2="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            String output = strings[2];
            if(strings[0].equals("not")){
            }else {
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
                    System.out.println("Error1 : "+ex.getMessage());
                }

                try {
                    //System.out.println(OUT);
                    //output = response;
                    if (strings[1].equals("add_order")) {
                        getorderno = Integer.parseInt(response.substring(response.indexOf('.') + 1, response.length()));
                        //new MyToast(getActivity(), "ข้อมูลการทำรายการถูกบันทึกเรียบร้อย", 1);
                    }
                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex.getMessage());
                }
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //dialogupload.dismiss();
            if(total2==0){
                Intent i = new Intent(getActivity(), BarcodePayment.class);
                i.putExtra("ID",""+0);
                i.putExtra("orderNo", getorderno);
                i.putExtra("branchID", branchID);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
            }else{
                //System.out.println("output : "+s);
                if(s.equals("A")){
                    Intent i = new Intent(getActivity(), BarcodePayment.class);
                    i.putExtra("ID",""+0);
                    i.putExtra("orderNo", getorderno);
                    i.putExtra("branchID", branchID);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                }else if(s.equals("B")){
                    //int totals=total2-fab_count;
                    Intent i = new Intent(getActivity(), TypePaymentActivity.class);
                    i.putExtra("ID",""+0);
                    i.putExtra("orderNo", getorderno);
                    i.putExtra("branchID", branchID);
                    i.putExtra("total", total2);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
                }
            }
        }
    }

}
//sort old index
class Element implements Comparable<Element> {

    int index, value;

    Element(int index, int value){
        this.index = index;
        this.value = value;
    }

    public int compareTo(Element e) {
        return this.value - e.value;
    }
}
