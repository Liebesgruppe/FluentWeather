package com.glazbeni.fluentweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.glazbeni.fluentweather.model.WeatherBean;
import com.glazbeni.fluentweather.util.HttpCallbackListener;
import com.glazbeni.fluentweather.util.HttpUtil;
import com.glazbeni.fluentweather.util.Utility;

import static com.glazbeni.fluentweather.activity.ShowWeatherActivity.URL_PART1;
import static com.glazbeni.fluentweather.activity.ShowWeatherActivity.URL_PART2;

/**
 * Created by 29044 on 17.9.3.
 */

public class AutoUpdateService extends Service {

    private MyCallback myCallback;
    private AlarmManager manager;
    private PendingIntent pendingIntent;

    public class MyBinder extends Binder {
        public AutoUpdateService getAutoService() {
            return AutoUpdateService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        refreshWeather();
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1000 * 60 * 60 * 3;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void refreshWeather() {
        HttpUtil.sendHttpRequest(URL_PART1 + Utility.locate(this) + URL_PART2
                , new HttpCallbackListener() {
                    @Override
                    public void onFinish(String respone) {
                        WeatherBean bean = Utility.handleWeatherResponse(respone);
                        myCallback.onWeatherRefreshed(bean);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AutoUpdateService.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.cancel(pendingIntent);
    }

    public static interface MyCallback {
        void onWeatherRefreshed(WeatherBean bean);
    }

    public void setMyCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }
}
