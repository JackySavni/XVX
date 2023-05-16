package com.video.player.videoplayer.xvxvideoplayer.extra;

import java.io.Serializable;

public class MediaData implements Serializable {
    public String name;
    public String path;
    public String uri;
    public String folder;
    public String length;
    public String addedDate;
    public String modifiedDate;
    public int videoDuration;
    public int layoutType;
    public String resolution;
    public int videoLastPlayPosition = 0;

    public MediaData() {
    }

    public MediaData(String name, String path, String uri, String folder, String length, String addedDate, String modifiedDate, int videoDuration, int layoutType, String resolution) {
        this.name = name;
        this.path = path;
        this.uri = uri;
        this.folder = folder;
        this.length = length;
        this.addedDate = addedDate;
        this.modifiedDate = modifiedDate;
        this.videoDuration = videoDuration;
        this.layoutType = layoutType;
        this.resolution = resolution;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getVideoLastPlayPosition() {
        return videoLastPlayPosition;
    }

    public void setVideoLastPlayPosition(int videoLastPlayPosition) {
        this.videoLastPlayPosition = videoLastPlayPosition;
    }

    //    public String getName() {
//        return this.name;
//    }
//
//    public void setName(String str) {
//        this.name = str;
//    }
//
    public String getPath() {
        return this.path;
    }
//
//    public void setPath(String str) {
//        this.path = str;
//    }
//
//    public Uri getUri() {
//        return uri;
//    }
//
//    public void setUri(Uri uri) {
//        this.uri = uri;
//    }
//
    public String getFolder() {
        return this.folder;
    }

    public void setFolder(String str) {
        this.folder = str;
    }
//
//    public String getLength() {
//        return this.length;
//    }
//
//    public void setLength(String str) {
//        this.length = str;
//    }
//
//    public String getAddeddate() {
//        return this.addeddate;
//    }
//
//    public void setAddeddate(String str) {
//        this.addeddate = str;
//    }
//
//    public String getModifieddate() {
//        return this.modifieddate;
//    }
//
//    public void setModifieddate(String str) {
//        this.modifieddate = str;
//    }
//
////    public String getDuration() {
////        return this.duration;
////    }
////
////    public void setDuration(String str) {
////        this.duration = str;
////    }
//
//    public String getResolution() {
//        return this.resolution;
//    }
//
//    public void setResolution(String str) {
//        this.resolution = str;
//    }
//
//    public int getVideoDuration() {
//        return this.videoDuration;
//    }
//
//    public void setVideoDuration(int i) {
//        this.videoDuration = i;
//    }
//
//    public int getLayoutType() {
//        return this.layoutType;
//    }
//
//    public void setLayoutType(int i) {
//        this.layoutType = i;
//    }
//
//    public int getVideoLastPlayPosition() {
//        return this.videoLastPlayPosition;
//    }
//
//    public void setVideoLastPlayPosition(int i) {
//        this.videoLastPlayPosition = i;
//    }
}
