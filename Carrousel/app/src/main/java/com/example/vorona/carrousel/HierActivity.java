package com.example.vorona.carrousel;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.vorona.carrousel.file.Folder;

/**
 * Activity to represent file hierarchy.
 * Folders and image-files are only shown.
 */
public class HierActivity extends AppCompatActivity {

    /**
     * Attached fragment.
     */
    HierFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hier);
        run();

    }

    @Override
    public void onBackPressed() {
        if (fragment == null)
            super.onBackPressed();
        else
            fragment.onBackPressed();
    }

    public void onPlayClick(View view) {
            fragment.onPlayClick();
    }

    public void setFragment(HierFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Try to reload content of selected folder.
     */
    public void onRetryClick(View view) {
        run();
    }

    /**
     * Opens new fragment with content of selected folder.
     */
    private void run() {
        fragment = HierFragment.newInstance(new Folder());
        FragmentTransaction fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragment_holder, fragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }
}
