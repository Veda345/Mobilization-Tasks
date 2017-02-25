package com.example.vorona.server.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.example.vorona.server.model.Singer;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 31/07/2016.
 */
public class DbBackend implements DbContract {

    private final DBHelper mDbOpenHelper;

    public DbBackend(Context context) {
        mDbOpenHelper = new DBHelper(context);
    }

    public void insertSinger(Singer singer) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        ContentValues values = createCV(singer);
        db.beginTransaction();
        try {
            db.insert(ARTISTS, null, values);
            //inserting genres
            for (String g : singer.getGenres()) {
                ContentValues genre = new ContentValues();
                genre.put(Genre.GENRE, g);
                db.insert(GENRES, null, genre);
            }

            //lets take artist from table by its id and get his local id
            Cursor artist = db.query(ARTISTS, null, Artist.ID + " = ?",
                    new String[]{Long.toString(singer.getId())}, null, null, null);

            if (artist.moveToFirst()) {
                int relative_id = artist.getInt(artist.getColumnIndex(Artist.LOCAL_ID));

                for (String g : singer.getGenres()) {
                    //take genre's id
                    Cursor genre = db.query(GENRES, null, Genre.GENRE + " = ?",
                            new String[]{g}, null, null, null);
                    if (genre.moveToFirst()) {
                        //insert pair of artist and genre
                        int genre_id = genre.getInt(genre.getColumnIndex(Genre.ID));
                        ContentValues artistGenre = new ContentValues();
                        artistGenre.put(ArtistGenre.ARTIST_ID, relative_id);
                        artistGenre.put(ArtistGenre.GENRE_ID, genre_id);
                        db.insert(ARTIST_GENRES, null, artistGenre);
                    }
                    genre.close();
                }
                db.setTransactionSuccessful();
                artist.close();
            } else {
                Log.w("DbBackend", "Inserted but didn't find later");
            }
        } finally {
            db.endTransaction();
        }
    }

    public void insertSingerUnique(Singer singer) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        ContentValues values = createCV(singer);
        db.beginTransaction();
        try {
            Cursor presence = db.query(ARTISTS, null, Artist.NAME + " = ?",
                    new String[]{singer.getName()}, null, null, null);
            if (presence.moveToFirst()) {
                presence.close();
                db.setTransactionSuccessful();
                return;
            }
            db.insert(ARTISTS, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void insertList(List<Singer> singers) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Singer singer : singers) {
                insertSinger(singer);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void insertListUnique(List<Singer> singers) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Singer singer : singers) {
                insertSingerUnique(singer);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private ContentValues createCV(Singer singer) {
        ContentValues cv = new ContentValues();
        cv.put(Artist.ID, singer.getId());
        cv.put(Artist.NAME, singer.getName());
        cv.put(Artist.BIO, singer.getBio());
        cv.put(Artist.ALBUM, singer.getAlbums());
        cv.put(Artist.TRACKS, singer.getTracks());
        cv.put(Artist.COVER, singer.getCover_big());
        cv.put(Artist.COVER_SMALL, singer.getCover_small());
        return cv;
    }
}
