package com.video.player.videoplayer.xvxvideoplayer.model;

public class VP_TransferModel {
    public String appHead, appIcon, appLink, comment, head;

    public VP_TransferModel() {
    }

    public VP_TransferModel(String appHead, String appIcon, String appLink, String comment, String head) {
        this.appHead = appHead;
        this.appIcon = appIcon;
        this.appLink = appLink;
        this.comment = comment;
        this.head = head;
    }

    public String getAppHead() {
        return appHead;
    }

    public void setAppHead(String appHead) {
        this.appHead = appHead;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
