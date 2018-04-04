package org.videolan.medialibrary.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public abstract class MediaLibraryItem implements Parcelable {

    public static final int TYPE_ALBUM    = 1 << 1;
    public static final int TYPE_ARTIST   = 1 << 2;
    public static final int TYPE_GENRE    = 1 << 3;
    public static final int TYPE_PLAYLIST = 1 << 4;
    public static final int TYPE_MEDIA    = 1 << 5;
    public static final int TYPE_DUMMY    = 1 << 6;
    public static final int TYPE_STORAGE  = 1 << 7;
    public static final int TYPE_HISTORY  = 1 << 9;

    public static final int FLAG_NONE = 0;
    public static final int FLAG_SELECTED = 1;


    public abstract MediaWrapper[] getTracks();
    public abstract int getItemType();

    long mId;
    protected String mTitle;
    String mDescription;
    private int mFlags;

    protected MediaLibraryItem() {}

    protected MediaLibraryItem(long id, String name) {
        mId = id;
        mTitle = name;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getArtworkMrl() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDescription() {
        return mDescription;
    }


    public void setDescription(String description) {
        mDescription = description;
    }

    public void setStateFlags(int flags) {
        mFlags = flags;
    }

    public void addStateFlags(int flags) {
        mFlags |= flags;
    }

    public boolean hasStateFlags(int flags) {
        return (mFlags & flags) != 0;
    }

    public void toggleStateFlag(int flag) {
        if (hasStateFlags(flag))
            removeStateFlags(flag);
        else
            addStateFlags(flag);
    }

    public void removeStateFlags(int flags) {
        mFlags &= ~flags;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
    }

    protected MediaLibraryItem(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof MediaLibraryItem && mId == ((MediaLibraryItem) obj).getId());
    }

    public boolean equals(MediaLibraryItem other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getItemType() != other.getItemType()) return false;
        if (mId != 0) return mId == other.getId();
        if (getItemType() == TYPE_DUMMY) return TextUtils.equals(getTitle(), other.getTitle());
        if (getItemType() == TYPE_MEDIA)
            return TextUtils.equals(((MediaWrapper)this).getLocation(), ((MediaWrapper)other).getLocation());
        if (getItemType() == TYPE_STORAGE)
            return TextUtils.equals(((Storage)this).getName(), ((Storage)other).getName());
        return false;
    }
}
