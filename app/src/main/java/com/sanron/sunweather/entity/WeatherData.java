package com.sanron.sunweather.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

/**
 * 天气数据
 *
 * @author Administrator
 */
public class WeatherData {
    private String city;
    private Date updateTime;//更新时间
    private InstantWeather instantWeather;
    private AirEnvironment airEnvironment;
    private Warning warning;
    private List<Weather> weathers;

    public WeatherData() {
        weathers = new ArrayList<Weather>();
    }

    public InstantWeather getInstantWeather() {
        return instantWeather;
    }

    public void setInstantWeather(InstantWeather instantWeather) {
        this.instantWeather = instantWeather;
    }

    public AirEnvironment getAirEnvironment() {
        return airEnvironment;
    }

    public void setAirEnvironment(AirEnvironment airEnvironment) {
        this.airEnvironment = airEnvironment;
    }

    public Warning getWarning() {
        return warning;
    }

    public void setWarning(Warning warning) {
        this.warning = warning;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Weather getWeatherByDay(Date date) {
        int index = getWeatherIndexByDate(date);
        return index == -1 ? null : weathers.get(getWeatherIndexByDate(date));
    }

    @SuppressWarnings("deprecation")
    public int getWeatherIndexByDate(Date date) {
        if (weathers == null) {
            return -1;
        }
        for (int i = 0; i < weathers.size(); i++) {
            Weather weather = weathers.get(i);
            if (weather.getDate().getYear() == date.getYear()
                    && weather.getDate().getMonth() == date.getMonth()
                    && weather.getDate().getDate() == date.getDate()) {
                return i;
            }
        }
        return -1;
    }
}
