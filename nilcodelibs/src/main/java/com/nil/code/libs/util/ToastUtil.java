package com.nil.code.libs.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by jniu on 2017/5/17.
 */

public class ToastUtil {

    private Context context;
    private static Toast toast;

    public ToastUtil(Context context) {
        this.context = context;
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public void show(int redId) {
        toast.setText(redId);
        toast.show();
    }

    public void show(CharSequence s) {
        toast.setText(s);
        toast.show();
    }

    public void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static void shortToast(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void longToast(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
