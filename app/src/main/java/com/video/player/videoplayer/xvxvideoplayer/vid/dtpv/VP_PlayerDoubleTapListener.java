package com.video.player.videoplayer.xvxvideoplayer.vid.dtpv;

public interface VP_PlayerDoubleTapListener {

    /**
     * Called when double tapping starts, after double tap gesture
     *
     * @param posX x tap position on the root view
     * @param posY y tap position on the root view
     */
    default void onDoubleTapStarted(float posX, float posY) { }

    /**
     * Called for each ongoing tap (also single tap) (MotionEvent#ACTION_DOWN)
     * when double tap started and still in double tap mode defined
     * by
     *
     * @param posX x tap position on the root view
     * @param posY y tap position on the root view
     */
    default void onDoubleTapProgressDown(float posX, float posY) { }

    /**
     * Called for each ongoing tap (also single tap) (MotionEvent#ACTION_UP}
     * when double tap started and still in double tap mode defined
     * by
     *
     * @param posX x tap position on the root view
     * @param posY y tap position on the root view
     */
    default void onDoubleTapProgressUp(float posX, float posY) { }

    /**
     * Called when  is over
     */
    default void onDoubleTapFinished() { }
}