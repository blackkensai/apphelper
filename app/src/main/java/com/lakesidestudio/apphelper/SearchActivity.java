package com.lakesidestudio.apphelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lakesidestudio.apphelper.domain.Constants;
import com.lakesidestudio.apphelper.domain.SearchCondition;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Search Application");
        SearchCondition searchCondition = (SearchCondition) getIntent().getExtras().get(Constants.SEARCH_CONDITION);
        if (searchCondition.name != null && searchCondition.name.length() > 0) {
            ((EditText) findViewById(R.id.searchText)).setText(searchCondition.name);
        }
        if (searchCondition.system != null) {
            if (searchCondition.system) {
                ((RadioButton) findViewById(R.id.systemIs)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.systemNot)).setChecked(true);
            }
        }
        if (searchCondition.game != null) {
            ((CheckBox) findViewById(R.id.gameCheckbox)).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Bundle bundle = new Bundle();
                SearchCondition searchCondition = new SearchCondition();
                bundle.putParcelable(Constants.SEARCH_CONDITION, searchCondition);

                searchCondition.name = ((EditText) findViewById(R.id.searchText)).getText().toString();

                int systemId = ((RadioGroup) findViewById(R.id.systemRadio)).getCheckedRadioButtonId();
                if (systemId == R.id.systemIs) {
                    searchCondition.system = true;
                } else if (systemId == R.id.systemNot) {
                    searchCondition.system = false;
                } else {
                    searchCondition.system = null;
                }

                if (((CheckBox) findViewById(R.id.gameCheckbox)).isChecked()) {
                    searchCondition.game = true;
                } else {
                    searchCondition.game = null;
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
