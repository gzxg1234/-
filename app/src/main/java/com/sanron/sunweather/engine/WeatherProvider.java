package com.sanron.sunweather.engine;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sanron.sunweather.entity.AirEnvironment;
import com.sanron.sunweather.entity.City;
import com.sanron.sunweather.entity.InstantWeather;
import com.sanron.sunweather.entity.Warning;
import com.sanron.sunweather.entity.Weather;
import com.sanron.sunweather.entity.Weather.HoursForecast;
import com.sanron.sunweather.entity.Weather.LifeIndex;
import com.sanron.sunweather.entity.WeatherData;
import com.sanron.sunweather.utils.DateUtils;
import com.sanron.sunweather.utils.StringUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 天气服务
 *
 * @author Sanron
 */
public class WeatherProvider {

    protected OkHttpClient httpClient;

    public static final String WEATHER_API1 = "http://apis.baidu.com/showapi_open_bus/weather_showapi/address";
    public static final String WEATHER_API2 = "http://wthrcdn.etouch.cn/WeatherApi";
    public static final String API_KEY = "c0a04284975e8ea322293cddb50d64fb";// 百度apikey

    public static final int TIME_OUT = 5; //网络超时时间
    public static WeatherProvider instance;

    public static WeatherProvider getInstance() {
        if (instance == null) {
            synchronized (WeatherProvider.class) {
                if (instance == null) {
                    instance = new WeatherProvider();
                }
            }
        }
        return instance;
    }

    private WeatherProvider() {
        httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(TIME_OUT, TimeUnit.SECONDS);
    }

    public void getWeahterData(final City city, final OnGetWeatherListener onGetWeatherListener) {
        String url = WEATHER_API1
                + "?areaid=" + city.getId()
                + "&needMoreDay=1&needIndex=1&needAlarm=1&need3HourForcast=1";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .get()
                .build();
        Call call = httpClient.newCall(request);
        onGetWeatherListener.onStart(city);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Response response) throws IOException {
                final WeatherData weatherData = new WeatherData();
                weatherData.setCity(city.getName());
                try {
                    parseJson(weatherData, response.body().string());
                    onGetWeatherListener.onSuccess(weatherData);
                } catch (Exception e) {
                    e.printStackTrace();
                    onGetWeatherListener.onError(city, "数据解析错误");
                    return;
                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                onGetWeatherListener.onError(city, "网络错误");
            }
        });
    }



    private void parseJson(WeatherData weatherData, String json) throws Exception {
        JsonObject jsonObject = new Gson().fromJson(json, JsonElement.class).getAsJsonObject();
        if (jsonObject == null || jsonObject.get("errNum") != null) {
            throw new Exception("data error");
        }
        JsonObject j_showapi_res_body = jsonObject.getAsJsonObject("showapi_res_body");
        if (j_showapi_res_body == null) {
            throw new Exception();
        }
        Iterator<Map.Entry<String, JsonElement>> it = j_showapi_res_body.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, JsonElement> entry = it.next();
            String key = entry.getKey();
            if ("alarmList".equals(key)) {
                //天气预警
                JsonArray j_alarmlist = (JsonArray) entry.getValue();
                if (j_alarmlist.size() > 0) {
                    Warning warning = new Warning();
                    JsonObject j_alarm = j_alarmlist.get(0).getAsJsonObject();
                    String province = j_alarm.getAsJsonPrimitive("province").getAsString();
                    String city = j_alarm.getAsJsonPrimitive("city").getAsString();
                    String type = j_alarm.getAsJsonPrimitive("signalType").getAsString();
                    String level = j_alarm.getAsJsonPrimitive("signalLevel").getAsString();
                    String time = j_alarm.getAsJsonPrimitive("issueTime").getAsString();
                    String detail = j_alarm.getAsJsonPrimitive("issueContent").getAsString();
                    warning.setAlarmDegree(level);
                    warning.setAlarmType(type);
                    warning.setTime(DateUtils.parse(time, "yyyy-MM-dd HH:mm:ss"));
                    warning.setCityName(province + city);
                    warning.setAlarmDetails(detail);
                    weatherData.setWarning(warning);
                }
            } else if ("now".equals(key)) {
                JsonObject j_now = (JsonObject) entry.getValue();
                //实时天气
                InstantWeather instantWeather = new InstantWeather();
                String shidu = j_now.getAsJsonPrimitive("sd").getAsString();
                String temp = j_now.getAsJsonPrimitive("temperature").getAsString();
                String windDirection = j_now.getAsJsonPrimitive("wind_direction").getAsString();
                String windPower = j_now.getAsJsonPrimitive("wind_power").getAsString();
                String tempTime = j_now.getAsJsonPrimitive("temperature_time").getAsString();
                instantWeather.setHumidity(shidu);
                instantWeather.setInstantTEMP(Integer.valueOf(temp));
                instantWeather.setWindDirection(windDirection);
                instantWeather.setWindPower(windPower);

                Date hm = DateUtils.parse(tempTime, "HH:mm");
                Date time = new Date();
                if (hm.getHours() > time.getHours()) {
                    //如果更新时间大于得到的本地时间，则更新时间可能为昨天的
                    time = DateUtils.addDate(time, -1);
                }
                time.setHours(hm.getHours());
                time.setMinutes(hm.getMinutes());
                instantWeather.setTime(time);

                //空气环境
                JsonObject j_aqi = j_now.getAsJsonObject("aqiDetail");
                AirEnvironment airEnvironment = new AirEnvironment();
                airEnvironment.setAqi(Integer.valueOf(j_aqi.getAsJsonPrimitive("aqi").getAsString()));
                airEnvironment.setPm25(Integer.valueOf(j_aqi.getAsJsonPrimitive("pm2_5").getAsString()));
                airEnvironment.setPm10(Integer.valueOf(j_aqi.getAsJsonPrimitive("pm10").getAsString()));
                airEnvironment.setSo2(Integer.valueOf(j_aqi.getAsJsonPrimitive("so2").getAsString()));
                airEnvironment.setNo2(Integer.valueOf(j_aqi.getAsJsonPrimitive("no2").getAsString()));
                airEnvironment.setMajorPollutants(j_aqi.getAsJsonPrimitive("primary_pollutant").getAsString());
                airEnvironment.setQuality(j_aqi.getAsJsonPrimitive("quality").getAsString());

                weatherData.setAirEnvironment(airEnvironment);
                weatherData.setInstantWeather(instantWeather);
            } else if (key.matches("^f\\d+$")) {
                if (weatherData.getWeathers().size() == 7) {
                    continue;
                }
                JsonObject element = (JsonObject) entry.getValue();
                Weather weather = new Weather();

                //天气日期
                Date weatherDate = DateUtils.parse(element.getAsJsonPrimitive("day").getAsString(), "yyyyMMdd");

                //逐三小时预报
                JsonArray j_3hoursForcast = element.getAsJsonArray("3hourForcast");
                if (j_3hoursForcast != null) {
                    List<HoursForecast> hoursForecasts = new ArrayList<HoursForecast>();
                    for (int i = 0; i < j_3hoursForcast.size(); i++) {
                        JsonObject j_hourFrocast = j_3hoursForcast.get(i).getAsJsonObject();
                        HoursForecast hoursForecast = new HoursForecast();

                        //小时区间
                        String j_hour = j_hourFrocast.getAsJsonPrimitive("hour").getAsString();
                        String[] hour = StringUtils.matcherText(j_hour, "\\d+");
                        if (hour != null && hour.length == 2) {
                            hoursForecast.setLeftHour(Integer.valueOf(hour[0]));
                            hoursForecast.setRightHour(Integer.valueOf(hour[1]));
                        }

                        //温度区间
                        JsonPrimitive j_tempMin = j_hourFrocast.getAsJsonPrimitive("temperature_min");
                        JsonPrimitive j_tempMax = j_hourFrocast.getAsJsonPrimitive("temperature_max");
                        if (j_tempMin != null) {
                            hoursForecast.setLeftTemp(Integer.valueOf(j_tempMin.getAsString()));
                        }
                        if (j_tempMax != null) {
                            hoursForecast.setRightTemp(Integer.valueOf(j_tempMax.getAsString()));
                        }

                        hoursForecast.setWeatherType(j_hourFrocast.getAsJsonPrimitive("weather").getAsString());
                        hoursForecast.setWindDirection(j_hourFrocast.getAsJsonPrimitive("wind_direction").getAsString());
                        hoursForecast.setWindPower(j_hourFrocast.getAsJsonPrimitive("wind_power").getAsString());
                        hoursForecasts.add(hoursForecast);
                    }
                    weather.setHoursForecasts(hoursForecasts);
                }

                //生活指数
                JsonObject indexs = element.getAsJsonObject("index");
                Iterator<Map.Entry<String, JsonElement>> it2 = indexs.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry<String, JsonElement> entry2 = it2.next();
                    String key2 = entry2.getKey();
                    JsonObject jsonObject2 = entry2.getValue().getAsJsonObject();
                    LifeIndex indexItem = new LifeIndex();
                    if ("ac".equals(key2)) {
                        indexItem.setName("空调指数");
                    } else if ("ag".equals(key2)) {
                        indexItem.setName("过敏指数");
                    } else if ("aqi".equals(key2)) {
                        indexItem.setName("空气指数");
                    } else if ("beauty".equals(key2)) {
                        indexItem.setName("化妆指数");
                    } else if ("cl".equals(key2)) {
                        indexItem.setName("晨练指数");
                    } else if ("clothes".equals(key2)) {
                        indexItem.setName("穿衣指数");
                    } else if ("cold".equals(key2)) {
                        indexItem.setName("感冒指数");
                    } else if ("dy".equals(key2)) {
                        indexItem.setName("钓鱼指数");
                    } else if ("glass".equals(key2)) {
                        indexItem.setName("太阳镜指数");
                    } else if ("gj".equals(key2)) {
                        indexItem.setName("逛街指数");
                    } else if ("hc".equals(key2)) {
                        indexItem.setName("寒潮指数");
                    } else if ("ls".equals(key2)) {
                        indexItem.setName("晾晒指数");
                    } else if ("mf".equals(key2)) {
                        indexItem.setName("美发指数");
                    } else if ("nl".equals(key2)) {
                        indexItem.setName("外出指数");
                    } else if ("pj".equals(key2)) {
                        indexItem.setName("饮酒指数");
                    } else if ("pk".equals(key2)) {
                        indexItem.setName("放风筝指数");
                    } else if ("travel".equals(key2)) {
                        indexItem.setName("旅游指数");
                    } else if ("uv".equals(key2)) {
                        indexItem.setName("紫外线指数");
                    } else if ("wash_car".equals(key2)) {
                        indexItem.setName("洗车指数");
                    } else if ("xq".equals(key2)) {
                        indexItem.setName("心情指数");
                    } else if ("yh".equals(key2)) {
                        indexItem.setName("约会指数");
                    } else if ("zs".equals(key2)) {
                        indexItem.setName("中暑指数");
                    }

                    JsonPrimitive title = jsonObject2.getAsJsonPrimitive("title");
                    JsonPrimitive desc = jsonObject2.getAsJsonPrimitive("desc");
                    if(title != null){
                        indexItem.setValue(title.getAsString());
                    }
                    if(desc != null){
                        indexItem.setDetail(desc.getAsString());
                    }
                    weather.getLifeIndexs().add(indexItem);
                }

                //
                weather.setDayType(element.getAsJsonPrimitive("day_weather").getAsString());
                weather.setDayTEMP(element.getAsJsonPrimitive("day_air_temperature").getAsInt());
                weather.setDayWindDirect(element.getAsJsonPrimitive("day_wind_direction").getAsString());
                weather.setDayWindPower(element.getAsJsonPrimitive("day_wind_power").getAsString());
                weather.setNightType(element.getAsJsonPrimitive("night_weather").getAsString());
                weather.setNightTEMP(element.getAsJsonPrimitive("night_air_temperature").getAsInt());
                weather.setNightWindDirect(element.getAsJsonPrimitive("night_wind_direction").getAsString());
                weather.setNightWindPower(element.getAsJsonPrimitive("night_wind_power").getAsString());

                //日出日落
                String[] ss = element.getAsJsonPrimitive("sun_begin_end").getAsString().split("[|]");
                Date sunRise = DateUtils.parse(ss[0], "HH:mm");
                Date sunSet = DateUtils.parse(ss[1], "HH:mm");
                sunRise.setYear(weatherDate.getYear());
                sunRise.setMonth(weatherDate.getMonth());
                sunRise.setDate(weatherDate.getDate());
                sunSet.setYear(weatherDate.getYear());
                sunSet.setMonth(weatherDate.getMonth());
                sunSet.setDate(weatherDate.getDate());

                weather.setSunriseTime(sunRise);
                weather.setSunsetTime(sunSet);
                weather.setDate(weatherDate);
                weatherData.getWeathers().add(weather);

            } else if ("time".equals(key)) {
                String j_time = entry.getValue().getAsJsonPrimitive().getAsString();
                Date time = DateUtils.parse(j_time, "yyyyMMddHHmmss");
                weatherData.setUpdateTime(time);
            }

        }
    }

    public interface OnGetWeatherListener {
        void onSuccess(WeatherData weatherData);

        void onError(City city, String error);

        void onStart(City city);
    }
}
