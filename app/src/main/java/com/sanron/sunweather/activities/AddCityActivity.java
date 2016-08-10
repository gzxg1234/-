package com.sanron.sunweather.activities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.sanron.sunweather.R;
import com.sanron.sunweather.common.CityProvider;
import com.sanron.sunweather.entity.City;
import com.sanron.sunweather.utils.StringUtils;

public class AddCityActivity extends Activity implements OnItemClickListener, TextWatcher {

    private EditText etWord;
    private ListView lvMatch;
    private List<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        initView();
        setListener();
    }

    private void initView() {
        etWord = (EditText) findViewById(R.id.et_word);
        lvMatch = (ListView) findViewById(R.id.lv_match_city);
    }

    private void setListener() {
        etWord.addTextChangedListener(this);
        lvMatch.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent data = new Intent();
        data.putExtra("newCity", cities.get(position));
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        final String word = s.toString();
        if (word.trim().equals("")) {
            lvMatch.setAdapter(null);
            return;
        }

        cities = (LinkedList<City>) CityProvider.findMatchText(word);
        List<String> listdata = new ArrayList<String>();
        for (City city : cities) {
            StringBuffer sb = new StringBuffer();
            sb.append(city.getName()).append(" ");
            City parent = city.getParent();
            if (parent != null) {
                sb.append("( ");
                City parentParent = parent.getParent();
                if (parentParent != null) {
                    sb.append(parentParent.getName()).append(" ");
                }
                sb.append(parent.getName()).append(" )");
            }
            listdata.add(sb.toString());
        }

        //刷新数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddCityActivity.this, android.R.layout.simple_list_item_1, listdata);
        lvMatch.setAdapter(adapter);
    }
}
