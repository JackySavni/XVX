package com.video.player.videoplayer.xvxvideoplayer.activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;

import java.io.File;
import java.util.Map;

public class VP_Permission extends AppCompatActivity implements View.OnClickListener {
    private TextView vp_btnAllow;
    private RelativeLayout vp_PermissionLayout;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vp_activity_permission);
        initView();
    }

    private void initView() {
        vp_btnAllow = findViewById(R.id.btnAllow);
        vp_PermissionLayout = findViewById(R.id.ex_permission_layout);
        vp_btnAllow.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnAllow) {
            if (hasAllPermission()) {
                goToHome();
            } else {
                if (shouldShowRequestPermissionRationale()) {
                    Snackbar snackbar = Snackbar
                            .make(vp_PermissionLayout, "Photos and Media Permission is necessary for this app. Without that app can't work.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Grant Permission", v -> multiplePermissionLauncher.launch(permissions()));
                    snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent2));
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.WHITE);
                    TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setLines(3);
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    snackbar.show();
                } else {
                    multiplePermissionLauncher.launch(permissions());
                }
            }
        }
    }

    private final ActivityResultLauncher<String[]> multiplePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            boolean allGranted = true;
            for (String key : result.keySet()) {
                allGranted = allGranted && Boolean.TRUE.equals(result.get(key));
            }
            if (allGranted) {
                goToHome();
            } else {
                if (!shouldShowRequestPermissionRationale()) {
                    Snackbar snackbar = Snackbar
                            .make(vp_PermissionLayout, "Go to settings and allow permissions.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Go to Setting", v -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);
                            });
                    snackbar.setActionTextColor(ContextCompat.getColor(VP_Permission.this, R.color.colorAccent2));
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.WHITE);
                    TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(VP_Permission.this, R.color.colorPrimary));
                    snackbar.show();
                } else {
                    multiplePermissionLauncher.launch(permissions());
                }
            }
        }
    });

    private boolean shouldShowRequestPermissionRationale() {
        boolean shouldShow = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shouldShow = (ActivityCompat.shouldShowRequestPermissionRationale(VP_Permission.this,
                    READ_MEDIA_IMAGES)
                    || ActivityCompat.shouldShowRequestPermissionRationale(VP_Permission.this,
                    READ_MEDIA_AUDIO)
                    || ActivityCompat.shouldShowRequestPermissionRationale(VP_Permission.this,
                    READ_MEDIA_VIDEO));

        } else {
            shouldShow = shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE);
        }
        return shouldShow;
    }

    private boolean hasAllPermission() {
        boolean hasPermissions = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasPermissions = (checkSelfPermission(READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
//                    && checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED);
        } else {
            hasPermissions = checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return hasPermissions;
    }

    public static String[] storage_permissions = {
            READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            READ_MEDIA_IMAGES,
            READ_MEDIA_AUDIO,
//            CAMERA,
            READ_MEDIA_VIDEO

    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = storage_permissions;
        }
        return p;
    }

    private void goToHome() {
        if (!new File(VP_Constant.VP_HIDE_PATH).exists()) {
            new File(VP_Constant.VP_HIDE_PATH).mkdirs();
        }
        startActivity(new Intent(this, VP_Home.class));
        finish();
    }
}
