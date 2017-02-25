package com.example.vorona.carrousel;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vorona.carrousel.file.File;
import com.example.vorona.carrousel.file.Folder;
import com.example.vorona.carrousel.file.Image;
import com.example.vorona.carrousel.list.FirstRecyclerAdapter;
import com.example.vorona.carrousel.list.PerformerSelectedListener;

import java.util.ArrayList;

public class HierFragment extends Fragment
        implements PerformerSelectedListener, NavigationView.OnNavigationItemSelectedListener {


    private HierAsyncTask downloadTask;

    private RecyclerView rv;
    private ProgressBar p_bar;
    private TextView title, error;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ImageView retry;

    private Folder cur_folder;


    public static HierFragment newInstance(Folder folder) {

        Bundle args = new Bundle();
        args.putParcelable("Folder", folder);
        HierFragment fragment = new HierFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hier, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cur_folder = getArguments().getParcelable("Folder");
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Customize toolbar and navigationView
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.navView);
        navigationView.setCheckedItem(R.id.nav_disk_fragment);
        navigationView.setNavigationItemSelectedListener(this);

        title = (TextView) view.findViewById(R.id.title);
        error = (TextView) view.findViewById(R.id.txt_perf);
        rv = (RecyclerView) view.findViewById(R.id.list_perf);
        p_bar = (ProgressBar) view.findViewById(R.id.progress_perf);
        retry = (ImageView) view.findViewById(R.id.imgRetry);

        int count = getFragmentManager().getBackStackEntryCount();
        if (count >= 1) {
            toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
            title.setText(cur_folder.getName());
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_hamburger);
            title.setText(getString(R.string.app_name));
        }

        rv.setAdapter(new FirstRecyclerAdapter(new ArrayList<File>()));
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

        downloadTask = new HierAsyncTask(this);
        SharedPreferences pref = getActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        String token = pref.getString("Code", "-1");
        downloadTask.execute(token, cur_folder.getPath());

    }


    void updateView(HierAsyncTask task) {
        switch (task.getState()) {
            case DOWNLOADING:
                p_bar.setVisibility(View.VISIBLE);
                rv.setVisibility(View.INVISIBLE);
                error.setVisibility(View.INVISIBLE);
                retry.setVisibility(View.INVISIBLE);
                break;
            case DONE:
                p_bar.setVisibility(View.INVISIBLE);
                rv.setVisibility(View.VISIBLE);
                error.setVisibility(View.INVISIBLE);
                retry.setVisibility(View.INVISIBLE);
                break;
            case ERROR:
                p_bar.setVisibility(View.INVISIBLE);
                rv.setVisibility(View.INVISIBLE);
                error.setVisibility(View.VISIBLE);
                error.setText(getString(R.string.error));
                retry.setVisibility(View.VISIBLE);
                break;
            case EMPTY:
                p_bar.setVisibility(View.INVISIBLE);
                rv.setVisibility(View.INVISIBLE);
                error.setVisibility(View.VISIBLE);
                error.setText(getString(R.string.empty_folder));
                retry.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

    }

    @Override
    public void onPerformerSelected(File file) {
        if (file instanceof Folder) {
            ((HierActivity)getActivity()).setFragment(createFragment((Folder) file));
        } else {
            if (file instanceof Image) {
                Intent intent = new Intent(getActivity(), SingleImageActivity.class);
                intent.putParcelableArrayListExtra("IMAGES", downloadTask.images);
                int pos = downloadTask.images.indexOf(file);
                intent.putExtra("SELECTED", pos);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                onBackPressed();
            } else {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);

                }
            }
        }
        return true;
    }

    /**
     * Open selected activity. If selected activity and current activity are the same won't do anything.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_disk_fragment) {
//TODO
        } else if (id == R.id.nav_prefs_fragment) {
            getActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE).edit().remove("Code").commit();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getFragmentManager().getBackStackEntryCount();

            if (count == 1) {
                getActivity().onBackPressed();
                getFragmentManager().popBackStack();
                return;
            }

            if (count == 2) {
                toolbar.setNavigationIcon(R.drawable.ic_hamburger);
                title.setText(getString(R.string.app_name));
                getFragmentManager().popBackStack();
                return;
            }

            title.setText(cur_folder.getName());
            if (downloadTask.getState() == DownloadState.DOWNLOADING) {
                downloadTask.cancel(true);
            }
            getFragmentManager().popBackStack();
        }

    }

    public HierFragment createFragment(Folder folder) {
        HierFragment fragment = HierFragment.newInstance(folder);
        FragmentTransaction fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragment_holder, fragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
        return fragment;
    }

    void onPlayClick() {
        if (downloadTask.images.size() > 0) {
            Intent intent = new Intent(getActivity(), SlideShowActivity.class);
            intent.putParcelableArrayListExtra("IMAGES", downloadTask.images);
            startActivity(intent);
        }
    }
}
