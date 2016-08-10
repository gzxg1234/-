package com.sanron.sunweather.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.sanron.sunweather.entity.WeatherData;
import com.sanron.sunweather.utils.ACache;
import com.sanron.sunweather.utils.ACache.ACacheManager;


public class CacheManager {

    public static final int SAVE_TIME = 60 * 60 * 24 * 2;//缓存有效期 48小时
    public static final int CACHE_LIMIT = 1024 * 1024;//最多1MB缓存


    //读取
    public static Map<String, WeatherData> getWeatherDatas(Context context, List<String> cities) {
        Map<String, WeatherData> weatherDatas = new HashMap<String, WeatherData>();
        Gson gson = new Gson();
        for (int i = 0; i < cities.size(); i++) {
            String jsonObj = ACache.get(context, CACHE_LIMIT, Integer.MAX_VALUE).getAsString(cities.get(i));
            WeatherData weatherData = gson.fromJson(jsonObj, WeatherData.class);
            weatherDatas.put(cities.get(i), weatherData);
        }
        return weatherDatas;
    }

    //保存
    public static void saveWeatherData(Context context, WeatherData weatherData) {
        Gson gson = new Gson();
        ACache.get(context, CACHE_LIMIT, Integer.MAX_VALUE).put(weatherData.getCity(), gson.toJson(weatherData), SAVE_TIME);
    }

    //清理缓存
    public static long clearCache(Context context) {
        return ACache.get(context, CACHE_LIMIT, Integer.MAX_VALUE).clear();
    }

}
