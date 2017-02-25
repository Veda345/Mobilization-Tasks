package com.example.vorona.server.db;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;

import com.example.vorona.server.model.Singer;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 31/07/2016.
 */
public class DbProvider {

    private final DbBackend mDbBackend;
    private final CustomExecutor mExecutor;

    public DbProvider(Context context) {
        mDbBackend = new DbBackend(context);
        mExecutor = new CustomExecutor();
    }

    @VisibleForTesting
    DbProvider(DbBackend dbBackend,
               CustomExecutor executor) {
        mDbBackend = dbBackend;
        mExecutor = executor;
    }

    public void insertListUnique(final List<Singer> singer) {
        mExecutor.execute(() -> mDbBackend.insertListUnique(singer));
    }

    public void insertList(final List<Singer> singer) {
        mExecutor.execute(() -> mDbBackend.insertList(singer));
    }

    private class CustomExecutor extends ThreadPoolExecutor {
        CustomExecutor() {
            super(2, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        }
    }
}
