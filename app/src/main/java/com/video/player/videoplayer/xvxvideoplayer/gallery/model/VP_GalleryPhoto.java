package com.video.player.videoplayer.xvxvideoplayer.gallery.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class VP_GalleryPhoto implements Parcelable {

    public static final String VD_PHOTO_KEY = "photoKey";
    public static final String VD_PHOTO_POSITION_KEY = "photoPositionKey";
    //
    private Uri vp_uri;
    private String vp_name, vp_path, vp_date, vp_resolution;
    private long vp_size;

    public VP_GalleryPhoto() {
    }

    public VP_GalleryPhoto(Uri vp_uri, String vp_name, String vp_path, String vp_date, String vp_resolution, Long vp_size) {
        this.vp_uri = vp_uri;
        this.vp_name = vp_name;
        this.vp_path = vp_path;
        this.vp_date = vp_date;
        this.vp_resolution = vp_resolution;
        this.vp_size = vp_size;
    }

    protected VP_GalleryPhoto(Parcel in) {
        vp_uri = in.readParcelable(Uri.class.getClassLoader());
        vp_name = in.readString();
        vp_path = in.readString();
        vp_date = in.readString();
        vp_resolution = in.readString();
        vp_size = in.readLong();
    }

    public static final Creator<VP_GalleryPhoto> CREATOR = new Creator<VP_GalleryPhoto>() {
        @Override
        public VP_GalleryPhoto createFromParcel(Parcel in) {
            return new VP_GalleryPhoto(in);
        }

        @Override
        public VP_GalleryPhoto[] newArray(int size) {
            return new VP_GalleryPhoto[size];
        }
    };

    public Uri getVp_uri() {
        return vp_uri;
    }

    public String getVp_name() {
        return vp_name;
    }

    public String getVp_path() {
        return vp_path;
    }

    public Long getVp_size() {
        return vp_size;
    }

    public void setVp_uri(Uri vp_uri) {
        this.vp_uri = vp_uri;
    }

    public void setVp_path(String vp_path) {
        this.vp_path = vp_path;
    }

    public String getVp_date() {
        return vp_date;
    }

    public void setVp_date(String vp_date) {
        this.vp_date = vp_date;
    }

    public String getVp_resolution() {
        return vp_resolution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(vp_uri, flags);
        parcel.writeString(vp_name);
        parcel.writeString(vp_path);
        parcel.writeString(vp_date);
        parcel.writeString(vp_resolution);
        parcel.writeLong(vp_size);
    }

}
