package com.lakesidestudio.apphelper;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lakesidestudio.apphelper.util.DeviceManagerUtil;

/**
 * Created by blackkensai on 16-10-5.
 */

public class LockActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceManagerUtil.lock(this);
    }
}
