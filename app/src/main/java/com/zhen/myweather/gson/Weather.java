package com.zhen.myweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZHEN on 2018/2/3.
 */

public class Weather {
    public AQI aqi;
    public Basic basic;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
    public Now now;
    public String status;
    public Suggestion suggestion;
}
