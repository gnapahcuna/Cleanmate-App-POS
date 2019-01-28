package com.techlogn.cleanmate_pos_v36;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BasketActivity extends AppCompatActivity {


    private TabLayout tablayout;

    private String tabNames[] = {"first_fragment2", "second_fragment2", "third_fragment2", "fourth_fragment2"};


    private int[] tabIconsUnSelected = {
            R.drawable.icon_tab1,
            R.drawable.icon_tab2,
            R.drawable.icon_tab3,
            R.drawable.icon_tab4};

    private int[] tabIconsSelected = {
            R.drawable.icon_tab1_un_,
            R.drawable.icon_tab2_un,
            R.drawable.icon_tab3_un,
            R.drawable.icon_tab4_un};


    public static Drawable setDrawableSelector(Context context, int normal, int selected) {

        Drawable state_normal = ContextCompat.getDrawable(context, normal);

        Drawable state_pressed = ContextCompat.getDrawable(context, selected);

        StateListDrawable drawable = new StateListDrawable();

        drawable.addState(new int[]{android.R.attr.state_selected},
                state_pressed);
        drawable.addState(new int[]{android.R.attr.state_enabled},
                state_normal);

        return drawable;
    }

    public static ColorStateList setTextselector(int normal, int pressed) {
        ColorStateList colorStates = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected},
                        new int[]{}
                },
                new int[]{
                        pressed,
                        normal});
        return colorStates;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_basket);

        //Log
        /*Bugfender.init(this, "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getApplication());*/

        try{
            String key=getIntent().getExtras().get("key").toString();
            if(key.equals("1")){
                System.out.println(""+key);
                initView();
                initTab();
                setupTabLayout();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, FirstFragmentBasket.newInstance());
                fragmentTransaction.commit();
                TabLayout.Tab tab = tablayout.getTabAt(0);
                tab.select();
            }else if(key.equals("error")){
                System.out.println(""+key);
                initView();
                initTab();
                setupTabLayout();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, FourthFragmentBasket.newInstance());
                fragmentTransaction.commit();
                TabLayout.Tab tab = tablayout.getTabAt(3);
                tab.select();
            }
        }catch (Exception e){
            System.out.println("Not Loop");
            initView();
            setupTabLayout();
            initTab();
        }

    }

    private void setupTabLayout() {

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void initView() {
        tablayout = (TabLayout) findViewById(R.id.tablayout);
    }

    private void initTab() {
        if (tablayout != null) {
            for (int i = 0; i < tabNames.length; i++) {
                tablayout.addTab(tablayout.newTab());
                TabLayout.Tab tab = tablayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(getTabView(i));
            }
        }

    }

    private View getTabView(int position) {
        View view = LayoutInflater.from(BasketActivity.this).inflate(R.layout.view_tabs, null);

        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageDrawable(setDrawableSelector(BasketActivity.this, tabIconsUnSelected[position], tabIconsSelected[position]));


        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return view;
    }

    private void switchTab(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (position) {

            case 0:

                fragmentTransaction.replace(R.id.content_frame, FirstFragmentBasket.newInstance());
                break;

            case 1:

                fragmentTransaction.replace(R.id.content_frame, SecondFragmentBasket.newInstance());
                break;

            case 2:
                fragmentTransaction.replace(R.id.content_frame, ThirdFragmentBasket.newInstance());
                break;

            case 3:
                fragmentTransaction.replace(R.id.content_frame, FourthFragmentBasket.newInstance());
                break;
        }
        fragmentTransaction.commit();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
