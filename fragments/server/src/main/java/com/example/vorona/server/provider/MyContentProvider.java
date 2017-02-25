package com.example.vorona.server.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import com.example.vorona.server.db.DBHelper;
import com.example.vorona.server.db.DbContract;

import static com.example.vorona.server.db.DbContract.Artist.ID;
import static com.example.vorona.server.db.DbContract.Artist.NAME;

public class MyContentProvider extends ContentProvider implements DbContract {

    static final String PROVIDER_NAME = "ru.yandex.yamblz.database";
    static final String URL = "content://" + PROVIDER_NAME + "/" + ARTISTS;
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final int ART = 1;
    static final int ART_ID = 2;

    private DBHelper dbHelper;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, ARTISTS, ART);
        uriMatcher.addURI(PROVIDER_NAME, ARTISTS+"/#", ART_ID);
    }

    @VisibleForTesting
    public MyContentProvider(Context context) {
        dbHelper = new DBHelper(context);
    }


    @Override
    public boolean onCreate() {
        Log.w("ContentProvider", "onCreate");
        Context context = getContext();
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db != null;
    }



    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ART:
                count = db.delete(ARTISTS, selection, selectionArgs);
                break;

            case ART_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(ARTISTS, ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ART:
                return "vnd.android.cursor.dir/vnd.ru.yandex.yamblz.database.artists";
            case ART_ID:
                return "vnd.android.cursor.item/vnd.ru.yandex.yamblz.database.artists";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor presence = db.query(ARTISTS, null, NAME + " = ?", new String[]{values.get(NAME).toString()}, null, null, null);
        if (presence.moveToFirst()) {
            presence.close();
            return null;
        }
        presence.close();
        long rowID = db.insert(ARTISTS, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("Provider", "query, " + uri.toString());
        //Yes, we will ignore all projections and selections and always return whole list
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String tables = ARTISTS + " LEFT JOIN " + ARTIST_GENRES + " ON " +
                ARTISTS + "." + Artist.ID + "=" + ARTIST_GENRES + "." + ArtistGenre.ARTIST_ID +
                " LEFT JOIN " + GENRES + " ON " +
                GENRES + "." + Genre.ID + "=" + ARTIST_GENRES + "." + ArtistGenre.GENRE_ID;

        String[] columns = new String[]{
                ARTISTS + "." + Artist.ID,
                ARTISTS + "." + Artist.NAME,
                ARTISTS + "." + Artist.TRACKS,
                ARTISTS + "." + Artist.ALBUM,
                ARTISTS + "." + Artist.BIO,
                ARTISTS + "." + Artist.COVER,
                ARTISTS + "." + Artist.COVER_SMALL,
                "group_concat" + "(" + GENRES + "." + Genre.GENRE + ", ', ') as " + Artist.GENRES};
        String groupBy = Artist.NAME;
        Cursor c = db.query(tables, columns, null, null, groupBy, null, null);

        System.out.println(c.getCount());
        return c;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ART:
                count = db.update(ARTISTS, values, selection, selectionArgs);
                break;

            case ART_ID:
                count = db.update(ARTISTS, values, ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


}
