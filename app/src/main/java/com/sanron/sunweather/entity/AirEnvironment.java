package com.sanron.sunweather.entity;

/**
 * 大气环境
 *
 * @author Administrator
 */
public class AirEnvironment {
    private int aqi;//空气质量指数
    private int pm25;//细颗粒物指数
    private String quality;//质量
    private String majorPollutants;//主要污染物
    private int o3;//臭氧数值
    private int co;//一氧化碳
    private int pm10;//可吸入颗粒物
    private int so2;//二氧化硫
    private int no2;//二氧化氮

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getMajorPollutants() {
        return majorPollutants;
    }

    public void setMajorPollutants(String majorPollutants) {
        this.majorPollutants = majorPollutants;
    }

    public int getO3() {
        return o3;
    }

    public void setO3(int o3) {
        this.o3 = o3;
    }

    public int getCo() {
        return co;
    }

    public void setCo(int co) {
        this.co = co;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public int getSo2() {
        return so2;
    }

    public void setSo2(int so2) {
        this.so2 = so2;
    }

    public int getNo2() {
        return no2;
    }

    public void setNo2(int no2) {
        this.no2 = no2;
    }
}