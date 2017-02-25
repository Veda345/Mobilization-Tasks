package com.example.vorona.server.db;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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
import static com.example.vorona.server.db.DbContract.ARTIST_GENRES;
import static com.example.vorona.server.db.DbContract.GENRES;

/**
 * Created by vorona on 05.08.16.
 */

@RunWith(RobolectricTestRunner.class)
public class DbBackendTest {

    private static final String PROVIDER_NAME = "ru.yandex.yamblz.database";
    private static final String URL = "content://" + PROVIDER_NAME + "/artists";
    private static final Uri CONTENT_URI = Uri.parse(URL);

    @Test
    public void testInsertSinger() {
        DBHelper helper = new DBHelper(RuntimeEnvironment.application);
        SQLiteDatabase db = helper.getWritableDatabase();

        DbBackend dbBackend = new DbBackend(RuntimeEnvironment.application);

        //Empty table, one singer added
        Singer singer = new Singer();
        singer.setName("AAAA");
        List<String> g = new ArrayList<>();
        g.add("BlaBla");
        singer.setGenres(g);
        dbBackend.insertSinger(singer);
        Assert.assertEquals(1, getCount(db, ARTISTS));

        //One another
        Singer singer2 = new Singer();
        singer.setName("BBB");
        dbBackend.insertSinger(singer2);
        Assert.assertEquals(2, getCount(db, ARTISTS));

        //The same singer should also be added
        Singer singer3 = new Singer();
        singer.setName("AAAA");
        dbBackend.insertSinger(singer3);
        int p =  getCount(db, ARTISTS);
        Assert.assertEquals(3, p);
    }

    @Test
    public void testInsertUniqueSinger() {
        DBHelper helper = new DBHelper(RuntimeEnvironment.application);
        SQLiteDatabase db = helper.getWritableDatabase();

        DbBackend dbBackend = new DbBackend(RuntimeEnvironment.application);

        //Empty table, one singer added
        Singer singer = new Singer();
        singer.setName("AAAA");
        dbBackend.insertSingerUnique(singer);
        Assert.assertEquals(1, getCount(db, ARTISTS));

        //One another
        Singer singer2 = new Singer();
        singer.setName("BBB");
        dbBackend.insertSingerUnique(singer2);
        Assert.assertEquals(2, getCount(db, ARTISTS));

        //The same singer should not be added
        Singer singer3 = new Singer();
        singer.setName("AAAA");
        dbBackend.insertSingerUnique(singer3);
        int p =  getCount(db, ARTISTS);
        Assert.assertEquals(2, p);
    }

    @Test
    public void testInsertList() {
        DBHelper helper = new DBHelper(RuntimeEnvironment.application);
        SQLiteDatabase db = helper.getWritableDatabase();
        DbBackend dbBackend = new DbBackend(RuntimeEnvironment.application);
        List<Singer> singerList = new ArrayList<>();
        //Empty table, list of singers added
        Singer singer = new Singer();
        singer.setName("AAAA");
        Singer singer2 = new Singer();
        singer.setName("BBB");
        Singer singer3 = new Singer();
        singer.setName("AAAA");
        singerList.add(singer);
        singerList.add(singer2);
        singerList.add(singer3);
        dbBackend.insertList(singerList);
        int p = getCount(db, ARTISTS);
        Assert.assertEquals(3, p);
    }

    @Test
    public void testInsertUniqueList() {
        DBHelper helper = new DBHelper(RuntimeEnvironment.application);
        SQLiteDatabase db = helper.getWritableDatabase();
        DbBackend dbBackend = new DbBackend(RuntimeEnvironment.application);
        List<Singer> singerList = new ArrayList<>();
        //Empty table, list of singers added
        Singer singer = new Singer();
        singer.setName("AAAA");
        Singer singer2 = new Singer();
        singer.setName("BBB");
        Singer singer3 = new Singer();
        singer.setName("AAAA");
        singerList.add(singer);
        singerList.add(singer2);
        singerList.add(singer3);
        dbBackend.insertListUnique(singerList);
        int p = getCount(db, ARTISTS);
        Assert.assertEquals(2, p);
    }

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
        Cursor c = db.query(GENRES, null, null, null, null, null, null);
        Assert.assertEquals(c.getCount(), 1);
        c.close();
        ContentProvider provider = new MyContentProvider(RuntimeEnvironment.application);
        Cursor cursor = provider.query(CONTENT_URI, null, null, null, null);
        Singer s = new Singer();
        cursor.moveToFirst();
        setSinger(s, cursor);
        cursor.close();
        Assert.assertEquals(g.get(0), s.getGenres().get(0));
    }

    private int getCount(SQLiteDatabase db, String table) {
        return db.rawQuery("select * from " + table, null).getCount();
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
