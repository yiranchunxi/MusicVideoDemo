package com.siasun.musicvideo.constant;




/**
 * <pre>
 *     @author yangchong
 *     blog  : www.pedaily.cn
 *     time  : 2017/03/22
 *     desc  : 存放常量
 *     revise:
 * </pre>
 */
public class Constant {

    /**----------------------------------同轨迹音视频文件----------------------------------------**/
    public static final String AUDIO_URL = "http://p2modh813.bkt.clouddn.com/sqdx.mp3";
    public static final String DEVIE_URL = "http://p2modh813.bkt.clouddn.com/sqdx.mp4";
    public static String[] AUDIO_LIST = {
            "http://p2modh813.bkt.clouddn.com/sqdx.mp3",
            "http://p2modh813.bkt.clouddn.com/Kalimba.mp3",
            "http://p2modh813.bkt.clouddn.com/Sleep%20Away.mp3",
            "http://p2modh813.bkt.clouddn.com/hah.mp3",
    };





    public static final String SP_NAME = "yc";
    public static final long CLICK_TIME = 500;

    public interface ViewType{
        int TYPE_VIEW = 0;
        int TYPE_BANNER = 1;
        int TYPE_GV = 2;
        int TYPE_TITLE = 3;
        int TYPE_MORE = 4;
        int TYPE_AD = 5;
        int TYPE_LIST2 = 6;
        int TYPE_AD2 = 7 ;
        int TYPE_GV2 = 8;
        int TYPE_LIST3 = 9;
        int TYPE_GV_BOTTOM = 10;
        int TYPE_LIST4 = 11;
    }



    /**--------------------------------------action----------------------------------------------**/
    public static final String EXTRA_NOTIFICATION = "extra_notification";
    public static final String LOCK_SCREEN = "lock_screen";
    public static final String LOCK_SCREEN_ACTION = "cn.ycbjie.lock";

    /**--------------------------------------键--------------------------------------------------**/
    public static final String KEY_IS_LOGIN = "is_login";
    public static final String KEY_FIRST_SPLASH = "first_splash";
    public static final String KEY_NIGHT_STATE = "night_state";
    public static final String FILTER_SIZE = "filter_size";
    public static final String FILTER_TIME = "filter_time";
    public static final String MUSIC_ID = "music_id";
    public static final String PLAY_MODE = "play_mode";
    public static final String IS_SCREEN_LOCK = "is_screen_lock";
    public static final String APP_OPEN_COUNT = "app_open_count";
    public static final String PLAY_POSITION = "play_position";

    /**
     * 网络缓存最大值
     */
    public static final int CACHE_MAXSIZE = 1024 * 1024 * 30;

    /**
     * 网络缓存保存时间
     */
    public static final int TIME_CACHE = 60 * 60; // 一小时

}
