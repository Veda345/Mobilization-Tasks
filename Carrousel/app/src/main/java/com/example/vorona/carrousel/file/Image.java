package com.example.vorona.carrousel.file;

import android.os.Parcel;
import android.os.Parcelable;

import com.yandex.disk.client.ListItem;

import java.io.OutputStream;

/**
 * Created by vorona on 24.04.16.
 */
public class Image extends File implements Parcelable {
    OutputStream out;
    String path, name;

    public Image(ListItem item) {
        this.it = item;
        path = it.getFullPath();
        name = it.getName();
    }

    protected Image(Parcel in) {
        path = in.readString();
        name = in.readString();
        ListItem.Builder b = new ListItem.Builder();
        b.setDisplayName(name);
        b.setFullPath(path);
        it = b.build();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public OutputStream getOut() {
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(it, flags);
        dest.writeString(path);
        dest.writeString(name);
    }
}
