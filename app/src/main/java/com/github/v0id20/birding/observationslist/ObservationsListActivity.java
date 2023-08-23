package com.github.v0id20.birding.observationslist;

import static java.util.Objects.requireNonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.github.v0id20.birding.birdobservationitem.BirdObservation;
import com.github.v0id20.birding.R;
import com.github.v0id20.birding.chooselocation.ChooseLocationActivity;
import com.google.android.material.tabs.TabLayout;


public class ObservationsListActivity extends AppCompatActivity {
    public static final String OBSERVATIONS_TYPE_RECENT = "recent";
    public static final String OBSERVATIONS_TYPE_NOTABLE = "notable";
    public static final String FRAGMENT_OBSERVATIONS_TYPE = "fragment";
    public static final int INVALID_LATITUDE = -1000;
    public static final int INVALID_LONGITUDE = -1000;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_observations_list);
        Intent i = getIntent();
        String regionCode = i.getStringExtra(BirdObservation.REGION_CODE_EXTRA);
        double currentLatitude = i.getDoubleExtra(BirdObservation.LATITUDE_EXTRA, INVALID_LATITUDE);
        double currentLongitude = i.getDoubleExtra(BirdObservation.LONGITUDE_EXTRA, INVALID_LONGITUDE);
        TextView header = findViewById(R.id.header);
        String countryName = null;
        if (!regionCode.equals(ChooseLocationActivity.REGION_CODE_NEARBY)) {
            String regionName = i.getStringExtra(BirdObservation.REGION_NAME_EXTRA);
            countryName = i.getStringExtra(BirdObservation.COUNTRY_NAME_EXTRA);
            header.setText(countryName + ", " + regionName);
        } else if (currentLatitude != INVALID_LATITUDE && currentLongitude != INVALID_LONGITUDE) {
            header.setText(getString(R.string.nearby));
        }
        Bundle locationData = new Bundle();
        locationData.putString(BirdObservation.REGION_CODE_EXTRA, regionCode);
        locationData.putString(BirdObservation.COUNTRY_NAME_EXTRA, countryName);
        locationData.putDouble(BirdObservation.LATITUDE_EXTRA, currentLatitude);
        locationData.putDouble(BirdObservation.LONGITUDE_EXTRA, currentLongitude);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.recent));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.notable));

        tabLayout.getTabAt(0).setCustomView(R.layout.tab_custom_selected);
        tabLayout.getTabAt(1).setCustomView(R.layout.tab_custom_unselected);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), locationData);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new MyTabListener());
        tabLayout.setTabRippleColor(null);
    }

    class MyTabListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            int mid = tabLayout.getWidth() / 2;
            float slideTo = tabLayout.getTabAt(1).getCustomView().getLeft() + mid - tabLayout.getTabAt(0).getCustomView().getLeft();
            if (tab.getPosition() == 0) {
                tab.view.setTranslationZ(3);
                requireNonNull(tabLayout.getTabAt(1)).view.setTranslationZ(0);
            } else if (tab.getPosition() == 1) {
                tab.view.setTranslationZ(3);
                requireNonNull(tabLayout.getTabAt(0)).view.setTranslationZ(0);
                slideTo = -slideTo;
            }
            TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
            translateAnimation.setDuration(200);
            MyAnimationListener myAnimationListener = new MyAnimationListener(tab, tab.getPosition());
            translateAnimation.setAnimationListener(myAnimationListener);
            tab.view.clearAnimation();
            tab.view.startAnimation(translateAnimation);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }

        class MyAnimationListener implements Animation.AnimationListener {
            int selectedTabIndex;
            TabLayout.Tab tab;

            public MyAnimationListener(TabLayout.Tab tab, int selectedTabIndex) {
                this.tab = tab;
                this.selectedTabIndex = selectedTabIndex;
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (selectedTabIndex == 0) {
                    tab.setCustomView(R.layout.tab_custom_unselected);
                    requireNonNull(tabLayout.getTabAt(1)).setCustomView(R.layout.tab_custom_selected);
                } else if (selectedTabIndex == 1) {
                    tab.setCustomView(R.layout.tab_custom_unselected);
                    requireNonNull(tabLayout.getTabAt(0)).setCustomView(R.layout.tab_custom_selected);

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        }
    }

}


