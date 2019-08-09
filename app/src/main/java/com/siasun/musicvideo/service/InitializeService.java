package com.siasun.musicvideo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.siasun.musicvideo.app.BaseApplication;
import com.siasun.musicvideo.constant.BaseConfig;

import java.net.Proxy;

public class InitializeService extends IntentService {
    private static final String ACTION_INIT = "initApplication";

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }


    public InitializeService(){
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }

    private void initApplication() {

        //初始化配置信息
        BaseConfig.INSTANCE.initConfig();
        initDownLoadLib();
    }

    /**
     * 初始化下载库
     */
    private void initDownLoadLib() {
        FileDownloader.setupOnApplicationOnCreate(BaseApplication.getInstance())
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000)
                        .readTimeout(15_000)
                        .proxy(Proxy.NO_PROXY)
                ))
                .commit();
    }
}
