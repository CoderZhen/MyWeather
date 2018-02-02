package com.zhen.myweather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.zhen.myweather.db.City;
import com.zhen.myweather.db.County;
import com.zhen.myweather.db.Province;
import com.zhen.myweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseAreaFragment extends Fragment {
    private static final String TAG = "ChooseAreaFragment-vv";

    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String COUNTY = "county";
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private ListView listView;

    private final int LEVEL_PROVINCE = 0;
    private final int LEVEL_CITY = 1;
    private final int LEVEL_COUNTY = 2;
    private int currentLevel;

    private Province selectProvince;
    private City selectCity;
    private County selectCounty;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private List<String> dataList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
        init(view);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectProvince = provinceList.get(position);
                    queryCity();
                } else if (currentLevel == LEVEL_CITY) {
                    selectCity = cityList.get(position);
                    queryCounty();
                }
            }
        });

        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_CITY) {
                    queryProvince();
                } else if (currentLevel == LEVEL_COUNTY) {
                    queryCity();
                }
            }
        });
        queryProvince();
    }

    private void queryProvince() {
        toolbar_title.setText("中国");
        provinceList = DataSupport.findAll(Province.class);
        toolbar_back.setVisibility(View.INVISIBLE);
        if (!provinceList.isEmpty()) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            currentLevel = LEVEL_PROVINCE;
            listView.setSelection(0);
        } else {
            String address = "http://guolin.tech/api/china";
            requestFromServer(address, PROVINCE);
        }
    }

    private void queryCity() {
        toolbar_title.setText(selectProvince.getProvinceName());
        toolbar_back.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectProvince.getProvinceId())).find(City.class);
        if (!cityList.isEmpty()) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            currentLevel = LEVEL_CITY;
            listView.setSelection(0);
        } else {
            int provinceId = selectProvince.getProvinceId();
            Log.d(TAG, provinceId + "");
            String address = "http://guolin.tech/api/china/" + provinceId;
            requestFromServer(address, CITY);
        }
    }

    private void queryCounty() {
        toolbar_title.setText(selectCity.getCityName());
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectCity.getCityId())).find(County.class);
        if (!countyList.isEmpty()) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceId = selectProvince.getProvinceId();
            int cityId = selectCity.getCityId();
            String address = "http://guolin.tech/api/china/" + provinceId + "/" + cityId;
            requestFromServer(address, COUNTY);
        }
    }

    private void requestFromServer(String address, final String type) {
        RxVolley.get(address, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                boolean result = false;
                if (PROVINCE.equals(type)) {
                    result = Utility.handlerProvinceResponse(t);
                } else if (CITY.equals(type)) {
                    result = Utility.handlerCityResponse(t, selectProvince.getProvinceId());
                } else if (COUNTY.equals(type)) {
                    result = Utility.handlerCountyResponse(t, selectCity.getCityId());
                }

                if (result) {
                    if (PROVINCE.equals(type)) {
                        queryProvince();
                    } else if (CITY.equals(type)) {
                        queryCity();
                    } else if (COUNTY.equals(type)) {
                        queryCounty();
                    }
                }
            }
        });
    }

    private void init(View view) {
        toolbar_back = view.findViewById(R.id.toolbar_back);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        listView = view.findViewById(R.id.listView);
    }
}
