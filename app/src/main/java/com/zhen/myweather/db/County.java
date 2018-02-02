package com.zhen.myweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ZHEN on 2018/2/2.
 */

public class County extends DataSupport {

    private int countyId;
    private int cityId;
    private String countyName;
    private String weatherId;

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
