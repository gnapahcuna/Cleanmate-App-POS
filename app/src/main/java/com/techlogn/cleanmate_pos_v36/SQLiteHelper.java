package com.techlogn.cleanmate_pos_v36;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anucha on 3/7/2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper sqLiteDB;
    private static final String DB_NAME = "dbase";
    private static final int DB_VERSION = 1;
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public static synchronized SQLiteHelper getInstance(Context context) {
        if(sqLiteDB == null) {
            sqLiteDB = new SQLiteHelper(context.getApplicationContext());
        }
        return sqLiteDB;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql= "CREATE TABLE product (ProductID PRIMARY KEY,"+
                        "ServiceNameTH,"+
                        "ServiceNameEN,"+
                        "ProductNameTH,"+
                        "ProductNameEN,"+
                        "ProductPrice,"+
                        "ImageFile,"+
                        "ColorCode,"+
                        "ServiceType,"+
                        "CategoryID,"+
                        "Num)";
        db.execSQL(sql);


        sql="CREATE TABLE tb_service (ServiceType PRIMARY KEY,"+
                "ServiceNameTH,"+
                "ServiceNameEN)";
        db.execSQL(sql);


        sql="CREATE TABLE category (CategoryID PRIMARY KEY,"+
                "CategoryNameTH,"+
                "CategoryNameEN,"+
                "ColorCode)";
        db.execSQL(sql);

        sql="CREATE TABLE promotion_sale (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "ServiceType,"+
                "ProductID,"+
                "Price)";
        db.execSQL(sql);

        sql="CREATE TABLE tb_coupon (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "ProductID,"+
                "CouponNumber,"+
                "ProductPrice,"+
                "CouponNum,"+
                "ProductNameTH)";
        db.execSQL(sql);

        sql="CREATE TABLE brochure (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "ImageFile)";
        db.execSQL(sql);

        sql="CREATE TABLE privilage (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "CouponNo,"+
                "Value)";
        db.execSQL(sql);

        sql="CREATE TABLE supplies (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "SuppliesID,"+
                "SuppliesNameTH,"+
                "Price)";
        db.execSQL(sql);

        sql="CREATE TABLE CouponPriceList (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "BranchID,"+
                "CouponType,"+
                "CouponPrice,"+
                "ServiceType,"+
                "ProductID)";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
