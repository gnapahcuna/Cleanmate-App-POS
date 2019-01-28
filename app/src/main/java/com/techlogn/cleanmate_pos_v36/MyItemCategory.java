package com.techlogn.cleanmate_pos_v36;

/**
 * Created by anucha on 1/4/2018.
 */

public class MyItemCategory {
        private String mTextProductTH;
        private String mTextProductEN;
        private String mColor;

        public MyItemCategory (String productTH, String productEN,String color) {
            // mTextNameServiceTH=serviceName;
            mTextProductTH = productTH;
            mTextProductEN = productEN;
            mColor=color;
        }

        public void setProductTH(String textProductTH) {
            mTextProductTH = textProductTH;
        }
        public String getProductTH() {
            return mTextProductTH;
        }
        public void setProductEN(String textProductEN) {
            mTextProductEN = textProductEN;
        }
        public String getProductEN() {
            return mTextProductEN;
        }
        public void setColor(String color){
            mColor=color;
        }
        public String getColor(){
            return mColor;
        }
}