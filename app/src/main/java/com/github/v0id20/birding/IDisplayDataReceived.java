package com.github.v0id20.birding;

import java.util.ArrayList;

public interface IDisplayDataReceived {
    void displayDataReceived(ArrayList<BirdObservationItem> arrayList);

    void onItemClick(BirdObservation birdObservation);
}
