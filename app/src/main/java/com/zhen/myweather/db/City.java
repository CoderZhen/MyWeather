package com.zhen.myweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ZHEN on 2018/2/2.
 */

public class City extends DataSupport {

    private int cityId;
    private String cityName;
    private int provinceId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
