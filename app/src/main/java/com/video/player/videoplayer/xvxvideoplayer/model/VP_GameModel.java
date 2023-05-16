package com.video.player.videoplayer.xvxvideoplayer.model;

import java.util.ArrayList;

public class VP_GameModel {
    public String vp_gameCode, vp_gameTitle, vp_gameThumb;
    public ArrayList<String> vp_gameCat;

    public VP_GameModel() {
    }

    public VP_GameModel(String vp_gameCode, String vp_gameTitle, String vp_gameThumb,
                        ArrayList<String> vp_gameCat) {
        this.vp_gameCode = vp_gameCode;
        this.vp_gameTitle = vp_gameTitle;
        this.vp_gameThumb = vp_gameThumb;
        this.vp_gameCat = vp_gameCat;
    }

    public VP_GameModel(String vp_gameCode, String vp_gameTitle, String vp_gameThumb) {
        this.vp_gameCode = vp_gameCode;
        this.vp_gameTitle = vp_gameTitle;
        this.vp_gameThumb = vp_gameThumb;
    }

    public String getVp_gameCode() {
        return vp_gameCode;
    }

    public void setVp_gameCode(String vp_gameCode) {
        this.vp_gameCode = vp_gameCode;
    }

    public String getVp_gameTitle() {
        return vp_gameTitle;
    }

    public void setVp_gameTitle(String vp_gameTitle) {
        this.vp_gameTitle = vp_gameTitle;
    }

    public String getVp_gameThumb() {
        return vp_gameThumb;
    }

    public void setVp_gameThumb(String vp_gameThumb) {
        this.vp_gameThumb = vp_gameThumb;
    }

    public ArrayList<String> getVp_gameCat() {
        return vp_gameCat;
    }

    public void setVp_gameCat(ArrayList<String> vp_gameCat) {
        this.vp_gameCat = vp_gameCat;
    }
}
