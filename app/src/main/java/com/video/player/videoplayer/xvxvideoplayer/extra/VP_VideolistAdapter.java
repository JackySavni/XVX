package com.video.player.videoplayer.xvxvideoplayer.extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.player.videoplayer.xvxvideoplayer.R;

import java.util.ArrayList;

public class VP_VideolistAdapter extends RecyclerView.Adapter<VP_VideolistAdapter.ViewHolder> {
    LayoutInflater vp_inflater;
    Context vp_context;
    OnClickVideo vp_onClickVideo;
    ArrayList<MediaData> _mediaData;

    public interface OnClickVideo {
        void onClickVideo(int i);
    }

    public VP_VideolistAdapter(Context context2, ArrayList<MediaData> arrayList, OnClickVideo onClickVideo2) {
        this.vp_context = context2;
        this._mediaData = arrayList;
        this.vp_inflater = LayoutInflater.from(context2);
        this.vp_onClickVideo = onClickVideo2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.vp_inflater.inflate(R.layout.vp_item_video_list, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.vp_videoTitle.setText(this._mediaData.get(viewHolder.getAbsoluteAdapterPosition()).name);
        viewHolder.vp_duration.setText(this._mediaData.get(viewHolder.getAbsoluteAdapterPosition()).videoDuration + "");
        Glide.with(this.vp_context).load(this._mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path).into(viewHolder.vp_videoThumb);
    }

    @Override
    public int getItemCount() {
        return this._mediaData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView vp_duration;
        ImageView vp_videoThumb;
        TextView vp_videoTitle;

        public ViewHolder(View view) {
            super(view);
            vp_videoThumb = (ImageView) view.findViewById(R.id.vp_video_thumb);
            vp_duration = (TextView) view.findViewById(R.id.vp_duration);
            vp_videoTitle = (TextView) view.findViewById(R.id.vp_video_title);
            view.setOnClickListener(view1 -> VP_VideolistAdapter.this.vp_onClickVideo.onClickVideo(ViewHolder.this.getAbsoluteAdapterPosition()));
        }
    }
}
