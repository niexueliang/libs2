package com.nil.code.libs.util;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by code_nil on 2017/8/23.
 * 这是一个关于输入法的工具类。
 */

public class KeybordUtil {


    /**
     * 强制弹出输入法窗口并与某个EditText关联
     *
     * @param view 与输入法关联的控件
     */
    public static void open(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭弹出的输入法窗口
     *
     * @param view 任意控件
     */
    public static void close(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * EditText失焦之后，自动关闭软键盘。该方法需要重写Activity中的onTouchEvent的ACTION_DOWN方法。
     *
     * @param acton     触摸事件
     * @param mActivity 上下文
     */
    public static void autoClose(int acton, Activity mActivity) {
        if (acton == MotionEvent.ACTION_DOWN) {
            //判定是否有控件获取到焦点
            View view = mActivity.getCurrentFocus();
            if (view != null) {
                //判定是否有为该控件撑起的窗体令牌，这里指的是软键盘
                if (mActivity.getCurrentFocus().getWindowToken() != null) {
                    //关闭软键盘
                    close(view);
                }
            }
        }
    }
}
