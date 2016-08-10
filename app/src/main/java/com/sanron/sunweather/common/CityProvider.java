package com.sanron.sunweather.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sanron.sunweather.entity.City;

public class CityProvider {

    private static HashMap<String, City> cities;

    public static void init(Context context) {

        if (cities != null) return;

        cities = new HashMap<String, City>();
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            is = assetManager.open("cities.dat");
            isr = new InputStreamReader(is);
            JsonElement element = new JsonParser().parse(isr);
            JsonArray jsonArrayP = element.getAsJsonArray();
            for (int i = 0; i < jsonArrayP.size(); i++) {
                //遍历省
                JsonObject jsonObjectP = jsonArrayP.get(i).getAsJsonObject();
                String idP = jsonObjectP.get("id").getAsString();
                String nameP = jsonObjectP.get("name").getAsString();
                City cityP = new City();
                cityP.setId(idP);
                cityP.setName(nameP);
                JsonElement e = jsonObjectP.get("cities");
                if (e == null) {
                    cities.put(cityP.getName(), cityP);
                } else {
                    JsonArray jsonArrayC = e.getAsJsonArray();
                    for (int ii = 0; ii < jsonArrayC.size(); ii++) {
                        //遍历市
                        JsonObject jsonObjectC = jsonArrayC.get(ii).getAsJsonObject();
                        String idC = jsonObjectC.get("id").getAsString();
                        String nameC = jsonObjectC.get("name").getAsString();
                        City cityC = new City();
                        cityC.setId(idC);
                        cityC.setName(nameC);
                        cityC.setParent(cityP);
                        JsonElement ee = jsonObjectC.get("cities");
                        if (ee == null) {
                            cities.put(cityC.getName(), cityC);
                        } else {
                            JsonArray jsonArrayA = ee.getAsJsonArray();
                            for (int iii = 0; iii < jsonArrayA.size(); iii++) {
                                //遍历县
                                JsonObject jsonObjectA = jsonArrayA.get(iii).getAsJsonObject();
                                String idA = jsonObjectA.get("id").getAsString();
                                String nameA = jsonObjectA.get("name").getAsString();
                                if (nameA.equals(cityC.getName())) {
                                    cities.put(cityC.getName(), cityC);
                                    continue;
                                }
                                City cityA = new City();
                                cityA.setId(idA);
                                cityA.setName(nameA);
                                cityA.setParent(cityC);
                                cities.put(cityA.getName(), cityA);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static City findByName(String name) {
        if (cities == null) {
            return null;
        }
        return cities.get(name);
    }

    public static List<City> findMatchText(String text) {
        if (cities == null) {
            return null;
        }
        List<City> result = new LinkedList<City>();
        Iterator<Map.Entry<String, City>> it = cities.entrySet().iterator();
        while (it.hasNext()) {
            City city = it.next().getValue();
            if (city.getName().contains(text)) {
                result.add(city);
            }
        }
        return result;
    }


}
