package com.zhen.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZHEN on 2018/2/3.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updataTime;
    }
}
