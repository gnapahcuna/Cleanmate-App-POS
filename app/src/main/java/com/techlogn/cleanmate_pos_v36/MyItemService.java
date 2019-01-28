package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 1/3/2018.
 */

public class MyItemService {
    private String mTextTitle;
    private String mTextSubTitle;
    private String mImage;

    public MyItemService(String textTitle,String textSub,String image){
        mTextTitle=textTitle;
        mTextSubTitle=textSub;
        mImage=image;
    }
    public void setTextTitle(String textTitle){
        mTextTitle=textTitle;
    }
    public String getTextTitle(){
        return mTextTitle;
    }
    public void setTextSubTitle(String textSub){
        mTextSubTitle=textSub;
    }
    public String getTextSubTitle(){
        return mTextSubTitle;
    }
    public void setImage(String img){
        mImage=img;
    }
    public String getImage(){
        return mImage;
    }
}