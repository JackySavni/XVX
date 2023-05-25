package com.video.player.videoplayer.xvxvideoplayer.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Utils;

import java.io.File;
import java.util.Date;

public class VP_VideoDetailsDialog extends DialogFragment {
    MediaData vp_video;
    private View vp_view;

    public static VP_VideoDetailsDialog getInstance(MediaData media_Data) {
        VP_VideoDetailsDialog VPVideoDetailsDialog = new VP_VideoDetailsDialog();
        VPVideoDetailsDialog.vp_video = media_Data;
        return VPVideoDetailsDialog;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.vp_dialog_video_details, viewGroup, false);
        this.vp_view = inflate;
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view2, Bundle bundle) {
        super.onViewCreated(view2, bundle);
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        TextView vp_videoName = (TextView) this.vp_view.findViewById(R.id.vp_videoName);
        TextView vp_videoPath = (TextView) this.vp_view.findViewById(R.id.vp_videoPath);
        TextView vp_videoModifyDate = (TextView) this.vp_view.findViewById(R.id.vp_videoModifyDate);
        TextView vp_videoResolution = (TextView) this.vp_view.findViewById(R.id.vp_videoResolution);
        TextView vp_videoSize = (TextView) this.vp_view.findViewById(R.id.vp_videoSize);
        TextView vp_videoDuration = (TextView) this.vp_view.findViewById(R.id.vp_videoDuration);
        Button vp_btnOk = (Button) this.vp_view.findViewById(R.id.btnOk);
        vp_videoName.setText(this.vp_video.name);
        vp_videoPath.setText(this.vp_video.path);
        vp_videoModifyDate.setText(DateFormat.format("dd/MM/yyyy", new Date(new File(this.vp_video.path).lastModified())).toString());
        vp_videoSize.setText(VP_Utils.formatSize(Long.parseLong(this.vp_video.length)));
        vp_videoDuration.setText(this.vp_video.videoDuration + "");
        vp_videoResolution.setText(this.vp_video.resolution);
        vp_btnOk.setOnClickListener(view -> VP_VideoDetailsDialog.this.getDialog().dismiss());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
