package com.zhen.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZHEN on 2018/2/3.
 */

public class AQI {
    @SerializedName("city")
    public City city;

    public class City{
        @SerializedName("aqi")
        public int aqi;
        @SerializedName("pm25")
        public int pm25;
    }

}
