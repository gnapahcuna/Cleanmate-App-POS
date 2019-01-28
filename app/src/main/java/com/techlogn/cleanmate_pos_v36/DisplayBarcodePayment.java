package com.techlogn.cleanmate_pos_v36;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayBarcodePayment extends AppCompatActivity {

    TextView tv1;
    ArrayList<TextView>arr_text_menu;
    LinearLayout ln_menu;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_barcode_paymrnt);


        ln_menu=findViewById(R.id.lv_menu);
        arr_text_menu=new ArrayList<>();

        loadDoc();
    }

    private void loadDoc() {

    }
}
