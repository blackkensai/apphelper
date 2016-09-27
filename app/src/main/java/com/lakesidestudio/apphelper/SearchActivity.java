package com.lakesidestudio.apphelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Search Application");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.SEARCH_NAME, ((EditText) findViewById(R.id.searchText)).getText().toString());
                int systemId = ((RadioGroup) findViewById(R.id.systemRadio)).getCheckedRadioButtonId();
                if (systemId == R.id.systemIs) {
                    bundle.putBoolean(Constants.SEARCH_SYSTEM, true);
                } else if (systemId == R.id.systemNot) {
                    bundle.putBoolean(Constants.SEARCH_SYSTEM, false);
                }
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
