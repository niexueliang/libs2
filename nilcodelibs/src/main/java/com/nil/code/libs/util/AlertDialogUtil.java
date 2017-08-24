package com.nil.code.libs.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Mwh on 2017/8/23.
 * 展示自定义的dialog
 */

public class AlertDialogUtil {
    AlertDialog alertDialog;
    float density;

    public AlertDialogUtil(Activity activity, View view) {
        alertDialog = new AlertDialog.Builder(activity).setView(view).create();
        density = activity.getResources().getDisplayMetrics().density;
    }

    /**
     * 设置背景色
     * 当要设置背景为透明时，设置背景色位Color.TRANSPARENT
     */
    public AlertDialogUtil setCancleBackgroundDrawable(int color) {
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(color));
        return this;
    }

    /**
     * 设置dialog的背景
     */
    public AlertDialogUtil setCancleBackgroundDrawable(Drawable drawable) {
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        return this;
    }

    public AlertDialogUtil setCanceledOnTouchOutside(boolean isCancle) {
        alertDialog.setCanceledOnTouchOutside(isCancle);
        return this;
    }



    public AlertDialogUtil showPostion(int width, int height,int y) {
        //需要首先显示不然将会无法设置布局大小
        alertDialog.show();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        //(int) (density * width)
        layoutParams.width = (int) (density * width);
        layoutParams.height = (int) (density * height);
        //设置布局偏移
        layoutParams.y = y;
        window.setAttributes(layoutParams);
        return this;
    }

    public void show() {
        if (alertDialog != null) {
            alertDialog.show();
        }
    }

    public void dismiss() {
        if (alertDialog != null) {
            alertDialog.cancel();
        }
    }
}
