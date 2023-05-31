package com.github.v0id20.birding;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class ObservationPresenter implements ObservationAdapter.OnObservationClickListener {
    private Context context;

    public ObservationPresenter(Context context) {
        this.context = context;
    }


    @Override
    public void onBirdObservationClick(BirdObservation obs) {
        Intent i = new Intent(context, ViewBirdObservationActivity.class);
        i.putExtra(BirdObservation.COMMON_NAME_EXTRA, obs.getComName());
        i.putExtra(BirdObservation.SCIENTIFIC_NAME_EXTRA, obs.getSciName());
        i.putExtra(BirdObservation.COUNTRY_NAME_EXTRA, obs.getCountryName());
        i.putExtra(BirdObservation.LOCATION_NAME_EXTRA, obs.getLocName());
        i.putExtra(BirdObservation.LATITUDE_EXTRA, obs.getLatitude());
        i.putExtra(BirdObservation.LONGITUDE_EXTRA, obs.getLongitude());
        i.putExtra(BirdObservation.OBSERVATION_DATE_EXTRA, obs.getObsDt());
        ((AppCompatActivity) context).startActivity(i);
    }

    public void onContentLoaded() {
        ((ViewObservationsListActivity) context).setLoaderGone();
    }
}
