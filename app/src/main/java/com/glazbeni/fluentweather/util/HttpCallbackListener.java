package com.glazbeni.fluentweather.util;

/**
 * Created by 29044 on 17.8.22.
 */

public interface HttpCallbackListener {

    /**
     * 向服务器请求数据成功时调用
     * @param respone
     */
    void onFinish(String respone);

    /**
     * 向服务器请求数据失败时调用
     * @param e
     */
    void onError(Exception e);

}
