package com.github.v0id20.birding;

import java.util.ArrayList;

public interface IDisplayData {
    void displayData(ArrayList<BirdObservation> arrayList);

    void onItemClick(BirdObservation birdObservation);
}
