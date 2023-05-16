package com.video.player.videoplayer.xvxvideoplayer.trend;

import static com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options.VP_GamingActivity.vp_ShowAppOpen;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_AppOpenIntent;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_TrendChangeAct;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_intent1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showTrendInterAd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

public class VP_TrendSecure extends AppCompatActivity {
    public ImageView vp_p1;
    private TextView vp_forgot;
    public ImageView vp_p3;
    public ImageView vp_p2;
    public String vp_passcode;
    public ImageView vp_p4;
    public TextView vp_title;
    public int vp_temp = 0;
    private EditText vp_editText;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vp_activity_secure);
        initView();
    }

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    private void initView() {
        this.vp_title = findViewById(R.id.vp_title);
        this.vp_forgot = findViewById(R.id.forgot);
        ImageView backsecure = findViewById(R.id.back_secure);
        if (VP_MyApplication.vp_getTrendSetPass()) {
            this.vp_title.setText("Enter passcode");
        } else {
            this.vp_title.setText("Enter new passcode");
        }
        if (VP_MyApplication.vp_getTrendSetPass()) {
            this.vp_forgot.setVisibility(0);
        } else {
            this.vp_forgot.setVisibility(8);
        }
        this.vp_forgot.setOnClickListener(view -> startActivityForResult(new Intent(VP_TrendSecure.this, VP_TrendSecurityQuestion.class).putExtra("type", 1), 100));
        backsecure.setOnClickListener(view -> onBackPressed());
        initViewSet();
        findViewById(R.id.bx).setOnClickListener(v -> vp_editText.setText(""));
    }

    private void initViewSet() {
        this.vp_p1 = findViewById(R.id.p1);
        this.vp_p2 = findViewById(R.id.p2);
        this.vp_p3 = findViewById(R.id.p3);
        this.vp_p4 = findViewById(R.id.p4);
        vp_editText = findViewById(R.id.pass);
        findViewById(R.id.b0).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + "0"));
        findViewById(R.id.b1).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + "1"));
        findViewById(R.id.b2).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + ExifInterface.GPS_MEASUREMENT_2D));
        findViewById(R.id.b3).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + ExifInterface.GPS_MEASUREMENT_3D));
        findViewById(R.id.b4).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + "4"));
        findViewById(R.id.b5).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + "5"));
        findViewById(R.id.b6).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + "6"));
        findViewById(R.id.b7).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + "7"));
        findViewById(R.id.b8).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + "8"));
        findViewById(R.id.b9).setOnClickListener(view -> vp_editText.setText(vp_editText.getText().toString().trim() + "9"));
        vp_editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @SuppressLint("WrongConstant")
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() == 0) {
                    vp_p1.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                    vp_p2.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                    vp_p3.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                    vp_p4.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                } else if (charSequence.length() == 1) {
                    vp_p1.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    vp_p2.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                    vp_p3.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                    vp_p4.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                } else if (charSequence.length() == 2) {
                    vp_p1.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    vp_p2.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    vp_p3.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                    vp_p4.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                } else if (charSequence.length() == 3) {
                    vp_p1.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    vp_p2.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    vp_p3.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    vp_p4.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_unselected));
                } else if (charSequence.length() == 4) {
                    vp_p1.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    vp_p2.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    vp_p3.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    vp_p4.setImageDrawable(ContextCompat.getDrawable(VP_TrendSecure.this, R.drawable.vp_iv_selected));
                    if (!VP_MyApplication.vp_getTrendSetPass()) {
                        if (vp_temp == 0) {
                            vp_passcode = String.valueOf(charSequence);
                            vp_editText.setText("");
                            vp_temp = 1;
                            vp_title.setText("Enter confirm passcode");
                        } else if (vp_passcode.equals(charSequence.toString())) {
                            VP_MyApplication.vp_putTrendPass(vp_passcode);
                            VP_MyApplication.vp_putTrendSetPass(true);
                            setPasswordSuccess();
                            Toast.makeText(VP_TrendSecure.this, "Passcode set successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            vp_editText.setText("");
                            vp_temp = 0;
                            vp_title.setText("Enter new passcode");
                            Toast.makeText(VP_TrendSecure.this, "Please confirm password as same!", Toast.LENGTH_SHORT).show();
                        }
                    } else if (VP_MyApplication.vp_getTrendPass().equals(charSequence.toString())) {
                        setPasswordSuccess();
                    } else {
                        vp_editText.setText("");
                        Toast.makeText(VP_TrendSecure.this, "Please enter correct passcode!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void setPasswordSuccess() {
        if (vp_editText != null) {
            vp_editText.setText("");
        }
        if (VP_MyApplication.vp_getTrendSetQuestion()) {
            vp_showTrendInterAd(this, new Intent(this, VP_TrendVidCats.class), true);
            return;
        }
        vp_showTrendInterAd(this, new Intent(this, VP_TrendSecurityQuestion.class)
                .putExtra("type", 0), true);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 100) {
            if (VP_MyApplication.vp_getTrendSetPass()) {
                this.vp_title.setText("Enter passcode");
            } else {
                this.vp_title.setText("Enter new passcode");
            }
            if (VP_MyApplication.vp_getTrendSetPass()) {
                this.vp_forgot.setVisibility(0);
            } else {
                this.vp_forgot.setVisibility(8);
            }
            this.vp_p1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vp_iv_unselected));
            this.vp_p2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vp_iv_unselected));
            this.vp_p3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vp_iv_unselected));
            this.vp_p4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vp_iv_unselected));
            this.vp_passcode = "";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        vp_ShowAppOpen = true;
        if (!vp_AppOpenIntent(this) && vp_TrendChangeAct) {
            vp_TrendChangeAct = false;
            startActivity(vp_intent1);
            finish();
        }
    }

}