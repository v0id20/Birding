package com.github.v0id20.birding;

import java.util.ArrayList;

public class ObservationPresenter implements ObservationAdapter.OnObservationClickListener, ViewObservationsListModel.onApiResponseReceived {

    private ViewObservationsListModel viewObservationsListModel;
    private IDisplayData displayDataObject;

    public ObservationPresenter(IDisplayData displayDataObject) {
        super();
        viewObservationsListModel = new ViewObservationsListModel();
        this.displayDataObject = displayDataObject;
    }

    public void getData(ViewObservationsListModel.onApiResponseReceived presenterInstance, String regionCode, double latitude, double longitude, String type) {
        viewObservationsListModel.getData(presenterInstance, regionCode, latitude, longitude, type);
    }

    public void updateContents(IDisplayData fragment, ArrayList<BirdObservation> arrayList) {
        fragment.displayData(arrayList);

    }

    @Override
    public void onBirdObservationClick(BirdObservation obs) {
        displayDataObject.onItemClick(obs);
    }

    @Override
    public void onApiResponseReceived(ArrayList<BirdObservation> arrayList) {
        updateContents(displayDataObject, arrayList);
    }
}
