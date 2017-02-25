package ru.yandex.yamblz.ui.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.loaders.ArtistLoader;
import ru.yandex.yamblz.model.Singer;
import ru.yandex.yamblz.ui.adapters.PerformerSelectedListener;
import ru.yandex.yamblz.ui.fragments.FullInfoDialogFragment;
import ru.yandex.yamblz.ui.fragments.ListFragment;
import ru.yandex.yamblz.ui.fragments.ListProvider;
import ru.yandex.yamblz.ui.fragments.OnMoreClicked;
import ru.yandex.yamblz.ui.fragments.PlaceHolderFragment;


public class TabbedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Singer>>,
        OnMoreClicked, ListProvider, PerformerSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static List<Singer> singers;

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onArticleSelected(int position) {
        if (mViewPager != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.full_info_container, FullInfoDialogFragment.newInstance(singers.get(position)), "info")
                    .addToBackStack("")
                    .commit();
        } else {
            DialogFragment newFragment = FullInfoDialogFragment.newInstance(singers.get(position));
            newFragment.show(getSupportFragmentManager(), "info");
        }
    }

    @Override
    public Loader<List<Singer>> onCreateLoader(int id, Bundle args) {
        return new ArtistLoader(this);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) openFragment();
        }
    };

    private void openFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.master, new ListFragment(), "list")
                .commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail, PlaceHolderFragment.newInstance(0), "info")
                    .commit();
    }

    @Override
    public void onLoadFinished(Loader<List<Singer>> loader, List<Singer> data) {
        singers = data;
        if (mViewPager != null) { //Phone version
            mViewPager.setAdapter(mSectionsPagerAdapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        } else { //Tablet version
            if (findViewById(R.id.master) == null)
                Log.w("Activity", "No Master");
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Singer>> loader) {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            if (findViewById(R.id.tabs) != null)
            findViewById(R.id.tabs).setVisibility(View.VISIBLE);
        } else
            super.onBackPressed();
    }

    @Override
    public List<Singer> getList() {
        return singers;
    }

    @Override
    public void onPerformerSelected(int singer) {
        Log.w("Activity", "PerformerSelected");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detail, PlaceHolderFragment.newInstance(singer), "info")
                .commit();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceHolderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return (singers != null) ? singers.size() : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return singers.get(position).getName();
        }
    }
}
