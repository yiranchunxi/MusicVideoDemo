package com.siasun.musicvideo.app.callback;

import android.util.Log;

import cn.ycbjie.ycthreadpoollib.callback.ThreadCallback;

public class LogCallback implements ThreadCallback {

    private final String TAG = "LogCallback";

    @Override
    public void onError(String name, Throwable t) {
        Log.e(TAG, String.format("[任务线程%s]/[回调线程%s]执行失败: %s", name, Thread.currentThread(), t.getMessage()), t);
    }

    @Override
    public void onCompleted(String name) {

        Log.e(TAG, String.format("[任务线程%s]/[回调线程%s]执行完毕：", name, Thread.currentThread()));
    }

    @Override
    public void onStart(String name) {
        Log.e(TAG, String.format("[任务线程%s]/[回调线程%s]执行开始：", name, Thread.currentThread()));
    }

}
