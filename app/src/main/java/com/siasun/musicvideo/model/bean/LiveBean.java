package com.siasun.musicvideo.model.bean;

public class LiveBean {

    private int  coverUrl;
    private String  liveTitle;
    private String  likeCount;
    private String  commentCount;
    private String  seeCount;
    private String  livedTime;


    public int getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(int coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getSeeCount() {
        return seeCount;
    }

    public void setSeeCount(String seeCount) {
        this.seeCount = seeCount;
    }

    public String getLivedTime() {
        return livedTime;
    }

    public void setLivedTime(String livedTime) {
        this.livedTime = livedTime;
    }
}
