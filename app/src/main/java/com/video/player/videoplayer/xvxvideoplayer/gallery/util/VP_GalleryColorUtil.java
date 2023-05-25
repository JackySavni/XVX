package com.video.player.videoplayer.xvxvideoplayer.gallery.util;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import java.util.Random;

public class VP_GalleryColorUtil {

    /**
     * @return random color
     */
    public static int getRandomColor() {
        Random random = new Random();
        return Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));
    }

    /**
     * @return rainbow gradient
     */
    public static GradientDrawable vp_getRainbowGradient() {
        return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN,
                        Color.GREEN, Color.YELLOW, Color.RED});
    }

}
