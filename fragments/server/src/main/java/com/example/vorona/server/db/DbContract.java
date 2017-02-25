package com.example.vorona.server.db;

/**
 * Created by user on 31/07/2016.
 */
public interface DbContract {
    String DB_NAME = "main.sqlite";

    String ARTISTS = "artists";
    String GENRES = "gs";
    String ARTIST_GENRES = "artists_genres";
    interface Artist {
        String LOCAL_ID = "local_id";
        String ID = "singer_id";
        String BIO = "bio";
        String ALBUM = "albums";
        String TRACKS = "tracks";
        String NAME = "name";
        String COVER = "cover";
        String COVER_SMALL = "cover_small";
        String GENRES = "artist_genres";
    }

    interface Genre{
        String ID = "genre_id";
        String GENRE = "genre_name";
    }

    interface ArtistGenre{
        String ARTIST_ID = "artist_id";
        String GENRE_ID = "genre_id";
    }
}
