package com.video.player.videoplayer.xvxvideoplayer.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryEmojiListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder.VP_EmojiViewHolder;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditor;

public class VP_GalleryEmojiAdapter extends RecyclerView.Adapter<VP_EmojiViewHolder> {

    private final VP_GalleryEmojiListener VP_galleryEmojiListener;
    private final ArrayList<String> vp_emojis;

    public VP_GalleryEmojiAdapter(VP_GalleryEmojiListener VP_galleryEmojiListener, Context context) {
        this.VP_galleryEmojiListener = VP_galleryEmojiListener;
        this.vp_emojis = emojiList(context);
    }

    private ArrayList<String> emojiList(Context context) {
        ArrayList<String> convertedEmojiList = new ArrayList<>();
        int[] emojiUnicodeArray = context.getResources().getIntArray(R.array.photo_editor_emoji);

        for (int emoji : emojiUnicodeArray) {
            convertedEmojiList.add(new String(Character.toChars(emoji)));
        }
        return convertedEmojiList;
    }
    @NonNull
    @Override
    public VP_EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vp_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vp_gallery_card_emoji, parent, false);
        return new VP_EmojiViewHolder(vp_view);
    }

    @Override
    public void onBindViewHolder(@NonNull VP_EmojiViewHolder holder, int position) {
        //get emoji from list
        String vp_emoji = vp_emojis.get(position);
        //get view from holder
        TextView vp_txtEmoji = holder.vp_txtEmoji;
        if (vp_emoji != null && vp_txtEmoji != null) {
            //set emoji
            vp_txtEmoji.setText(vp_emoji);

            holder.itemView.setOnClickListener(v -> {
                //notify emoji clicked
                if (VP_galleryEmojiListener != null) {
                    VP_galleryEmojiListener.onEmojiClicked(vp_emoji);
                }
            });
        }
    }

    /**
     * @return number of emojis
     */
    @Override
    public int getItemCount() {
        return vp_emojis.size();
    }


}
