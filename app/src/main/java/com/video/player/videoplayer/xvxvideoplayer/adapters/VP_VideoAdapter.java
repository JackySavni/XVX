package com.video.player.videoplayer.xvxvideoplayer.adapters;

import static com.video.player.videoplayer.xvxvideoplayer.activities.VP_Videolist.vp_videoList;
import static com.video.player.videoplayer.xvxvideoplayer.fragments.VP_RecentFragment.vp_ivNoData;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.showInterAd2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showInterAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.database.VP_Database;
import com.video.player.videoplayer.xvxvideoplayer.dialog.VP_VideoDetailsDialog;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryMediaController;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_EventBus;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Utils;
import com.video.player.videoplayer.xvxvideoplayer.vid.VP_Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VP_VideoAdapter extends RecyclerView.Adapter<VP_VideoAdapter.ViewHolder> {
    FragmentActivity vp_activity;
    public final VP_Database VPDatabase;
    RecyclerView.ViewHolder vp_holder;
    int vp_i;
    LayoutInflater vp_inflater;
    ArrayList<MediaData> mediaData;
    int vp_type;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vp_duration;
        TextView vp_videoDate;
        ImageView vp_videoOption;
        TextView vp_videoSize;
        ImageView vp_videoThumb;
        TextView vp_videoTitle;

        public ViewHolder(View view) {
            super(view);
            this.vp_videoTitle = view.findViewById(R.id.vp_video_title);
            this.vp_videoDate = view.findViewById(R.id.vp_video_date);
            this.vp_videoSize = view.findViewById(R.id.vp_video_size);
            this.vp_duration = view.findViewById(R.id.vp_duration);
            this.vp_videoThumb = view.findViewById(R.id.vp_video_thumb);
            this.vp_videoOption = view.findViewById(R.id.vp_video_option);
        }
    }

    public VP_VideoAdapter(FragmentActivity fragmentActivity, ArrayList<MediaData> arrayList, int i2, int i3) {
        this.vp_activity = fragmentActivity;
        this.mediaData = arrayList;
        this.vp_i = i2;
        this.vp_type = i3;
        this.VPDatabase = new VP_Database(fragmentActivity);
        this.vp_inflater = LayoutInflater.from(fragmentActivity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i2) {
        View view;
//        if (this.i == 0) {
//            view = this.ex_inflater.inflate(R.layout.ex_item_video_liner, viewGroup, false);
//        } else {
            view = this.vp_inflater.inflate(R.layout.vp_item_video_grid, viewGroup, false);
//        }
        return new ViewHolder(view);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i2) {
        if (this.vp_i == 1) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.vp_activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
        Glide.with(this.vp_activity).load(this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path).into(viewHolder.vp_videoThumb);
        viewHolder.vp_videoTitle.setText(this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).name);
        viewHolder.vp_videoSize.setText(VP_Utils.formatSize(Long.parseLong(this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).length)));
        viewHolder.vp_duration.setText(this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).videoDuration + "");
        viewHolder.vp_duration.setText(vp_getDuration(this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).videoDuration));
        viewHolder.vp_videoDate.setText(DateFormat.format("dd/MM/yyyy", new Date(new File(this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path).lastModified())).toString());
        viewHolder.vp_videoOption.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(VP_VideoAdapter.this.vp_activity, view);
            popupMenu.inflate(R.menu.vp_video_menu);
            Menu menu = popupMenu.getMenu();
            if (VP_VideoAdapter.this.vp_type == 2) {
                menu.findItem(R.id.vp_menu_v_delete).setTitle("Remove");
            }
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                boolean z = true;
                switch (menuItem.getItemId()) {
                    case R.id.vp_menu_v_delete:
                        if (VP_VideoAdapter.this.vp_type != 2) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VP_VideoAdapter.this.vp_activity);
                            builder.setTitle("Delete Video");
                            builder.setMessage("Are you sure you have to Delete " + VP_VideoAdapter.this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).name + " ?");
                            builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                                boolean z12;
                                File file = new File(VP_VideoAdapter.this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    deleteVideo(new ArrayList<>(Collections.singletonList(Uri.parse(mediaData.get(viewHolder.getAbsoluteAdapterPosition()).uri))), null, vp_activity);
                                }
//                                else {
//                                    deleteVideoFile(new ArrayList<>(Collections.singletonList(galleryPhotoRaw.getPath())), new ArrayList<>(Collections.singletonList(photoPosition)), true, this);
//                                }

//                                if (file.exists() && file.delete()) {
                                if (VP_VideoAdapter.this.vp_type == 1) {
                                    VP_EventBus event_Bus = new VP_EventBus();
                                    event_Bus.setVp_type(1);
                                    event_Bus.setMediaData(mediaData.get(viewHolder.getAbsoluteAdapterPosition()));
                                    org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
                                }

//                                if (VideoAdapter.this.type == 1) {
//                                    EventBus event_Bus = new EventBus();
//                                    event_Bus.setType(1);
//                                    event_Bus.setValue(0);
//                                    org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
//                                }
                                ArrayList<MediaData> list = new Gson().fromJson(VP_MyApplication.vp_getRecentPlay(), new TypeToken<List<MediaData>>() {
                                }.getType());
                                if (list != null && list.size() > 0) {
                                    for (int y = 0; y < list.size(); y++) {
                                        if (list.get(y).getPath().equals(mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path)) {
                                            list.remove(y);
                                            VP_MyApplication.vp_putRecentPlay(new Gson().toJson(list));
                                            break;
                                        }
                                    }
                                }
                                VP_VideoAdapter.this.mediaData.remove(viewHolder.getAbsoluteAdapterPosition());
                                VP_VideoAdapter.this.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
                                if (VP_VideoAdapter.this.vp_type == 0) {
                                    if (list != null && list.size() == 0) {
                                        vp_ivNoData.setVisibility(View.VISIBLE);
                                    }
                                } else if (list != null && VP_VideoAdapter.this.vp_type == 1 && list.size() == 0) {
                                    vp_ivNoData.setVisibility(View.VISIBLE);
                                    vp_videoList.setVisibility(View.GONE);
                                }
                            });
                            builder.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.cancel());
                            builder.show();
                        } else {
                            ArrayList<MediaData> list = new Gson().fromJson(VP_MyApplication.vp_getRecentPlay(), new TypeToken<List<MediaData>>() {
                            }.getType());
                            if (list != null && list.size() > 0) {
                                for (int y = 0; y < list.size(); y++) {
                                    if (list.get(y).getPath().equals(mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path)) {
                                        list.remove(y);
                                        VP_MyApplication.vp_putRecentPlay(new Gson().toJson(list));
                                        VP_VideoAdapter.this.mediaData.remove(viewHolder.getAbsoluteAdapterPosition());
                                        VP_VideoAdapter.this.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case R.id.vp_menu_v_details:
                        VP_VideoDetailsDialog.getInstance(VP_VideoAdapter.this.mediaData.get(viewHolder.getAbsoluteAdapterPosition())).show(VP_VideoAdapter.this.vp_activity.getSupportFragmentManager(), "");
                        break;
//                    case R.id.menu_v_hide:
//                        if (MyApplication.getSetPass()) {
//                            AlertDialog.Builder builder2 = new AlertDialog.Builder(VideoAdapter.this.activity);
//                            builder2.setTitle("Hide Video");
//                            builder2.setMessage("Are you sure you have to Hide " + VideoAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).name + " ?");
//                            builder2.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
//                                boolean z1;
//                                if (!new File(Constant.HIDE_PATH).exists()) {
//                                    new File(Constant.HIDE_PATH).mkdirs();
//                                }
//                                String parent = new File(VideoAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path).getParent();
//                                String str = Constant.HIDE_PATH + "/" + VideoAdapter.this.database.getID() + "_" + VideoAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).name + VideoAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path.substring(VideoAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path.lastIndexOf("."));
//                                File file = new File(parent, VideoAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path.substring(VideoAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path.lastIndexOf("/") + 1));
//                                File file2 = new File(str);
//                                if (file.renameTo(file2)) {
//                                    HideData hide_Data = new HideData();
//                                    hide_Data.setName(file2.getName());
//                                    hide_Data.setPath(parent);
//                                    VideoAdapter.this.database.addHide(hide_Data);
//                                    try {
//                                        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//                                        intent.setData(Uri.fromFile(file2));
//                                        VideoAdapter.this.activity.sendBroadcast(intent);
//                                        if (VideoAdapter.this.type == 1) {
//                                            EventBus event_Bus = new EventBus();
//                                            event_Bus.setType(1);
//                                            event_Bus.setValue(0);
//                                            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    List list = (List) new Gson().fromJson(MyApplication.getRecentPlay(), new TypeToken<List<MediaData>>() {
//                                    }.getType());
//                                    if (list != null) {
//                                        int i21 = 0;
//                                        while (true) {
//                                            if (i21 >= list.size()) {
//                                                i21 = 0;
//                                                z1 = false;
//                                                break;
//                                            } else if (((MediaData) list.get(i21)).path.equals(VideoAdapter.this.mediadatas.get(viewHolder.getAbsoluteAdapterPosition()).path)) {
//                                                z1 = true;
//                                                break;
//                                            } else {
//                                                i21++;
//                                            }
//                                        }
//                                        if (z1) {
//                                            list.remove(i21);
//                                            MyApplication.putRecentPlay(new Gson().toJson(list));
//                                        }
//                                    }
//                                    VideoAdapter.this.mediadatas.remove(viewHolder.getAbsoluteAdapterPosition());
//                                    VideoAdapter.this.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
//                                    if (VideoAdapter.this.type == 0) {
//                                        if ((list != null ? list.size() : 0) == 0) {
//                                            ivnodata.setVisibility(View.INVISIBLE);
//                                            videorecycler.setVisibility(View.VISIBLE);
//                                        }
//                                    } else if (VideoAdapter.this.type == 1) {
//                                        if ((list != null ? list.size() : 0) == 0) {
//                                            VideolistActivity.ivnodata.setVisibility(View.INVISIBLE);
//                                            videolist.setVisibility(View.VISIBLE);
//                                        }
//                                    } else if ((list != null ? list.size() : 0) == 0) {
//                                        ivnodata.setVisibility(View.VISIBLE);
//                                        recentrecycler.setVisibility(View.INVISIBLE);
//                                    }
//                                }
//                            });
//                            builder2.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.cancel());
//                            builder2.show();
//                            break;
//                        } else {
//                            VideoAdapter.this.activity.startActivity(new Intent(VideoAdapter.this.activity, SecureActivity.class));
//                            break;
//                        }
                    case R.id.vp_menu_v_share:
                        Uri uriForFile = FileProvider.getUriForFile(VP_VideoAdapter.this.vp_activity, VP_VideoAdapter.this.vp_activity.getPackageName() + ".provider", new File(VP_VideoAdapter.this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path));
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.SEND");
                        intent.setType("video/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra("android.intent.Extra.TEXT", (VP_VideoAdapter.this.vp_activity.getResources().getString(R.string.app_name) + " Created By :") + "\n" + ("https://play.google.com/store/apps/details?id=" + VP_VideoAdapter.this.vp_activity.getPackageName()));
                        intent.putExtra("android.intent.Extra.SUBJECT", VP_VideoAdapter.this.vp_activity.getResources().getString(R.string.app_name));
                        intent.putExtra("android.intent.Extra.STREAM", uriForFile);
                        VP_VideoAdapter.this.vp_activity.startActivity(Intent.createChooser(intent, "Share with..."));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
                }
                return false;
            });
            popupMenu.show();
        });
        viewHolder.itemView.setOnClickListener(view -> {
            boolean z = true;
            if (VP_VideoAdapter.this.vp_type == 0) {
                if (vp_GInter1(vp_activity).equals(vp_GInter2(vp_activity))) {
                    showInterAd2(vp_activity, VP_Player.vp_getIntent(VP_VideoAdapter.this.vp_activity, VP_VideoAdapter.this.mediaData, viewHolder.getAbsoluteAdapterPosition()));
                } else {
                    vp_showInterAd(vp_activity, VP_Player.vp_getIntent(VP_VideoAdapter.this.vp_activity, VP_VideoAdapter.this.mediaData, viewHolder.getAbsoluteAdapterPosition()));
                }
//                ((VP_Home) VP_VideoAdapter.this.ex_activity).myStartActivity(VP_Player.getIntent(VP_VideoAdapter.this.ex_activity, VP_VideoAdapter.this.ex_mediaData, viewHolder.getAbsoluteAdapterPosition()));
            } else if (VP_VideoAdapter.this.vp_type == 1) {
                if (vp_GInter1(vp_activity).equals(vp_GInter2(vp_activity))) {
                    showInterAd2(vp_activity, VP_Player.vp_getIntent(VP_VideoAdapter.this.vp_activity, VP_VideoAdapter.this.mediaData, viewHolder.getAbsoluteAdapterPosition()));
                } else {
                    vp_showInterAd(vp_activity, VP_Player.vp_getIntent(VP_VideoAdapter.this.vp_activity, VP_VideoAdapter.this.mediaData, viewHolder.getAbsoluteAdapterPosition()));
                }
//                ((VP_Videolist) VP_VideoAdapter.this.ex_activity).myStartActivity(VP_Player.getIntent(VP_VideoAdapter.this.ex_activity, VP_VideoAdapter.this.ex_mediaData, viewHolder.getAbsoluteAdapterPosition()));
            } else {
                if (vp_GInter1(vp_activity).equals(vp_GInter2(vp_activity))) {
                    showInterAd2(vp_activity, VP_Player.vp_getIntent(VP_VideoAdapter.this.vp_activity, mediaData, viewHolder.getAbsoluteAdapterPosition()));
                } else {
                    vp_showInterAd(vp_activity, VP_Player.vp_getIntent(VP_VideoAdapter.this.vp_activity, mediaData, viewHolder.getAbsoluteAdapterPosition()));
                }
//                ((VP_Home) VP_VideoAdapter.this.ex_activity).myStartActivity(VP_Player.getIntent(VP_VideoAdapter.this.ex_activity, ex_mediaData, viewHolder.getAbsoluteAdapterPosition()));
            }
            List vp_list = new Gson().fromJson(VP_MyApplication.vp_getRecentPlay(), new TypeToken<List<MediaData>>() {
            }.getType());
            if (vp_list != null) {
                int i = 0;
                int i212 = 0;
                while (true) {
                    if (i212 >= vp_list.size()) {
                        z = false;
                        break;
                    } else if (((MediaData) vp_list.get(i212)).path.equals(VP_VideoAdapter.this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()).path)) {
                        i = i212;
                        break;
                    } else {
                        i212++;
                    }
                }
                if (z) {
                    vp_list.remove(i);
                    vp_list.add(VP_VideoAdapter.this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()));
                    VP_MyApplication.vp_putRecentPlay(new Gson().toJson(vp_list));
                } else {
                    vp_list.add(VP_VideoAdapter.this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()));
                    VP_MyApplication.vp_putRecentPlay(new Gson().toJson(vp_list));
                }
            } else {
                ArrayList vp_arrayList = new ArrayList();
                vp_arrayList.add(VP_VideoAdapter.this.mediaData.get(viewHolder.getAbsoluteAdapterPosition()));
                VP_MyApplication.vp_putRecentPlay(new Gson().toJson(vp_arrayList));
            }
            new Handler().postDelayed(() -> {
                VP_EventBus event_Bus = new VP_EventBus();
                event_Bus.setVp_type(3);
                event_Bus.setVp_value(0);
                org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
            }, 1500);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void deleteVideo(ArrayList<Uri> uri, ArrayList<MediaData> list, Activity activity) {
        try {
            PendingIntent vp_pendingIntent = MediaStore.createDeleteRequest(activity.getContentResolver(), uri);
            activity.startIntentSenderForResult(vp_pendingIntent.getIntentSender(),
                    VP_GalleryMediaController.REQ_CODE_DELETE, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String vp_getDuration(int millis) {
        long vp_hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(vp_hours);
        long vp_minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(vp_minutes);
        long vp_seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return (vp_hours > 0 ? vp_hours + " : " : "") + vp_minutes + " : " + vp_seconds;
    }

    @SuppressLint("ResourceType")
    public void initRenameDialog(final int i2) {
        final Dialog dialog = new Dialog(this.vp_activity, R.style.WideDialog);
        dialog.setContentView(R.layout.vp_dialog_new_folder);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        final EditText vp_editText = dialog.findViewById(R.id.vp_folder_name);
        ((TextView) dialog.findViewById(R.id.vp_title)).setText("Rename");
        vp_editText.setText(this.mediaData.get(i2).name);
        dialog.findViewById(R.id.vp_cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.findViewById(R.id.vp_ok).setOnClickListener(view -> {
            if (TextUtils.isEmpty(vp_editText.getText().toString().trim())) {
                Toast.makeText(VP_VideoAdapter.this.vp_activity, "Enter folder name!", 0).show();
                return;
            }
            String parent = new File(VP_VideoAdapter.this.mediaData.get(i2).path).getParent();
            File vp_file = new File(parent, VP_VideoAdapter.this.mediaData.get(i2).path.substring(VP_VideoAdapter.this.mediaData.get(i2).path.lastIndexOf("/") + 1));
            String substring = VP_VideoAdapter.this.mediaData.get(i2).path.substring(VP_VideoAdapter.this.mediaData.get(i2).path.lastIndexOf("."));
            File file2 = new File(parent, vp_editText.getText().toString().trim() + substring);
            if (vp_file.exists() && vp_file.renameTo(file2)) {
                VP_VideoAdapter.this.mediaData.get(i2).setName(file2.getName());
                VP_VideoAdapter.this.mediaData.get(i2).setPath(file2.getPath());
                VP_VideoAdapter.this.mediaData.set(i2, VP_VideoAdapter.this.mediaData.get(i2));
                VP_VideoAdapter.this.notifyItemChanged(i2);
                if (VP_VideoAdapter.this.vp_type == 1) {
                    VP_EventBus event_Bus = new VP_EventBus();
                    event_Bus.setVp_type(1);
                    event_Bus.setVp_value(0);
                    org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return mediaData.size();
    }
}
