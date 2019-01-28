package com.techlogn.cleanmate_pos_v36;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.PermissionActivity;
import com.luseen.simplepermission.permissions.PermissionUtils;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;

public class FactoryListPackage extends PermissionActivity {

    String proID,orderDetailID,branchID,orderNo,orderID,packageType;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private GetIPAPI getIPAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_list_package);

        getIPAPI=new GetIPAPI();
        //Log
        /*Bugfender.init(this, "RlG2SafK3kOHo2XvAfqwEZMMOnLl0yGB", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(getApplication());*/


        cd = new ConnectionDetector(FactoryListPackage.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            scan();
            proID = getIntent().getExtras().get("productID").toString();
            orderDetailID = getIntent().getExtras().get("orderDetail").toString();
            branchID = getIntent().getExtras().get("branchID").toString();
            orderID = getIntent().getExtras().get("orderID").toString();
            orderNo = getIntent().getExtras().get("orderNo").toString();
            packageType = getIntent().getExtras().get("packageType").toString();

        }else {
            new MyToast(FactoryListPackage.this, "ไม่มีการเชื่อมต่อ Internet", 0);
        }
    }
    private void scanBarcode() {
        Intent it = new Intent(this, Barcode.class);
        finish();
        startActivityForResult(it, 1010);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String contents;
        if (requestCode == 1010 && resultCode == RESULT_OK) {
            contents = data.getStringExtra("barcode");
            final ProgressDialog dialog = new ProgressDialog(FactoryListPackage.this);
            dialog.setIcon(R.mipmap.loading);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กรุณารอสักครู่....");
            dialog.setIndeterminate(true);
            dialog.show();
            Ion.with(FactoryListPackage.this)
                    .load(getIPAPI.IPAddress+"/test%20server%20for%20mobile%20app/Transport.php")
                    .setBodyParameter("Data1", branchID)
                    .setBodyParameter("orderDetail",orderDetailID)
                    .setBodyParameter("Data3", orderNo)
                    .setBodyParameter("Data4", "" + contents)
                    .setBodyParameter("packageType","" + packageType)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {

                            new MyToast(getBaseContext(), result, 1);
                            dialog.dismiss();
                        }
                    });
        }
    }
    public void scan(){
        if (PermissionUtils.isMarshmallowOrHigher()) {
            requestPermission(com.luseen.simplepermission.permissions.Permission.CAMERA, new SinglePermissionCallback() {
                @Override
                public void onPermissionResult(boolean permissionGranted,
                                               boolean isPermissionDeniedForever) {

                    if (!permissionGranted) {
                        return;
                    }
                }
            });
        }
        scanBarcode();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
