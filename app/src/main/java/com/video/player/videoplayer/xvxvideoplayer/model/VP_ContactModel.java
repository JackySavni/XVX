package com.video.player.videoplayer.xvxvideoplayer.model;

public class VP_ContactModel {
    private String name;
    private int image;

    public VP_ContactModel(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
