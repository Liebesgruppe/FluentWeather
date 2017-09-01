package com.glazbeni.fluentweather.util;

/**
 * Created by 29044 on 17.8.22.
 */

public interface HttpCallbackListener {
    void onFinish(String respone);
    void onError(Exception e);
}
