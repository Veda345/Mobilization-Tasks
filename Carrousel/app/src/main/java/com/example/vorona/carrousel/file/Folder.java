package com.example.vorona.carrousel.file;

import android.os.Parcel;
import android.os.Parcelable;

import com.yandex.disk.client.ListItem;

/**
 * Created by vorona on 24.04.16.
 */
public class Folder extends File implements Parcelable{

    public Folder(ListItem item) {
        it = item;
    }

    protected Folder(Parcel in) {
        String path = in.readString();
        String name = in.readString();
        ListItem.Builder b = new ListItem.Builder();
        b.setDisplayName(name);
        b.setFullPath(path);
        it = b.build();
    }

    public Folder() {
        ListItem.Builder b = new ListItem.Builder();
        b.setFullPath("");
        b.setDisplayName("");
        it = b.build();
    }

    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(it.getFullPath());
        dest.writeString(it.getName());
    }

}
