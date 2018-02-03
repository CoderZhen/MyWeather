package com.zhen.myweather.util;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;

import com.google.gson.Gson;
import com.zhen.myweather.db.City;
import com.zhen.myweather.db.County;
import com.zhen.myweather.db.Province;
import com.zhen.myweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZHEN on 2018/2/2.
 */

public class Utility {

    public static boolean handlerProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceId(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handlerCityResponse(String response,int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setProvinceId(provinceId);
                    city.setCityId(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handlerCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setCityId(cityId);
                    county.setCountyId(jsonObject.getInt("id"));
                    county.setCountyName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handlerWeatherResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject heWeather = new JSONObject(response).getJSONArray("HeWeather").getJSONObject(0);
                String weather = heWeather.toString();
                return new Gson().fromJson(weather,Weather.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
