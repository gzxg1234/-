package com.sanron.sunweather.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sanron.sunweather.R;
import com.sanron.sunweather.engine.AppConfig;
import com.sanron.sunweather.engine.CacheManager;
import com.sanron.sunweather.view.MySpinner;
import com.sanron.sunweather.view.MySpinner.OnItemSelectedListener;
import com.viewpagerindicator.TitlePageIndicator;

public class SettingActivity extends Activity implements OnClickListener {

    private AppConfig appConfig;
    private ViewPager pagerSetting;
    private TitlePageIndicator pagerIndicator;
    private List<View> views;
    private View commonView;
    private Switch switchLocation;
    private Button btnClearCache;

    private View updateView;
    private MySpinner policySpinner;
    private TextView tvPolicyLab;

    private View aboutView;

    public final String[] TITLES = new String[]{"常规", "更新", "关于"};
    public final String[] POLICY_ITEM = new String[]{"智能刷新(推荐)", "总是刷新", "不刷新"};
    public final String[] POLICY_LAB = new String[]{"将根据数据发布时间判断是否需要尝试刷新",
            "应用启动时总是刷新数据",
            "在数据缓存过期时更新"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        appConfig = AppConfig.getInstance(this);
        initView();
        setListener();
    }

    private void initView() {
        commonView = (LayoutInflater.from(this).inflate(R.layout.setting_common, null));

        switchLocation = (Switch) commonView.findViewById(R.id.swtich_location);
        switchLocation.setChecked(appConfig.isLocation());
        switchLocation.setText(appConfig.isLocation() ? "开" : "关");
        btnClearCache = (Button) commonView.findViewById(R.id.btn_clear_cache);

        updateView = LayoutInflater.from(this).inflate(R.layout.setting_update, null);
        policySpinner = (MySpinner) updateView.findViewById(R.id.spinner_update_policy);
        policySpinner.setData(Arrays.asList(POLICY_ITEM));
        policySpinner.setPosition(appConfig.getUpdatePolicy());

        tvPolicyLab = (TextView) updateView.findViewById(R.id.tv_lab2);
        tvPolicyLab.setText(POLICY_LAB[appConfig.getUpdatePolicy()]);

        aboutView = LayoutInflater.from(this).inflate(R.layout.setting_about, null);

        views = new ArrayList<View>();
        views.add(commonView);
        views.add(updateView);
        views.add(aboutView);

        pagerIndicator = (TitlePageIndicator) findViewById(R.id.pager_indicator);
        pagerSetting = (ViewPager) findViewById(R.id.pager_setting);
        pagerSetting.setAdapter(new SettingAdapter());
        pagerIndicator.setViewPager(pagerSetting);

    }

    private void setListener() {
        btnClearCache.setOnClickListener(this);
        policySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View parent, int position) {

            }
        });

        switchLocation.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    if (appConfig.isLocationPrimaryCity()) {
                        //关闭自动定位，如果当前主城市是定位的，则设置其不是定位的城市
                        appConfig.setLocationPrimaryCity(false);
                    }
                }
                appConfig.setLocation(isChecked);
                buttonView.setText(isChecked ? "开" : "关");
            }
        });

        policySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View parent, int position) {
                appConfig.setUpdatePolicy(position);
                tvPolicyLab.setText(POLICY_LAB[position]);
            }
        });
    }

    public class SettingAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_clear_cache: {
                long clearCache = CacheManager.clearCache(this);
                Toast.makeText(this, "清除缓存 " + Formatter.formatFileSize(this, clearCache), 0).show();
            }
            break;

        }
    }


}


