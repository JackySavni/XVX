package com.video.player.videoplayer.xvxvideoplayer.model;

public class VP_MaintainModel {
    public String comment,head;

    public VP_MaintainModel() {
    }

    public VP_MaintainModel(String comment, String head) {
        this.comment = comment;
        this.head = head;
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
