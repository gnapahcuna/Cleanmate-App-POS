package com.techlogn.cleanmate_pos_v36;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by anucha on 1/3/2018.
 */

public class MyAdapterSuppliesOrder extends ArrayAdapter {

    ViewHolderSuppliesOrder vHolder;
    int mPosition;
    View v;
    Fragment fragment;
    private Context mContext;
    private ArrayList<CustomItemSuppliesOrder> items;
    private int mLayout;
    LayoutInflater inflater;
    ArrayList<String>arrNum;
    SharedPreferences sp;

    ListView mListview;
    TextView result;
    int count_pro_total = 0,sum_num = 0,num = 1,count_pro=0;
    double price = 0.00,price_total=0.00;

    public MyAdapterSuppliesOrder(Context context, int layout, ArrayList<CustomItemSuppliesOrder> arrayList, ListView listView, TextView text) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        items = arrayList;

        mListview=listView;
        result=text;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            this.mPosition = position;

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayout, parent, false);
            vHolder = new ViewHolderSuppliesOrder(convertView);
            convertView.setTag(vHolder);

        } else {
            vHolder = (ViewHolderSuppliesOrder) convertView.getTag();
        }
        sp = getContext().getSharedPreferences("Supplies", Context.MODE_PRIVATE);

        final CustomItemSuppliesOrder item = items.get(position);
        vHolder.textProductTH.setText(item.mSuppliesNameTH);

        vHolder.textprice.setText(String.format("%,.2f", Double.parseDouble(item.mTextPrice)));

        vHolder.num.setText(item.mNum);
        vHolder.thb.setText("บาท");

        arrNum = new ArrayList<>();
        Map<String, ?> entries2 = sp.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                System.out.println(temp+" == "+item.mSuppliesNameTH);
                if (temp.trim().equals(item.mSuppliesNameTH.trim())) {
                    char chk = getData2[i].charAt(1);
                    if (chk == 'a') {
                        arrNum.add(getData2[i].substring(3));
                    } else if (chk == 'c') {
                        count_pro = Integer.parseInt(getData2[i].substring(3));
                    }

                }
            }
        }
        System.out.println(sp.getAll().size());
        vHolder.num.setText("");
        for (int i = 0; i < arrNum.size(); i++) {
            if (items.get(position).mSuppliesNameTH.trim().equals(arrNum.get(i).trim())) {
                vHolder.num.setText("" + count_pro);
            }
        }

        vHolder.more.setImageResource(R.drawable.ic_more_vert_black_24dp);
        vHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price_total = 0.00;
                count_pro_total = 0;
                price = Double.parseDouble(item.mTextPrice);
                Map<String, ?> entries2 = sp.getAll();
                Set<String> keys2 = entries2.keySet();
                String[] getData2;
                List<String> list2 = new ArrayList<String>(keys2);
                for (String temp : list2) {
                    //System.out.println(temp+" = "+mProductID);
                    if (temp.trim().equals(item.mSuppliesNameTH)) {
                        for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                            getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                            char chk = getData2[i].charAt(1);
                            if (chk == 'c') {
                                count_pro = Integer.parseInt(getData2[i].substring(3));
                            } else if (chk == 'b') {
                                price = Double.parseDouble(getData2[i].substring(3));
                            }
                        }
                        break;
                    } else {
                        price = Double.parseDouble(item.mTextPrice);
                        count_pro = 1;
                    }
                    for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                        getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                        char chk = getData2[i].charAt(1);
                        if (chk == 'c') {
                            count_pro_total += Integer.parseInt(getData2[i].substring(3));
                        } else if (chk == 'b') {
                            price_total += Double.parseDouble(getData2[i].substring(3));
                        }
                    }
                }

                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.custom_select_num);
                dialog.show();
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                title.setText("เลือกจำนวนตามต้องการ");
                Button okButton = dialog.findViewById(R.id.btn_ok);
                final NumberPicker numberPicker = dialog.findViewById(R.id.number_picker);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(50);
                numberPicker.setValue(count_pro);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sum_num = numberPicker.getValue();

                        SharedPreferences.Editor editor = sp.edit();
                        ArrayList<String> arr_proTH = new ArrayList<>();
                        arr_proTH.add(item.mSuppliesNameTH);
                        StringBuilder sb = new StringBuilder();
                        HashSet<String> mSet = new HashSet<>();
                        for (int j = 0; j < arr_proTH.size(); j++) {
                            mSet.add("<a>" + item.mSuppliesNameTH);
                            mSet.add("<b>" + item.mTextPrice);
                            mSet.add("<c>" + numberPicker.getValue());
                        }
                        editor.putStringSet(item.mSuppliesNameTH, mSet);
                        editor.apply();

                        int results=0;
                        int counts=0;
                        ArrayList<Double>arr1=new ArrayList<>();
                        ArrayList<Integer>arr2=new ArrayList<>();
                        Map<String,?> entries2 = sp.getAll();
                        Set<String> keys2 = entries2.keySet();
                        String[] getData2;
                        List<String> list2 = new ArrayList<String>(keys2);
                        for (String temp : list2) {
                            for (int k = 0; k < sp.getStringSet(temp, null).size(); k++) {
                                getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                                char chk=getData2[k].charAt(1);
                                if (chk == 'b') {
                                    arr1.add(Double.parseDouble(getData2[k].substring(3)));
                                } else if (chk == 'c') {
                                    arr2.add(Integer.parseInt(getData2[k].substring(3)));
                                }

                            }
                        }
                        for(int i=0;i<arr1.size();i++){
                            results+=arr1.get(i)*arr2.get(i);
                            counts+=arr2.get(i);
                        }
                        DecimalFormat formatter = new DecimalFormat("#,###,###.##");
                        result.setText("ราคารวม "+formatter.format(results)+" บาท");

                        vHolder.num.setText("" + num);
                        MyAdapterSuppliesOrder.this.notifyDataSetChanged();
                        mListview.setAdapter(MyAdapterSuppliesOrder.this);


                        dialog.dismiss();
                    }
                });
                MyFont myFont=new MyFont(mContext);
                okButton.setTypeface(myFont.setFont());
                title.setTypeface(myFont.setFont());
                numberPicker.setTypeface(myFont.setFont());
            }
        });


        return convertView;
    }
}