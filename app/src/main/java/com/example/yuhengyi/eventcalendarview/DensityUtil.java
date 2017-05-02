package com.example.yuhengyi.eventcalendarview;

import android.content.Context;

/**
 * Created by yuhengyi on 2017/5/2.
 */

public class DensityUtil {
    public static int dp2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5F);
    }
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue / fontScale + 0.5F);
    }
}
