package com.video.player.videoplayer.xvxvideoplayer.gallery.view.mainnavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryAlbumsAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.tasks.VP_GalleryAlbumsAsyncTask;

public class VP_GalleryAlbumsFragment extends Fragment {

    private RecyclerView vp_recyclerView;
    private VP_GalleryAlbumsAdapter vp_adapter;
    private ScaleGestureDetector vp_scaleGestureDetector;
    private ConstraintLayout vp_layoutPhotos;
    private int vp_spanCount = 3;
    private TextView vp_txtTitle;

    public VP_GalleryAlbumsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vp_gallery_fragment_albums, container, false);
        vp_init(view);
        vp_setListeners();
        //set title
        vp_txtTitle.setText("Albums");
//        getAlbums();
        return view;
    }

    /**
     * get albums from directory pictures & DCIM
     */
    private void getAlbums() {
        new VP_GalleryAlbumsAsyncTask(getContext(), vp_adapter).execute();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void vp_setListeners() {
        this.vp_recyclerView.setOnTouchListener((view1, motionEvent) -> {
            vp_scaleGestureDetector.onTouchEvent(motionEvent);
            return false;
        });
    }

    private void vp_init(View view) {
        this.vp_recyclerView = view.findViewById(R.id.vp_recyclerViewAlbums);
        this.vp_adapter = new VP_GalleryAlbumsAdapter(getActivity());
        this.vp_recyclerView.setAdapter(vp_adapter);
        this.vp_scaleGestureDetector = new ScaleGestureDetector(getContext(), onScaleGestureListener);
        this.vp_layoutPhotos = view.findViewById(R.id.layoutAlbums);
        this.vp_txtTitle = view.findViewById(R.id.vp_txtTitle);
    }

    private final ScaleGestureDetector.SimpleOnScaleGestureListener onScaleGestureListener =
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScaleBegin(ScaleGestureDetector detector) {
                    vp_recyclerView.stopScroll();
                    return super.onScaleBegin(detector);
                }

                @Override
                public boolean onScale(ScaleGestureDetector detector) {

                    //detect current span and set new span
                    if (detector.getCurrentSpan() > 200 && detector.getTimeDelta() > 200) {
                        if (detector.getCurrentSpan() - detector.getPreviousSpan() < -1) {
                            if (vp_spanCount == 3) {
                                vp_spanCount = 2;
                                vp_adapter.showLinearManager(false);
                                vp_setLayoutManager(vp_spanCount, vp_recyclerView, getContext(), vp_adapter);
                                return true;
                            } else if (vp_spanCount == 2) {
                                vp_spanCount = 1;
                                vp_adapter.showLinearManager(true);
                                vp_setLayoutManager(vp_spanCount, vp_recyclerView, getContext(), vp_adapter);
                                return true;
                            }

                        } else if (detector.getCurrentSpan() - detector.getPreviousSpan() > 1) {

                            if (vp_spanCount == 1) {
                                vp_spanCount = 2;
                                vp_adapter.showLinearManager(false);
                                vp_setLayoutManager(vp_spanCount, vp_recyclerView, getContext(), vp_adapter);
                                return true;
                            } else if (vp_spanCount == 2) {
                                vp_spanCount = 3;
                                vp_adapter.showLinearManager(false);
                                vp_setLayoutManager(vp_spanCount, vp_recyclerView, getContext(), vp_adapter);
                                return true;
                            }
                        }

                    }
                    return false;
                }

            };

    /**
     * * start transition
     * * change layout manager depending of the span count
     * * notify adapter
     *
     * @param vp_count
     * @param vp_recyclerView
     * @param vp_context
     * @param adapter
     */
    public void vp_setLayoutManager(int vp_count, RecyclerView vp_recyclerView, Context vp_context, VP_GalleryAlbumsAdapter adapter) {
        vp_recyclerView.post(() -> {
            try {
                if (vp_spanCount == 1) {
                    TransitionManager.beginDelayedTransition(vp_recyclerView);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(vp_context, RecyclerView.VERTICAL, false);
                    vp_recyclerView.setLayoutManager(linearLayoutManager);
                } else {
                    TransitionManager.beginDelayedTransition(vp_layoutPhotos);
                    GridLayoutManager manager = new GridLayoutManager(getContext(), vp_count);
                    vp_recyclerView.setLayoutManager(manager);
                }
                adapter.notifyDataSetChanged();
            } catch (NullPointerException e) {
                e.getMessage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getAlbums();
    }

}