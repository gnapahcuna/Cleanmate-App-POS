package com.techlogn.cleanmate_pos_v36;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.libRG.CustomTextView;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by anucha on 2/16/2018.
 */

public class MyAdapterCategoryList extends ArrayAdapter {

    ViewHolderCategory vHolder;
    View v;
    Fragment fragment;
    private Context mContext;
    private ArrayList<CustomItemCategory> items;
    private int mLayout,mLayout1;
    int count_pro=1,numClick=0,price=0;
    int num=1,mCount=0;
    CustomTextView count;
    LayoutInflater inflater;
    String result;
    String mDataID;
    SharedPreferences sp;
    ArrayList<String>arrNum;
    int mPosition;
    int sum=0;
    ListView mListview;
    TextView mText1,mText2,mText3;
    int total_price=0,totalCount=0;
    int count_pro_total = 0, price_total = 0,sum_num = 0;

    private GetIPAPI getIPAPI;

    public MyAdapterCategoryList(Context context, int layout, ArrayList<CustomItemCategory> arrayList, String dataID, int layout1, ListView listView, TextView text1, TextView text2, TextView text3) {
        super(context, layout, arrayList);
        mContext = context;
        mLayout = layout;
        items = arrayList;
        mLayout1=layout1;
        mDataID=dataID;
        mListview=listView;

        mText1=text1;
        mText2=text2;
        mText3=text3;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {

            this.mPosition=position;

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayout, parent, false);
            vHolder = new ViewHolderCategory(convertView);
            convertView.setTag(vHolder);

            v=inflater.inflate(mLayout1,parent,false);
        } else {
            vHolder = (ViewHolderCategory)convertView.getTag();
        }

        getIPAPI=new GetIPAPI();

        sp=getContext().getSharedPreferences("ORDER", Context.MODE_PRIVATE);


        final String subImg = getIPAPI.IPAddressImage+"/" + items.get(position).mImage.substring(3).trim();
        final CustomItemCategory item = items.get(position);
        vHolder.textProductTH.setText(item.mTextProductTH);
        vHolder.textProductEN.setText(item.mTextProductEN);
        vHolder.textprice.setText(getFormatedAmount(Integer.parseInt(item.mTextPrice)));
        Picasso.with(getContext()).load(subImg).into(vHolder.image);
        vHolder.viewColor.setBackgroundColor(Color.parseColor(item.mColor));


        vHolder.serTH.setText(item.mServiceNameTH);
        vHolder.serEN.setText(item.mServiceNameEN);
        vHolder.serType.setText(item.mServiceType);
        vHolder.proID.setText(item.mTextProductID);
        vHolder.string_image.setText(subImg);
        vHolder.color_code.setText(item.mColor);


        vHolder.serTH.setVisibility(View.GONE);
        vHolder.serEN.setVisibility(View.GONE);
        vHolder.serType.setVisibility(View.GONE);
        vHolder.proID.setVisibility(View.GONE);
        vHolder.string_image.setVisibility(View.GONE);
        vHolder.color_code.setVisibility(View.GONE);
        vHolder.thb.setText("บาท");


        final MyFont myFont =new MyFont(getContext());
        vHolder.textProductTH.setTypeface(myFont.setFont());
        vHolder.textProductEN.setTypeface(myFont.setFont());
        vHolder.textprice.setTypeface(myFont.setFont());
        vHolder.thb.setTypeface(myFont.setFont());

        arrNum=new ArrayList<>();
        Map<String,?> entries2 = sp.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                if(temp.trim().equals(item.mTextProductID.trim())){
                    //System.out.println("Match");
                    char chk=getData2[i].charAt(1);
                    if(chk=='h'){
                        arrNum.add(getData2[i].substring(3));
                    }else if(chk=='g'){
                        count_pro=Integer.parseInt(getData2[i].substring(3));
                    }

                }
            }
        }
        vHolder.num.setText("");

        for(int i=0;i<arrNum.size();i++){
            if(items.get(position).mTextProductID.trim().equals(arrNum.get(i).trim())){
                vHolder.num.setText(""+count_pro);


            }
        }
        numClick=sp.getAll().size();
        vHolder.more.setImageResource(R.drawable.ic_more_vert_black_24dp);
        vHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price_total = 0;
                count_pro_total = 0;
                price = Integer.parseInt(item.mTextPrice);
                Map<String, ?> entries2 = sp.getAll();
                Set<String> keys2 = entries2.keySet();
                String[] getData2;
                List<String> list2 = new ArrayList<String>(keys2);
                ArrayList<String> arr_price = new ArrayList<>();
                ArrayList<String> arr_count = new ArrayList<>();
                for (String temp : list2) {
                    //System.out.println(temp+" = "+mProductID);
                    if (temp.trim().equals(item.mTextProductID)) {
                        for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                            getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                            char chk = getData2[i].charAt(1);
                            if (chk == 'g') {
                                count_pro = Integer.parseInt(getData2[i].substring(3));
                            } else if (chk == 'c') {
                                price = Integer.parseInt(getData2[i].substring(3));
                            }
                        }
                        break;
                    } else {
                        price = Integer.parseInt(item.mTextPrice);
                        count_pro = 1;
                    }
                    for (int i = 0; i < sp.getStringSet(temp, null).size(); i++) {
                        getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                        char chk = getData2[i].charAt(1);
                        if (chk == 'g') {
                            count_pro_total += Integer.parseInt(getData2[i].substring(3));
                        } else if (chk == 'c') {
                            price_total += Integer.parseInt(getData2[i].substring(3));
                        }
                    }
                }
                int c = price * count_pro;
                System.out.println(price + "x" + count_pro + " = " + c);
                System.out.println(count_pro_total + "><" + price_total);

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

                        SharedPreferences sp = getContext().getSharedPreferences("ORDER", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        ArrayList<String> arr_proTH = new ArrayList<>();
                        arr_proTH.add(item.mTextProductTH);
                        StringBuilder sb = new StringBuilder();
                        HashSet<String> mSet = new HashSet<>();
                        for (int j = 0; j < arr_proTH.size(); j++) {
                            mSet.add("<a>" + item.mTextProductTH);
                            mSet.add("<b>" + item.mTextProductEN);
                            mSet.add("<c>" + item.mTextPrice);
                            mSet.add("<d>" + item.mServiceNameTH);
                            mSet.add("<e>" + item.mServiceNameEN);
                            mSet.add("<f>" + subImg);
                            mSet.add("<g>" + numberPicker.getValue());
                            mSet.add("<h>" + item.mTextProductID);
                            mSet.add("<i>" + item.mColor);
                            mSet.add("<j>" + item.mServiceType);

                        }
                        editor.putStringSet(item.mTextProductID, mSet);
                        editor.apply();

                        int results=0;
                        int counts=0;
                        ArrayList<Integer>arr1=new ArrayList<>();
                        ArrayList<Integer>arr2=new ArrayList<>();
                        Map<String,?> entries2 = sp.getAll();
                        Set<String> keys2 = entries2.keySet();
                        String[] getData2;
                        List<String> list2 = new ArrayList<String>(keys2);
                        for (String temp : list2) {
                            for (int k = 0; k < sp.getStringSet(temp, null).size(); k++) {
                                getData2 = sp.getStringSet(temp, null).toArray(new String[sp.getStringSet(temp, null).size()]);
                                char chk=getData2[k].charAt(1);
                                if (chk == 'g') {
                                    arr1.add(Integer.parseInt(getData2[k].substring(3)));
                                } else if (chk == 'c') {
                                    arr2.add(Integer.parseInt(getData2[k].substring(3)));
                                }

                            }
                        }
                        for(int i=0;i<arr1.size();i++){
                            results+=arr1.get(i)*arr2.get(i);
                            counts+=arr1.get(i);
                        }
                        /*int sum_counts=counts+1;
                        int sum_result=results+Integer.parseInt(item.);*/

                        mText1.setText(" " + sp.getAll().size() + " ");
                        mText2.setText(" " + counts + " ");
                        mText3.setText(" " + getFormatedAmount(results) + " ");

                        vHolder.num.setText("" + num);
                        MyAdapterCategoryList.this.notifyDataSetChanged();
                        mListview.setAdapter(MyAdapterCategoryList.this);


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
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }


}