package com.sanron.sunweather.engine;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class AppConfig {
    private SharedPreferences sp;
    private boolean location;//是否自动获取位置
    private int updatePolicy;//刷新策略
    private int updateSpan;//后台更新间隔 (单位分钟)
    private boolean isLocationPrimaryCity;//主城市是否是定位城市
    private LinkedList<String> cities;//用户添加的地区

    public static final int DEFAULT_UPDATE_SPAN = 30;
    public static final String SP_NAME = "config";

    public static final int UPDATE_AUTO = 0;//根据数据时间判断是否刷新
    public static final int UPDATE_ALWAYS = 1;//总是刷新
    public static final int UPDATE_NO = 2;//不刷新

    public static AppConfig instance;

    public static AppConfig getInstance(Context context) {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    synchronized (AppConfig.class) {
                        instance = new AppConfig(context.getApplicationContext());
                    }
                }
            }
        }
        return instance;
    }

    private AppConfig(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        location = sp.getBoolean("location", true);
        setUpdatePolicy(sp.getInt("update_policy", UPDATE_AUTO));
        updateSpan = sp.getInt("update_pan", DEFAULT_UPDATE_SPAN);
        isLocationPrimaryCity = sp.getBoolean("is_location_primary", false);
        String jsonCities = sp.getString("cities", null);
        if (jsonCities != null) {
            cities = new Gson().fromJson(jsonCities,
                    new TypeToken<LinkedList<String>>() {
                    }.getType());
        } else {
            cities = new LinkedList<String>();
        }
    }


    public boolean isLocation() {
        return location;
    }

    public void setLocation(boolean location) {
        this.location = location;
        setValue("location", location);
    }


    public int getUpdateSpan() {
        return updateSpan;
    }

    public void setUpdateSpan(int updateSpan) {
        this.updateSpan = updateSpan;
        setValue("update_pan", location);
    }


    public int getUpdatePolicy() {
        return updatePolicy;
    }

    public void setUpdatePolicy(int updatePolicy) {
        this.updatePolicy = updatePolicy;
        setValue("update_policy", updatePolicy);
    }


    public boolean isLocationPrimaryCity() {
        return isLocationPrimaryCity;
    }

    public void setLocationPrimaryCity(boolean isLocationPrimaryCity) {
        this.isLocationPrimaryCity = isLocationPrimaryCity;
        setValue("is_location_primary", isLocationPrimaryCity);
    }

    public List<String> getCities() {
        return cities;
    }

    public String getCity(int position) {
        return cities.get(position);
    }

    public void addCity(String city) {
        if (cities.contains(city)) return;
        cities.add(city);
        setValue("cities", new Gson().toJson(cities));
    }

    public void delCity(String city) {
        cities.remove(city);
        setValue("cities", new Gson().toJson(cities));
    }


    public void setPrimaryCity(String city) {
        if (isLocationPrimaryCity) {
            //如果当前主城市是定位的，则替换
            if (cities.size() > 0) {
                cities.set(0, city);
            } else {
                cities.add(city);
            }
        } else {
            //不是定位的，则把要设的主城市移动到0位置
            if (cities.contains(city)) {
                cities.remove(city);
            }
            cities.addFirst(city);
        }
        setValue("cities", new Gson().toJson(cities));
    }

    @SuppressWarnings("unchecked")
    private void setValue(String name, Object value) {
        Editor editor = sp.edit();
        if (value instanceof Integer) {
            editor.putInt(name, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(name, (Boolean) value);
        } else if (value instanceof String) {
            editor.putString(name, (String) value);
        } else if (value instanceof Float) {
            editor.putFloat(name, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(name, (Long) value);
        } else if (value instanceof Set<?>) {
            editor.putStringSet(name, (Set<String>) value);
        }
        editor.commit();
    }


}
