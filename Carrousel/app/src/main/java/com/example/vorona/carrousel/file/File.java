package com.example.vorona.carrousel.file;

import com.yandex.disk.client.ListItem;

/**
 * Folder or Photo
 */
public class File {
    ListItem it;

    public String getName() {
        return it.getName();
    }

    public String getPath() {
        return it.getFullPath();
    }

    public ListItem getIt() {
        return it;
    }

    public long getSize() {
        return it.getContentLength();
    }

    public long getDate() {
        return it.getLastUpdated();
    }

}
