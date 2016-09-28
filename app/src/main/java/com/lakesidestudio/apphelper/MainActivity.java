package com.lakesidestudio.apphelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.lakesidestudio.apphelper.domain.Constants;
import com.lakesidestudio.apphelper.domain.SearchCondition;

public class MainActivity extends AppCompatActivity {
    public static final int SEARCH_REQUEST = 100;
    private AppHelper appHelper;
    private AppListAdapter appListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appHelper = new AppHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(Constants.SEARCH_CONDITION, ((AppListAdapter.AppListFilter) appListAdapter.getFilter()).getSearchCondition());
                intent.putExtras(extras);
                startActivityForResult(intent, SEARCH_REQUEST);
            }
        });

        readAppList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_googleplayservice_upgrade:
                this.appHelper.openGooglePlayServicePage4Upgrade();
                return true;
            case R.id.action_googleplayservice_setting:
                this.appHelper.openGooglePlayServiceSetting();
                return true;
            case R.id.action_googleframework_setting:
                this.appHelper.openGoogleFrameworkSetting();
                return true;
            case R.id.action_googleplaystore_setting:
                this.appHelper.openGooglePlaySetting();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readAppList() {
        ListView listView = (ListView) findViewById(R.id.listview);
        appListAdapter = new AppListAdapter(this);
        listView.setAdapter(appListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SEARCH_REQUEST:
                if (resultCode == RESULT_OK) {
                    ListView listView = (ListView) findViewById(R.id.listview);
                    Bundle extras = data.getExtras();
                    AppListAdapter appListAdapter = (AppListAdapter) listView.getAdapter();
                    AppListAdapter.AppListFilter filter = (AppListAdapter.AppListFilter) appListAdapter.getFilter();
                    filter.setSearchCondition((SearchCondition) extras.get(Constants.SEARCH_CONDITION));
                    filter.filter(null);
                }
                break;
        }
    }
}
