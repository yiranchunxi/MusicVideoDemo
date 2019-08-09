package com.siasun.musicvideo.utils.scan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.siasun.musicvideo.constant.Constant;
import com.siasun.musicvideo.model.bean.AudioBean;
import com.siasun.musicvideo.utils.SPUtils;
import com.siasun.musicvideo.utils.musicUtils.CoverLoader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FileMusicScanManager {

    private static FileMusicScanManager mInstance;
    private static final Object mLock = new Object();

    public static FileMusicScanManager getInstance(){
        if (mInstance == null){
            synchronized (mLock){
                if (mInstance == null){
                    mInstance = new FileMusicScanManager();
                }
            }
        }
        return mInstance;
    }

    /**----------------------------------扫描歌曲------------------------------------------------**/

    private static final String SELECTION = MediaStore.Audio.AudioColumns.SIZE + " >= ? AND " +
            MediaStore.Audio.AudioColumns.DURATION + " >= ?";


    /**
     * 扫描歌曲
     */
    @NonNull
    public List<AudioBean> scanMusic(Context context) {
        List<AudioBean> musicList = new ArrayList<>();
        String mFilterSize = SPUtils.getInstance(Constant.SP_NAME).getString(Constant.FILTER_SIZE,"0");
        String mFilterTime = SPUtils.getInstance(Constant.SP_NAME).getString(Constant.FILTER_TIME,"0");

        long filterSize = parseLong(mFilterSize) * 1024;
        long filterTime = parseLong(mFilterTime) * 1000;

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.AudioColumns.IS_MUSIC,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.ALBUM,
                        MediaStore.Audio.AudioColumns.ALBUM_ID,
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                        MediaStore.Audio.AudioColumns.SIZE,
                        MediaStore.Audio.AudioColumns.DURATION
                },
                SELECTION,
                new String[]{String.valueOf(filterSize), String.valueOf(filterTime)},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            return musicList;
        }

        int i = 0;
        while (cursor.moveToNext()) {
            // 是否为音乐，魅族手机上始终为0
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_MUSIC));
            if (!isFly() && isMusic == 0) {
                continue;
            }

            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
            String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)));
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

            if(fileName.endsWith("mp3")){
                AudioBean music = new AudioBean();
                music.setId(String.valueOf(id));
                music.setType(AudioBean.Type.LOCAL);
                music.setTitle(title);
                music.setArtist(artist);
                music.setAlbum(album);
                music.setAlbumId(albumId);
                music.setDuration(duration);
                music.setPath(path);
                music.setFileName(fileName);
                music.setFileSize(fileSize);
                if (++i <= 20) {
                    // 只加载前20首的缩略图
                    CoverLoader.getInstance().loadThumbnail(music);
                }
                musicList.add(music);
            }


        }
        cursor.close();
        return musicList;
    }


    private long parseLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    private boolean isFly() {
        String flyFlag = getSystemProperty("ro.build.display.id");
        return !TextUtils.isEmpty(flyFlag) && flyFlag.toLowerCase().contains("fly");
    }


    private String getSystemProperty(String key) {
        try {
            @SuppressLint("PrivateApi")
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, key);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }
}
