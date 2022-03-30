package org.po;

import cn.hutool.json.JSONObject;

/**
 * description:
 * author:xiangyang
 */
public class VideoInfo {
    private String videoName;
    private JSONObject videoInfo;
    private String videoBaseUrl;
    private String audioBaseUrl;
    private String videoBaseRange;
    private String audioBaseRange;
    private String videoSize;
    private String audioSize;

    public VideoInfo(String videoName, JSONObject videoInfo, String videoBaseUrl, String audioBaseUrl, String videoBaseRange, String audioBaseRange, String videoSize, String audioSize) {
        this.videoName = videoName;
        this.videoInfo = videoInfo;
        this.videoBaseUrl = videoBaseUrl;
        this.audioBaseUrl = audioBaseUrl;
        this.videoBaseRange = videoBaseRange;
        this.audioBaseRange = audioBaseRange;
        this.videoSize = videoSize;
        this.audioSize = audioSize;
    }

    public VideoInfo() {
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "videoName='" + videoName + '\'' +
                ", videoInfo=" + videoInfo +
                ", videoBaseUrl='" + videoBaseUrl + '\'' +
                ", audioBaseUrl='" + audioBaseUrl + '\'' +
                ", videoBaseRange='" + videoBaseRange + '\'' +
                ", audioBaseRange='" + audioBaseRange + '\'' +
                ", videoSize='" + videoSize + '\'' +
                ", audioSize='" + audioSize + '\'' +
                '}';
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public JSONObject getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(JSONObject videoInfo) {
        this.videoInfo = videoInfo;
    }

    public String getVideoBaseUrl() {
        return videoBaseUrl;
    }

    public void setVideoBaseUrl(String videoBaseUrl) {
        this.videoBaseUrl = videoBaseUrl;
    }

    public String getAudioBaseUrl() {
        return audioBaseUrl;
    }

    public void setAudioBaseUrl(String audioBaseUrl) {
        this.audioBaseUrl = audioBaseUrl;
    }

    public String getVideoBaseRange() {
        return videoBaseRange;
    }

    public void setVideoBaseRange(String videoBaseRange) {
        this.videoBaseRange = videoBaseRange;
    }

    public String getAudioBaseRange() {
        return audioBaseRange;
    }

    public void setAudioBaseRange(String audioBaseRange) {
        this.audioBaseRange = audioBaseRange;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public String getAudioSize() {
        return audioSize;
    }

    public void setAudioSize(String audioSize) {
        this.audioSize = audioSize;
    }
}

