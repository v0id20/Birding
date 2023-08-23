package com.github.v0id20.birding.observationslist;

import com.github.v0id20.birding.birdobservationitem.BirdObservation;
import com.github.v0id20.birding.birdobservationitem.BirdObservationItem;

import java.util.ArrayList;

public interface IDisplayDataReceived {
    void displayDataReceived(ArrayList<BirdObservationItem> arrayList);

    void displayNoDataMessage();

    void displayErrorMessage();

    void onItemClick(BirdObservation birdObservation);
}
