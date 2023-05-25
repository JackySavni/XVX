package com.video.player.videoplayer.xvxvideoplayer.util;

import java.text.DecimalFormat;

public class VP_Utils {
    public static String formatSize(long j) {
        if (j <= 0) {
            return "0";
        }
        double vp_d = (double) j;
        int vp_log10 = (int) (Math.log10(vp_d) / Math.log10(1024.0d));
        StringBuilder vp_sb = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double vp_pow = Math.pow(1024.0d, (double) vp_log10);
        Double.isNaN(vp_d);
        Double.isNaN(vp_d);
        vp_sb.append(decimalFormat.format(vp_d / vp_pow));
        vp_sb.append(" ");
        vp_sb.append(new String[]{"B", "KB", "MB", "GB", "TB"}[vp_log10]);
        return vp_sb.toString();
    }
}
