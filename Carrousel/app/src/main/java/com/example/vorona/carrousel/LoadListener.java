package com.example.vorona.carrousel;

import com.yandex.disk.client.DownloadListener;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class LoadListener extends DownloadListener{

    public OutputStream out = new ByteArrayOutputStream();
    @Override
    public OutputStream getOutputStream(boolean append) throws IOException {
        if (!append) {
            out = new ByteArrayOutputStream();
        }
        return out;
    }
}
