package com.sanron.sunweather.activities.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sanron.sunweather.R;
import com.sanron.sunweather.activities.SettingActivity;
import com.sanron.sunweather.common.IconProvider;
import com.sanron.sunweather.entity.Weather;
import com.sanron.sunweather.entity.WeatherData;
import com.sanron.sunweather.event.CityEvent;
import com.sanron.sunweather.utils.DimenUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MenuFrag extends BaseFragment implements OnClickListener, OnItemClickListener,
        OnItemLongClickListener {

    private ViewGroup contentView;
    private ViewGroup menuContanier;
    private Button btnAddCity;
    private Button btnSetting;
    private Button btnRecommand;
    private ListView lvCities;

    private PopupWindow menuWindow;
    private View cityMenu;
    private ImageButton btnDelCity;
    private ImageButton btnSetPrimary;
    private ImageButton btnRefresh;

    private CityAdapter adapter;
    private List<String> cities;
    private Map<String, WeatherData> weatherDatas;
    private int curPosition;//当前显示菜单的城市位置

    private LayoutAnimationController menuInAnim;//菜单进入动画

    public static final int REQUEST_ADD_CITY = 0x110;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView(inflater);
        setListener();
        return contentView;
    }

    private void initView(LayoutInflater inflater) {
        contentView = (ViewGroup) inflater.inflate(R.layout.fragment_right_menu, null);
        menuContanier = (ViewGroup) contentView.findViewById(R.id.ll_menu);
        menuContanier.setLayoutAnimation(menuInAnim);

        btnAddCity = (Button) contentView.findViewById(R.id.btn_add_city);
        btnRecommand = (Button) contentView.findViewById(R.id.btn_recommand);
        btnSetting = (Button) contentView.findViewById(R.id.btn_setting);
        lvCities = (ListView) contentView.findViewById(R.id.lv_city);
        adapter = new CityAdapter();
        lvCities.setLayoutAnimation(menuInAnim);
        lvCities.setAdapter(adapter);
        lvCities.setVerticalScrollBarEnabled(false);

        cityMenu = inflater.inflate(R.layout.layout_city_menu, null);
        btnDelCity = (ImageButton) cityMenu.findViewById(R.id.btn_del_city);
        btnRefresh = (ImageButton) cityMenu.findViewById(R.id.btn_refresh_data);
        btnSetPrimary = (ImageButton) cityMenu.findViewById(R.id.btn_set_primary);
        menuWindow = new PopupWindow(getActivity());
        menuWindow.setContentView(cityMenu);
        menuWindow.setAnimationStyle(R.style.MenuWindowAnim);
        menuWindow.setWidth(LayoutParams.WRAP_CONTENT);
        menuWindow.setHeight(LayoutParams.WRAP_CONTENT);
        menuWindow.setOutsideTouchable(true);
        menuWindow.setBackgroundDrawable(new ColorDrawable(0));

        updateViews();
    }

    private void setAnimation() {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        menuInAnim = new LayoutAnimationController(animation, 0.1f);
    }

    private void setListener() {
        btnAddCity.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnRecommand.setOnClickListener(this);
        lvCities.setOnItemClickListener(this);
        lvCities.setOnItemLongClickListener(this);
        btnSetPrimary.setOnClickListener(this);
        btnDelCity.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public void setWeatherDatas(Map<String, WeatherData> weatherDatas) {
        this.weatherDatas = weatherDatas;
    }

    @Override
    public void setWeatherData(WeatherData weatherData) {
        dataChanged = true;
    }

    @Override
    protected void updateViews() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_add_city: {
                eventBus.post(new CityEvent(CityEvent.TYPE_ADD, 0));
            }
            break;

            case R.id.btn_del_city: {
                eventBus.post(new CityEvent(CityEvent.TYPE_DEL, curPosition));
                menuWindow.dismiss();
            }
            break;

            case R.id.btn_refresh_data: {
                eventBus.post(new CityEvent(CityEvent.TYPE_REFRESH, curPosition));
                menuWindow.dismiss();
            }
            break;

            case R.id.btn_set_primary: {
                eventBus.post(new CityEvent(CityEvent.TYPE_PRIMARY, curPosition));
                menuWindow.dismiss();
            }
            break;

            case R.id.btn_setting: {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
            break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (weatherDatas.get(cities.get(position)) != null) {
            //有数据才可以切换
            eventBus.post(new CityEvent(CityEvent.TYPE_SELECT, position));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        showCityMenu(view, position);
        return true;
    }

    public void showCityMenu(final View view, final int position) {

        if (position == 0) {
            btnDelCity.setVisibility(View.GONE);
            btnSetPrimary.setVisibility(View.GONE);
        } else {
            btnDelCity.setVisibility(View.VISIBLE);
            btnSetPrimary.setVisibility(View.VISIBLE);
        }
        cityMenu.measure(0,0);
        curPosition = position;
        menuWindow.showAsDropDown(view
                , -cityMenu.getMeasuredWidth() - DimenUtils.dip2px(getActivity(), 10)
                , -cityMenu.getMeasuredHeight());
    }


    public class CityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cities == null ? 0 : cities.size();
        }

        @Override
        public Object getItem(int position) {
            return cities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String city = cities.get(position);
            WeatherData weatherData = weatherDatas.get(city);
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.list_item_city, parent, false);
            }
            TextView tvCity = (TextView) convertView.findViewById(R.id.tv_city);
            TextView tvTemp = (TextView) convertView.findViewById(R.id.tv_temp);
            TextView tvType = (TextView) convertView.findViewById(R.id.tv_weather_type);
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_weather_icon);
            tvCity.setText(city);

            if (position == 0) {
                tvCity.setCompoundDrawablePadding(10);
                tvCity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_city, 0, 0, 0);
            } else {
                tvCity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            if (weatherData != null) {
                Weather todayWeather = weatherData.getWeatherByDay(new Date());
                tvTemp.setText(todayWeather.getDayTEMP() + "°~" + todayWeather.getNightTEMP() + "°");
                tvType.setText(todayWeather.getDayType());
                int iconId = IconProvider.getTypeSmallIcon(todayWeather.getDayType(), true);
                ivIcon.setImageResource(iconId);
            } else {
                tvTemp.setText("");
                tvType.setText("");
                ivIcon.setImageBitmap(null);
            }

            return convertView;
        }

    }
}
