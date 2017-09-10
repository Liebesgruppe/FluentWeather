package com.glazbeni.fluentweather.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glazbeni.fluentweather.R;
import com.glazbeni.fluentweather.model.DailyForecastBean;
import com.glazbeni.fluentweather.model.WeatherBean;
import com.glazbeni.fluentweather.service.AutoUpdateService;
import com.glazbeni.fluentweather.util.HttpCallbackListener;
import com.glazbeni.fluentweather.util.HttpUtil;
import com.glazbeni.fluentweather.util.RuntimePermission;
import com.glazbeni.fluentweather.util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 29044 on 17.8.24.
 */

public class ShowWeatherActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView
            tvUpdateTime, //气象发布时间
            tvLocation, //所在城市
            tvTemp, //实时温度
            tvTempMaxMin, //最高最低气温
            tvWeather, //天气
            tvPM25, //PM2.5
            tvQlty, //空气质量
            tvDailyTime0, //天预报的时间
            tvDailyTime1,
            tvDailyTime2,
            tvDailyTmp0, //天预报的温度
            tvDailyTmp1,
            tvDailyTmp2,
            tvAirBrf, //空气指数简介
            tvComfBrf, //舒适度简介
            tvCwBrf, //洗车指数简介
            tvDrsgBrf, //穿衣指数简介
            tvFluBrf, //感冒指数简介
            tvSportBrf, //运动指数简介
            tvTravBrf, //旅游指数简介
            tvUvBrf; //紫外线指数简介

    private ImageView
            ivDailyWeather0, //天预报的图例
            ivDailyWeather1,
            ivDailyWeather2,
            ivWeather;  //天气图片

    private SwipeRefreshLayout srLayout;
    private WeatherBean bean;
    private String district;
    private Toolbar toolbar;
    private Intent i;
    public static final String URL_PART1 = "https://free-api.heweather.com/v5/weather?city=";
    public static final String URL_PART2 = "&key=457631a5733743148d3733cbc98a2500";
    public static final int GET_WEATHER_BEAN = 100;
    private Message msg;
    private boolean isBind;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_WEATHER_BEAN:
                    loadData();
                    srLayout.setRefreshing(false);
                    break;
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, IBinder service) {
            AutoUpdateService.MyBinder myBinder = (AutoUpdateService.MyBinder) service;
            AutoUpdateService myService = myBinder.getAutoService();
            myService.setMyCallback(new AutoUpdateService.MyCallback() {
                @Override
                public void onWeatherRefreshed(WeatherBean bean) {
                    msg = new Message();
                    msg.what = GET_WEATHER_BEAN;
                    msg.obj = bean;
                    handler.sendEmptyMessage(msg.what);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_weather);

        initView();
        Intent intent = getIntent();
        String city = intent.getStringExtra("cityName");
        if (city != null) {
            getJSONWeather(city);
        } else {
            RuntimePermission.requestPermissions(this, 1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION}
                    , new RuntimePermission.OnPermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            district = Utility.locate(ShowWeatherActivity.this);
                            getJSONWeather(district);
                            i = new Intent(ShowWeatherActivity.this, AutoUpdateService.class);
                            startService(i);
                            bindService(i, connection, BIND_AUTO_CREATE);
                            isBind = true;
                        }

                        @Override
                        public void onPermissionDenied() {
                            new AlertDialog.Builder(ShowWeatherActivity.this)
                                    .setTitle("FBI WARNING")
                                    .setMessage("必要的权限没有得到授予，应用可能无法正常运作(っ °Д °;)っ")
                                    .setPositiveButton("我觉得可以", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    }).show();
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            unbindService(connection);
            stopService(i);
            isBind = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RuntimePermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getJSONWeather(Utility.locate(this));
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location_city:
                startActivity(new Intent(ShowWeatherActivity.this, LocationCity.class));
                break;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        getJSONWeather(tvLocation.getText().toString());
    }

    /**
     * 初始化布局资源
     */
    private void initView() {
        tvUpdateTime = (TextView) findViewById(R.id.tv_update_time);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvTemp = (TextView) findViewById(R.id.tv_temp);
        tvTempMaxMin = (TextView) findViewById(R.id.tv_temp_max_min);
        tvWeather = (TextView) findViewById(R.id.tv_weather);
        tvPM25 = (TextView) findViewById(R.id.tv_pm25);
        tvQlty = (TextView) findViewById(R.id.tv_qlty);
        tvDailyTmp0 = (TextView) findViewById(R.id.tv_daily_tmp1);
        tvDailyTmp1 = (TextView) findViewById(R.id.tv_daily_tmp2);
        tvDailyTmp2 = (TextView) findViewById(R.id.tv_daily_tmp3);
        tvDailyTime0 = (TextView) findViewById(R.id.tv_daily_time1);
        tvDailyTime1 = (TextView) findViewById(R.id.tv_daily_time2);
        tvDailyTime2 = (TextView) findViewById(R.id.tv_daily_time3);
        ivDailyWeather0 = (ImageView) findViewById(R.id.iv_daily_weather1);
        ivDailyWeather1 = (ImageView) findViewById(R.id.iv_daily_weather2);
        ivDailyWeather2 = (ImageView) findViewById(R.id.iv_daily_weather3);
        tvAirBrf = (TextView) findViewById(R.id.tv_air_brf);
        tvComfBrf = (TextView) findViewById(R.id.tv_comf_brf);
        tvCwBrf = (TextView) findViewById(R.id.tv_cw_brf);
        tvDrsgBrf = (TextView) findViewById(R.id.tv_drsg_brf);
        tvFluBrf = (TextView) findViewById(R.id.tv_flu_brf);
        tvSportBrf = (TextView) findViewById(R.id.tv_sport_brf);
        tvTravBrf = (TextView) findViewById(R.id.tv_trav_brf);
        tvUvBrf = (TextView) findViewById(R.id.tv_uv_brf);
        srLayout = (SwipeRefreshLayout) findViewById(R.id.sr_layout);
        toolbar = (Toolbar) findViewById(R.id.toobar);
        ivWeather = (ImageView) findViewById(R.id.iv_weather);
    }

    /**
     * 向控件中装载数据
     */
    public void loadData() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.ic_location_on_white_24dp);
        toolbar.setOnMenuItemClickListener(this);
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.cv1, R.color.cv2, R.color.cv3, R.color.cv4);
        tvUpdateTime.setText("发布于：" + bean.getUpdateTime().substring(11));
        tvLocation.setText(bean.getCity());
        tvTemp.setText(bean.getTemp());
        tvTempMaxMin.setText(bean.getDailyForecastBeanList().get(0).getDailyTmpMax() + "° / " + bean.getDailyForecastBeanList().get(0).getDailyTmpMin() + "°C");
        tvWeather.setText(bean.getTxt() + " ");
        tvPM25.setText(bean.getPm25());
        tvQlty.setText(bean.getQlty());
        setDaily(tvDailyTime0, ivDailyWeather0, tvDailyTmp0, 0);
        setDaily(tvDailyTime1, ivDailyWeather1, tvDailyTmp1, 1);
        setDaily(tvDailyTime2, ivDailyWeather2, tvDailyTmp2, 2);
        tvAirBrf.setText(bean.getAirBrf());
        tvComfBrf.setText(bean.getCofmBrf());
        tvCwBrf.setText(bean.getCwBrf());
        tvDrsgBrf.setText(bean.getDrsgBrf());
        tvFluBrf.setText(bean.getFluBrf());
        tvSportBrf.setText(bean.getSportBrf());
        tvTravBrf.setText(bean.getTravBrf());
        tvUvBrf.setText(bean.getUvBrf());
        ivWeather.setImageResource(getResources().getIdentifier("w" + bean.getWeatherCode(), "drawable", "com.glazbeni.fluentweather"));
    }

    /**
     * 开启线程，从服务器获取JSON格式的天气数据
     */
    private void getJSONWeather(String city) {
        HttpUtil.sendHttpRequest(URL_PART1 + city + URL_PART2
                , new HttpCallbackListener() {
                    @Override
                    public void onFinish(String respone) {
                        bean = Utility.handleWeatherResponse(respone);
                        msg = new Message();
                        msg.what = GET_WEATHER_BEAN;
                        msg.obj = bean;
                        handler.sendEmptyMessage(msg.what);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(ShowWeatherActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 填充三天的天气信息
     */
    private void setDaily(TextView tv0, ImageView iv, TextView tv1, int i) {

        long time0 = System.currentTimeMillis();
        long time1 = time0 + 1000 * 60 * 60 * 24;
        long time2 = time1 + 1000 * 60 * 60 * 24;
        Date date1 = new Date(time1);
        Date date2 = new Date(time2);
        SimpleDateFormat format = new SimpleDateFormat("E");
        String week0 = "今天";
        String week1 = format.format(date1);
        String week2 = format.format(date2);
        List<String> weeks = new ArrayList<>();
        weeks.add(week0);
        weeks.add(week1);
        weeks.add(week2);
        List<DailyForecastBean> dailyBean = bean.getDailyForecastBeanList();
        tv0.setText(dailyBean.get(i).getDailyDate().substring(6, 7) + "/" + dailyBean.get(i).getDailyDate().substring(8) + "\n" + weeks.get(i));
        tv1.setText("↑" + dailyBean.get(i).getDailyTmpMax() + "°" + "↓" + dailyBean.get(i).getDailyTmpMin() + "°");
        iv.setImageResource(getResources().getIdentifier("w" + dailyBean.get(i).getDailyWeatherCode(), "drawable", "com.glazbeni.fluentweather"));
    }

}
