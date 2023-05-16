package com.video.player.videoplayer.xvxvideoplayer.gallery.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class VP_GalleryBitmapUtils {

    /**
     * @param vp_imgRes
     * @param vp_resources
     * @return bitmap from drawable
     */
    public static Bitmap vp_getBitmap(int vp_imgRes, Resources vp_resources) {
        return BitmapFactory.decodeResource(vp_resources, vp_imgRes);
    }

    /**
     * @param imgPath
     * @return bitmap from a path
     */
    public static Bitmap vp_getBitmap(String imgPath) {
        return BitmapFactory.decodeFile(imgPath);
    }

}
