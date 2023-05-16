package com.video.player.videoplayer.xvxvideoplayer.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.activities.VP_HideVideo;
import com.video.player.videoplayer.xvxvideoplayer.database.VP_Database;
import com.video.player.videoplayer.xvxvideoplayer.dialog.VP_VideoDetailsDialog;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Utils;
import com.video.player.videoplayer.xvxvideoplayer.vid.VP_Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class VP_HideAdapter extends RecyclerView.Adapter<VP_HideAdapter.ViewHolder> {
    FragmentActivity ex_activity;
    public final VP_Database VPDatabase;
    int i;
    LayoutInflater ex_inflater;
    ArrayList<MediaData> ex_mediaData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ex_duration;
        TextView ex_videoDate;
        ImageView ex_videoOption;
        TextView ex_videoSize;
        ImageView ex_videoThumb;
        TextView ex_videoTitle;

        public ViewHolder(View view) {
            super(view);
            this.ex_videoTitle = view.findViewById(R.id.vp_video_title);
            this.ex_videoDate = view.findViewById(R.id.vp_video_date);
            this.ex_videoSize = view.findViewById(R.id.vp_video_size);
            this.ex_duration = view.findViewById(R.id.vp_duration);
            this.ex_videoThumb = view.findViewById(R.id.vp_video_thumb);
            this.ex_videoOption = view.findViewById(R.id.vp_video_option);
        }
    }

    public VP_HideAdapter(FragmentActivity fragmentActivity, ArrayList<MediaData> arrayList, int i2) {
        this.ex_activity = fragmentActivity;
        this.ex_mediaData = arrayList;
        this.i = i2;
        this.VPDatabase = new VP_Database(fragmentActivity);
        this.ex_inflater = LayoutInflater.from(fragmentActivity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i2) {
        View view;
        if (this.i == 0) {
            view = this.ex_inflater.inflate(R.layout.vp_item_video_liner, viewGroup, false);
        } else {
            view = this.ex_inflater.inflate(R.layout.vp_item_video_grid, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i2) {
        if (this.i == 1) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.ex_activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
        Glide.with(this.ex_activity).load(this.ex_mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path).into(viewHolder.ex_videoThumb);
        viewHolder.ex_videoTitle.setText(this.ex_mediaData.get(viewHolder.getAbsoluteAdapterPosition()).name);
        viewHolder.ex_videoSize.setText(VP_Utils.formatSize(Long.parseLong(this.ex_mediaData.get(viewHolder.getAbsoluteAdapterPosition()).length)));
        viewHolder.ex_duration.setText(this.ex_mediaData.get(viewHolder.getAbsoluteAdapterPosition()).videoDuration + "");
        viewHolder.ex_videoDate.setText(DateFormat.format("dd/MM/yyyy", new Date(new File(this.ex_mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path).lastModified())).toString());
        viewHolder.ex_videoOption.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(VP_HideAdapter.this.ex_activity, view);
            popupMenu.inflate(R.menu.vp_video_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint({"ResourceType", "NonConstantResourceId"})
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.vp_menu_v_delete:
                            AlertDialog.Builder builder = new AlertDialog.Builder(VP_HideAdapter.this.ex_activity);
                            builder.setTitle("Delete Video");
                            builder.setMessage("Are you sure you have to Delete " + VP_HideAdapter.this.ex_mediaData.get(viewHolder.getAbsoluteAdapterPosition()).name + " ?");
                            builder.setPositiveButton(17039379, (dialogInterface, i) -> {
                                File file = new File(VP_HideAdapter.this.ex_mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path);
                                if (file.exists() && file.delete()) {
                                    VP_HideAdapter.this.ex_mediaData.remove(viewHolder.getAbsoluteAdapterPosition());
                                    VP_HideAdapter.this.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
                                    if (VP_HideAdapter.this.ex_mediaData.size() == 0) {
                                        VP_HideVideo.ex_ivNoData.setVisibility(0);
                                        VP_HideVideo.ex_hideRecycler.setVisibility(8);
                                    }
                                }
                            });
                            builder.setNegativeButton(17039369, (dialogInterface, i) -> dialogInterface.cancel());
                            builder.show();
                            return false;
                        case R.id.vp_menu_v_details:
                            VP_VideoDetailsDialog.getInstance(VP_HideAdapter.this.ex_mediaData.get(viewHolder.getAbsoluteAdapterPosition())).show(VP_HideAdapter.this.ex_activity.getSupportFragmentManager(), "");
                            return false;
//                        case R.id.menu_v_hide:
//                            AlertDialog.Builder builder2 = new AlertDialog.Builder(HideAdapter.this.activity);
//                            builder2.setTitle("Unhide Video");
//                            builder2.setMessage("Are you sure you have to Unhide " + HideAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).name + " ?");
//                            builder2.setPositiveButton(17039379, (dialogInterface, i) -> {
//                                HideData hideData = HideAdapter.this.database.getHideData(HideAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).name);
//                                String parent = new File(HideAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path).getParent();
//                                HideAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path.substring(HideAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path.lastIndexOf("."));
//                                File file = new File(parent, HideAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path.substring(HideAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path.lastIndexOf("/") + 1));
//                                if (hideData != null) {
//                                    File file2 = new File(hideData.getPath(), HideAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).name);
//                                    if (file.renameTo(file2)) {
//                                        HideAdapter.this.database.deleteHide(hideData.getName());
//                                        HideAdapter.this.mediadatas.remove(viewHolder.getAbsoluteAdapterPosition());
//                                        HideAdapter.this.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
//                                        if (HideAdapter.this.mediadatas.size() == 0) {
//                                            HidevideoActivity.ivnodata.setVisibility(0);
//                                            HidevideoActivity.hiderecycler.setVisibility(8);
//                                        }
//                                        try {
//                                            if (Build.VERSION.SDK_INT >= 19) {
//                                                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//                                                intent.setData(Uri.fromFile(file2));
//                                                HideAdapter.this.activity.sendBroadcast(intent);
//                                                EventBus event_Bus = new EventBus();
//                                                event_Bus.setType(1);
//                                                event_Bus.setValue(0);
//                                                org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
//                                                return;
//                                            }
//                                            HideAdapter.this.activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(file2)));
//                                            EventBus event_Bus2 = new EventBus();
//                                            event_Bus2.setType(1);
//                                            event_Bus2.setValue(0);
//                                            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus2);
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                } else {
//                                    if (!new File(Constant.YOUR_PATH).exists()) {
//                                        new File(Constant.YOUR_PATH).mkdirs();
//                                    }
//                                    File file3 = new File(Constant.YOUR_PATH, HideAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).name);
//                                    if (file.renameTo(file3)) {
//                                        HideAdapter.this.mediadatas.remove(viewHolder.getAbsoluteAdapterPosition());
//                                        HideAdapter.this.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
//                                        if (HideAdapter.this.mediadatas.size() == 0) {
//                                            HidevideoActivity.ivnodata.setVisibility(0);
//                                            HidevideoActivity.hiderecycler.setVisibility(8);
//                                        }
//                                        try {
//                                            if (Build.VERSION.SDK_INT >= 19) {
//                                                Intent intent2 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//                                                intent2.setData(Uri.fromFile(file3));
//                                                HideAdapter.this.activity.sendBroadcast(intent2);
//                                                EventBus event_Bus3 = new EventBus();
//                                                event_Bus3.setType(1);
//                                                event_Bus3.setValue(0);
//                                                org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus3);
//                                                return;
//                                            }
//                                            HideAdapter.this.activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(file3)));
//                                            EventBus event_Bus4 = new EventBus();
//                                            event_Bus4.setType(1);
//                                            event_Bus4.setValue(0);
//                                            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus4);
//                                        } catch (Exception e2) {
//                                            e2.printStackTrace();
//                                        }
//                                    }
//                                }
//                            });
//                            builder2.setNegativeButton(17039369, (dialogInterface, i) -> dialogInterface.cancel());
//                            builder2.show();
//                            return false;
                        case R.id.vp_menu_v_share:
                            Uri uriForFile = FileProvider.getUriForFile(VP_HideAdapter.this.ex_activity, VP_HideAdapter.this.ex_activity.getPackageName() + ".provider", new File(VP_HideAdapter.this.ex_mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path));
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.SEND");
                            intent.setType("video/*");
                            intent.addFlags(1);
                            intent.putExtra("android.intent.Extra.TEXT", (VP_HideAdapter.this.ex_activity.getResources().getString(R.string.app_name) + " Created By :") + "\n" + ("https://play.google.com/store/apps/details?id=" + VP_HideAdapter.this.ex_activity.getPackageName()));
                            intent.putExtra("android.intent.Extra.SUBJECT", VP_HideAdapter.this.ex_activity.getResources().getString(R.string.app_name));
                            intent.putExtra("android.intent.Extra.STREAM", uriForFile);
                            VP_HideAdapter.this.ex_activity.startActivity(Intent.createChooser(intent, "Share with..."));
                            return false;
                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((VP_HideVideo) VP_HideAdapter.this.ex_activity).myStartActivity(VP_Player.vp_getIntent(VP_HideAdapter.this.ex_activity, ex_mediaData, viewHolder.getAbsoluteAdapterPosition()));
            }
        });
    }

    @SuppressLint("ResourceType")
    public void initRenameDialog(final int i2) {
        final Dialog dialog = new Dialog(this.ex_activity, R.style.WideDialog);
        dialog.setContentView(R.layout.vp_dialog_new_folder);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        final EditText editText = dialog.findViewById(R.id.vp_folder_name);
        ((TextView) dialog.findViewById(R.id.vp_title)).setText("Rename");
        editText.setText(this.ex_mediaData.get(i2).name);
        dialog.findViewById(R.id.vp_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.vp_ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    Toast.makeText(VP_HideAdapter.this.ex_activity, "Enter folder name!", 0).show();
                    return;
                }
                String parent = new File(VP_HideAdapter.this.ex_mediaData.get(i2).path).getParent();
                File file = new File(parent, VP_HideAdapter.this.ex_mediaData.get(i2).path.substring(VP_HideAdapter.this.ex_mediaData.get(i2).path.lastIndexOf("/") + 1));
                File file2 = new File(parent, editText.getText().toString().trim());
                if (file.exists() && file.renameTo(file2)) {
                    VP_HideAdapter.this.ex_mediaData.get(i2).setName(file2.getName());
                    VP_HideAdapter.this.ex_mediaData.get(i2).setPath(file2.getPath());
                    VP_HideAdapter.this.ex_mediaData.set(i2, VP_HideAdapter.this.ex_mediaData.get(i2));
                    VP_HideAdapter.this.notifyItemChanged(i2);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return this.ex_mediaData.size();
    }
}
