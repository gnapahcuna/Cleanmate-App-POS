package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anucha on 1/4/2018.
 */

public class FragmentCategory extends Fragment {

    View myView;
    ArrayList<MyItemCategory> items;
    String dataID,branchID;
    SharedPreferences sharedPreferences;
    ListView listView;
    MyAdapterCategory myAdapter;
    TextView textTitle;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_category, container, false);


        //Log
        /*Bugfender.init(getActivity(), "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getActivity().getApplication());*/


        mSQLite=SQLiteHelper.getInstance(getActivity());

        items=new ArrayList<>();
        sharedPreferences = getContext().getSharedPreferences("ORDER", Activity.MODE_PRIVATE);

        MyFont myFont=new MyFont(getActivity());
        textTitle=myView.findViewById(R.id.textService);
        textTitle.setTypeface(myFont.setFont());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dataID=bundle.getString("ID");
            branchID=bundle.getString("branchID");
        }

        myView.setFocusableInTouchMode(true);
        myView.requestFocus();
        myView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Intent i=new Intent(getActivity(),MenuActivity.class);
                        i.putExtra("ID",dataID);
                        i.putExtra("branchID",branchID);
                        startActivity(i);
                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });

        return myView;
    }
    @Override
    public void onStart() {
        super.onStart();

        mDb = mSQLite.getReadableDatabase();
        String sql = "SELECT * FROM category";
        Cursor cursor = mDb.rawQuery(sql, null);
        String str = "";
        while (cursor.moveToNext()) {

            if (cursor.getInt(0) < 3) {
                str += "0";

            }

            items.add(new MyItemCategory(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)));
        }

        myAdapter=new MyAdapterCategory(getActivity(),R.layout.category_list,items);
        listView=myView.findViewById(R.id.list_cate);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putInt("key",i+1);
                bundle.putString("ID",dataID);

                FragmentCategoryList fragment_cate_list = new FragmentCategoryList();
                fragment_cate_list.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,fragment_cate_list)
                        .commit();
            }
        });
        listView.setAdapter(myAdapter);


        cursor.close();
    }
}