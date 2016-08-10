package com.sanron.sunweather.activities.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanron.sunweather.R;
import com.sanron.sunweather.common.IconProvider;
import com.sanron.sunweather.entity.Weather;
import com.sanron.sunweather.utils.ChineseCalendar;
import com.sanron.sunweather.utils.DateUtils;

/**
 * 点击花瓣弹出窗口的viewpager适配器
 *
 * @author Sanron
 */
public class ForcastAdapter extends PagerAdapter {
    private Context context;
    private List<Weather> data;
    private SparseArray<View> views;

    public ForcastAdapter(Context context) {
        this.context = context;
        this.views = new SparseArray<View>();
    }

    @Override
    public void notifyDataSetChanged() {
        for (int i = 0; i < views.size(); i++) {
            setView(views.valueAt(i), views.keyAt(i));
        }
        super.notifyDataSetChanged();
    }

    private void setView(View view, int position) {
        Weather weather = data.get(position);
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
        TextView tvLunarDate = (TextView) view.findViewById(R.id.tv_lunar_date);
        ImageView ivDayType = (ImageView) view.findViewById(R.id.iv_day_type);
        ImageView ivNightType = (ImageView) view.findViewById(R.id.iv_night_type);
        TextView tvDayType = (TextView) view.findViewById(R.id.tv_day_type);
        TextView tvNightType = (TextView) view.findViewById(R.id.tv_night_type);
        TextView tvHighTemp = (TextView) view.findViewById(R.id.tv_high_temp);
        TextView tvLowTemp = (TextView) view.findViewById(R.id.tv_low_temp);
        TextView tvSunrise = (TextView) view.findViewById(R.id.tv_sunrise_time);
        TextView tvSunset = (TextView) view.findViewById(R.id.tv_sunset_time);
        tvDate.setText(DateUtils.format(weather.getDate(), "yyyy年MM月dd日 E"));
        int dayIconId = IconProvider.getTypeIcon(weather.getDayType(), true);
        int nightIconId = IconProvider.getTypeIcon(weather.getNightType(), false);
        ivDayType.setImageResource(dayIconId);
        ivNightType.setImageResource(nightIconId);
        tvDayType.setText(weather.getDayType());
        tvNightType.setText(weather.getNightType());
        tvHighTemp.setText("白天最高" + weather.getDayTEMP() + "°");
        tvLowTemp.setText("夜里最低" + weather.getNightTEMP() + "°");
        tvSunrise.setText("日出 " + DateUtils.format(weather.getSunriseTime(), "HH:mm"));
        tvSunset.setText("日落" + DateUtils.format(weather.getSunsetTime(), "HH:mm"));
        ChineseCalendar chineseCalendar = new ChineseCalendar(weather.getDate());
        tvLunarDate.setText(
                "农历 "
                        + chineseCalendar.getChinese(ChineseCalendar.CHINESE_MONTH)
                        + chineseCalendar.getChinese(ChineseCalendar.CHINESE_DATE));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.pager_forecast, container, false);
            setView(view, position);
            views.put(position, view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void setData(List<Weather> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}