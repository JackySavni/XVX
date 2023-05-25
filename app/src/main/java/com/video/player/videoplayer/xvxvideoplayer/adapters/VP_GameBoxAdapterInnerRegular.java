package com.video.player.videoplayer.xvxvideoplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options.VP_GamingActivity;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_GameModel;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;

import java.util.ArrayList;

public class VP_GameBoxAdapterInnerRegular extends RecyclerView.Adapter<VP_GameBoxAdapterInnerRegular.MyViewHolder> {
    Activity vp_context;
    private ArrayList<VP_GameModel> vp_arrayList;

    public VP_GameBoxAdapterInnerRegular(Activity vp_context, ArrayList<VP_GameModel> vp_arrayList) {
        this.vp_context = vp_context;
        this.vp_arrayList = vp_arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(vp_context).inflate(R.layout.vp_games_raw_layout_regular, parent,
                false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final VP_GameModel model = vp_arrayList.get(position);
        ItemViewHolder itemViewHolder =
                (ItemViewHolder) holder;
        itemViewHolder.vp_gameTitle.setText(model.getVp_gameTitle());
        itemViewHolder.vp_gameTitle.setSelected(true);
        itemViewHolder.setThumb(model.getVp_gameThumb());

        itemViewHolder.vp_mView.setOnClickListener(v -> {
            VP_GamingActivity.vp_ShowAppOpen = false;
            VP_ChromeLauncher.vp_launchGame(vp_context, "noCash", model.getVp_gameCode());
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends MyViewHolder {
        ImageView vp_gameThumb;
        TextView vp_gameTitle;
        View vp_mView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            vp_mView = itemView;
            vp_gameThumb = vp_mView.findViewById(R.id.vp_game_thumb);
            vp_gameTitle = vp_mView.findViewById(R.id.vp_game_title);
        }

        public void setThumb(String gameThumb) {
            ImageView thumb_image = (ImageView) vp_mView.findViewById(R.id.vp_game_thumb);
            Picasso.get().load(gameThumb).into(thumb_image);
        }
    }
}

