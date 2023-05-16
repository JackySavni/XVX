package com.video.player.videoplayer.xvxvideoplayer.gallery.view.mainnavigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryPhotoAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryPhotoController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.tasks.VP_GalleryPhotosAsyncTask;

public class VP_GalleryPhotosFragment extends Fragment implements VP_GalleryPhotoAdapter.VP_PhotoListener {
    private RecyclerView vp_recyclerView;
    private VP_GalleryPhotoAdapter vp_adapter;
    private TextView vp_txtTitle;
    private ScaleGestureDetector vp_scaleGestureDetector;
    private Toolbar vp_toolbar;

    public VP_GalleryPhotosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vp_gallery_fragment_photos, container, false);
        vp_init(view);
        vp_setListeners();
        vp_getPhotos();
        vp_setToolbar();

        return view;
    }

    /**
     * set toolbar
     */
    private void vp_setToolbar() {
        Activity vp_activity = getActivity();
        if (vp_activity != null) {
            vp_activity.setActionBar(vp_toolbar);
        }
    }

    /**
     * get all galleryPhotos
     */
    public void vp_getPhotos() {
        new VP_GalleryPhotosAsyncTask(getContext(), vp_adapter).execute();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void vp_setListeners() {
        this.vp_recyclerView.setOnTouchListener((view1, motionEvent) -> {
            vp_scaleGestureDetector.onTouchEvent(motionEvent);
            return false;
        });

    }

    public void vp_init(View view) {
        this.vp_recyclerView = view.findViewById(R.id.vp_recyclerViewPhotos);
        this.vp_adapter = new VP_GalleryPhotoAdapter(getActivity(), true, this);
        this.vp_recyclerView.setAdapter(vp_adapter);
        this.vp_txtTitle = view.findViewById(R.id.vp_txtTitle);
        ConstraintLayout layoutPhotos = view.findViewById(R.id.layoutPhotos);
        this.vp_scaleGestureDetector = new ScaleGestureDetector(getContext(),
                VP_GalleryPhotoController.getScaleGestureDetector(vp_recyclerView, vp_adapter, layoutPhotos));
        this.vp_toolbar = view.findViewById(R.id.vp_toolbar);
    }

    /**
     * @return photo adapter
     */
    public VP_GalleryPhotoAdapter getVp_adapter() {
        return vp_adapter;
    }

    public RecyclerView getRecView() {
        return vp_recyclerView;
    }

    public VP_GalleryPhotoAdapter.VP_PhotoListener getPhotoListener() {
        return this;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void notifyPhotosCount(String title, int photosCount) {
        vp_txtTitle.setText("Photos ∙ " + photosCount);
    }

    @Override
    public void onResume() {
        super.onResume();
        vp_getPhotos();
    }

}