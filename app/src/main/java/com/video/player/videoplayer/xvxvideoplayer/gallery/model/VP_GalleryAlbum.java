package com.video.player.videoplayer.xvxvideoplayer.gallery.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class VP_GalleryAlbum implements Parcelable {
    private final String vp_name;
    private final int vp_nrPhotos;
    private final long vp_size;
    private final ArrayList<VP_GalleryPhoto> VPGalleryPhotos;

    public VP_GalleryAlbum(String vp_name, int vp_nrPhotos, long vp_size, ArrayList<VP_GalleryPhoto> VPGalleryPhotos) {
        this.vp_name = vp_name;
        this.vp_nrPhotos = vp_nrPhotos;
        this.vp_size = vp_size;
        this.VPGalleryPhotos = VPGalleryPhotos;
    }

    protected VP_GalleryAlbum(Parcel in) {
        this.vp_name = in.readString();
        this.vp_nrPhotos = in.readInt();
        this.vp_size = in.readLong();
        this.VPGalleryPhotos = in.createTypedArrayList(VP_GalleryPhoto.CREATOR);
    }

    public static final Creator<VP_GalleryAlbum> CREATOR = new Creator<VP_GalleryAlbum>() {
        @Override
        public VP_GalleryAlbum createFromParcel(Parcel in) {
            return new VP_GalleryAlbum(in);
        }

        @Override
        public VP_GalleryAlbum[] newArray(int size) {
            return new VP_GalleryAlbum[size];
        }
    };

    public String getVp_name() {
        return vp_name;
    }

    public long getVp_size() {
        return vp_size;
    }

    public int getVp_nrPhotos() {
        return vp_nrPhotos;
    }

    public ArrayList<VP_GalleryPhoto> getPhotos() {
        return VPGalleryPhotos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(vp_name);
        parcel.writeLong(vp_size);
        parcel.writeInt(vp_nrPhotos);
        parcel.writeTypedList(VPGalleryPhotos);
    }

}

