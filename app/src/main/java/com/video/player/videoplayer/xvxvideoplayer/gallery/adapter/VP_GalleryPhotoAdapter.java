package com.video.player.videoplayer.xvxvideoplayer.gallery.adapter;

import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.showInterAd2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showInterAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryMediaController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryPhoto;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.VP_GalleryMainActivity;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.mainnavigation.VP_GalleryPhotosFragment;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.viewer.VP_GalleryAlbumActivity;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.viewer.VP_GalleryPhotoActivity;
import com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder.VP_PhotoViewHolder;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class VP_GalleryPhotoAdapter extends RecyclerView.Adapter<VP_PhotoViewHolder> {
    private final Activity vp_activity;
    public ArrayList<VP_GalleryPhoto> VPGalleryPhotos;
    private int vp_spanCount;
    private String vp_albumTitle;
    private ActionMode vp_actionMode;
    private final HashMap<Integer, VP_PhotoViewHolder> vp_mapViews;
    private final boolean vp_enableActionMode;
    private VP_PhotoListener vp_photoListener;

    public interface VP_PhotoListener {
        void notifyPhotosCount(String title, int photosCount);
    }

    public VP_GalleryPhotoAdapter(Activity vp_activity, boolean vp_enableActionMode, @Nullable VP_PhotoListener vp_photoListener) {
        this.vp_activity = vp_activity;
        this.VPGalleryPhotos = new ArrayList<>();
        this.vp_spanCount = 4;
        this.vp_mapViews = new HashMap<>();
        this.vp_enableActionMode = vp_enableActionMode;
        this.vp_photoListener = vp_photoListener;
    }

    public VP_GalleryPhotoAdapter(Activity vp_activity, ArrayList<VP_GalleryPhoto> VPGalleryPhotos, String vp_albumTitle, boolean vp_enableActionMode, @Nullable VP_PhotoListener vp_photoListener) {
        this.vp_activity = vp_activity;
        this.VPGalleryPhotos = VPGalleryPhotos;
        this.vp_albumTitle = vp_albumTitle;
        this.vp_spanCount = 4;
        this.vp_mapViews = new HashMap<>();
        this.vp_enableActionMode = vp_enableActionMode;
        this.vp_photoListener = vp_photoListener;
    }

    @NonNull
    @Override
    public VP_PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vp_gallery_card_photo, parent, false);
        return new VP_PhotoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VP_PhotoViewHolder holder, int position) {
        //get galleryPhoto
        VP_GalleryPhoto VPGalleryPhoto = VPGalleryPhotos.get(position);
        //get all views from holder
        ImageView vp_img = holder.vp_img, vp_imgSelected = holder.vp_imgSelected;
        TextView vp_txtNameSize = holder.vp_txtNameSize;

        if (VPGalleryPhoto != null && vp_img != null && vp_imgSelected != null) {
            //show images
            Glide.with(vp_activity).load(VPGalleryPhoto.getVp_path())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(vp_img);
            //remove extension from name
            String name = VPGalleryPhoto.getVp_name()
                    .replace(".jpg", "")
                    .replace(".jpeg", "")
                    .replace(".png", "")
                    .replace(".gif", "");

            //set name and file size
            vp_txtNameSize.setText(name + " ∙ " + Formatter.formatFileSize(vp_activity, VPGalleryPhoto.getVp_size()));
            //show/hide name depending of spanCount
            vp_txtNameSize.setVisibility(vp_spanCount == 2 ? View.VISIBLE : View.GONE);
            //show/hide check image, depeding if action mode is enabled and user select an image
            vp_imgSelected.setVisibility(vp_mapViews.containsKey(position) ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(view -> {
                //show galleryPhoto or select an image
                if (vp_actionMode == null) {
                    Intent vp_intent = new Intent(vp_activity, VP_GalleryPhotoActivity.class);
                    vp_intent.putExtra(VP_GalleryPhoto.VD_PHOTO_KEY, VPGalleryPhoto);
                    vp_intent.putExtra(VP_GalleryPhoto.VD_PHOTO_POSITION_KEY, position);
                    String baseActivity = "";
                    if (vp_activity instanceof VP_GalleryMainActivity) {
                        baseActivity = "GalleryMainActivity";
                    } else if (vp_activity instanceof VP_GalleryAlbumActivity) {
                        baseActivity = "GalleryAlbumActivity";
                    }
                    vp_intent.putExtra("BaseActivity", baseActivity);
                    if (vp_GInter1(vp_activity).equals(vp_GInter2(vp_activity))) {
                        showInterAd2(vp_activity, vp_intent);
                    } else {
                        vp_showInterAd(vp_activity, vp_intent);
                    }
                } else {
                    vp_onItemSelect(position, holder);
                }
            });

            holder.itemView.setOnLongClickListener(v -> {
                //enter in action mode and mark clicked image
                if (vp_enableActionMode) {
                    vp_onItemSelect(position, holder);
                }
                return true;
            });
        }

    }

    /**
     * @return number of galleryPhotos
     */
    @Override
    public int getItemCount() {
        return VPGalleryPhotos.size();
    }

    /**
     * set galleryPhotos and notify list size
     *
     * @param VPGalleryPhotos
     */
    public void setPhotos(ArrayList<VP_GalleryPhoto> VPGalleryPhotos) {
        this.VPGalleryPhotos.clear();
        this.VPGalleryPhotos.addAll(VPGalleryPhotos);
        this.vp_activity.runOnUiThread(() -> {
            notifyDataSetChanged();
            if (vp_photoListener != null) {
                if (VPGalleryPhotos.size() != 0) {
                    vp_photoListener.notifyPhotosCount(VPGalleryPhotos.get(0).getVp_name(), VPGalleryPhotos.size());
                } else {
                    vp_photoListener.notifyPhotosCount("No Photos", 0);
                }
            }
        });
    }

    public void vp_setAlbumPhotos(ArrayList<VP_GalleryPhoto> VPGalleryPhotos, String albumTitle) {
        this.VPGalleryPhotos.clear();
        this.VPGalleryPhotos.addAll(VPGalleryPhotos);
        this.vp_albumTitle = albumTitle;
        this.vp_activity.runOnUiThread(() -> {
            notifyDataSetChanged();
            if (vp_photoListener != null) {
                vp_photoListener.notifyPhotosCount(albumTitle, VPGalleryPhotos.size());
            }
        });
    }

    /**
     * set span count
     *
     * @param vp_spanCount
     */
    public void setVp_spanCount(int vp_spanCount) {
        this.vp_spanCount = vp_spanCount;
    }

    /**
     * show-hide check
     * add-remove from map
     * enter-exit from action mode
     * show number of items selected
     *
     * @param position
     * @param holder
     */
    public void vp_onItemSelect(int position, VP_PhotoViewHolder holder) {
        vp_toggleSelection(position, holder);

        boolean vp_hasItemsChecked = vp_mapViews.size() > 0;

        //enter in action mode
        if (vp_hasItemsChecked && vp_actionMode == null) {
            try {
                if (vp_activity != null) {
                    vp_actionMode = ((AppCompatActivity) vp_activity).startSupportActionMode(vp_actionModeCallback);
                }
            } catch (NullPointerException e) {
                e.getMessage();
            }
            //exit from action mode
        } else if (!vp_hasItemsChecked && vp_actionMode != null) {
            vp_actionMode.finish();
            vp_hideAllImgsSelected();
        }
        vp_showNrImgsSelected();
    }

    /**
     * show number of items selected
     */
    private void vp_showNrImgsSelected() {
        if (vp_actionMode != null) {
            vp_actionMode.setTitle(String.valueOf(vp_mapViews.size()));
        }
    }

    /**
     * hide all checks from map
     * clear map
     */
    private void vp_hideAllImgsSelected() {
        for (int vp_key : vp_mapViews.keySet()) {
            VP_PhotoViewHolder VPPhotoViewHolder = vp_mapViews.get(vp_key);
            if (VPPhotoViewHolder != null) {
                VPPhotoViewHolder.vp_imgSelected.setVisibility(View.GONE);
            }
        }
        vp_mapViews.clear();
    }

    /**
     * show-hide check
     * add-remove holder from map
     *
     * @param position
     * @param holder
     */
    private void vp_toggleSelection(int position, VP_PhotoViewHolder holder) {
        if (vp_mapViews.containsKey(position)) {
            holder.vp_imgSelected.setVisibility(View.GONE);
            vp_mapViews.remove(position);
        } else {
            holder.vp_imgSelected.setVisibility(View.VISIBLE);
            vp_mapViews.put(position, holder);
        }
    }

    private final ActionMode.Callback vp_actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //inflate menu
            mode.getMenuInflater().inflate(R.menu.vp_gallery_menu_action_mode_media, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int vp_itemId = item.getItemId();

            if (vp_itemId == R.id.vp_menuAMDelete) {
                //get all keys from map
                ArrayList<Integer> posList = new ArrayList<>();
                ArrayList<Uri> uriList = new ArrayList<>();
                ArrayList<String> pathList = new ArrayList<>();
                ArrayList<VP_GalleryPhoto> gallPhotList = new ArrayList<>();

                for (int position : vp_mapViews.keySet()) {
                    gallPhotList.add(VPGalleryPhotos.get(position));
                    uriList.add(VPGalleryPhotos.get(position).getVp_uri());
                }

                //delete galleryPhoto
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    VP_GalleryMediaController.ex_deleteMedia(uriList, gallPhotList, -1, vp_activity);
                } else {
                    VP_GalleryMediaController.vp_deleteMediaFile(pathList, posList, true, vp_activity);
                }

                //exit from action mode
                mode.finish();
                return true;
            } else if (vp_itemId == R.id.vp_menuAMShare) {
                //get all keys
                ArrayList<Uri> vp_listImages = new ArrayList<>();
                for (int vp_key : vp_mapViews.keySet()) {
                    //get galleryPhoto and add to arrayList
                    VP_GalleryPhoto VPGalleryPhoto = VPGalleryPhotos.get(vp_key);
                    vp_listImages.add(VPGalleryPhoto.getVp_uri());
                }
                //show image or images depending of the number of galleryPhotos selected
                String vp_imgs = vp_listImages.size() == 1 ? "image" : "images";
                //share photo/s
                VP_GalleryMediaController.vp_shareMultipleMedias(vp_listImages, "Share " + vp_imgs, "image/jpeg", vp_activity);
                //exit from action mode
                mode.finish();
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //exit from action mode and hide all checks
            if (vp_actionMode != null) {
                vp_actionMode = null;
                vp_hideAllImgsSelected();
            }
        }
    };

    /**
     * delete photo
     * <p>
     * //     * @param position
     */
    public void vp_deletePhoto(Activity activity) {
        this.vp_activity.runOnUiThread(() -> {
            if (activity instanceof VP_GalleryMainActivity) {
                VP_GalleryViewPagerTitleAdapter vp_adapter = VP_GalleryMainActivity.vp_getAdapter();
                if (vp_adapter != null) {
                    VP_GalleryPhotosFragment vp_fragment = (VP_GalleryPhotosFragment) vp_adapter.getItem(0);
                    vp_fragment.vp_getPhotos();
                }
            } else if (activity instanceof VP_GalleryAlbumActivity) {
                Log.d("Check:ListSize", String.valueOf(VPGalleryPhotos.size()));
                this.VPGalleryPhotos.clear();
                this.VPGalleryPhotos = ((VP_MyApplication) activity.getApplicationContext()).VPGalleryAlbum.getPhotos();
                ((VP_GalleryAlbumActivity) activity).getVp_adapter().vp_setAlbumPhotos(VPGalleryPhotos, vp_albumTitle);
            }
//            this.galleryPhotos.remove(position);
        });
//        this.activity.runOnUiThread(() -> {
//            this.galleryPhotos.remove(position);
//            notifyItemRemoved(position);
//            notifyDataSetChanged();
//        });
    }

}
