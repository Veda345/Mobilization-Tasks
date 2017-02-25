package com.example.vorona.carrousel;

import android.content.Intent;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vorona.carrousel.file.Image;

import java.util.List;

/**
 * Activity for representing swiping fragments showing single images.
 */
public class SingleImageActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.app.FragmentStatePagerAdapter} that will provide
     * fragments for each of the sections.
     */
    private CollectionPagerAdapter collectionPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static List<Image> images;

    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent im = getIntent();
        images = im.getParcelableArrayListExtra("IMAGES");
        pos = im.getIntExtra("SELECTED", -10);
        setContentView(R.layout.activity_single_image);

        collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(collectionPagerAdapter);
        mViewPager.setCurrentItem(pos);

    }


    public void onPrevClick(View view) {
        onBackPressed();
    }


    public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putParcelable(PlaceholderFragment.ARG_SECTION_NUMBER, images.get(i));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return images.size();
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putParcelable(ARG_SECTION_NUMBER, images.get(sectionNumber));
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_single_image, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Bundle args = getArguments();
            SingleImageAsyncTask task = new SingleImageAsyncTask(this);
            task.execute(((Image) args.getParcelable(ARG_SECTION_NUMBER)));
            if ((args.getParcelable(ARG_SECTION_NUMBER)) != null) {
                ((TextView)view.findViewById(R.id.file_name)).setText(((Image) args.getParcelable(ARG_SECTION_NUMBER)).getName());
            }
        }


    }
}
