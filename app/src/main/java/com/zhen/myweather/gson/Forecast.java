package com.zhen.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZHEN on 2018/2/3.
 */

public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String weather_info;
    }
}
