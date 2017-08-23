package com.nil.code.libs.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 工具类--状态栏设置
 * Created by code_nil on 2017/8/23.
 */

public class StatusUtil {
    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        Context appContext = context.getApplicationContext();
        int result = 0;
        int resourceId =
                appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 对于Lollipop 的设备，只需要在style.xml中设置colorPrimaryDark即可
     * 对于4.4的设备，如下设置padding即可，颜色同样在style.xml中配置
     *
     * @param activity
     * @param view
     * @param color
     */
    public static void setStatusbarColor(Activity activity, View view, int color) {

        //对于Lollipop 的设备，只需要在style.xml中设置colorPrimaryDark即可
        //对于4.4的设备，如下设置padding即可，颜色同样在style.xml中配置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            view.setBackgroundColor(color);
            setStatusbarHeight(activity, view);
        }
    }

    /**
     * 4.4以上 设置状态栏透明
     *
     * @param activity
     */
    public static void setStatusbarTranslucent(Activity activity) {

        setStatusbarColor(activity, 0x00000000);
    }

    public static void setStatusbarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            if (decorViewGroup.findViewById(android.R.id.title) == null) {
                View mStatusBarTintView = new View(activity);
                mStatusBarTintView.setId(android.R.id.title);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
                mStatusBarTintView.setLayoutParams(params);
                mStatusBarTintView.setBackgroundColor(color);
                decorViewGroup.addView(mStatusBarTintView);
            } else {
                decorViewGroup.findViewById(android.R.id.title).setBackgroundColor(color);
            }

        }
    }


    public static void setStatusbarHeight(Activity activity, View view) {
        if (!isKitkat()) return;
        int statusBarHeight = getStatusBarHeight(activity);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = params.height + statusBarHeight;

        view.setLayoutParams(params);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + statusBarHeight, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setStatusbarMargin(Activity activity, View view) {
        if (!isKitkat()) return;
        int statusBarHeight = getStatusBarHeight(activity);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = params.topMargin + statusBarHeight;
        view.setLayoutParams(params);
    }


    private static boolean isKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
