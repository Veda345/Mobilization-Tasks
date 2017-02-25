package ru.yandex.yamblz.db;

/**
 * Created by user on 31/07/2016.
 */

public interface DbContract {
    String DB_NAME = "main.sqlite";

    String ARTISTS = "artists";
    interface Artist {
        String LOCAL_ID = "local_id";
        String ID = "id";
        String BIO = "bio";
        String ALBUM = "albums";
        String TRACKS = "tracks";
        String NAME = "name";
        String COVER = "cover";
        String GENRES = "genres";
        String COVER_SMALL = "cover_small";
    }
}
