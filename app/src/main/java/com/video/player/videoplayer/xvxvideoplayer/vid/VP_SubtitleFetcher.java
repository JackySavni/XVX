package com.video.player.videoplayer.xvxvideoplayer.vid;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.MediaItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class VP_SubtitleFetcher {

    private VP_Player vp_activity;
    private CountDownLatch vp_countDownLatch;
    private final List<Uri> vp_urls;
    private Uri vp_subtitleUri;
    private final List<Uri> vp_foundUrls;

    public VP_SubtitleFetcher(VP_Player vp_activity, List<Uri> vp_urls) {
        this.vp_activity = vp_activity;
        this.vp_urls = vp_urls;
        this.vp_foundUrls = new ArrayList<>();
    }

    public void start() {

        new Thread(() -> {

            OkHttpClient client = new OkHttpClient.Builder()
                    //.callTimeout(15, TimeUnit.SECONDS)
                    .build();

            Callback callback = new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    vp_countDownLatch.countDown();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Uri vp_url = Uri.parse(response.request().url().toString());
                    VP_Utils.vp_log(response.code() + ": " + vp_url);
                    if (response.isSuccessful()) {
                        vp_foundUrls.add(vp_url);
                    }
                    response.close();
                    vp_countDownLatch.countDown();
                }
            };

            vp_countDownLatch = new CountDownLatch(vp_urls.size());

            for (Uri url : vp_urls) {
                // Total Commander 3.24 / LAN plugin 3.20 does not support HTTP HEAD
                //Request request = new Request.Builder().url(url.toString()).head().build();
                if (HttpUrl.parse(url.toString()) == null) {
                    vp_countDownLatch.countDown();
                    continue;
                }
                Request request = new Request.Builder().url(url.toString()).build();
                client.newCall(request).enqueue(callback);
            }

            try {
                vp_countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Uri url : vp_urls) {
                if (vp_foundUrls.contains(url)) {
                    vp_subtitleUri = url;
                    break;
                }
            }

            if (vp_subtitleUri == null) {
                return;
            }

            VP_Utils.vp_log(vp_subtitleUri.toString());

            // ProtocolException when reusing client:
            // java.net.ProtocolException: Unexpected status line: 1
            client = new OkHttpClient.Builder()
                    //.callTimeout(15, TimeUnit.SECONDS)
                    .build();

            Request vp_request = new Request.Builder().url(vp_subtitleUri.toString()).build();
            try (Response vp_response = client.newCall(vp_request).execute()) {
                final ResponseBody vp_responseBody = vp_response.body();

                if (vp_responseBody == null || vp_responseBody.contentLength() > 2_000_000) {
                    return;
                }

                InputStream vp_inputStream = vp_responseBody.byteStream();
                Uri vp_convertedSubtitleUri = VP_SubtitleUtils.vp_convertInputStreamToUTF(vp_activity, vp_subtitleUri, vp_inputStream);

                if (vp_convertedSubtitleUri == null) {
                    return;
                }

                vp_activity.runOnUiThread(() -> {
                    vp_activity.mVPPrefs.vp_updateSubtitle(vp_convertedSubtitleUri);
                    if (VP_Player.vp_player != null) {
                        MediaItem vp_mediaItem = VP_Player.vp_player.getCurrentMediaItem();
                        if (vp_mediaItem != null) {
                            MediaItem.SubtitleConfiguration vp_subtitle = VP_SubtitleUtils.vp_buildSubtitle(vp_activity, vp_convertedSubtitleUri, null, true);
                            vp_mediaItem = vp_mediaItem.buildUpon().setSubtitleConfigurations(Collections.singletonList(vp_subtitle)).build();
                            VP_Player.vp_player.setMediaItem(vp_mediaItem, false);
                            Boolean.parseBoolean("true");
                            Toast.makeText(vp_activity, "Subtitle found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (IOException e) {
                VP_Utils.vp_log(e.toString());
                e.printStackTrace();
            }
        }).start();
    }

}
