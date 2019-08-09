package com.siasun.musicvideo.utils.musicUtils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.siasun.musicvideo.R;
import com.siasun.musicvideo.activity.MainActivity;
import com.siasun.musicvideo.constant.Constant;
import com.siasun.musicvideo.model.action.MusicPlayAction;
import com.siasun.musicvideo.model.bean.AudioBean;
import com.siasun.musicvideo.receiver.NotificationStatusBarReceiver;
import com.siasun.musicvideo.service.PlayService;


public class NotificationUtils {


    private PlayService playService;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0x111;

    public static NotificationUtils get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static NotificationUtils instance = new NotificationUtils();
    }

    private NotificationUtils() {

    }

    /**
     * 1.创建一个NotificationManager的引用
     * @param playService           PlayService对象
     */
    public void init(PlayService playService) {
        this.playService = playService;
        notificationManager = (NotificationManager) playService.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("syrb", "zjsy", NotificationManager.IMPORTANCE_MIN);
            channel.setShowBadge(true);
            channel.setLightColor(Color.RED);
            channel.enableLights(true);
            notificationManager.createNotificationChannel(channel);
        }

    }

    /**
     * 开始播放
     * @param music             music
     */
    public void showPlay(AudioBean music) {
        if (music == null) {
            return;
        }
        playService.startForeground(NOTIFICATION_ID, buildNotification(playService, music, true));
        //这个方法是启动Notification到前台
    }


    /**
     * 暂停
     * @param music             music
     */
    public void showPause(AudioBean music) {
        //这个方法是停止Notification
        if (music == null) {
            return;
        }
        playService.stopForeground(false);
        notificationManager.notify(NOTIFICATION_ID, buildNotification(playService, music, false));

    }


    /**
     * 结束所有的
     */
    public void cancelAll() {
        notificationManager.cancelAll();
    }

    private Notification buildNotification(Context context, AudioBean music, boolean isPlaying) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constant.EXTRA_NOTIFICATION, true);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
                //设置通知的图标
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(getCustomViews(context, music, isPlaying))
                //设置状态栏的标题
                //.setTicker("有新消息呢")
                //设置标题
                .setContentTitle("这个是标题2")
                //消息内容
                .setContentText("这个是内容2")
                //在右边显示一个数量,等价于setContentInfo函数.如果有设置setContentInfo函数,那么本函数会给覆盖
                //.setNumber(12)
                //是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
                //.setOnlyAlertOnce(true)
                //滚动条,indeterminate true - 不确定的,不会显示进度,false - 根据max和progress情况显示进度条
                //.setProgress (100, 50, true)
                //设置默认的提示音
                //.setDefaults(Notification.DEFAULT_ALL)
                //设置该通知的优先级
                //.setPriority(Notification.PRIORITY_DEFAULT)
                //让通知左右滑的时候不能取消通知
                .setOngoing(true)
                //设置该通知的优先级
                .setPriority(Notification.PRIORITY_DEFAULT)
                //设置通知时间，默认为系统发出通知的时间，通常不用设置
                //.setWhen(System.currentTimeMillis())
                //打开程序后图标消失
                .setVibrate(null)
                .setVibrate(new long[]{0l})
                .setSound(null)
                .setLights(0,0,0)
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setAutoCancel(false);
        return builder.build();
    }


    /**
     * 设置自定义通知栏布局
     * @param context                   上下文
     * @param music
     * @return                          RemoteViews
     */
    private RemoteViews getCustomViews(Context context, AudioBean music, boolean isPlaying) {
        String title = music.getTitle();
        String subtitle = FileMusicUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum());
        Bitmap cover = CoverLoader.getInstance().loadThumbnail(music);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_player);
        if (cover != null) {
            remoteViews.setImageViewBitmap(R.id.iv_image, cover);
        } else {
            remoteViews.setImageViewResource(R.id.iv_image, R.mipmap.ic_launcher);
        }
        remoteViews.setTextViewText(R.id.tv_title, title);
        remoteViews.setTextViewText(R.id.tv_artist, subtitle);
        if(isPlaying){
            remoteViews.setImageViewResource(R.id.btn_start,R.mipmap.notify_btn_dark_pause_normal);
        }else {
            remoteViews.setImageViewResource(R.id.btn_start,R.mipmap.notify_btn_dark_play_normal);
        }

        // 设置 点击通知栏的上一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_pre, getReceiverPendingIntent(context, MusicPlayAction.TYPE_PRE,1));
        // 设置 点击通知栏的下一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_next, getReceiverPendingIntent(context, MusicPlayAction.TYPE_NEXT,2));
        // 设置 点击通知栏的播放暂停按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_start, getReceiverPendingIntent(context, MusicPlayAction.TYPE_START_PAUSE,3));
        // 设置 点击通知栏的根容器时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.ll_root, getActivityPendingIntent(context));
        return remoteViews;
    }


    private PendingIntent getActivityPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constant.EXTRA_NOTIFICATION, true);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent getReceiverPendingIntent(Context context, String type , int code) {
        Intent intent = new Intent(NotificationStatusBarReceiver.ACTION_STATUS_BAR);
        intent.putExtra(NotificationStatusBarReceiver.EXTRA, type);
        return PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
