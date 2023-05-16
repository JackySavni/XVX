package com.video.player.videoplayer.xvxvideoplayer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

public class VP_SecurityQuestion extends AppCompatActivity {
    private RelativeLayout ex_root;
    public Spinner ex_questionSpinner;
    private int ex_type;
    public EditText ex_securityAnswer;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vp_activity_securityquestion);
        this.ex_type = getIntent().getIntExtra("type", 0);
        initView();
    }

    private void initView() {
        ex_questionSpinner = findViewById(R.id.questionSpinner);
        TextView saveQuestion = findViewById(R.id.saveQuestion);
        this.ex_securityAnswer = findViewById(R.id.securityAnswer);
        ImageView backquestion = findViewById(R.id.back_question);
        this.ex_root = findViewById(R.id.root);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.vp_spinner_list, getResources().getStringArray(R.array.security_question));
        arrayAdapter.setDropDownViewResource(R.layout.vp_spinner_list);
        this.ex_questionSpinner.setAdapter(arrayAdapter);
        if (this.ex_type == 1) {
            this.ex_questionSpinner.setSelection(Integer.parseInt(VP_MyApplication.vp_getSecurityQuestion()));
            this.ex_questionSpinner.setEnabled(false);
        }
        saveQuestion.setOnClickListener(view -> {
            String obj = VP_SecurityQuestion.this.ex_securityAnswer.getText().toString();
            if (TextUtils.isEmpty(obj)) {
                Toast.makeText(VP_SecurityQuestion.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                return;
            }
            VP_SecurityQuestion.this.hideSoftKeyboard();
            if (!VP_MyApplication.vp_getSetQuestion()) {
                VP_MyApplication.vp_putSetQuestion(true);
                VP_MyApplication.vp_putSecurityQuestion(String.valueOf(VP_SecurityQuestion.this.ex_questionSpinner.getSelectedItemPosition()));
                VP_MyApplication.vp_putAnswerQuestion(obj);
                VP_SecurityQuestion.this.setPasswordSuccess();
            } else if (VP_MyApplication.vp_getAnswerQuestion().equals(obj)) {
                VP_MyApplication.vp_putSetPass(false);
                VP_MyApplication.vp_putPass("");
                VP_SecurityQuestion.this.setResult(-1, new Intent());
                VP_SecurityQuestion.this.finish();
            } else {
                Toast.makeText(VP_SecurityQuestion.this, "Enter correct security answer!", Toast.LENGTH_SHORT).show();
            }
        });
        backquestion.setOnClickListener(view -> VP_SecurityQuestion.this.onBackPressed());
    }

    public void setPasswordSuccess() {
        startActivity(new Intent(this, VP_HideVideo.class));
        finish();
    }

    @SuppressLint("WrongConstant")
    public void hideSoftKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.ex_root.getWindowToken(), 0);
    }
}
