package org.videolan.medialibrary.media;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Storage extends MediaLibraryItem {

    Uri uri;
    String description;

    @Override
    public MediaWrapper[] getTracks() {
        return new MediaWrapper[0];
    }

    @Override
    public int getItemType() {
        return TYPE_STORAGE;
    }

    public Storage(Uri uri){
        this.uri = uri;
        mTitle = uri.getLastPathSegment();
    }

    public String getName() {
        return Uri.decode(mTitle);
    }

    public void setName(String name) {
        mTitle = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Uri getUri() {
        return uri;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(uri, i);
        parcel.writeString(description);
    }

    public static Parcelable.Creator<Storage> CREATOR
            = new Parcelable.Creator<Storage>() {
        public Storage createFromParcel(Parcel in) {
            return new Storage(in);
        }

        public Storage[] newArray(int size) {
            return new Storage[size];
        }
    };

    private Storage(Parcel in) {
        super(in);
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.description = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Storage && TextUtils.equals(mTitle, ((Storage)obj).getTitle());
    }
}
