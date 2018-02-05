package com.zhen.myweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.zhen.myweather.gson.Weather;
import com.zhen.myweather.util.Utility;

public class AutoUpdateService extends Service {
    private static final String TAG = "AutoUpdateService-vv";

    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AutoUpdateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        if (manager != null) {
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void AutoUpdateWeather() {
        SharedPreferences spfe = getSharedPreferences("myweather", MODE_PRIVATE);
        String weatherString = spfe.getString("weather", null);

        if (weatherString != null) {
            Weather weather = Utility.handlerWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=2b8b3667f7594107aec9f04c1d2ba65b";
            RxVolley.get(weatherUrl, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    SharedPreferences.Editor editor = getSharedPreferences("myweather", MODE_PRIVATE).edit();
                    editor.putString("weather", t);
                    editor.apply();
                }
            });
        }
    }

    private void loadUpdateBg() {
        String bgUrl = "http://guolin.tech/api/bing_pic";
        RxVolley.get(bgUrl, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                SharedPreferences.Editor editor = getSharedPreferences("weatherbg", MODE_PRIVATE).edit();
                editor.putString("bingBg", t);
                editor.apply();
            }
        });
    }
}
