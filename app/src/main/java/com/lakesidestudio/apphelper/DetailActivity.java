package com.lakesidestudio.apphelper;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class DetailActivity extends AppCompatActivity {
    private AppHelper appHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appHelper = new AppHelper(this);
        Intent intent = getIntent();
        final ApplicationInfo applicationInfo = (ApplicationInfo)intent.getParcelableExtra("ApplicationInfo");
        this.setTitle(appHelper.getAppName(applicationInfo));

        findViewById(R.id.google_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appHelper.openInGooglePlay(applicationInfo);
            }
        });

        findViewById(R.id.setting_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appHelper.openSettingActivity(applicationInfo);
            }
        });

        findViewById(R.id.run_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appHelper.runApplication(applicationInfo);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
