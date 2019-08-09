package com.siasun.musicvideo.api.http;

import com.siasun.musicvideo.model.bean.ArtistInfo;
import com.siasun.musicvideo.model.bean.DownloadInfo;
import com.siasun.musicvideo.model.bean.MusicLrc;
import com.siasun.musicvideo.model.bean.OnlineMusicList;
import com.siasun.musicvideo.model.bean.SearchMusic;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface OnLineMusicApi {


    /**
     * 最新音乐
     */
    @GET("v1/restserver/ting")
    Observable<OnlineMusicList> getList2(@Query("method") String method,
                                         @Query("type") String type,
                                         @Query("size") int size,
                                         @Query("offset") int offset);


    /**
     * 个人详情
     */
    @GET("v1/restserver/ting")
    Observable<ArtistInfo> getArtistInfo(@Query("method") String method,
                                         @Query("tinguid") String tinguid);



    /**
     * 搜索音乐
     */
    @GET("v1/restserver/ting")
    Observable<SearchMusic> startSearchMusic(@Query("method") String method,
                                             @Query("query") String query);


    /**
     * 搜索音乐歌词
     */
    @GET("v1/restserver/ting")
    Observable<MusicLrc> getLrc(@Query("method") String method,
                                @Query("songid") String songid);



    /**
     * 获取歌词信息
     */
    @GET("v1/restserver/ting")
    Observable<OnlineMusicList> getSongListInfo(@Query("method") String method,
                                                @Query("type") String type,
                                                @Query("size") String size,
                                                @Query("offset") String offset);



    /**
     * 获取下载链接
     */
    @GET("v1/restserver/ting")
    Observable<DownloadInfo> getMusicDownloadInfo(@Query("method") String method,
                                                  @Query("songid") String songid);

}
