package com.video.player.videoplayer.xvxvideoplayer.model;

import java.util.ArrayList;

public class VP_GamesMainModel {
    public String vp_catName;
    public ArrayList<VP_GameModel> vp_catArraylist;

    public VP_GamesMainModel() {
    }

    public VP_GamesMainModel(String vp_catName, ArrayList<VP_GameModel> vp_catArraylist) {
        this.vp_catName = vp_catName;
        this.vp_catArraylist = vp_catArraylist;
    }

    public String getVp_catName() {
        return vp_catName;
    }

    public void setVp_catName(String vp_catName) {
        this.vp_catName = vp_catName;
    }

    public ArrayList<VP_GameModel> getVp_catArraylist() {
        return vp_catArraylist;
    }

    public void setVp_catArraylist(ArrayList<VP_GameModel> vp_catArraylist) {
        this.vp_catArraylist = vp_catArraylist;
    }
}
