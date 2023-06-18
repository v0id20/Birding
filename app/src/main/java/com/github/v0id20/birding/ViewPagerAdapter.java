package com.github.v0id20.birding;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Context myContext;
    private final Bundle bundle;
    int totalTabs;

    public ViewPagerAdapter(Context context, FragmentManager fm, int totalTabs, Bundle bundle) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentRecentObservations fragmentRecentObservations = new FragmentRecentObservations();
                fragmentRecentObservations.setArguments(bundle);
                return fragmentRecentObservations;
            case 1:
            default:
                FragmentNotableObservations fragmentNotableObservations = new FragmentNotableObservations();
                fragmentNotableObservations.setArguments(bundle);
                return fragmentNotableObservations;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
