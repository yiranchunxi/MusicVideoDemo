package com.siasun.musicvideo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.siasun.musicvideo.app.BaseAppHelper;
import com.siasun.musicvideo.model.action.MusicPlayAction;
import com.siasun.musicvideo.service.PlayService;


public class NotificationStatusBarReceiver extends BroadcastReceiver {

    public static final String ACTION_STATUS_BAR = "YC_ACTION_STATUS_BAR";
    public static final String EXTRA = "extra";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        String extra = intent.getStringExtra(EXTRA);
        if (TextUtils.equals(extra, MusicPlayAction.TYPE_NEXT)) {
            PlayService.startCommand(context, MusicPlayAction.TYPE_NEXT);
            Log.e(ACTION_STATUS_BAR,"NotifiyStatusBarReceiver"+"下一首");
        } else if (TextUtils.equals(extra, MusicPlayAction.TYPE_START_PAUSE)) {
            if(BaseAppHelper.get().getPlayService()!=null){
                boolean playing = BaseAppHelper.get().getPlayService().isPlaying();
                if(playing){
                    Log.e(ACTION_STATUS_BAR,"NotifiyStatusBarReceiver"+"暂停");
                }else {
                    Log.e(ACTION_STATUS_BAR,"NotifiyStatusBarReceiver"+"播放");
                }
                PlayService.startCommand(context, MusicPlayAction.TYPE_START_PAUSE);
            }

        }else if(TextUtils.equals(extra, MusicPlayAction.TYPE_PRE)){
            PlayService.startCommand(context, MusicPlayAction.TYPE_PRE);
            Log.e(ACTION_STATUS_BAR,"NotifiyStatusBarReceiver"+"上一首");
        }
    }
}
