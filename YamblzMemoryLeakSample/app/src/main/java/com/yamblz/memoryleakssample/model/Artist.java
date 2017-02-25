package com.yamblz.memoryleakssample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by i-sergeev on 01.07.16
 */
public class Artist implements Parcelable {
    @SerializedName("id")
    private final String id;

    @SerializedName("name")
    private final String name;

    @SerializedName("genres")
    private final String[] genres;

    @SerializedName("tracks")
    private final int tracksCount;

    @SerializedName("albums")
    private final int albumsCount;

    @SerializedName("link")
    private final String webCite;

    @SerializedName("description")
    private final String description;

    @SerializedName("cover")
    private final Cover cover;

    public Artist(String id,
                  String name,
                  String[] genres,
                  int tracksCount,
                  int albumsCount,
                  String webCite, String description, Cover cover) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.tracksCount = tracksCount;
        this.albumsCount = albumsCount;
        this.webCite = webCite;
        this.description = description;
        this.cover = cover;
    }

    protected Artist(Parcel in) {
        id = in.readString();
        name = in.readString();
        genres = in.createStringArray();
        tracksCount = in.readInt();
        albumsCount = in.readInt();
        webCite = in.readString();
        description = in.readString();
        cover = in.readParcelable(Cover.class.getClassLoader());
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public int getTracksCount() {
        return tracksCount;
    }

    public int getAlbumsCount() {
        return albumsCount;
    }

    public String getWebCite() {
        return webCite;
    }

    public String getDescription() {
        return description;
    }

    public Cover getCover() {
        return cover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeParcelable(cover, flags);
        dest.writeString(description);
        dest.writeInt(albumsCount);
        dest.writeInt(tracksCount);
        dest.writeStringArray(genres);
    }
}
