package com.zhen.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZHEN on 2018/2/3.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String weather_info;
    }
}
