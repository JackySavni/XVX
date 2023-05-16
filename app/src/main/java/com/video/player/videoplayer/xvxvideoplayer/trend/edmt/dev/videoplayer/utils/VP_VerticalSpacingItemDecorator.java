package com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VP_VerticalSpacingItemDecorator extends RecyclerView.ItemDecoration {

    private final int vp_verticalSpaceHeight;

    public VP_VerticalSpacingItemDecorator(int vp_verticalSpaceHeight) {
        this.vp_verticalSpaceHeight = vp_verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        outRect.top = vp_verticalSpaceHeight;
    }
}
