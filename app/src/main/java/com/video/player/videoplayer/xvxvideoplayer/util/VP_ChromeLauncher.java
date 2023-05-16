package com.video.player.videoplayer.xvxvideoplayer.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.core.content.ContextCompat;

import com.video.player.videoplayer.xvxvideoplayer.R;

public class VP_ChromeLauncher {
    public static void vp_launchGame(Activity context, String gameType, String gameCode) {
        String vp_gameUrl = "";

        switch (gameType) {
            case "noCash":
                vp_gameUrl = "https://www.gamezop.com/g/" + gameCode + "?id=" + VP_SharePref.vp_GameZopID(context);
                break;
            default:
                vp_gameUrl = gameType;
                break;
        }

        if (vp_isChromeCustomTabsSupported(context)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setUrlBarHidingEnabled(true);
            CustomTabColorSchemeParams vp_params = new CustomTabColorSchemeParams.Builder()
                    .setNavigationBarColor(ContextCompat.getColor(context, R.color.app_background))
                    .setToolbarColor(ContextCompat.getColor(context, R.color.app_background))
                    .setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.app_background))
                    .build();
            builder.setDefaultColorSchemeParams(vp_params);
            CustomTabsIntent vp_customTabsIntent = builder.build();
            String PACKAGE_NAME = "com.android.chrome";
            vp_customTabsIntent.intent.setData(Uri.parse(vp_gameUrl));
            vp_customTabsIntent.intent.setPackage(PACKAGE_NAME);
            try {
                vp_customTabsIntent.launchUrl(context, Uri.parse(vp_gameUrl));
            } catch (ActivityNotFoundException ignored) {
            }
        }
    }

    public static boolean vp_isChromeCustomTabsSupported(@NonNull final Context context) {
        Intent vp_serviceIntent = new Intent("android.support.customtabs.action.CustomTabsService");
        vp_serviceIntent.setPackage("com.android.chrome");

        CustomTabsServiceConnection vp_serviceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(final ComponentName componentName,
                                                     final CustomTabsClient customTabsClient) {
            }

            @Override
            public void onServiceDisconnected(final ComponentName name) {

            }
        };

        boolean vp_customTabsSupported =
                context.bindService(vp_serviceIntent, vp_serviceConnection,
                        Context.BIND_AUTO_CREATE | Context.BIND_WAIVE_PRIORITY);
        context.unbindService(vp_serviceConnection);

        return vp_customTabsSupported;
    }

}
