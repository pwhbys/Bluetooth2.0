
package com.WideMouth.bluetooth20.Util;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;

import org.litepal.crud.LitePalSupport;

/**
 * dp与px相互转换的工具类
 */
public class WMUtil {
    public static Settings settings;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context 上下文对象
     * @param pxValue 字体大小像素
     * @return 字体大小sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context 上下文对象
     * @param spValue 字体大小sp
     * @return 字体大小像素
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void setSystemBarStyle(Context context, Window window, boolean miniAlpha,
                                         boolean statusShader, boolean lightStatus,
                                         boolean navigationShader, boolean lightNavigation) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        // statusShader &= Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
        lightStatus &= Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        navigationShader &= Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
        lightNavigation &= Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;

        int visibility = /*View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                      | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |*/View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (lightStatus) {
            visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        if (lightNavigation) {
            visibility |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        window.getDecorView().setSystemUiVisibility(visibility);

        setSystemBarColor(context, window, miniAlpha, statusShader, lightStatus, navigationShader, lightNavigation);
    }

    public static void setSystemBarColor(Context context, Window window, boolean miniAlpha,
                                         boolean statusShader, boolean lightStatus,
                                         boolean navigationShader, boolean lightNavigation) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        // statusShader &= Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
        lightStatus &= Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        navigationShader &= Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
        lightNavigation &= Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;

        if (!statusShader) {
            // window.setStatusBarColor(Color.TRANSPARENT);
            // window.setStatusBarColor(Color.argb(1, 0, 0, 0));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // window.setStatusBarColor(getStatusBarColor23(context, lightStatus, miniAlpha));
        } else {
            // window.setStatusBarColor(getStatusBarColor21());
        }
        if (!navigationShader) {
            // window.setNavigationBarColor(Color.TRANSPARENT);
            // window.setNavigationBarColor(Color.argb(1, 0, 0, 0));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //  window.setNavigationBarColor(getStatusBarColor26(context, lightNavigation, miniAlpha));
        } else {
            //  window.setNavigationBarColor(getNavigationBarColor21());
        }
    }

}
