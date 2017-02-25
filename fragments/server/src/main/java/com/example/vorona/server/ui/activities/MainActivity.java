package com.example.vorona.server.ui.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.vorona.server.R;
import com.example.vorona.server.db.DbBackend;
import com.example.vorona.server.db.DbProvider;
import com.example.vorona.server.loaders.JsonLoader;
import com.example.vorona.server.model.Singer;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Singer>> {

    @BindView(R.id.txtResult)
    TextView resulst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<Singer>> onCreateLoader(int i, Bundle bundle) {
        return new JsonLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Singer>> loader, List<Singer> singerList) {
        if (singerList != null) {
            resulst.setText("Downloaded!");
            DbProvider provider = new DbProvider(this);
            provider.insertListUnique(singerList);
        }
        else
            resulst.setText("Failed!");
    }

    @Override
    public void onLoaderReset(Loader<List<Singer>> loader) {

    }
}
