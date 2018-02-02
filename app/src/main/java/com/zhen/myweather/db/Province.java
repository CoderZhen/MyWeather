package com.zhen.myweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ZHEN on 2018/2/2.
 */

public class Province extends DataSupport {
    private int provinceId;
    private String provinceName;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
