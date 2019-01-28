package com.techlogn.cleanmate_pos_v36;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomAdapterSuppliesOrder extends RecyclerView.Adapter<CustomSuppliesOrderHolder> {
      private Context mContext;
      private ArrayList<CustomItemSuppliesOrderList> mItems;
      private Bitmap mBitmap;
      private RoundedBitmapDrawable mRoundedBitmap;
      ProgressDialog dialog;
      private GetIPAPI getIPAPI =new GetIPAPI();
      RecyclerView mRcv;
      Dialog mPg;
      NotificationBadge mBage;
      int mCount;

      public CustomAdapterSuppliesOrder(Context context, ArrayList<CustomItemSuppliesOrderList> items,RecyclerView rc,Dialog pg, NotificationBadge badge,int counts) {
            mContext = context;
            mItems = items;
            mRcv=rc;
            mPg=pg;
            mBage=badge;
            mCount=counts;
      }

      public interface OnItemClickListener {
            void onItemClick(View item, int position);
      }

      private OnItemClickListener mListener;

      public void setOnClickListener(OnItemClickListener listener) {
            mListener = listener;
      }

      @Override
      public CustomSuppliesOrderHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            final View v = inflater.inflate(R.layout.list_supplies_order, parent, false);
            final CustomSuppliesOrderHolder vHolder = new CustomSuppliesOrderHolder(v);
            return vHolder;
      }

      @Override
      public void onBindViewHolder(final CustomSuppliesOrderHolder vHolder, final int position) {
            final CustomItemSuppliesOrderList item = mItems.get(position);
            vHolder.textOrder.setText("เลขที่ใบสั่ง : "+item.order);
            vHolder.textDate.setText("วันที่สั่ง : "+item.date);
            vHolder.textStatus.setText("สถานะ : "+item.status);
            vHolder.total.setText("ราคารวม "+String.format("%,.2f", item.total)+" บาท");

            final MyAdapterSuppliesOrderLevel1 mAdapter = new MyAdapterSuppliesOrderLevel1(mContext, R.layout.list_supplies_order_level1, item.content);
            mAdapter.notifyDataSetChanged();
            vHolder.list.getLayoutParams().height=(mContext.getResources().getDisplayMetrics().heightPixels*35)/100;
            vHolder.list.setAdapter(mAdapter);

            System.out.println("Is Check : "+item.isCheck);
            if(item.isCheck==0){
                  vHolder.btnGet.setEnabled(false);
                  vHolder.btnGet.setBackground(mContext.getResources().getDrawable(R.drawable.relative_layout_background_enable));
            }else{
                  vHolder.btnGet.setEnabled(true);
                  vHolder.btnGet.setBackground(mContext.getResources().getDrawable(R.drawable.relative_layout_background));
            }

            vHolder.btnGet.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.custon_alert_dialog);
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView title = dialog.findViewById(R.id.tv_quit_learning);
                        TextView des = dialog.findViewById(R.id.tv_description);
                        title.setText("แจ้งเตือน");
                        des.setText("ยืนยันการทำรายการ?");
                        Button okButton = dialog.findViewById(R.id.btn_ok);
                        okButton.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                    dialog.dismiss();
                                    String url1 = getIPAPI.IPAddress+"/CleanmatePOS/IsSuppliesBranchEmp.php?OrderNo="+item.order;
                                    new MyAsyncTask().execute(url1);

                                    mItems.remove(position);
                                    mRcv.setAdapter(CustomAdapterSuppliesOrder.this);
                                    mRcv.setLayoutManager(new LinearLayoutManager(mContext));

                                    if(mItems.size()==0){
                                          mPg.dismiss();
                                          mCount--;
                                          mBage.setNumber(mCount);
                                    }else{
                                          mCount--;
                                          mBage.setNumber(mCount);
                                    }
                              }
                        });
                        Button declineButton = dialog.findViewById(R.id.btn_cancel);
                        declineButton.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                    dialog.dismiss();
                              }
                        });
                        MyFont myFont = new MyFont(mContext);
                        okButton.setTypeface(myFont.setFont());
                        declineButton.setTypeface(myFont.setFont());
                        title.setTypeface(myFont.setFont());
                        des.setTypeface(myFont.setFont());
                  }
            });


      }

      @Override
      public int getItemCount() {
            return mItems.size();
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
            protected String doInBackground(String... strings) {


                  String response = "";
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
                        System.out.println("Error1 : " + ex);
                  }

                  String output = "";
                  try {
                        output = response;
                        System.out.println(response);
                  } catch (Exception ex) {
                        System.out.println("Error2 : " + ex);
                  }
                  return output;
            }

            @Override
            protected void onPostExecute(String s) {
                  super.onPostExecute(s);
                  dialog.dismiss();

                  new MyToast(mContext,s,2);
            }
      }

}
