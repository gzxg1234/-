package com.sanron.sunweather.activities.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanron.sunweather.R;
import com.sanron.sunweather.activities.adapter.ForcastAdapter;
import com.sanron.sunweather.common.IconProvider;
import com.sanron.sunweather.entity.InstantWeather;
import com.sanron.sunweather.entity.Warning;
import com.sanron.sunweather.entity.Weather;
import com.sanron.sunweather.event.AboveEvent;
import com.sanron.sunweather.utils.ChineseCalendar;
import com.sanron.sunweather.utils.DateUtils;
import com.sanron.sunweather.view.PetalContanier;
import com.sanron.sunweather.view.PetalContanier.OnItemClickListener;
import com.sanron.sunweather.view.PetalView;
import com.sanron.sunweather.view.RotateView;
import com.sanron.sunweather.view.ScrollText;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Date;
import java.util.List;

public class AboveFrag extends BaseFragment implements OnClickListener {


    private ViewGroup contentView;
    /**
     * 顶栏view
     */
    private ViewGroup topView;
    private TextView tvCity;
    private TextView tvUpdateTime;
    private ImageView ivShare;
    private ImageView ivMenu;

    /**
     * 中间显示的即时天气
     */
    private ViewGroup centerView;
    private ImageView ivWeatherIcon;
    private ImageView ivSun;
    private ImageView ivWarning;
    private TextView tvTemperature;
    private TextView tvTempLowHigh;
    private TextView tvHumidity;
    private TextView tvType;
    private TextView tvNightType;
    private TextView tvLunarDate;
    private TextView tvDate;
    private TextView tvWind;
    private RelativeLayout rlRightTop;
    private ImageView ivFly1;
    private ImageView ivFly2;

    /**
     * 底部
     */
    private ViewGroup bottomView;
    private RotateView rotateView;
    private PetalContanier petals;
    private ScrollText scrollText;

    /**
     * 预报天气和天气预警window
     */
    private PopupWindow pagerWindow;
    private View forecastContentView;
    private CirclePageIndicator indicator;
    private ForcastAdapter forcastAdapter;
    private View warningContentView;
    private TextView tvCityName;
    private ImageView ivWarnIcon;
    private TextView tvWarnType;
    private TextView tvWarnTime;
    private TextView tvWarnDetail;

    private int timeInterval = 0;//现在的时间段

    public static final int FLY1_DURATION = 11000;
    public static final int FLY2_DURATION = 9000;
    public static final int DURATION = 400;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView(inflater);
        setListener();
        return contentView;
    }

    private void initView(LayoutInflater inflater) {
        contentView = (ViewGroup) inflater.inflate(R.layout.fragment_above, null);

        topView = (ViewGroup) contentView.findViewById(R.id.top);
        tvCity = (TextView) topView.findViewById(R.id.tv_city);
        tvUpdateTime = (TextView) topView.findViewById(R.id.tv_update_time);
        ivShare = (ImageView) topView.findViewById(R.id.iv_share);
        ivMenu = (ImageView) topView.findViewById(R.id.iv_menu);

        centerView = (ViewGroup) contentView.findViewById(R.id.center);
        ivWarning = (ImageView) centerView.findViewById(R.id.iv_warning);
        ivWeatherIcon = (ImageView) centerView
                .findViewById(R.id.iv_weather_icon);
        tvTemperature = (TextView) centerView.findViewById(R.id.tv_temperature);
        tvTempLowHigh = (TextView) centerView
                .findViewById(R.id.tv_temperature_low_high);
        tvHumidity = (TextView) centerView.findViewById(R.id.tv_humidity);
        tvType = (TextView) centerView.findViewById(R.id.tv_weather_type);
        rlRightTop = (RelativeLayout) centerView.findViewById(R.id.rl_right_top);
        ivFly1 = (ImageView) centerView.findViewById(R.id.iv_fly1);
        ivFly2 = (ImageView) centerView.findViewById(R.id.iv_fly2);

        tvNightType = (TextView) centerView
                .findViewById(R.id.tv_weather_type_night);
        tvLunarDate = (TextView) centerView.findViewById(R.id.tv_lunar_date);
        tvWind = (TextView) centerView.findViewById(R.id.tv_wind);
        tvDate = (TextView) centerView.findViewById(R.id.tv_date);

        bottomView = (ViewGroup) contentView.findViewById(R.id.bottom);
        rotateView = (RotateView) bottomView.findViewById(R.id.rotate_view);
        petals = (PetalContanier) bottomView.findViewById(R.id.petals);
        scrollText = (ScrollText) bottomView.findViewById(R.id.tv_state_tip);


        warningContentView = LayoutInflater.from(getActivity()).inflate(R.layout.window_warning, null);
        tvCityName = (TextView) warningContentView.findViewById(R.id.tv_city_name);
        ivWarnIcon = (ImageView) warningContentView.findViewById(R.id.iv_warn_icon);
        tvWarnType = (TextView) warningContentView.findViewById(R.id.tv_warn_text);
        tvWarnTime = (TextView) warningContentView.findViewById(R.id.tv_warn_time);
        tvWarnDetail = (TextView) warningContentView.findViewById(R.id.tv_warn_detail);

        forecastContentView = LayoutInflater.from(getActivity()).inflate(R.layout.window_forecast, null);
        ViewPager viewPager = (ViewPager) forecastContentView.findViewById(R.id.pager_forcast);
        indicator = (CirclePageIndicator) forecastContentView.findViewById(R.id.pager_indicator);
        forcastAdapter = new ForcastAdapter(getActivity());
        viewPager.setAdapter(forcastAdapter);
        indicator.setViewPager(viewPager);

        pagerWindow = new PopupWindow(getActivity());
        pagerWindow.setAnimationStyle(R.style.PagerWindow);
        pagerWindow.setBackgroundDrawable(new ColorDrawable(0x80ffffff));
        pagerWindow.setWidth(LayoutParams.MATCH_PARENT);
        pagerWindow.setHeight(LayoutParams.MATCH_PARENT);
        pagerWindow.setFocusable(true);

        updateViews();
    }

    private void setListener() {

        topView.setOnClickListener(this);
        centerView.setOnClickListener(this);
        ivMenu.setOnClickListener(this);

        rotateView.setOnScrollListener(new RotateView.OnScrollListener() {
            @Override
            public void onScroll(int offY) {
                petals.setTranslationY(petals.getTranslationY() - offY);
                scrollText.setTranslationY(petals.getTranslationY() - offY);
            }
        });

        rotateView.setOnStateChangeListener(new RotateView.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                eventBus.post(new AboveEvent(
                        AboveEvent.TYPE_BOTTOM_STATE_CHANGE, state));
            }
        });

        petals.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                indicator.setCurrentItem(position);
                pagerWindow.setContentView(forecastContentView);
                pagerWindow.showAtLocation(contentView, Gravity.TOP, 0, 0);
            }
        });

        //设置window触摸拦截，使之点击消失
        pagerWindow.setTouchInterceptor(new OnTouchListener() {
            private int lastX = 0;
            private int lastY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {
                        lastX = x;
                        lastY = y;
                    }
                    break;

                    case MotionEvent.ACTION_UP: {
                        if (x == lastX && y == lastY) {
                            pagerWindow.dismiss();
                            return true;
                        }
                    }
                    break;
                }
                return false;
            }
        });

        //设置飘动的云动画
        ivFly1.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ivFly1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int parentWidth = rlRightTop.getMeasuredWidth();
                int fly1Width = ivFly1.getMeasuredWidth();
                TranslateAnimation animation = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0
                        , Animation.RELATIVE_TO_PARENT, fly1Width / (float) parentWidth - 1
                        , Animation.RELATIVE_TO_PARENT, 0
                        , Animation.RELATIVE_TO_PARENT, 0);
                animation.setRepeatCount(Integer.MAX_VALUE);
                animation.setRepeatMode(Animation.REVERSE);
                animation.setInterpolator(new LinearInterpolator());
                animation.setDuration(FLY1_DURATION);
                ivFly1.startAnimation(animation);
            }
        });

        ivFly2.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ivFly2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int parentWidth = rlRightTop.getMeasuredWidth();
                int fly2Width = ivFly2.getMeasuredWidth();
                TranslateAnimation animation = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0
                        , Animation.RELATIVE_TO_PARENT, fly2Width / (float) parentWidth - 1
                        , Animation.RELATIVE_TO_PARENT, 0
                        , Animation.RELATIVE_TO_PARENT, 0);
                animation.setRepeatCount(Integer.MAX_VALUE);
                animation.setRepeatMode(Animation.REVERSE);
                animation.setInterpolator(new LinearInterpolator());
                animation.setDuration(FLY2_DURATION);
                ivFly2.startAnimation(animation);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_menu: {
                eventBus.post(new AboveEvent(AboveEvent.TYPE_TOP_MENU_CLICK, null));
            }
            break;

            case R.id.center: {
                eventBus.post(new AboveEvent(AboveEvent.TYPE_CENTER_CLICK, null));
            }
            break;

            case R.id.iv_warning: {
                pagerWindow.setContentView(warningContentView);
                pagerWindow.showAtLocation(contentView, Gravity.TOP, 0, 0);
            }
            break;
        }
    }

    /**
     * 设置状态text
     */
    public void setStateText(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                scrollText.addText(text);
            }
        });
    }


    /**
     * 设置view
     */
    @Override
    protected void updateViews() {
        if (weatherData == null) return;
        timeInterval = DateUtils.getTimeInterval();
        setTopView();
        setCenterView();
        setBottomView();
    }

    private void setTopView() {
        tvCity.setText(weatherData.getCity());

        long time = (System.currentTimeMillis() -
                weatherData.getInstantWeather().getTime().getTime()) / 1000 / 60;
        if (time < 60) {
            tvUpdateTime.setText(time + "分钟前");
        } else {
            tvUpdateTime.setText(time / 60 + "小时前");
        }
    }

    private void setCenterView() {
        InstantWeather instantWeather = weatherData.getInstantWeather();
        tvTemperature.setText(instantWeather.getInstantTEMP() + "°");
        tvWind.setText(instantWeather.getWindDirection() + " "
                + instantWeather.getWindPower());
        tvHumidity.setText(instantWeather.getHumidity());

        //判断白天黑夜
        Date today = new Date();
        Weather todayWeather = weatherData.getWeatherByDay(today);

        if (timeInterval == 1) {
            //现在时间是 0点~8点 时间段,应该使用昨天20-8点时间段的天气数据
            Date yestaday = DateUtils.addDate(today, -1);
            Weather yestadayWeather = weatherData.getWeatherByDay(yestaday);

            if (yestadayWeather == null) {
                //没有昨天的数据
                tvTempLowHigh.setText(todayWeather.getDayTEMP() + "°~"
                        + todayWeather.getNightTEMP() + "°");
                tvType.setText(todayWeather.getDayType());
                tvNightType.setText("夜里 " + todayWeather.getNightType());
            } else {
                tvTempLowHigh.setText("最低 " + yestadayWeather.getNightTEMP() + "°");
                tvType.setText(yestadayWeather.getNightType());
                tvNightType.setText("");
            }

        } else if (timeInterval == 2) {
            //现在是8-20点时间段
            tvTempLowHigh.setText(todayWeather.getDayTEMP() + "°~"
                    + todayWeather.getNightTEMP() + "°");
            tvType.setText(todayWeather.getDayType());
            tvNightType.setText("夜里 " + todayWeather.getNightType());
        } else if (timeInterval == 3) {
            //现在是20~24点时间段
            tvTempLowHigh.setText("最低 " + todayWeather.getNightTEMP() + "°");
            tvType.setText(todayWeather.getNightType());
            tvNightType.setText("");
        }

        // 获取图标
        int iconId = IconProvider.getTypeIcon(tvType.getText().toString(), timeInterval == 2);
        ivWeatherIcon.setImageResource(iconId);
        Warning warning = weatherData.getWarning();
        if (warning != null) {
            int warnIconId = IconProvider.getWarningIcon(warning.getAlarmType(), warning.getAlarmDegree());
            ivWarning.setVisibility(View.VISIBLE);
            ivWarning.setImageResource(warnIconId);
            ivWarning.setOnClickListener(this);
        } else {
            ivWarning.setVisibility(View.INVISIBLE);
            ivWarning.setOnClickListener(null);
        }
        // 设置时间
        ChineseCalendar chineseCalendar = new ChineseCalendar();
        String lunarMonth = chineseCalendar.getChinese(ChineseCalendar.CHINESE_MONTH);
        String lunarDay = chineseCalendar.getChinese(ChineseCalendar.CHINESE_DATE);
        tvLunarDate.setText("农历 " + lunarMonth + lunarDay);
        tvDate.setText(DateUtils.format(chineseCalendar.getTime(), "MM月dd号 E"));
    }

    private void setBottomView() {
        List<Weather> weathers = weatherData.getWeathers();
        Date today = new Date();
        int todayWeatherIndex = 0;
        if (timeInterval != 1 ||
                (todayWeatherIndex = weatherData.getWeatherIndexByDate(DateUtils.addDate(today, -1))) == -1) {
            todayWeatherIndex = weatherData.getWeatherIndexByDate(today);
        }

        for (int i = 0; i < 5 && i<weathers.size(); i++) {
            Weather weather = weathers.get(i);
            PetalView petalView = (PetalView) petals.getChildAt(i);
            int iconId = IconProvider.getTypeSmallIcon(weather.getDayType(), true);
            petalView.setWeatherIcon(iconId);
            petalView.setTemperature(
                    weather.getDayTEMP() + "°~" + weather.getNightTEMP() + "°");
            if (i == todayWeatherIndex) {
                petalView.setWeekDay("今天");
                continue;
            }
            petalView.setWeekDay(
                    DateUtils.format(weather.getDate(), "E"));
        }
        scrollText.addText("");

        forcastAdapter.setData(weathers.subList(0, 5));
        Warning warning = weatherData.getWarning();
        if (warning != null) {
            tvCityName.setText(warning.getCityName());
            int warnIconId = IconProvider.getWarningIcon(warning.getAlarmType(), warning.getAlarmDegree());
            ivWarnIcon.setImageResource(warnIconId);
            tvWarnType.setText(warning.getAlarmType() + warning.getAlarmDegree() + "预警");
            tvWarnTime.setText(DateUtils.format(warning.getTime(), "MM月dd日 HH:mm发布"));
            tvWarnDetail.setText(warning.getAlarmDetails());
        }
    }

    /**
     * 设置顶部可见
     */
    public void setTopVisiable(boolean visiable) {
        if (visiable) {
            topView.animate().translationY(0).setDuration(400).start();
        } else {
            topView.animate().translationY(-topView.getHeight())
                    .setDuration(DURATION).start();
        }
    }

    public void setRotateViewState(int state) {
        if (state == RotateView.STATE_DOWN) {
            rotateView.toDown();
        } else if (state == RotateView.STATE_HIDE) {
            rotateView.toHide();
        } else if (state == RotateView.STATE_UP) {
            rotateView.toUp();
        }
    }

    /**
     * 设置即使天气可见
     */
    public void setCenterVisiable(boolean visiable) {
        ObjectAnimator animator = null;
        PropertyValuesHolder valuesHolderY = null;
        PropertyValuesHolder valuesHolderAlpha = null;
        if (visiable) {
            centerView.setEnabled(true);
            valuesHolderY = PropertyValuesHolder.ofFloat("translationY",
                    centerView.getHeight(), 0);
            valuesHolderAlpha = PropertyValuesHolder.ofFloat("alpha", 0, 1);
        } else {
            centerView.setEnabled(false);
            valuesHolderY = PropertyValuesHolder.ofFloat("translationY", 0,
                    centerView.getHeight());
            valuesHolderAlpha = PropertyValuesHolder.ofFloat("alpha", 1, 0);
        }
        animator = ObjectAnimator.ofPropertyValuesHolder(centerView,
                valuesHolderY, valuesHolderAlpha);
        if (!visiable) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    centerView.setTranslationY(Integer.MAX_VALUE);
                }
            });
        }
        animator.setDuration(DURATION);
        animator.start();
    }

}
