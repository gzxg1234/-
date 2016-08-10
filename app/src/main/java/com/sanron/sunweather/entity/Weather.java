package com.sanron.sunweather.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 天气
 *
 * @author Administrator
 */
public class Weather {
    private Date date;//时间
    private int dayTEMP;//白天最高温度
    private int nightTEMP;//晚上最低温度
    private String dayType;//白天天气类型
    private String dayWindDirect;//白天风向
    private String dayWindPower;//白天风力
    private String nightType;//晚上天气类型
    private String nightWindDirect;//晚上风向
    private String nightWindPower;//晚上风力
    private Date sunriseTime;//日起时间
    private Date sunsetTime;//日落
    private List<HoursForecast> hoursForecasts;//逐小时预报
    private List<LifeIndex> LifeIndexs;//生活指数

    public Weather() {
        hoursForecasts = new ArrayList<HoursForecast>();
        LifeIndexs = new ArrayList<LifeIndex>();
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getDayWindDirect() {
        return dayWindDirect;
    }

    public void setDayWindDirect(String dayWindDirect) {
        this.dayWindDirect = dayWindDirect;
    }

    public String getDayWindPower() {
        return dayWindPower;
    }

    public void setDayWindPower(String dayWindPower) {
        this.dayWindPower = dayWindPower;
    }

    public String getNightType() {
        return nightType;
    }

    public void setNightType(String nightType) {
        this.nightType = nightType;
    }

    public String getNightWindDirect() {
        return nightWindDirect;
    }

    public void setNightWindDirect(String nightWindDirect) {
        this.nightWindDirect = nightWindDirect;
    }

    public String getNightWindPower() {
        return nightWindPower;
    }

    public void setNightWindPower(String nightWindPower) {
        this.nightWindPower = nightWindPower;
    }


    public List<HoursForecast> getHoursForecasts() {
        return hoursForecasts;
    }

    public void setHoursForecasts(List<HoursForecast> hoursForecasts) {
        this.hoursForecasts = hoursForecasts;
    }


    public int getDayTEMP() {
        return dayTEMP;
    }

    public void setDayTEMP(int dayTEMP) {
        this.dayTEMP = dayTEMP;
    }


    public int getNightTEMP() {
        return nightTEMP;
    }

    public void setNightTEMP(int nightTEMP) {
        this.nightTEMP = nightTEMP;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public Date getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(Date sunsetTime) {
        this.sunsetTime = sunsetTime;
    }


    public Date getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(Date sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public List<LifeIndex> getLifeIndexs() {
        return LifeIndexs;
    }

    public void setLifeIndexs(List<LifeIndex> lifeIndexs) {
        LifeIndexs = lifeIndexs;
    }

    /**
     * 生活指数
     *
     * @author Administrator
     */
    public static class LifeIndex {
        private String name;//指数名称
        private String value;//指数值
        private String detail;//详情

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

    /**
     * 小时预报
     */
    public static class HoursForecast {
        private int leftHour;
        private int rightHour;
        private int leftTemp;
        private int rightTemp;
        private String weatherType;
        private String windDirection;
        private String windPower;

        public int getLeftHour() {
            return leftHour;
        }

        public void setLeftHour(int leftHour) {
            this.leftHour = leftHour;
        }

        public int getRightHour() {
            return rightHour;
        }

        public void setRightHour(int rightHour) {
            this.rightHour = rightHour;
        }

        public int getLeftTemp() {
            return leftTemp;
        }

        public void setLeftTemp(int leftTemp) {
            this.leftTemp = leftTemp;
        }

        public int getRightTemp() {
            return rightTemp;
        }

        public void setRightTemp(int rightTemp) {
            this.rightTemp = rightTemp;
        }

        public String getWeatherType() {
            return weatherType;
        }

        public void setWeatherType(String weatherType) {
            this.weatherType = weatherType;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getWindPower() {
            return windPower;
        }

        public void setWindPower(String windPower) {
            this.windPower = windPower;
        }

    }
}
