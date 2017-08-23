package com.nil.code.libs.util;

import android.os.CountDownTimer;

/**
 * CountDownTimer是Android官方提供的倒计时类
 * 现在一般用rxjava来做
 */
public class CountDownTimerUtil extends CountDownTimer {


    CallBack callBack;
    /**
     * @param millisInFuture  timer执行总计时长
     * @param countDownInterval  多少周期执行一次
     */
    public CountDownTimerUtil(long millisInFuture, long countDownInterval, CallBack callBack) {
        super(millisInFuture, countDownInterval);
        this.callBack=callBack;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        callBack.sendMessage(millisUntilFinished/1000+"秒后重发!");
    }

    @Override
    public void onFinish() {
        callBack.sendMessage("finish");
    }

    public interface CallBack{
        public void sendMessage(String msg);
    }
}
