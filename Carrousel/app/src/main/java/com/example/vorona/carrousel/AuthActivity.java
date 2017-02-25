package com.example.vorona.carrousel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Starting Activity with OAuth
 */
public class AuthActivity extends Activity {

    private static String CLIENT_ID = "f946d1f8d9264ee1b3dd559e6949194d";
    private static String OAUTH_URL ="https://oauth.yandex.ru/authorize";

    WebView web;
    Button auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //Prefs stores current access_token
        final SharedPreferences pref = getSharedPreferences("Prefs", MODE_PRIVATE);

        if (!pref.getString("Code", "-1").equals("-1")) {
            Intent gr = new Intent(this, HierActivity.class);
            startActivity(gr);
            overridePendingTransition(R.anim.from_right, R.anim.to_left);
        }
        auth = (Button)findViewById(R.id.auth);
        auth.setOnClickListener(new View.OnClickListener() {
            Dialog auth_dialog;
            @Override
            public void onClick(View arg0) {
                auth_dialog = new Dialog(AuthActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                web = (WebView)auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(OAUTH_URL + "?response_type=token&client_id=" + CLIENT_ID);
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);

                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        String authCode;
                        if (url.contains("access_token")) {
                            authCode = url.substring(url.indexOf("access_token="));
                            authCode = authCode.substring(13, authCode.indexOf("&"));
                            authComplete = true;

                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("Code", authCode);
                            edit.commit();

                            auth_dialog.dismiss();
                            Intent gr = new Intent(AuthActivity.this, HierActivity.class);
                            startActivity(gr);
                            overridePendingTransition(R.anim.from_right, R.anim.to_left);
                            finish();
                        } else if (url.contains("error=access_denied")) {
                            authComplete = false;
                            auth_dialog.dismiss();
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setTitle("Авторизация");
                auth_dialog.setCancelable(true);
            }
        });
    }

}