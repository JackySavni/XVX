package com.video.player.videoplayer.xvxvideoplayer.gallery.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class VP_GalleryViewPagerTitleAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> vp_fragments;
    private final ArrayList<String> vp_titles;

    public VP_GalleryViewPagerTitleAdapter(@NonNull FragmentManager fm,
                                           ArrayList<Fragment> vp_fragments,
                                           ArrayList<String> vp_titles) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.vp_fragments = vp_fragments;
        this.vp_titles = vp_titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return vp_fragments.get(position);
    }

    @Override
    public int getCount() {
        return vp_fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return vp_titles.get(position);
    }

}
