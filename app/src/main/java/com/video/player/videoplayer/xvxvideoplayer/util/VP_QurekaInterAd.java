package com.video.player.videoplayer.xvxvideoplayer.util;

import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_MGL_ID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_QurekaID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_isValidContext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.video.player.videoplayer.xvxvideoplayer.R;

import java.util.Random;

public class VP_QurekaInterAd {
    public static ImageView adClose;
    public static boolean isOdd = false;

    public static void showQurekaAd(Activity activity, Intent intent, boolean isTrendAd, boolean isFinish) {
        Dialog qurekaAdDialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        qurekaAdDialog.setContentView(R.layout.vp_row_qureka_layout);
        qurekaAdDialog.setCanceledOnTouchOutside(false);
        qurekaAdDialog.setCancelable(false);

        adClose = qurekaAdDialog.findViewById(R.id.qureka_inter_close_btn);

        RelativeLayout game_inter_ad_layout = qurekaAdDialog.findViewById(R.id.game_inter_ad_layout);

        ImageView game_inter_logo = qurekaAdDialog.findViewById(R.id.game_inter_logo);
        Animation logoAnim = AnimationUtils.loadAnimation(activity, R.anim.vp_zoom_out);
        game_inter_logo.startAnimation(logoAnim);

        ImageView game_inter_mediaView = qurekaAdDialog.findViewById(R.id.game_inter_mediaView);
        Animation mediaViewAnim = AnimationUtils.loadAnimation(activity, R.anim.vp_fade_out);
        game_inter_mediaView.startAnimation(mediaViewAnim);

        ShimmerTextView game_inter_name = qurekaAdDialog.findViewById(R.id.game_inter_name);
        Shimmer shimmer = new Shimmer();
        shimmer.start(game_inter_name);

        TextView game_inter_top_desc = qurekaAdDialog.findViewById(R.id.game_inter_top_desc);

        Random rand = new Random();
        if (isOdd) {
            game_inter_top_desc.setText(R.string.mgl_banner_ttl);
            game_inter_name.setText(R.string.mgl_games);
            game_inter_logo.setImageResource(R.drawable.vp_mgl_logo);
            game_inter_mediaView.setImageResource(MGLSquareImageArray[rand.nextInt(MGLSquareImageArray.length)]);
        } else {
            game_inter_top_desc.setText(R.string.playQuiz);
            game_inter_name.setText(R.string.qureka_lite);
            game_inter_logo.setImageResource(R.drawable.vp_qureka_logo);
            game_inter_mediaView
                    .setImageResource(qurekaSquareImageArray[rand.nextInt(qurekaSquareImageArray.length)]);
        }
        game_inter_ad_layout.setOnClickListener(view -> {
            if (isOdd) {
                VP_ChromeLauncher.vp_launchGame(activity, vp_MGL_ID(activity), "");
            } else {
                VP_ChromeLauncher.vp_launchGame(activity, vp_QurekaID(activity), "");
            }
        });

        adClose.setOnClickListener(v -> {
            vp_generateOddEven();
            if (vp_isValidContext(activity)) {
                if (isTrendAd) {
                    VP_SharePref.vp_adTimerEditorTrendQureka(activity);
                } else {
                    VP_SharePref.vp_adTimerEditorQureka(activity);
                }
                qurekaAdDialog.dismiss();
                activity.startActivity(intent);
                if (isFinish) {
                    activity.finish();
                }
            }
        });
        qurekaAdDialog.show();
    }

    public static int[] qurekaSquareImageArray = {R.drawable.vp_square_banner1
            , R.drawable.vp_square_banner2, R.drawable.vp_square_banner4,
            R.drawable.vp_square_banner5};

    public static int[] MGLSquareImageArray = {R.drawable.vp_mgl_square1
            , R.drawable.vp_mgl_square2, R.drawable.vp_mgl_square3,
            R.drawable.vp_mgl_square4, R.drawable.vp_mgl_square5};

    public static int[] qurekaBigBannerArray = {R.drawable.big_banner1, R.drawable.big_banner2,
            R.drawable.big_banner4, R.drawable.big_banner5, R.drawable.big_banner6
            , R.drawable.big_banner7, R.drawable.big_banner8, R.drawable.big_banner9,
            R.drawable.big_banner10, R.drawable.big_banner11, R.drawable.big_banner12};

    public static void vp_generateOddEven() {
        int qurekaRandomValue;
        Random random = new Random();
        qurekaRandomValue = random.nextInt(20 + 1) + 1;

        isOdd = qurekaRandomValue % 2 != 0;
    }
}
