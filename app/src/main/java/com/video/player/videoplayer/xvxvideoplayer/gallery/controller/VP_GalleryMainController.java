package com.video.player.videoplayer.xvxvideoplayer.gallery.controller;

import androidx.fragment.app.Fragment;

import com.video.player.videoplayer.xvxvideoplayer.gallery.view.mainnavigation.VP_GalleryAlbumsFragment;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.mainnavigation.VP_GalleryPhotosFragment;

import java.util.ArrayList;

public class VP_GalleryMainController {

    /**
     * get all fragments from main navigation
     *
     * @return
     */
    public static ArrayList<Fragment> ex_getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new VP_GalleryPhotosFragment());
        fragments.add(new VP_GalleryAlbumsFragment());
        return fragments;
    }

    /**
     * get all titles
     *
     * @return
     */
    public static ArrayList<String> ex_getTitles() {
        ArrayList<String> vp_titles = new ArrayList<>();
        vp_titles.add("Photos");
        vp_titles.add("Albums");
        return vp_titles;
    }


}
