package com.techlogn.cleanmate_pos_v36;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by anucha on 1/15/2018.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabs = {"One", "Two", "Three"};
    private int mCount = mTabs.length;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        /*switch(position) {
            case 0: return Fragment_Service1.newInstance(null, null);
            case 1: return Fragment_Service2.newInstance(null, null);
            case 2: return Fragment_Service3.newInstance(null, null);
        }*/
        return null;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
