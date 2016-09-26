package com.lakesidestudio.apphelper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.service.notification.Condition.SCHEME;

/**
 * Created by blackkensai on 16-9-25.
 */

public class AppHelper {
    public static final String GOOGLE_PLAY_SERVICE_PACKAGE = "com.google.android.gms";
    public static final String GOOGLE_FRAMEWORK_PACKAGE = "com.google.android.gsf";
    public static final String GOOGLE_PLAY_PACKAGE = "com.android.vending";

    private Context context;

    public AppHelper(Context context) {
        this.context = context;
    }

    public List<ApplicationInfo> getAppList() {
        List<ApplicationInfo> packages = new ArrayList<>(100);
        for (ApplicationInfo info :
                context.getPackageManager()
                        .getInstalledApplications(PackageManager.GET_META_DATA)) {
//            if (!filter(info.packageName)) {
//                continue;
//            }
            packages.add(info);
        }
        Collections.sort(packages, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo a0, ApplicationInfo a1) {
                return a0.packageName.compareTo(a1.packageName);
            }
        });
        return packages;
    }

    public boolean isSystemApp(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public boolean isGame(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & ApplicationInfo.FLAG_IS_GAME) != 0;
    }

    public CharSequence getAppName(ApplicationInfo applicationInfo) {
        return context.getPackageManager().getApplicationLabel(applicationInfo);
    }

    public Drawable getAppIcon(ApplicationInfo applicationInfo) {
        return context.getPackageManager().getApplicationIcon(applicationInfo);
    }

    public void runApplication(ApplicationInfo applicationInfo) {
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(applicationInfo.packageName));
    }

    public void openSettingActivity(ApplicationInfo applicationInfo) {
        String packageName = applicationInfo.packageName;
        openSettingActivity(packageName);
    }

    private void openSettingActivity(String packageName) {
        final int apiLevel = Build.VERSION.SDK_INT;
        Intent intent = new Intent();

        if (apiLevel >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
        } else {
            final String appPkgName = (apiLevel == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");

            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra(appPkgName, packageName);
        }

        // Start Activity
        context.startActivity(intent);
    }

    public void openInGooglePlay(ApplicationInfo applicationInfo) {
        String packageName = applicationInfo.packageName;
        openInGooglePlay(packageName);

    }

    private void openInGooglePlay(String packageName) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals(GOOGLE_PLAY_PACKAGE)) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }
        }
    }

    public void openGooglePlayServicePage4Upgrade() {
        this.openInGooglePlay(GOOGLE_PLAY_SERVICE_PACKAGE);
    }

    public void openGooglePlayServiceSetting() {
        this.openSettingActivity(GOOGLE_PLAY_SERVICE_PACKAGE);
    }

    public void openGoogleFrameworkSetting() {
        this.openSettingActivity(GOOGLE_FRAMEWORK_PACKAGE);
    }

    public void openGooglePlaySetting() {
        this.openSettingActivity(GOOGLE_PLAY_PACKAGE);
    }
}
