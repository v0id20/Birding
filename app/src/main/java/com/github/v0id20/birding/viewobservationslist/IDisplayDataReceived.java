package com.github.v0id20.birding.viewobservationslist;

import com.github.v0id20.birding.BirdObservation;
import com.github.v0id20.birding.BirdObservationItem;

import java.util.ArrayList;

public interface IDisplayDataReceived {
    void displayDataReceived(ArrayList<BirdObservationItem> arrayList);

    void displayNoDataMessage();

    void displayErrorMessage();

    void onItemClick(BirdObservation birdObservation);
}
