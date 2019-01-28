package com.techlogn.cleanmate_pos_v36;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.libRG.CustomTextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by anucha on 2/24/2018.
 */

public class MyAdapterCoupon extends ArrayAdapter {

    ViewHolderCoupon vHolder;
    Fragment fragment;
    private Context mContext;
    private ArrayList<CustomItemCoupon> items;
    private int mLayout;
    LayoutInflater inflater;
    SharedPreferences sp;
    ListView mListview;
    int mPosition,totalPrice;
    ArrayList<String>arrNum;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    private ProgressDialog dialog;

    private GetIPAPI getIPAPI;
    private CustomTextView text_total;
    private int total;

    public MyAdapterCoupon(Context context, int layout, ArrayList<CustomItemCoupon> arrayList, ListView listView, CustomTextView text_total,int total) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        items = arrayList;
        mListview=listView;
        this.text_total=text_total;
        this.total=total;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {

            this.mPosition=position;

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayout, parent, false);
            vHolder = new ViewHolderCoupon(convertView);
            convertView.setTag(vHolder);

        } else {
            vHolder = (ViewHolderCoupon) convertView.getTag();
        }

        getIPAPI=new GetIPAPI();

        mSQLite = SQLiteHelper.getInstance(mContext);

        sp=getContext().getSharedPreferences("CouponValue", Context.MODE_PRIVATE);

        final CustomItemCoupon item = items.get(position);

        vHolder.textCouponNo.setText(item.mTextCouponNo);
        vHolder.textPrice.setText(item.mTextPrice);
        vHolder.textNo.setText(item.mNum+".");

        final MyFont myFont =new MyFont(getContext());
        vHolder.textPrice.setTypeface(myFont.setFont());
        vHolder.textCouponNo.setTypeface(myFont.setFont());
        vHolder.textPrice.setTypeface(myFont.setFont());

        vHolder.clear.setImageResource(R.drawable.ic_cancel_black_24dp2);
        vHolder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,""+item.mTextCouponNo,Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView title=dialog.findViewById(R.id.tv_quit_learning);
                TextView des=dialog.findViewById(R.id.tv_description);
                title.setText("แจ้งเตือน");
                des.setText("ยกเลิกการใช้คูปองหมายเลขนี้?");
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                declineButton.setVisibility(View.GONE);
                Button okButton =  dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        MyAdapterCoupon.this.remove(MyAdapterCoupon.this.getItem(position));
                        MyAdapterCoupon.this.remove(position);
                        MyAdapterCoupon.this.notifyDataSetChanged();
                        mListview.setAdapter(MyAdapterCoupon.this);
                        sp.edit().remove(item.mNum).commit();

                        mDb = mSQLite.getReadableDatabase();
                        String sqlDeletePrivilae = "Delete from privilage where CouponNo = '"+item.mTextCouponNo+"'";
                        mDb.execSQL(sqlDeletePrivilae);

                        String url=getIPAPI.IPAddress+"/CleanmatePOS/CancelCoupon.php?CouponNo="+item.mTextCouponNo;
                        new MyAsyncTask().execute(url);

                        total=total-Integer.parseInt(item.mTextPrice);
                        text_total.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+total+" บาท");

                        SharedPreferences sp1 = getContext().getSharedPreferences("CouponValue", Context.MODE_PRIVATE);
                        sp1.edit().remove(item.mTextCouponNo).apply();
                    }
                });
                okButton.setTypeface(myFont.setFont());
                declineButton.setTypeface(myFont.setFont());
                title.setTypeface(myFont.setFont());
                des.setTypeface(myFont.setFont());

                //laodData();
                //mTextTotal.setText("ราคารวม \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t "+getFormatedAmount(totalPrice)+" ฿");
            }
        });

        int width =mContext.getResources().getDisplayMetrics().widthPixels;

        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins((width*15)/100, 20, 0, 20);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins((width*40)/100, 20, 0, 20);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams3.setMargins((width*75)/100, 20, 0, 20);
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams4.setMargins((width*90)/100, 20, 0, 20);

        vHolder.textNo.setLayoutParams(layoutParams1);
        vHolder.textCouponNo.setLayoutParams(layoutParams2);
        vHolder.textPrice.setLayoutParams(layoutParams3);
        vHolder.clear.setLayoutParams(layoutParams4);


        return convertView;
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
                if(chk=='a'){
                    arrNum.add(getData2[k].substring(3));
                }
            }
        }
        for(int i=0;i<arrNum.size();i++){
            totalPrice+=Integer.parseInt(arrNum.get(i));
        }
        System.out.println("Total : "+totalPrice);
    }
    class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กำลังตรวจสอบข้อมูล");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected  String doInBackground(String... strings) {
            String response="";
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

            String output="";
            try {
                System.out.println(response);
            }catch (Exception ex){
                System.out.println("Error2 : "+ex);
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
        }
    }


}