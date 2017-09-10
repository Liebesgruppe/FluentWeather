package com.glazbeni.fluentweather.util;

/**
 * Created by 29044 on 17.9.5.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
