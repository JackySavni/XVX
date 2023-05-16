package com.video.player.videoplayer.xvxvideoplayer.vid;

import android.net.Uri;

import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

public class VP_SubtitleFinder {

    private VP_Player vp_activity;
    private Uri vp_baseUri;
    private String vp_path;
    private final List<Uri> vp_urls;

    public VP_SubtitleFinder(VP_Player vp_activity, Uri uri) {
        this.vp_activity = vp_activity;
        vp_path = uri.getPath();
        vp_path = vp_path.substring(0, vp_path.lastIndexOf('.'));
        vp_baseUri = uri;
        vp_urls = new ArrayList<>();
    }

    public static boolean isUriCompatible(Uri vp_uri) {
        String vp_pth = vp_uri.getPath();
        if (vp_pth != null) {
            return vp_pth.lastIndexOf('.') > -1;
        }
        return false;
    }

    private void vp_addLanguage(String vp_lang, String vp_suffix) {
        vp_urls.add(buildUri(vp_lang + "." + vp_suffix));
        vp_urls.add(buildUri(Util.normalizeLanguageCode(vp_lang) + "." + vp_suffix));
    }

    private Uri buildUri(String vp_suffix) {
        final String vp_newPath = vp_path + "." + vp_suffix;
        return vp_baseUri.buildUpon().path(vp_newPath).build();
    }

    public void vp_start() {
        // Prevent IllegalArgumentException in okhttp3.Request.Builder
        if (HttpUrl.parse(vp_baseUri.toString()) == null) {
            return;
        }

        for (String vp_suffix : new String[] { "srt", "ssa", "ass" }) {
            vp_urls.add(buildUri(vp_suffix));
            for (String vp_language : VP_Utils.vp_getDeviceLanguages()) {
                vp_addLanguage(vp_language, vp_suffix);
            }
        }
        vp_urls.add(buildUri("vtt"));

        VP_SubtitleFetcher VPSubtitleFetcher = new VP_SubtitleFetcher(vp_activity, vp_urls);
        VPSubtitleFetcher.start();
    }

}
