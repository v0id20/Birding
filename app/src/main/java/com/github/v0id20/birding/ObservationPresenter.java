package com.github.v0id20.birding;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class ObservationPresenter implements ObservationAdapter.OnObservationClickListener {
    private Context context;

    public ObservationPresenter(Context context) {
        this.context = context;
    }


    @Override
    public void onClick(BirdObservation obs) {

    }
}
