package com.techlogn.cleanmate_pos_v36;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by anucha on 1/11/2018.
 */

public class TabLayoutUtils {
    public static void enableTabs(TabLayout tabLayout, Boolean enable, ArrayList<Integer>arr,int chk){
        ViewGroup viewGroup = getTabViewGroup(tabLayout);
        ArrayList<Integer>mArr=arr;
        int check=chk;
        System.out.println("Check : "+check);
        if(check==0) {
            if (viewGroup != null)
            //for (int childIndex = 0; childIndex < viewGroup.getChildCount(); childIndex++)
            {
                for(int i=0;i<mArr.size();i++) {
                    View tabView = viewGroup.getChildAt(mArr.get(i));
                    if (tabView != null)
                        tabView.setEnabled(enable);
                }
            }
        }else if(check==1){

            if (viewGroup != null)
            //for (int childIndex = 0; childIndex < viewGroup.getChildCount(); childIndex++)
            {

                for(int i=0;i<mArr.size();i++) {
                    View tabView = viewGroup.getChildAt(0);
                    System.out.println(mArr.get(i));
                    if (tabView != null)
                        tabView.setEnabled(true);
                }
            }
        }
    }
    public static void enableTabs_true(TabLayout tabLayout, Boolean enable){
        ViewGroup viewGroup = getTabViewGroup(tabLayout);
        if (viewGroup != null)
            for (int childIndex = 0; childIndex < viewGroup.getChildCount(); childIndex++)
            {
                View tabView = viewGroup.getChildAt(childIndex+2);
                if ( tabView != null)
                    tabView.setEnabled(enable);
            }
    }

    public static View getTabView(TabLayout tabLayout, int position){
        View tabView = null;
        ViewGroup viewGroup = getTabViewGroup(tabLayout);
        if (viewGroup != null && viewGroup.getChildCount() > position)
            tabView = viewGroup.getChildAt(position);

        return tabView;
    }

    private static ViewGroup getTabViewGroup(TabLayout tabLayout){
        ViewGroup viewGroup = null;

        if (tabLayout != null && tabLayout.getChildCount() > 0 ) {
            View view = tabLayout.getChildAt(0);
            if (view != null && view instanceof ViewGroup)
                viewGroup = (ViewGroup) view;
        }
        return viewGroup;
    }

}
