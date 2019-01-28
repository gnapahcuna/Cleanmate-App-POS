package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText actionBarEditText;
    private RelativeLayout relativeLayout;
    private LinearLayout layoutMain;

    ProgressDialog dialog;

    private  ViewPager mPager;
    private  int currentPage = 0;
    EditText edit;

    String ID,branchID,firstname,latname,branchGroup,sTitle,title,branchName;
    ArrayList<String> arrImg;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    private String type,search;

    Dialog dialog_logout;

    MyFont myFont;
    private ArrayList<MyItemFactoryOrder> items1;
    String TYPE;

    private GetIPAPI getIPAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(this, "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getApplication());*/



        mSQLite = SQLiteHelper.getInstance(MenuActivity.this);
        items1=new ArrayList<>();
        relativeLayout = findViewById(R.id.rl_slide);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

        } else {
            new MyToast(MenuActivity.this, "ไม่มีการเชื่อมต่อ Internet", 0);
        }

        SharedPreferences sharedPreferences2 = MenuActivity.this.getSharedPreferences("ID", Activity.MODE_PRIVATE);
        Map<String, ?> entries2 = sharedPreferences2.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            //System.out.println(temp+" = "+sharedPreferences.getStringSet(temp,null));
            for (int i = 0; i < sharedPreferences2.getStringSet(temp, null).size(); i++) {
                getData2 = sharedPreferences2.getStringSet(temp, null).toArray(new String[sharedPreferences2.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk = getData2[i].charAt(1);
                if (chk == 'a') {
                    ID = getData2[i].substring(3);
                } else if (chk == 'b') {
                    branchID = getData2[i].substring(3);
                } else if (chk == 'c') {
                    firstname = getData2[i].substring(3);
                } else if (chk == 'd') {
                    latname = getData2[i].substring(3);
                } else if (chk == 'e') {
                    branchGroup = getData2[i].substring(3);
                } else if (chk == 'f') {
                    title = getData2[i].substring(3);
                } else if (chk == 'g') {
                    branchName = getData2[i].substring(3);
                }
            }
        }

        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("F")) {
            String chk = getIntent().getExtras().getString("F");
            String dataID = getIntent().getExtras().getString("dataID");
            /*if(chk.trim().endsWith("1")){
                Bundle bundle = new Bundle();
                bundle.putString("ID",ID);
                bundle.putString("branchID",branchID);
                Fragment1 fragment1 = new Fragment1();
                fragment1.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,fragment1)
                        .commit();
            }else */
            if (chk.trim().endsWith("2")) {
                Bundle bundle = new Bundle();
                bundle.putInt("key", Integer.parseInt(dataID));
                FragmentServiceProduct thirdFragment_1 = new FragmentServiceProduct();
                thirdFragment_1.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, thirdFragment_1)
                        .commit();
            } else if (chk.trim().endsWith("3")) {
                Bundle bundle = new Bundle();
                bundle.putString("ID", ID);
                bundle.putString("branchID", branchID);
                FragmentCategory fragment2 = new FragmentCategory();
                fragment2.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment2)
                        .commit();
            } else if (chk.trim().endsWith("4")) {
                Bundle bundle = new Bundle();
                bundle.putInt("key", Integer.parseInt(dataID));
                bundle.putString("ID", dataID);

                FragmentCategoryList fragment_cate_list = new FragmentCategoryList();
                fragment_cate_list.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment_cate_list)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            } else if (chk.trim().endsWith("5")) {
                Bundle bundle = new Bundle();
                bundle.putString("key", dataID);
                bundle.putString("ID", dataID);

                FragmentSearch fragmentSearch = new FragmentSearch();
                fragmentSearch.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentSearch)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            } else if (chk.trim().endsWith("1010")) {
                Bundle bundle = new Bundle();
                bundle.putString("orderNo", getIntent().getExtras().get("orderNo").toString());
                bundle.putString("productID", getIntent().getExtras().get("productID").toString());
                bundle.putString("productTH",getIntent().getExtras().get("productTH").toString());
                bundle.putString("productEN",getIntent().getExtras().get("productEN").toString());
                bundle.putString("Num",getIntent().getExtras().get("Num").toString());
                bundle.putString("bar1",getIntent().getExtras().get("bar1").toString());
                FragmentManageLevel3 fragment2_2 = new FragmentManageLevel3();
                fragment2_2.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment2_2)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            } else if (chk.trim().endsWith("manage")) {
                Bundle bundle = new Bundle();
                bundle.putString("orderNo", getIntent().getExtras().get("orderNo").toString());
                bundle.putString("IsSearch", "1");
                bundle.putString("IsReturnFactory", "Manage");

                SharedPreferences sharedPreferences3 = MenuActivity.this.getSharedPreferences("SearchManage", Activity.MODE_PRIVATE);
                Map<String, ?> entries3 = sharedPreferences3.getAll();
                Set<String> keys3 = entries3.keySet();
                String[] getData3;
                List<String> list3 = new ArrayList<String>(keys3);
                for (String temp : list3) {
                    //System.out.println(temp + " = " + sharedPreferences3.getStringSet(temp, null));
                    for (int i = 0; i < sharedPreferences3.getStringSet(temp, null).size(); i++) {
                        getData3 = sharedPreferences3.getStringSet(temp, null).toArray(new String[sharedPreferences3.getStringSet(temp, null).size()]);
                        //System.out.println(temp + " : " + getData3[i]);
                        char chk1 = getData3[i].charAt(1);
                        if (chk1 == 'a') {
                            type = getData3[i].substring(3);
                        } else if (chk1 == 'b') {
                            search = getData3[i].substring(3);
                        }
                    }
                }

                if (Integer.parseInt(type) == 1) {
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    String dates = "" + df.format("yyyy-MM-dd", new Date());
                    String url = getIPAPI.IPAddress+"/CleanmatePOS/factoryOrderNo.php?branchID=" + branchID + "&Date=" + dates;
                    new MyAsyncTask().execute(url, "search");
                } else if (Integer.parseInt(type) == 2) {
                    String url = getIPAPI.IPAddress+"/CleanmatePOS/searchFactory.php?branchID=" + branchID + "&search=" + search;
                    new MyAsyncTask().execute(url, "search");
                }

                /*FragmentManageLevel1Search fragmentManageLevel1Search = new FragmentManageLevel1Search();
                fragmentManageLevel1Search.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel1Search)
                        .commit();*/

            } else if (chk.trim().endsWith("report")) {
                Bundle bundle = new Bundle();
                bundle.putString("Date", getIntent().getExtras().get("Date").toString());
                FragmentReportDetail fragmentReportDetail = new FragmentReportDetail();
                fragmentReportDetail.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentReportDetail)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            }else if (chk.trim().endsWith("MapBarcodePK")) {
                Bundle bundle = new Bundle();
                bundle.putString("orderNo", getIntent().getExtras().get("orderNo").toString());
                bundle.putString("branchID", getIntent().getExtras().get("branchID").toString());
                bundle.putString("ID", getIntent().getExtras().get("ID").toString());
                bundle.putString("back", "back1");
                FragmentManageLevel2 fragmentManageLevel2 = new FragmentManageLevel2();
                fragmentManageLevel2.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel2)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            } else if (chk.trim().endsWith("MapBarcode")) {
                Bundle bundle = new Bundle();
                bundle.putString("orderNo", getIntent().getExtras().get("orderNo").toString());
                bundle.putString("productTH", getIntent().getExtras().get("productTH").toString());
                bundle.putString("productEN", getIntent().getExtras().get("productEN").toString());
                bundle.putString("productID", getIntent().getExtras().get("productID").toString());
                bundle.putString("Num",getIntent().getExtras().get("Num").toString());
                bundle.putString("bar1",getIntent().getExtras().get("bar1").toString());
                FragmentManageLevel3 fragmentManageLevel3 = new FragmentManageLevel3();
                fragmentManageLevel3.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel3)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            } else if (chk.trim().endsWith("ReturnFactory")) {
                Bundle bundle = new Bundle();
                bundle.putString("IsReturnFactory", "ReturnFactory");
                FragmentManageLevel0 fragmentManageLevel0 = new FragmentManageLevel0();
                fragmentManageLevel0.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel0)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            }else if (chk.trim().endsWith("ReturnCustomer")) {
                Bundle bundle = new Bundle();
                FragmentReturnCustomer fragmentManageLevel0 = new FragmentReturnCustomer();
                fragmentManageLevel0.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel0)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            }else if(chk.trim().endsWith("ReturnCust")){
                Bundle bundle = new Bundle();
                bundle.putString("key", getIntent().getExtras().get("OrderNo").toString());
                FragmentReturnCustomerList fragmentManageLevel0 = new FragmentReturnCustomerList();
                fragmentManageLevel0.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel0)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            }else if(chk.trim().endsWith("ReturnCust1")){
                Bundle bundle = new Bundle();
                bundle.putString("key", getIntent().getExtras().get("OrderNo").toString());
                FragmentReturnCustomer fragmentManageLevel0 = new FragmentReturnCustomer();
                fragmentManageLevel0.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel0)
                        .commit();
                relativeLayout.setVisibility(View.GONE);
            }

        } else {
            //System.out.println("N");
        }

        arrImg = new ArrayList<String>();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        myFont = new MyFont(MenuActivity.this);
        View ViewHead = navigationView.getHeaderView(0);
        TextView text_name = (TextView) ViewHead.findViewById(R.id.text_name);
        TextView text_branch = (TextView) ViewHead.findViewById(R.id.text_branch);
        TextView text_id = (TextView) ViewHead.findViewById(R.id.text_id);
        LinearLayout layout_nav = ViewHead.findViewById(R.id.layout_nav);

        text_name.setText("test");
        text_branch.setText("สะพานควาย");
        text_id.setText("ID");
        text_name.setTypeface(myFont.setFont());
        text_branch.setTypeface(myFont.setFont());
        text_id.setTypeface(myFont.setFont());

        navigationView.setNavigationItemSelectedListener(this);

        layoutMain = findViewById(R.id.layout_main);



        if (title.equals("0")) {
            sTitle = "นาย";
        } else if (title.equals("1")) {
            sTitle = "นาง";
        } else if (title.equals("2")) {
            sTitle = "นางสาว";
        }
        layout_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        text_name.setText(sTitle + " " + firstname + " " + latname);
        text_id.setText(ID);
        text_branch.setText("สาขา" + branchName);


        final ProgressDialog dialog = new ProgressDialog(MenuActivity.this);
        dialog.setIcon(R.mipmap.loading);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("กรุณารอสักครู่....");
        dialog.setIndeterminate(true);
        //dialog.show();
        Ion.with(MenuActivity.this)
                .load(getIPAPI.IPAddress+"/CleanmatePOS/Brochure.php")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                dialog.dismiss();
                                JsonObject jsonObject = (JsonObject) result.get(i);
                                arrImg.add(jsonObject.get("ImageFile").getAsString());
                            }
                            mPager = (ViewPager) findViewById(R.id.pager);
                            mPager.setAdapter(new MyAdapterSlideImage(getBaseContext(), arrImg));
                            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                            indicator.setViewPager(mPager);

                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == arrImg.size()) {
                                        currentPage = 0;
                                    }
                                    mPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 4000, 4000);
                        } catch (Exception ex) {
                            new MyToast(MenuActivity.this, "การเชื่อมต่อมีปัญหา", 0);
                        }
                    }
                });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);


        MenuItem menuItem=menu.findItem(R.id.action_search);
        edit=(EditText) MenuItemCompat.getActionView(menuItem);

        Configuration config = getResources().getConfiguration();
        int width=(getResources().getDisplayMetrics().widthPixels*55)/100;
        edit.setWidth(width);
        edit.setBackgroundResource(R.drawable.edt_text_main);
        edit.setHint("\tค้นหาสินค้าและบริการ");
        if (config.smallestScreenWidthDp >= 320&&config.smallestScreenWidthDp < 480) {
            edit.setTextSize(16);
        }else if(config.smallestScreenWidthDp >= 480&&config.smallestScreenWidthDp < 600){
            edit.setTextSize(18);
        }else{
            edit.setTextSize(20);
        }

        edit.setTypeface(myFont.setFont());
        edit.setInputType(InputType.TYPE_CLASS_TEXT);
        edit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_SEARCH){

                    if(edit.getHint().toString().trim().equals("ค้นหาสินค้าและบริการ")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("key", edit.getText().toString());
                        bundle.putString("ID", ID);
                        bundle.putString("branchID", branchID);

                        FragmentSearch fragmentSearch = new FragmentSearch();
                        fragmentSearch.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragmentSearch)
                                .commit();
                    }

                    relativeLayout.setVisibility(View.GONE);
                    layoutMain.setVisibility(View.GONE);

                    View view = MenuActivity.this.getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager)MenuActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        this.actionBarEditText=edit;



        return true;
    }
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Ayuthaya.ttf");
        MyFont myFont=new MyFont(MenuActivity.this);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_cate:
                SharedPreferences sharedPreferences1 = MenuActivity.this.getSharedPreferences("PRODUCT", Activity.MODE_PRIVATE);
                sharedPreferences1.edit().clear().apply();

                Bundle bundle1 = new Bundle();
                bundle1.putString("key",edit.getText().toString());
                bundle1.putString("ID",ID);
                bundle1.putString("branchID",branchID);

                FragmentCategory fragment_cate = new FragmentCategory();
                fragment_cate.setArguments(bundle1);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,fragment_cate)
                        .commit();

                edit.setText("");
                edit.setHint("\tค้นหาสินค้าและบริการ");

                relativeLayout.setVisibility(View.GONE);
                layoutMain.setVisibility(View.GONE);
                return true;
            /*case R.id.action_addcart:
                Intent i=new Intent(getBaseContext(),MainTabActivity.class);
                startActivity(i);
                return true;*/
            case R.id.action_searchIcon:

                if(edit.getHint().toString().trim().equals("ค้นหาสินค้าและบริการ")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("key", edit.getText().toString());
                    bundle.putString("ID", ID);
                    bundle.putString("branchID", branchID);

                    FragmentSearch fragmentSearch = new FragmentSearch();
                    fragmentSearch.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragmentSearch)
                            .commit();
                    //edit.setHint("ค้นหาสินค้าและบริการ");
                }

                relativeLayout.setVisibility(View.GONE);
                layoutMain.setVisibility(View.GONE);

                View view = MenuActivity.this.getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)MenuActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager=getFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_product) {
            Bundle bundle = new Bundle();
            bundle.putString("ID",ID);
            bundle.putString("branchID",branchID);
            bundle.putInt("key",1);

            edit.setText("");
            edit.setHint("\tค้นหาสินค้าและบริการ");

            FragmentServiceProduct fragment1_1 = new FragmentServiceProduct();
            fragment1_1.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame,fragment1_1)
                    .commit();
            MenuActivity.this.overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
            relativeLayout.setVisibility(View.GONE);
        }else if (id == R.id.nav_manage) {
            Bundle bundle = new Bundle();
            bundle.putString("edt",""+edit);
            edit.setText("");
            edit.setHint("\tค้นหาสินค้าและบริการ");

            FragmentManageLevel0 fragmentManageLevel0 = new FragmentManageLevel0();
            //fragmentManageLevel0.setArguments(edit);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame,fragmentManageLevel0)
                    .commit();
            MenuActivity.this.overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
            relativeLayout.setVisibility(View.GONE);
        /*else if (id == R.id.nav_manage) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FragmentManageLevel1())
                    .commit();
            edit.setText("");
            edit.setHint("\tค้นหารายการสำหรับจัดการสินค้า");
            *//*Intent i=new Intent(MenuActivity.this,Main4Activity.class);
            startActivity(i);*//*
            relativeLayout.setVisibility(View.GONE);
            layoutMain.setVisibility(View.GONE);*/
        } else if (id == R.id.nav_follow) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FragmentCheckStatusOrder())
                    .commit();
            edit.setText("");
            edit.setHint("\tค้นหาสินค้าและบริการ");
            MenuActivity.this.overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
            relativeLayout.setVisibility(View.GONE);
        } /*else if (id == R.id.nav_pay) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FragmentCancelOrder())
                    .commit();
            edit.setText("");
            edit.setHint("\tค้นหารายการสำหรับยกเลิกรายการ");
            MenuActivity.this.overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
            relativeLayout.setVisibility(View.GONE);
        }*/else if (id == R.id.nav_delivery) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FragmentReturnCustomer())
                    .commit();
            edit.setText("");
            edit.setHint("\tค้นหาสินค้าและบริการ");
            MenuActivity.this.overridePendingTransition(R.anim.pdlg_anim_fade_in, R.anim.pdlg_anim_fade_out);
            relativeLayout.setVisibility(View.GONE);
        } else if (id == R.id.nav_logout) {
            dialog_logout = new Dialog(MenuActivity.this);
            dialog_logout.setContentView(R.layout.custon_alert_dialog);
            dialog_logout.show();
            Window window = dialog_logout.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            TextView title=dialog_logout.findViewById(R.id.tv_quit_learning);
            TextView des=dialog_logout.findViewById(R.id.tv_description);
            title.setText("แจ้งเตือน");
            des.setText("คุณต้องการออกจากระบบใช่หรือไม่");
            Button declineButton = dialog_logout.findViewById(R.id.btn_cancel);
            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_logout.dismiss();
                }
            });
            Button okButton =  dialog_logout.findViewById(R.id.btn_ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url=getIPAPI.IPAddress+"/CleanmatePOS/Logout.php?IsSignOn="+0+"&id="+ID;
                    new MyAsyncTask().execute(url,"output");

                }
            });
            MyFont myFont=new MyFont(MenuActivity.this);
            okButton.setTypeface(myFont.setFont());
            declineButton.setTypeface(myFont.setFont());
            title.setTypeface(myFont.setFont());
            des.setTypeface(myFont.setFont());
        } else if (id == R.id.nav_report) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FragmentReport())
                    .commit();
            edit.setText("");
            edit.setHint("\tค้นหาสินค้าและบริการ");
            relativeLayout.setVisibility(View.GONE);
        }else if (id == R.id.nav_supplies) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FragmentSupplies())
                    .commit();
            edit.setText("");
            edit.setHint("\tค้นหาสินค้าและบริการ");
            relativeLayout.setVisibility(View.GONE);
        }
        /*else if (id == R.id.nav_retuen_factory) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FragmentReturnFactory())
                    .commit();
            edit.setText("");
            edit.setHint("\tค้นหารายการสินค้าคืนโรงงาน");
            relativeLayout.setVisibility(View.GONE);
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //DB Product
    private void insert(String productID, String serviceNameTH,String serviceNameEN,
                         String productNameTH,String productNameEN,String productPrice,
                         String imageFile,String colorCode,String serviceType,String categoryID,String num) {
        ContentValues cv=new ContentValues();
        cv.put("ProductID",productID);
        cv.put("ServiceNameTH",serviceNameTH);
        cv.put("ServiceNameEN",serviceNameEN);
        cv.put("ProductNameTH",productNameTH);
        cv.put("ProductNameEN",productNameEN);
        cv.put("ProductPrice",productPrice);
        cv.put("ImageFile",imageFile);
        cv.put("ColorCode",colorCode);
        cv.put("ServiceType",serviceType);
        cv.put("CategoryID",categoryID);
        cv.put("Num",num);
        mDb.insert("product",null,cv);

        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void insert_service(String ServiceType,String serviceNameTH,String serviceNameEN) {
        ContentValues cv=new ContentValues();
        cv.put("ServiceType",ServiceType);
        cv.put("ServiceNameTH",serviceNameTH);
        cv.put("ServiceNameEN",serviceNameEN);
        mDb.insert("tb_service",null,cv);

        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void insert_category(String cateID,String cateTH,String cateEN,String colorCode) {
        ContentValues cv=new ContentValues();
        cv.put("CategoryID",cateID);
        cv.put("CategoryNameTH",cateTH);
        cv.put("CategoryNameEN",cateEN);
        cv.put("ColorCode",colorCode);
        mDb.insert("category",null,cv);

        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void insert_brochure(String img) {
        ContentValues cv=new ContentValues();
        cv.put("ImageFile",img);
        mDb.insert("brochure",null,cv);

        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void insert_supplies(String SuppliesID,String NameTH,String Price) {
        ContentValues cv=new ContentValues();
        cv.put("SuppliesID",SuppliesID);
        cv.put("SuppliesNameTH",NameTH);
        cv.put("Price",Price);
        mDb.insert("supplies",null,cv);

        //showMessage("บันทึกข้อมูลแล้ว");
    }

    private void insert_couponpricelist(String BranchID,String CouponType,String CouponPrice,String ServiceType,String ProductID) {
        ContentValues cv=new ContentValues();
        cv.put("BranchID",BranchID);
        cv.put("CouponType",CouponType);
        cv.put("CouponPrice",CouponPrice);
        cv.put("ServiceType",ServiceType);
        cv.put("ProductID",ProductID);
        mDb.insert("CouponPriceList",null,cv);

        //showMessage("บันทึกข้อมูลแล้ว");
    }


    private void showMessage(String msg) {
        Toast.makeText(MenuActivity.this, msg, Toast.LENGTH_LONG).show();
    }
    private void loadProdcut(){

        String url=getIPAPI.IPAddress+"/CleanmatePOS/Service.php";
        String url1=getIPAPI.IPAddress+"/CleanmatePOS/Category.php";
        String url2=getIPAPI.IPAddress+"/CleanmatePOS/Product.php?BranchGroupID="+branchGroup;
        String url3=getIPAPI.IPAddress+"/CleanmatePOS/getSupplies.php";
        String url4=getIPAPI.IPAddress+"/CleanmatePOS/CouponPriceList.php?BranchID="+branchID;
        new MyAsyncTask().execute(url, url1,url2,url3,url4);

    }
    @Override
    public void onStart() {
        super.onStart();
        System.out.println("This OnStart");
        mDb = mSQLite.getReadableDatabase();
        String sql = "SELECT * FROM product";
        Cursor cursor = mDb.rawQuery(sql, null);

        int i = 0;
        while (cursor.moveToNext()) {
            i+=1;
        }
        if(i<=0){
            loadProdcut();
        }else{

        }

        String sql1 = "SELECT * FROM brochure ";
        cursor = mDb.rawQuery(sql1, null);
        while (cursor.moveToNext()) {
            arrImg.add(cursor.getString(1) );
        }


    }class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MenuActivity.this);
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
            String response1 = "";
            String response2 = "";
            String response3 = "";
            String response4 = "";
            TYPE="";
            // String response3 = "";
            String output = "";
            if (strings[1].equals("output")) {
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
                    System.out.println("Error1");
                }
                SharedPreferences sharedPreferences, sharedPreferences1, sharedPreferences2, sharedPreferences3, sharedPreferences4;
                sharedPreferences = getBaseContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);
                sharedPreferences1 = getBaseContext().getSharedPreferences("Member", Activity.MODE_PRIVATE);
                sharedPreferences2 = getBaseContext().getSharedPreferences("Coupon", Activity.MODE_PRIVATE);
                sharedPreferences3 = getBaseContext().getSharedPreferences("Appoint", Activity.MODE_PRIVATE);
                sharedPreferences4 = getBaseContext().getSharedPreferences("CouponValue", Activity.MODE_PRIVATE);

                sharedPreferences.edit().clear().apply();
                sharedPreferences1.edit().clear().apply();
                sharedPreferences2.edit().clear().apply();
                sharedPreferences3.edit().clear().apply();
                sharedPreferences1.edit().clear().apply();
                sharedPreferences4.edit().clear().apply();

                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                i.putExtra("Key",""+1);
                startActivity(i);
                finish();
                dialog_logout.dismiss();
            } else if (strings[1].equals("search")) {
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
                    System.out.println("Error1");
                }

                int index, num;
                try {
                    TYPE = strings[1];
                    JSONArray jsonArray = new JSONArray(response);
                    output = "" + jsonArray.length();
                    //System.out.println(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        index = i + 1;
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        try {
                            items1.add(new MyItemFactoryOrder("" + index, jsonObj.getString("OrderNo"), dateThai(jsonObj.getString("OrderDate")),
                                    jsonObj.getString("Barcode"), jsonObj.getString("BarcodePackage")));
                        } catch (Exception ex) {
                            items1.add(new MyItemFactoryOrder("" + index, jsonObj.getString("OrderNo"), dateThai(jsonObj.getString("OrderDate")),
                                    "0", "0"));
                        }
                    }

                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex);
                }
            }
           /* if(strings[1].equals("Brochure")){
                output=strings[1];
                System.out.println("output : "+output);
                try {
                    URL url=new URL(strings[0]);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    //httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    Scanner scanner=new Scanner(inputStream,"UTF-8");
                    response=scanner.useDelimiter("\\A").next();

                }catch (Exception ex){
                    System.out.println("Error1");
                }
                try {
                    JSONArray jsonArray3 = new JSONArray(response);
                    for (int i = 0; i < jsonArray3.length(); i++) {
                        JSONObject jsonObj = jsonArray3.getJSONObject(i);
                        insert_brochure(jsonObj.getString("ImageFile"));
                        arrImg.add(jsonObj.getString("ImageFile"));
                    }
                } catch (Exception ex) {
                    System.out.println("Error2");
                }
            }*/
            else {
                try {

                    URL url = new URL(strings[0]);
                    URL url1 = new URL(strings[1]);
                    URL url2 = new URL(strings[2]);
                    URL url3 = new URL(strings[3]);
                    URL url4 = new URL(strings[4]);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) url2.openConnection();
                    HttpURLConnection httpURLConnection3 = (HttpURLConnection) url3.openConnection();
                    HttpURLConnection httpURLConnection4 = (HttpURLConnection) url4.openConnection();

                    httpURLConnection.setDoOutput(true);
                    httpURLConnection1.setDoOutput(true);
                    httpURLConnection2.setDoOutput(true);
                    httpURLConnection3.setDoOutput(true);
                    httpURLConnection4.setDoOutput(true);
                    httpURLConnection.connect();
                    httpURLConnection1.connect();
                    httpURLConnection2.connect();
                    httpURLConnection3.connect();
                    httpURLConnection4.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStream inputStream1 = httpURLConnection1.getInputStream();
                    InputStream inputStream2 = httpURLConnection2.getInputStream();
                    InputStream inputStream3 = httpURLConnection3.getInputStream();
                    InputStream inputStream4 = httpURLConnection4.getInputStream();

                    Scanner scanner = new Scanner(inputStream, "UTF-8");
                    Scanner scanner1 = new Scanner(inputStream1, "UTF-8");
                    Scanner scanner2 = new Scanner(inputStream2, "UTF-8");
                    Scanner scanner3 = new Scanner(inputStream3, "UTF-8");
                    Scanner scanner4 = new Scanner(inputStream4, "UTF-8");

                    response = scanner.useDelimiter("\\A").next();
                    response1 = scanner1.useDelimiter("\\A").next();
                    response2 = scanner2.useDelimiter("\\A").next();
                    response3 = scanner3.useDelimiter("\\A").next();
                    response4 = scanner4.useDelimiter("\\A").next();


                } catch (Exception ex) {
                    System.out.println("Error3 : "+ex.getMessage());
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        insert_service(
                                jsonObject.optString("ServiceType"),
                                jsonObject.optString("ServiceNameTH"),
                                jsonObject.optString("ServiceNameEN")
                        );
                    }
                    JSONArray jsonArray1 = new JSONArray(response1);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObj = jsonArray1.getJSONObject(i);
                        insert_category(jsonObj.getString("CategoryID"),
                                jsonObj.getString("CategoryNameTH"),
                                jsonObj.getString("CategoryNameEN"),
                                jsonObj.getString("ColorCode"));
                    }
                    JSONArray jsonArray2 = new JSONArray(response2);
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObj = jsonArray2.getJSONObject(i);
                        insert(
                                jsonObj.getString("ProductID"),
                                jsonObj.getString("ServiceNameTH"),
                                jsonObj.getString("ServiceNameEN"),
                                jsonObj.getString("ProductNameTH"),
                                jsonObj.getString("ProductNameEN"),
                                jsonObj.getString("ProductPrice"),
                                jsonObj.getString("ImageFile"),
                                jsonObj.getString("ColorCode"),
                                jsonObj.getString("ServiceType"),
                                jsonObj.getString("CategoryID"),
                                "" + 0
                        );
                    }
                    JSONArray jsonArray4 = new JSONArray(response4);
                    for (int i = 0; i < jsonArray4.length(); i++) {
                        JSONObject jsonObj = jsonArray4.getJSONObject(i);
                        insert_couponpricelist(
                                jsonObj.getString("BranchID"),
                                jsonObj.getString("CouponType"),
                                jsonObj.getString("CouponPrice"),
                                jsonObj.getString("ServiceType"),
                                jsonObj.getString("ProductID")
                        );
                    }

                    JSONArray jsonArray3 = new JSONArray(response3);
                    for (int i = 0; i < jsonArray3.length(); i++) {
                        JSONObject jsonObj = jsonArray3.getJSONObject(i);
                        insert_supplies(jsonObj.getString("SuppliesID"),jsonObj.getString("SuppliesNameTH"),""+Double.parseDouble(jsonObj.getString("Price")));
                    }


                } catch (Exception ex) {
                    System.out.println("Error2 : "+ex.getMessage());
                }
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(TYPE.equals("search")){
                Bundle bundle = new Bundle();
                bundle.putString("IsBack", "back");
                FragmentManageLevel1 fragmentManageLevel1 = new FragmentManageLevel1(items1);
                fragmentManageLevel1.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragmentManageLevel1)
                        .commit();
                relativeLayout.setVisibility(View.GONE);

            }
            /*if(s.equals("Brochure")) {
                dialog.dismiss();
                mPager = (ViewPager) findViewById(R.id.pager);
                mPager.setAdapter(new MyAdapterSlideImage(getBaseContext(), arrImg));
                CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                indicator.setViewPager(mPager);

                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == arrImg.size()) {
                            currentPage = 0;
                        }
                        mPager.setCurrentItem(currentPage++, true);
                    }
                };
                Timer swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 4000, 4000);
            }*/
        }
    }

    public static String dateThai(String strDate) {
        String Months[] = {
                "ม.ค", "ก.พ", "มี.ค", "เม.ย",
                "พ.ค", "มิ.ย", "ก.ค", "ส.ค",
                "ก.ย", "ต.ค", "พ.ย", "ธ.ค"};

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        int year = 0, month = 0, day = 0;
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

        return String.format("%s %s %s", day, Months[month], year + 543);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
