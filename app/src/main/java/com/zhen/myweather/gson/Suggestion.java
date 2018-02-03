package com.zhen.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZHEN on 2018/2/3.
 */

public class Suggestion {

    public comFort comf;
    public carWash cw;
    public Sport sport;

    public class comFort{
        @SerializedName("txt")
        public String info;
    }

    public class carWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
