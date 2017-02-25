package com.example.vorona.infocaller;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by vorona on 27.07.16.
 */

class SearchAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    SearchAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String res = "";
        try {
            Document doc = Jsoup.connect("https://yandex.ru/search/?text=" + strings[0]).get();
            Elements newsHeadlines = doc.getElementsByClass("serp-item__title");
            Element elem = newsHeadlines.get(0);
            String s = elem.toString();
            s = s.substring(s.indexOf("href=") + 6, s.length());
            s = s.substring(0, s.indexOf('"'));
            return s;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (context != null && s != null) {
            NotificationManagerCompat.from(context).notify(0, new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notify)
                    .setLargeIcon(BitmapFactory
                            .decodeResource(context.getResources(), R.drawable.ic_notify))
                    .setContentTitle("Поиск вызывающего номера")
                    .setContentText(s)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build());
            context = null;
        }
    }
}
