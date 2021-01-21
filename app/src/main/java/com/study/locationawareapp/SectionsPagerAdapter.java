package com.study.locationawareapp;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.study.locationawareapp.ui.destination.DestinationFragment;
import com.study.locationawareapp.ui.directions.DirectionsFragment;
import com.study.locationawareapp.ui.map.MapFragment;

import org.jetbrains.annotations.NotNull;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.title_destination, R.string.title_map, R.string.title_directions};
    private final Context mContext;
    private final DestinationFragment destinationFragment;
    private final MapFragment mapFragment;
    private final DirectionsFragment directionsFragment;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        this.destinationFragment = new DestinationFragment();
        this.mapFragment = new MapFragment();
        this.directionsFragment = new DirectionsFragment();
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return destinationFragment;
            case 1:
                return mapFragment;
            case 2:
                return directionsFragment;
        }
        return mapFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}