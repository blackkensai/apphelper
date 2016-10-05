package com.lakesidestudio.apphelper.util;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.lakesidestudio.apphelper.QuickButtonAppWidget;

/**
 * Created by blackkensai on 16-10-5.
 */

public class DeviceManagerUtil {
    public static void lock(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        Class<?> cls = QuickButtonAppWidget.class;
        ComponentName componentName = new ComponentName(context, cls);
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            context.startActivity(intent);
        }
    }
}
