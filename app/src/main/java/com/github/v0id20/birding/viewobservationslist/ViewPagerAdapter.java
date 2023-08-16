package com.github.v0id20.birding.viewobservationslist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final Bundle bundle;
    private final int totalTabs;

    public ViewPagerAdapter(FragmentManager fm, int totalTabs, Bundle bundle) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.totalTabs = totalTabs;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ObservationsFragment fragmentRecentObservations = new ObservationsFragment();
                Bundle bundleRecent = new Bundle(bundle);
                bundleRecent.putString(ViewObservationsListActivity.FRAGMENT_OBSERVATIONS_TYPE, ViewObservationsListActivity.OBSERVATIONS_TYPE_RECENT);
                fragmentRecentObservations.setArguments(bundleRecent);
                return fragmentRecentObservations;
            default:
                ObservationsFragment fragmentNotableObservations = new ObservationsFragment();
                Bundle bundleNotable = new Bundle(bundle);
                bundleNotable.putString(ViewObservationsListActivity.FRAGMENT_OBSERVATIONS_TYPE, ViewObservationsListActivity.OBSERVATIONS_TYPE_NOTABLE);
                fragmentNotableObservations.setArguments(bundleNotable);
                return fragmentNotableObservations;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}