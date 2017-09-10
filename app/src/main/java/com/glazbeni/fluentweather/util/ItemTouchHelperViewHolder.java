package com.glazbeni.fluentweather.util;

/**
 * Created by 29044 on 17.9.5.
 */

public interface ItemTouchHelperViewHolder {

    /**
     * 拖拽或滑动item时调用
     */
    void onItemSelected();

    /**
     * 拖拽或滑动结束时调用
     */
    void onItemCLear();
}
