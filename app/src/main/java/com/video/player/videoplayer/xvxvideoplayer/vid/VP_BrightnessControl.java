package com.video.player.videoplayer.xvxvideoplayer.vid;

import android.app.Activity;
import android.view.WindowManager;

class VP_BrightnessControl {

    private final Activity vp_activity;

    public int vp_currentBrightnessLevel = -1;

    public VP_BrightnessControl(Activity vp_activity) {
        this.vp_activity = vp_activity;
    }

    public float getScreenBrightness() {
        return vp_activity.getWindow().getAttributes().screenBrightness;
    }

    public void vp_setScreenBrightness(final float brightness) {
        WindowManager.LayoutParams vp_lp = vp_activity.getWindow().getAttributes();
        vp_lp.screenBrightness = brightness;
        vp_activity.getWindow().setAttributes(vp_lp);
    }

    public void vp_changeBrightness(final VP_CustomStyledPlayerView vp_playerView, final boolean increase, final boolean vp_canSetAuto) {
        int vp_newBrightnessLevel = (increase ? vp_currentBrightnessLevel + 1 : vp_currentBrightnessLevel - 1);

        if (vp_canSetAuto && vp_newBrightnessLevel < 0)
            vp_currentBrightnessLevel = -1;
        else if (vp_newBrightnessLevel >= 0 && vp_newBrightnessLevel <= 30)
            vp_currentBrightnessLevel = vp_newBrightnessLevel;

        if (vp_currentBrightnessLevel == -1 && vp_canSetAuto)
            vp_setScreenBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
        else if (vp_currentBrightnessLevel != -1)
            vp_setScreenBrightness(vp_levelToBrightness(vp_currentBrightnessLevel));

        vp_playerView.vp_setHighlight(false);

        if (vp_currentBrightnessLevel == -1 && vp_canSetAuto) {
            vp_playerView.vp_setIconBrightnessAuto();
            vp_playerView.setCustomErrorMessage("");
        } else {
            vp_playerView.vp_setIconBrightness();
            vp_playerView.setCustomErrorMessage(" " + vp_currentBrightnessLevel);
        }
    }

    float vp_levelToBrightness(final int level) {
        final double vp_d = 0.064 + 0.936 / (double) 30 * (double) level;
        return (float) (vp_d * vp_d);
    }
}
