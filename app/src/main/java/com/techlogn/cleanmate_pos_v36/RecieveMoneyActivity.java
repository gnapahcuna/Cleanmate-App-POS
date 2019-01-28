package com.techlogn.cleanmate_pos_v36;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static maes.tech.intentanim.CustomIntent.customType;

public class RecieveMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    CardView[] cards;
    TextView[] texts;
    //ImageView[]images;
    CardView c1,c2,c3,c4,c5,c6,c7,c8,c9;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,total;
    ImageView back;
    //ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,back;

    int num1=0,num2=0,num3=0,num4=0,num5=0,num6=0,num7=0,num8=0,num9=0;
    int sum=0,orderNo,branchID,totals,isBack=0;

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_money);

        sp=getSharedPreferences("Cash", Context.MODE_PRIVATE);

        c1=findViewById(R.id.card1);
        c2=findViewById(R.id.card3);
        c3=findViewById(R.id.card5);
        c4=findViewById(R.id.card7);
        c5=findViewById(R.id.card9);
        c6=findViewById(R.id.card2);
        c7=findViewById(R.id.card4);
        c8=findViewById(R.id.card6);
        c9=findViewById(R.id.card8);


        t1=findViewById(R.id.txtNum1);
        t2=findViewById(R.id.txtNum3);
        t3=findViewById(R.id.txtNum5);
        t4=findViewById(R.id.txtNum7);
        t5=findViewById(R.id.txtNum9);
        t6=findViewById(R.id.txtNum2);
        t7=findViewById(R.id.txtNum4);
        t8=findViewById(R.id.txtNum6);
        t9=findViewById(R.id.txtNum8);

        /*img1=findViewById(R.id.img_more1);
        img2=findViewById(R.id.img_more3);
        img3=findViewById(R.id.img_more5);
        img4=findViewById(R.id.img_more7);
        img5=findViewById(R.id.img_more9);
        img6=findViewById(R.id.img_more2);
        img7=findViewById(R.id.img_more4);
        img8=findViewById(R.id.img_more6);
        img9=findViewById(R.id.img_more8);*/


        texts=new TextView[]{t1,t2,t3,t4,t5,t6,t7,t8,t9};

        cards=new CardView[]{c1,c2,c3,c4,c5,c6,c7,c8,c9};

        /*images=new ImageView[]{img1,img2,img3,img4,img5,img6,img7,img8,img9};*/

        total=findViewById(R.id.textTotal);

        for (CardView c:cards
                ) {
            c.setOnClickListener(this);
        }

        for (TextView t:texts
             ) {
            if(Integer.parseInt(t.getText().toString())==0){
                t.setVisibility(View.GONE);
            }else{
                t.setVisibility(View.VISIBLE);
            }
        }

       /* for (ImageView img:images
             ) {
            img.setOnClickListener(this);
        }*/

        int width=getResources().getDisplayMetrics().widthPixels;
        int height=getResources().getDisplayMetrics().heightPixels;

        RelativeLayout cancel = findViewById(R.id.btn_scan);
        cancel.getLayoutParams().width = (width*45)/100;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RecieveMoneyActivity.this);
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                TextView des = dialog.findViewById(R.id.tv_description);
                title.setText("แจ้งเตือน");
                des.setText("ยกเลิกข้อมูลการรับเงินสด");
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        sp.edit().clear().commit();
                        new MyToast(RecieveMoneyActivity.this,"ยกเลิกรายการเรียบร้อย",2);
                        setTextColor();
                        for (TextView t:texts
                                ) {
                            t.setText(""+0);
                            t.setTextColor(Color.parseColor("#000000"));
                            total.setText("");
                            sum=0;
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
                MyFont myFont = new MyFont(RecieveMoneyActivity.this);
                okButton.setTypeface(myFont.setFont());
                declineButton.setTypeface(myFont.setFont());
                title.setTypeface(myFont.setFont());
                des.setTypeface(myFont.setFont());
            }
        });
        if(sp.getAll().size()>0){
            Map<String, ?> entries2 = sp.getAll();
            Set<String> keys2 = entries2.keySet();
            String[] getData2;
            List<String> list2 = new ArrayList<String>(keys2);
            for (String temp : list2) {
                for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                    getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                    char chk = getData2[i].charAt(1);
                    if (chk == 'a') {
                        t1.setText(""+getFormatedAmount(Integer.parseInt(getData2[i].substring(3))));
                    } else if (chk == 'b') {
                        t2.setText(""+getFormatedAmount(Integer.parseInt(getData2[i].substring(3))));
                    } else if (chk == 'c') {
                        t3.setText(""+getFormatedAmount(Integer.parseInt(getData2[i].substring(3))));
                    }else if (chk == 'd') {
                        t4.setText(""+getFormatedAmount(Integer.parseInt(getData2[i].substring(3))));
                    } else if (chk == 'e') {
                        t5.setText(""+getFormatedAmount(Integer.parseInt(getData2[i].substring(3))));
                    }else if (chk == 'f') {
                        t6.setText(""+getFormatedAmount(Integer.parseInt(getData2[i].substring(3))));
                    } else if (chk == 'g') {
                        t7.setText(""+getFormatedAmount(Integer.parseInt(getData2[i].substring(3))));
                    }else if (chk == 'h') {
                        t8.setText(""+getFormatedAmount(Integer.parseInt(getData2[i].substring(3))));
                    } else if (chk == 'i') {
                        t9.setText(""+getFormatedAmount(Integer.parseInt(getData2[i].substring(3))));
                    }else if (chk == 'j') {
                        sum=Integer.parseInt(getData2[i].substring(3));
                        total.setText("รับเงินจำนวน "+getFormatedAmount(Integer.parseInt(getData2[i].substring(3)))+" บาท");
                    }


                }
            }
        }
        orderNo = Integer.parseInt(getIntent().getExtras().get("orderNo").toString());
        branchID = Integer.parseInt(getIntent().getExtras().get("branchID").toString());
        totals = Integer.parseInt(getIntent().getExtras().get("total").toString());

        try{
            isBack=Integer.parseInt(getIntent().getExtras().get("back").toString());
        }catch (Exception ex){
            //isBack=0;
        }
        System.out.print("isBack2 = "+isBack);

        RelativeLayout next = findViewById(R.id.layoutClick_addcart);
        next.getLayoutParams().width = (width*45)/100;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sum<totals){
                    new MyToast(RecieveMoneyActivity.this,"ยอดเงินยังไม่พอสำหรับชำระค่าบริการ",0);
                }else{
                    SharedPreferences.Editor editor = sp.edit();
                    ArrayList<String>arr_proTH=new ArrayList<>();
                    arr_proTH.add("Cash");
                    StringBuilder sb = new StringBuilder();
                    HashSet<String> mSet = new HashSet<>();
                    for (int i = 0; i < arr_proTH.size(); i++) {
                        mSet.add("<a>" + t1.getText().toString());
                        mSet.add("<b>" + t2.getText().toString());
                        mSet.add("<c>" + t3.getText().toString());
                        mSet.add("<d>" + t4.getText().toString());
                        mSet.add("<e>" + t5.getText().toString());
                        mSet.add("<f>" + t6.getText().toString());
                        mSet.add("<g>" + t7.getText().toString());
                        mSet.add("<h>" + t8.getText().toString());
                        mSet.add("<i>" + t9.getText().toString());
                        mSet.add("<j>" + sum);
                    }
                    editor.putStringSet("Cash", mSet);
                    editor.apply();

                    Intent intent = new Intent(getBaseContext(), ResultPaymentActivity.class);
                    intent.putExtra("select",""+1);
                    intent.putExtra("ID",""+0);
                    intent.putExtra("orderNo", orderNo);
                    intent.putExtra("branchID",branchID);
                    intent.putExtra("total", totals);
                    intent.putExtra("back", isBack);
                    startActivity(intent);
                    customType(RecieveMoneyActivity.this,"fadein-to-fadeout");
                    finish();
                }
            }
        });

        LinearLayout layout =findViewById(R.id.linearLayout7);

        back=findViewById(R.id.img_black2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TypePaymentActivity.class);
                intent.putExtra("ID",""+0);
                intent.putExtra("orderNo", orderNo);
                intent.putExtra("branchID",branchID);
                intent.putExtra("total", totals);
                intent.putExtra("back", isBack);
                startActivity(intent);
                customType(RecieveMoneyActivity.this,"fadein-to-fadeout");
                finish();
            }
        });

        setTextColor();

        ScrollView scrollView=findViewById(R.id.score);
        LinearLayout.LayoutParams rl =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        rl.setMargins(0, 0, 0, (height*32)/100);
        scrollView.setLayoutParams(rl);

        TextView title =findViewById(R.id.textTitle);
        title.setText("เลือกจำนวนเงิน (ยอดชำระ = "+ getFormatedAmount(totals) +" บาท)");
    }

    @Override
    public void onClick(View v) {

        final Dialog dialog = new Dialog(RecieveMoneyActivity.this);
        dialog.setContentView(R.layout.custom_select_num);
        TextView title = dialog.findViewById(R.id.tv_quit_learning);
        Button okButton = dialog.findViewById(R.id.btn_ok);
        final NumberPicker numberPicker = dialog.findViewById(R.id.number_picker);
        MyFont myFont=new MyFont(RecieveMoneyActivity.this);

        switch (v.getId()){
            //select card
            case R.id.card1 :
                num1=Integer.parseInt(t1.getText().toString())+1;
                t1.setText(""+getFormatedAmount(num1));
                sum+=1000;
                break;
            case R.id.card3 :
                num2=Integer.parseInt(t2.getText().toString())+1;
                t2.setText(""+getFormatedAmount(num2));
                sum+=500;
                break;
            case R.id.card5 :
                num3=Integer.parseInt(t3.getText().toString())+1;
                t3.setText(""+getFormatedAmount(num3));
                sum+=100;
                break;
            case R.id.card7:
                num4=Integer.parseInt(t4.getText().toString())+1;
                t4.setText(""+getFormatedAmount(num4));
                sum+=50;
                break;
            case R.id.card9 :
                num5=Integer.parseInt(t5.getText().toString())+1;
                t5.setText(""+getFormatedAmount(num5));
                sum+=20;
                break;
            case R.id.card2 :
                num6=Integer.parseInt(t6.getText().toString())+1;
                t6.setText(""+getFormatedAmount(num6));
                sum+=10;
                break;
            case R.id.card4 :
                num7=Integer.parseInt(t7.getText().toString())+1;
                t7.setText(""+getFormatedAmount(num7));
                sum+=5;
                break;
            case R.id.card6 :
                num8=Integer.parseInt(t8.getText().toString())+1;
                t8.setText(""+getFormatedAmount(num8));
                sum+=2;
                break;
            case R.id.card8 :
                num9=Integer.parseInt(t9.getText().toString())+1;
                t9.setText(""+getFormatedAmount(num9));
                sum+=1;
                break;


        }
        for (TextView t:texts
                ) {
            if(Integer.parseInt(t.getText().toString())==0){
                t.setVisibility(View.GONE);
            }else{
                t.setVisibility(View.VISIBLE);
            }
        }

        total.setText("รับเงินจำนวน "+getFormatedAmount(sum)+" บาท");
        setTextColor();
    }
    public void setTextColor(){
        for (TextView t:texts
                ) {
            if(Integer.parseInt(t.getText().toString())>0) {
                t.setTextColor(Color.parseColor("#0099cc"));
                //t.setVisibility(View.VISIBLE);
            }else{
                t.setTextColor(Color.parseColor("#000000"));
                //t.setVisibility(View.GONE);
            }
        }
    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
