package com.zhen.myweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.squareup.picasso.Picasso;
import com.zhen.myweather.gson.Forecast;
import com.zhen.myweather.gson.Weather;
import com.zhen.myweather.service.AutoUpdateService;
import com.zhen.myweather.util.Utility;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity-vv";
    public static final String WEATHER = "weather";
    private String mWeatherid;
    private ImageView weatherBg;
    private TextView weatherTitle;
    private TextView deggreeText;
    private TextView deggreeInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private ProgressDialog progressDialog;
    private TextView weatherUpdateTime;
    private ScrollView scrollView;
    public SwipeRefreshLayout swipeRefreshLayout;
    private ImageView weatherHome;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        loadBindPic();
        //系统状态栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        SharedPreferences spfe = getSharedPreferences("myweather", MODE_PRIVATE);
        String weatherContent = spfe.getString(WEATHER, null);
        if (weatherContent != null) {
            Weather weather = Utility.handlerWeatherResponse(weatherContent);
            showWeatherInfo(weather);
            mWeatherid = weather.basic.weatherId;
        } else {
            Log.d(TAG, "weatherContent 为空");
            mWeatherid = getIntent().getStringExtra("weatherid");
            scrollView.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherid);
        }

        weatherHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherid);
            }
        });
    }

    public void requestWeather(final String weatherid) {
        String address = "http://guolin.tech/api/weather?cityid=" + weatherid + "&key=2b8b3667f7594107aec9f04c1d2ba65b";
        showProgressDialog();
        RxVolley.get(address, new HttpCallback() {
            @Override
            public void onSuccess(final String t) {
                final Weather weather = Utility.handlerWeatherResponse(t);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && weather.status.equals("ok")) {
                            SharedPreferences.Editor editor = getSharedPreferences("myweather", MODE_PRIVATE).edit();
                            editor.putString(WEATHER, t);
                            editor.apply();
                            mWeatherid = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        stopProgressDialog();
                    }
                });
            }

            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(WeatherActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                stopProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        //title
        weatherTitle.setText(weather.basic.cityName);
        String updateTime = weather.basic.update.updataTime.split(" ")[1];
        weatherUpdateTime.setText(updateTime);
        //now
        deggreeText.setText(weather.now.temperature);
        deggreeInfoText.setText(weather.now.more.weather_info);
        //forecast
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView forecastTimeText = view.findViewById(R.id.forecast_time_text);
            TextView forecastInfoText = view.findViewById(R.id.forecast_info_text);
            TextView forecastMaxText = view.findViewById(R.id.forecast_max_text);
            TextView forecastMinText = view.findViewById(R.id.forecast_mix_text);
            forecastTimeText.setText(forecast.date);
            forecastInfoText.setText(forecast.more.weather_info);
            forecastMaxText.setText(forecast.temperature.max);
            forecastMinText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        //aqi
        aqiText.setText(String.valueOf(weather.aqi.city.aqi));
        pm25Text.setText(String.valueOf(weather.aqi.city.pm25));
        //suggestion
        String comfort = "舒适度:" + weather.suggestion.comf.info;
        String carWash = "洗车指数:" + weather.suggestion.cw.info;
        String sport = "运动指数:" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        scrollView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void loadBindPic() {
        String bingUrl = "http://guolin.tech/api/bing_pic";
        RxVolley.get(bingUrl, new HttpCallback() {
            @Override
            public void onSuccess(final String t) {
                final SharedPreferences.Editor spfe = getSharedPreferences("weatherbg",MODE_PRIVATE).edit();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spfe.putString("bingBg",t);
                        spfe.apply();
                        Picasso.with(WeatherActivity.this).load(t).into(weatherBg);
                    }
                });
            }
        });
    }

    private void initView() {
        scrollView = findViewById(R.id.scrollview);
        weatherBg = findViewById(R.id.weather_bg);
        weatherTitle = findViewById(R.id.weather_title);
        weatherHome = findViewById(R.id.weather_home);
        drawerLayout = findViewById(R.id.drawerLayout);
        weatherUpdateTime = findViewById(R.id.weather_updatetime);
        deggreeText = findViewById(R.id.degree_text);
        deggreeInfoText = findViewById(R.id.degree_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.carwash_text);
        sportText = findViewById(R.id.sport_text);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.BLACK);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("请稍等");
            progressDialog.show();
        }
    }

    public void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }
}
