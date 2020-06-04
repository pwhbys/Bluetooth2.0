package com.WideMouth.bluetooth20.Activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.WideMouth.bluetooth20.Util.Settings;
import com.WideMouth.bluetooth20.Util.WMUtil;

import org.litepal.LitePal;

public class LaunchActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (allPermissionsGranted()) {
            doAfterPermissionsGranted();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (allPermissionsGranted()) {
                    doAfterPermissionsGranted();
                } else {
                    Toast.makeText(this, "必须同意所有权限才能实现蓝牙通信功能！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void doAfterPermissionsGranted() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        if (LitePal.findAll(Settings.class).size() == 0) {
            Settings settings = new Settings();
            settings.save();
        }
        WMUtil.settings = LitePal.findLast(Settings.class);
        finish();
    }


    private boolean allPermissionsGranted() {
        for (String permission : PERMISSIONS_REQUIRED) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
