package com.sanron.sunweather.entity;

import java.util.Date;

public class InstantWeather {
    private Date time;//发布时间
    private int instantTEMP;//即时温度
    private String windPower;//风力
    private String humidity;//湿度
    private String windDirection;//风向

    public int getInstantTEMP() {
        return instantTEMP;
    }

    public void setInstantTEMP(int instantTEMP) {
        this.instantTEMP = instantTEMP;
    }

    public String getWindPower() {
        return windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
