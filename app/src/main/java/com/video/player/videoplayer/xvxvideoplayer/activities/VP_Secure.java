package com.video.player.videoplayer.xvxvideoplayer.activities;

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

public class VP_Secure extends AppCompatActivity {
    public ImageView ex_p1;
    private TextView ex_forgot;
    public ImageView ex_p3;
    public ImageView ex_p2;
    public String ex_passcode;
    public ImageView ex_p4;
    public TextView ex_title;
    public int ex_temp = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vp_activity_secure);
        initView();
    }

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    private void initView() {
        this.ex_title = findViewById(R.id.vp_title);
        this.ex_forgot = findViewById(R.id.forgot);
        ImageView backsecure = findViewById(R.id.back_secure);
        if (VP_MyApplication.vp_getSetPass()) {
            this.ex_title.setText("Enter passcode");
        } else {
            this.ex_title.setText("Enter new passcode");
        }
        if (VP_MyApplication.vp_getSetPass()) {
            this.ex_forgot.setVisibility(0);
        } else {
            this.ex_forgot.setVisibility(8);
        }
        this.ex_forgot.setOnClickListener(view -> VP_Secure.this.startActivityForResult(new Intent(VP_Secure.this, VP_SecurityQuestion.class).putExtra("type", 1), 100));
        backsecure.setOnClickListener(view -> VP_Secure.this.onBackPressed());
        initViewSet();
    }

    @SuppressLint("SetTextI18n")
    private void initViewSet() {
        this.ex_p1 = findViewById(R.id.p1);
        this.ex_p2 = findViewById(R.id.p2);
        this.ex_p3 = findViewById(R.id.p3);
        this.ex_p4 = findViewById(R.id.p4);
        final EditText editText = findViewById(R.id.pass);
        findViewById(R.id.b0).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "0"));
        findViewById(R.id.b1).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "1"));
        findViewById(R.id.b2).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + ExifInterface.GPS_MEASUREMENT_2D));
        findViewById(R.id.b3).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + ExifInterface.GPS_MEASUREMENT_3D));
        findViewById(R.id.b4).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "4"));
        findViewById(R.id.b5).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "5"));
        findViewById(R.id.b6).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "6"));
        findViewById(R.id.b7).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "7"));
        findViewById(R.id.b8).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "8"));
        findViewById(R.id.b9).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "9"));
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @SuppressLint({"WrongConstant", "ShowToast"})
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() == 0) {
                    VP_Secure.this.ex_p1.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                    VP_Secure.this.ex_p2.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                    VP_Secure.this.ex_p3.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                    VP_Secure.this.ex_p4.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                } else if (charSequence.length() == 1) {
                    VP_Secure.this.ex_p1.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    VP_Secure.this.ex_p2.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                    VP_Secure.this.ex_p3.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                    VP_Secure.this.ex_p4.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                } else if (charSequence.length() == 2) {
                    VP_Secure.this.ex_p1.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    VP_Secure.this.ex_p2.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    VP_Secure.this.ex_p3.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                    VP_Secure.this.ex_p4.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                } else if (charSequence.length() == 3) {
                    VP_Secure.this.ex_p1.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    VP_Secure.this.ex_p2.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    VP_Secure.this.ex_p3.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    VP_Secure.this.ex_p4.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_unselected));
                } else if (charSequence.length() == 4) {
                    VP_Secure.this.ex_p1.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    VP_Secure.this.ex_p2.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    VP_Secure.this.ex_p3.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    VP_Secure.this.ex_p4.setImageDrawable(ContextCompat.getDrawable(VP_Secure.this, R.drawable.vp_iv_selected));
                    if (!VP_MyApplication.vp_getSetPass()) {
                        if (VP_Secure.this.ex_temp == 0) {
                            VP_Secure.this.ex_passcode = String.valueOf(charSequence);
                            editText.setText("");
                            VP_Secure.this.ex_temp = 1;
                            VP_Secure.this.ex_title.setText("Enter confirm passcode");
                        } else if (VP_Secure.this.ex_passcode.equals(charSequence.toString())) {
                            VP_MyApplication.vp_putPass(VP_Secure.this.ex_passcode);
                            VP_MyApplication.vp_putSetPass(true);
                            VP_Secure.this.setPasswordSuccess();
                            Toast.makeText(VP_Secure.this, "Passcode set successfully!", 0).show();
                        } else {
                            editText.setText("");
                            VP_Secure.this.ex_temp = 0;
                            VP_Secure.this.ex_title.setText("Enter new passcode");
                            Toast.makeText(VP_Secure.this, "Please confirm password as same!", 0).show();
                        }
                    } else if (VP_MyApplication.vp_getPass().equals(charSequence.toString())) {
                        VP_Secure.this.setPasswordSuccess();
                    } else {
                        editText.setText("");
                        Toast.makeText(VP_Secure.this, "Please enter correct passcode!", 0).show();
                    }
                }
            }
        });
    }

    public void setPasswordSuccess() {
        if (VP_MyApplication.vp_getSetQuestion()) {
            startActivity(new Intent(this, VP_HideVideo.class));
            finish();
            return;
        }
        startActivity(new Intent(this, VP_SecurityQuestion.class).putExtra("type", 0));
        finish();
    }

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 100) {
            if (VP_MyApplication.vp_getSetPass()) {
                this.ex_title.setText("Enter passcode");
            } else {
                this.ex_title.setText("Enter new passcode");
            }
            if (VP_MyApplication.vp_getSetPass()) {
                this.ex_forgot.setVisibility(0);
            } else {
                this.ex_forgot.setVisibility(8);
            }
            this.ex_p1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vp_iv_unselected));
            this.ex_p2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vp_iv_unselected));
            this.ex_p3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vp_iv_unselected));
            this.ex_p4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vp_iv_unselected));
            this.ex_passcode = "";
        }
    }
}
