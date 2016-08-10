package com.sanron.sunweather.engine;

import android.content.Context;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.sanron.sunweather.common.CityProvider;
import com.sanron.sunweather.entity.City;

/**
 * 位置服务
 *
 * @author Sanron
 */
public class LocationProvider {

    private static LocationProvider instance;

    private LocationClient locationClient;

    public static LocationProvider getInstance(Context context) {
        if (instance == null) {
            synchronized (LocationProvider.class) {
                if (instance == null) {
                    instance = new LocationProvider(context);
                }
            }
        }
        return instance;
    }

    private LocationProvider(Context context) {
        locationClient = new LocationClient(context.getApplicationContext());
    }

    public void requestLocation(final OnLocationListener onLocationListener) {

        if (onLocationListener == null) {
            throw new IllegalArgumentException("listener is null");
        }

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                //停止定位
                locationClient.unRegisterLocationListener(this);
                locationClient.stop();

                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    City city = null;
                    String area = location.getDistrict();

                    //定位到县/区级别
                    if (area != null && !area.trim().equals("")) {
                        area = area.replaceAll("县|区", "");
                        city = CityProvider.findByName(area);
                    }

                    //定位成功
                    if (city != null) {
                        onLocationListener.onLocationSuccess(city);
                        return;
                    }

                    //未定位到县区级别  或是  无此县区级别天气数据
                    area = location.getCity();
                    if (area != null && !area.trim().equals("")) {
                        area = area.replaceAll("市", "");
                        city = CityProvider.findByName(area);
                    }

                    //定位成功
                    if (city != null) {
                        onLocationListener.onLocationSuccess(city);
                        return;
                    }
                }
                //定位失败
                onLocationListener.onLocationFailed();
            }
        });
        LocationClientOption opt = new LocationClientOption();
        opt.setLocationMode(LocationMode.Battery_Saving);
        opt.setIsNeedAddress(true);
        locationClient.setLocOption(opt);
        locationClient.start();
    }

    ;

    public interface OnLocationListener {
        public void onLocationFailed();

        public void onLocationSuccess(City city);
    }
}
