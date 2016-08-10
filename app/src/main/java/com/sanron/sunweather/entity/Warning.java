package com.sanron.sunweather.entity;

import java.util.Calendar;
import java.util.Date;

import android.text.format.Time;

public class Warning {
    private String cityName;//城市
    private String alarmType;//警告类型
    private String alarmDegree;//警告程度
    private String alarmDetails;//详细信息
    private Date time;//发布时间

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmDegree() {
        return alarmDegree;
    }

    public void setAlarmDegree(String alarmDegree) {
        this.alarmDegree = alarmDegree;
    }

    public String getAlarmDetails() {
        return alarmDetails;
    }

    public void setAlarmDetails(String alarmDetails) {
        this.alarmDetails = alarmDetails;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
