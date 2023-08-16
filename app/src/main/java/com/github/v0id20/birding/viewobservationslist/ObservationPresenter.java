package com.github.v0id20.birding.viewobservationslist;

import com.github.v0id20.birding.BirdObservation;
import com.github.v0id20.birding.BirdObservationItem;

import java.util.ArrayList;

public class ObservationPresenter implements ObservationAdapter.OnObservationClickListener, ViewObservationsListModel.onApiResponseReceived {
    private final ViewObservationsListModel viewObservationsListModel;
    private final IDisplayDataReceived displayDataObject;

    public ObservationPresenter(IDisplayDataReceived displayDataObject) {
        super();
        viewObservationsListModel = new ViewObservationsListModel();
        this.displayDataObject = displayDataObject;
    }

    public void getData(ViewObservationsListModel.onApiResponseReceived presenterInstance, String countryName, String regionCode, double latitude, double longitude, String type) {
        if (latitude != -1000 && longitude != -1000) {
            viewObservationsListModel.getData(presenterInstance, latitude, longitude, type);
        } else if (regionCode != null) {
            viewObservationsListModel.getData(presenterInstance, countryName, regionCode, type);
        }
    }


    //remove fragment parameter??
    private void updateContents(IDisplayDataReceived fragment, ArrayList<BirdObservationItem> arrayList) {
        fragment.displayDataReceived(arrayList);
    }

    private void setEmptyState() {
        displayDataObject.displayNoDataMessage();
    }

    @Override
    public void onBirdObservationClick(BirdObservation obs) {
        displayDataObject.onItemClick(obs);
    }

    @Override
    public void onApiResponseReceived(ArrayList<BirdObservationItem> arrayList) {
        if (arrayList.isEmpty()) {
            setEmptyState();
        } else {
            updateContents(displayDataObject, arrayList);
        }
    }

    @Override
    public void onErrorOccurred() {
        displayDataObject.displayErrorMessage();
    }
}
