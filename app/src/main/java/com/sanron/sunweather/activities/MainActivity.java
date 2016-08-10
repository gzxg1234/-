package com.sanron.sunweather.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.sanron.sunweather.R;
import com.sanron.sunweather.activities.fragment.AboveFrag;
import com.sanron.sunweather.activities.fragment.BaseFragment;
import com.sanron.sunweather.activities.fragment.ExtrasFrag;
import com.sanron.sunweather.activities.fragment.LifeIndexFrag;
import com.sanron.sunweather.activities.fragment.MenuFrag;
import com.sanron.sunweather.engine.AppConfig;
import com.sanron.sunweather.engine.CacheManager;
import com.sanron.sunweather.common.CityProvider;
import com.sanron.sunweather.engine.LocationProvider;
import com.sanron.sunweather.engine.LocationProvider.OnLocationListener;
import com.sanron.sunweather.engine.WeatherProvider;
import com.sanron.sunweather.engine.WeatherProvider.OnGetWeatherListener;
import com.sanron.sunweather.entity.City;
import com.sanron.sunweather.entity.Weather;
import com.sanron.sunweather.entity.WeatherData;
import com.sanron.sunweather.event.AboveEvent;
import com.sanron.sunweather.event.CityEvent;
import com.sanron.sunweather.utils.DateUtils;
import com.sanron.sunweather.utils.NetUtils;
import com.sanron.sunweather.view.RotateView;
import com.sanron.sunweather.view.v4.DrawerLayout;
import com.sanron.sunweather.view.v4.DrawerLayout.DrawerListener;
import com.umeng.update.UmengUpdateAgent;

import java.util.Date;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity {

    private AppConfig config;
    private DrawerLayout drawerLayout;
    private View contentView;
    private View menuView;

    private FragmentManager fm;
    private AboveFrag aboveFrag;
    private MenuFrag menuFrag;
    private EventBus eventBus;

    public static final String TAG_ABOVE = "above";
    public static final String TAG_MENU = "menu";
    public static final String TAG_EXTRAS = "extras";
    public static final String TAG_LIFE_INDEX = "lifeindex";

    private Map<String, WeatherData> weatherDatas;
    private int currentCityPosition = 0;        //当前显示城市的位置

    public static final int REQUEST_ADD_CITY = 0x110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UmengUpdateAgent.update(getApplicationContext());
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        initView();            //设置view
        initData();            //读取配置和缓存
        initFragment();        //设置fragment
        initConfig();        //
    }

    private void initView() {
        contentView = findViewById(R.id.content);
        menuView = findViewById(R.id.menu_container);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerLayout.setDrawerListener(new DrawerListener() {

            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                fm.beginTransaction()
                        .add(R.id.menu_container, menuFrag, TAG_MENU)
                        .commit();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                fm.beginTransaction()
                        .remove(menuFrag)
                        .commit();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //滑动菜单时滚动主内容
                int x = (int) (slideOffset * menuView.getMeasuredWidth());
                contentView.scrollTo(x, 0);
            }
        });
    }

    private void initData() {
        CityProvider.init(this);
        config = AppConfig.getInstance(this);
        weatherDatas = CacheManager.getWeatherDatas(this, config.getCities());
    }

    private void initFragment() {

        fm = getSupportFragmentManager();

        aboveFrag = new AboveFrag();
        menuFrag = new MenuFrag();
        menuFrag.setWeatherDatas(weatherDatas);
        menuFrag.setCities(config.getCities());
        if (config.getCities().size() != 0) {
            WeatherData curWeatherData = weatherDatas.get(config.getCity(currentCityPosition));
            aboveFrag.setWeatherData(curWeatherData);
        }

        fm.beginTransaction().
                add(R.id.fragment_container_above, aboveFrag, TAG_ABOVE)
                .commit();

        fm.addOnBackStackChangedListener(new OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                //fragment回退栈空时
                if (fm.getBackStackEntryCount() == 0) {
                    aboveFrag.setCenterVisiable(true);
                    aboveFrag.setTopVisiable(true);
                    aboveFrag.setRotateViewState(RotateView.STATE_UP);
                }
            }
        });

    }


    private void initConfig() {
        boolean netWorkEnable = NetUtils.checkNetwork(this);

        if (config.isLocation()) {
            if (!netWorkEnable) {
                aboveFrag.setStateText("无可用网络");
            } else {
                //有网络
                locationWeather();
            }
        }

        if (netWorkEnable) {
            final List<String> cities = config.getCities();
            switch (config.getUpdatePolicy()) {

                case AppConfig.UPDATE_ALWAYS: {
                    for (int i = 0; i < cities.size(); i++) {
                        if (config.isLocationPrimaryCity() && i == 0) {
                            continue;
                        }
                        WeatherProvider.getInstance().getWeahterData(CityProvider.findByName(cities.get(i)), getWeatherListener);
                    }
                }
                break;
                case AppConfig.UPDATE_AUTO: {
                    for (int i = 0; i < cities.size(); i++) {
                        if (config.isLocationPrimaryCity() && i == 0) {
                            continue;
                        }
                        WeatherData data = weatherDatas.get(cities.get(i));
                        if (data == null ||
                                (int) ((System.currentTimeMillis() - data.getInstantWeather().getTime().getTime()) / 1000 / 60) > 10) {
                            //数据是半小时前的 就更新
                            WeatherProvider.getInstance().getWeahterData(CityProvider.findByName(config.getCity(i)), getWeatherListener);
                        }
                    }
                }
                break;

            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        //当保存fragment状态时，activity被系统销毁后重启时fragment会被系统恢复，
        //而前面代码又add了一遍，造成fragment重叠
        //不保存fragment状态,懒得处理
    }

    //定位
    private void locationWeather() {
        aboveFrag.setStateText("获取位置 ");
        LocationProvider.getInstance(this).requestLocation(new OnLocationListener() {
            @Override
            public void onLocationSuccess(final City city) {
                aboveFrag.setStateText("定位到 " + city.getName());
                config.setPrimaryCity(city.getName());
                config.setLocationPrimaryCity(true);
                WeatherProvider.getInstance().getWeahterData(city, getWeatherListener);
            }

            @Override
            public void onLocationFailed() {
                aboveFrag.setStateText("定位失败");
            }
        });
    }

    public void onEventMainThread(AboveEvent event) {
        switch (event.getType()) {
            case AboveEvent.TYPE_TOP_MENU_CLICK: {
                //显示/隐藏侧滑菜单
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
            break;

            case AboveEvent.TYPE_BOTTOM_STATE_CHANGE: {
                int state = (Integer) event.getObj();
                if (state == RotateView.STATE_UP) {
                    clearFragmentBackStack();
                } else if (state == RotateView.STATE_DOWN) {
                    switchFragment(TAG_LIFE_INDEX);
                }
            }
            break;

            case AboveEvent.TYPE_CENTER_CLICK: {
                //显示空气环境等额外信息
                switchFragment(TAG_EXTRAS);
            }
            break;

        }
    }

    public void onEventMainThread(CityEvent event) {
        int position = event.getPosition();
        switch (event.getType()) {

            case CityEvent.TYPE_SELECT: {
                if (currentCityPosition == position) return;

                currentCityPosition = position;
                refreshFragments();
            }
            break;

            case CityEvent.TYPE_DEL: {
                String city = config.getCity(position);
                config.delCity(city);
                weatherDatas.remove(city);
                if (position <= currentCityPosition) {
                    currentCityPosition--;
                }
                refreshFragments();
            }
            break;

            case CityEvent.TYPE_REFRESH: {
                if (config.isLocationPrimaryCity() && position == 0) {
                    locationWeather();
                    return;
                }
                String cityName = config.getCity(position);
                City city = CityProvider.findByName(cityName);
                WeatherProvider.getInstance().getWeahterData(city, getWeatherListener);
            }
            break;

            case CityEvent.TYPE_PRIMARY: {
                if (config.isLocation() && config.isLocationPrimaryCity()) {
                    //当前主城市为定位的，不能更改主城市
                    Toast.makeText(this, "当前开启了自动定位，应用将自动把您的位置设为主城市", Toast.LENGTH_SHORT).show();
                    return;
                }
                config.setPrimaryCity(config.getCity(position));
                refreshFragments();
            }
            break;

            case CityEvent.TYPE_ADD: {
                Intent intent = new Intent(this, AddCityActivity.class);
                startActivityForResult(intent, REQUEST_ADD_CITY);
            }
            break;

        }
    }

    /**
     * 清空fragment回退栈
     */
    private void clearFragmentBackStack() {
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
    }


    /**
     * 切换fragment
     */
    public void switchFragment(String tag) {

        FragmentTransaction transaction = fm.beginTransaction();
        BaseFragment baseFragment = null;
        transaction.setCustomAnimations(
                R.anim.slide_up_in,
                R.anim.slide_down_out,
                R.anim.slide_up_in,
                R.anim.slide_down_out);

        if (TAG_EXTRAS.equals(tag)) {
            baseFragment = new ExtrasFrag();
            aboveFrag.setRotateViewState(RotateView.STATE_HIDE);
        } else if (TAG_LIFE_INDEX.equals(tag)) {
            baseFragment = new LifeIndexFrag();
            aboveFrag.setTopVisiable(false);
            aboveFrag.setRotateViewState(RotateView.STATE_DOWN);
        }

        aboveFrag.setCenterVisiable(false);

        baseFragment.setWeatherData(weatherDatas.get(config.getCity(currentCityPosition)));
        transaction.add(R.id.fragment_container_behind, baseFragment, tag);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 所有fragment刷新视图
     */
    private void refreshFragments() {
        List<Fragment> fragments = fm.getFragments();
        String cityName = config.getCity(currentCityPosition);
        WeatherData weatherData = weatherDatas.get(cityName);
        for (Fragment frag : fragments) {
            if (frag == null) continue;
            ((BaseFragment) frag).setWeatherData(weatherData);
            ((BaseFragment) frag).refreshViews();
        }
    }

    private OnGetWeatherListener getWeatherListener = new OnGetWeatherListener() {

        @Override
        public void onSuccess(WeatherData weatherData) {
            //缓存数据

            WeatherData data = weatherDatas.get(weatherData.getCity());
            if (data != null) {
                //把前一天的天气数据保存起来
                Weather yestadayWeather = data.getWeatherByDay(
                        DateUtils.addDate(new Date(), -1));
                if (yestadayWeather != null) {
                    weatherData.getWeathers().add(0, yestadayWeather);
                }
            }

            CacheManager.saveWeatherData(MainActivity.this, weatherData);
            weatherDatas.put(weatherData.getCity(), weatherData);
            refreshFragments();
        }

        @Override
        public void onError(City city, String error) {
            if (city.getName().equals(config.getCity(currentCityPosition))) {
                aboveFrag.setStateText(error);
            }
        }

        @Override
        public void onStart(City city) {
            if (city.getName().equals(config.getCity(currentCityPosition))) {
                aboveFrag.setStateText("请求数据");
            }
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MenuFrag.REQUEST_ADD_CITY: {
                if (resultCode == Activity.RESULT_OK) {
                    //添加城市
                    City newCity = (City) data.getSerializableExtra("newCity");
                    config.addCity(newCity.getName());
                    WeatherProvider.getInstance().getWeahterData(newCity, getWeatherListener);
                    refreshFragments();
                }
            }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        }
        super.onBackPressed();
    }
}
