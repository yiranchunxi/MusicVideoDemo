package com.siasun.musicvideo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.siasun.musicvideo.model.action.MusicPlayAction;
import com.siasun.musicvideo.service.PlayService;

public class AudioEarPhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if(action!=null && action.length()>0){
            switch (action){
                //来电/耳机拔出时暂停播放
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                    PlayService.startCommand(context, MusicPlayAction.TYPE_START_PAUSE);
                    break;
                default:
                    break;
            }
        }
    }
}
