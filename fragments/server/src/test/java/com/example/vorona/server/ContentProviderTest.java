package com.example.vorona.server;


import android.content.ContentProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.vorona.server.db.DBHelper;
import com.example.vorona.server.db.DbBackend;
import com.example.vorona.server.db.DbContract;
import com.example.vorona.server.model.Singer;
import com.example.vorona.server.provider.MyContentProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static com.example.vorona.server.db.DbContract.ARTISTS;
import static com.example.vorona.server.db.DbContract.GENRES;

/**
 * Created by vorona on 05.08.16.
 */

@RunWith(RobolectricTestRunner.class)
public class ContentProviderTest {

    private static final String PROVIDER_NAME = "ru.yandex.yamblz.database";
    private static final String URL = "content://" + PROVIDER_NAME + "/artists";
    private static final Uri CONTENT_URI = Uri.parse(URL);


    @Test
    public void testGenres() {
        DBHelper helper = new DBHelper(RuntimeEnvironment.application);
        SQLiteDatabase db = helper.getWritableDatabase();
        DbBackend dbBackend = new DbBackend(RuntimeEnvironment.application);

        Singer singer = new Singer();
        List<String> g = new ArrayList<>();
        g.add("BlaBla");
        singer.setGenres(g);
        singer.setName("AAAA");
        singer.setId(1);

        dbBackend.insertSinger(singer);

        ContentProvider provider = new MyContentProvider(RuntimeEnvironment.application);
        Cursor cursor = provider.query(CONTENT_URI, null, null, null, null);
        Singer s = new Singer();
        cursor.moveToFirst();
        setSinger(s, cursor);
        cursor.close();

        Assert.assertEquals(g.get(0), s.getGenres().get(0));

        Assert.assertEquals(singer.getName(), s.getName());
    }

    @Test
    public void testQuery() {
        DBHelper helper = new DBHelper(RuntimeEnvironment.application);
        SQLiteDatabase db = helper.getWritableDatabase();
        DbBackend dbBackend = new DbBackend(RuntimeEnvironment.application);

        Singer singer = new Singer();
        singer.setName("AAAA");
        singer.setId(1);

        Singer singer1 = new Singer();
        singer1.setName("BBBB");
        singer1.setId(2);

        dbBackend.insertSinger(singer);
        dbBackend.insertSinger(singer1);

        ContentProvider provider = new MyContentProvider(RuntimeEnvironment.application);
        Cursor cursor = provider.query(CONTENT_URI, null, null, null, null);

        Assert.assertEquals(cursor.getCount(), 2);
        cursor.close();
    }

    private void setSinger(Singer singer, Cursor c) {
        singer.setId(c.getInt(c.getColumnIndex(DbContract.Artist.ID)));
        singer.setName(c.getString(c.getColumnIndex(DbContract.Artist.NAME)));
        singer.setBio(c.getString(c.getColumnIndex(DbContract.Artist.BIO)));
        singer.setAlbums(c.getInt(c.getColumnIndex(DbContract.Artist.ALBUM)));
        singer.setTracks(c.getInt(c.getColumnIndex(DbContract.Artist.TRACKS)));
        singer.setCover_big(c.getString(c.getColumnIndex(DbContract.Artist.COVER)));
        singer.setCover_small(c.getString(c.getColumnIndex(DbContract.Artist.COVER_SMALL)));
        List<String> g = new ArrayList<>();
        g.add(c.getString(c.getColumnIndex(DbContract.Artist.GENRES)));
        singer.setGenres(g);
    }
}
