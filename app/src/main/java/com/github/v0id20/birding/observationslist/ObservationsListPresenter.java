package com.github.v0id20.birding.observationslist;

import com.github.v0id20.birding.birdobservationitem.BirdObservation;
import com.github.v0id20.birding.birdobservationitem.BirdObservationItem;
import com.github.v0id20.birding.chooselocation.ChooseLocationActivity;

import java.util.ArrayList;

public class ObservationsListPresenter implements ObservationAdapter.OnObservationClickListener, ObservationsListModel.OnApiResponseReceived {
    private final ObservationsListModel viewObservationsListModel;
    private final IDisplayDataReceived displayDataObject;

    public ObservationsListPresenter(IDisplayDataReceived displayDataObject) {
        viewObservationsListModel = new ObservationsListModel();
        this.displayDataObject = displayDataObject;
    }

    public void getData(String countryName, String regionCode, double latitude, double longitude, String type) {
        if (latitude != ObservationsListActivity.INVALID_LATITUDE && longitude != -ObservationsListActivity.INVALID_LONGITUDE) {
            viewObservationsListModel.getData(this, latitude, longitude, type);
        } else if (!regionCode.equals(ChooseLocationActivity.REGION_CODE_NEARBY)) {
            viewObservationsListModel.getData(this, countryName, regionCode, type);
        }
    }

    private void updateContents(ArrayList<BirdObservationItem> arrayList) {
        displayDataObject.displayDataReceived(arrayList);
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
            updateContents(arrayList);
        }
    }

    @Override
    public void onErrorOccurred() {
        displayDataObject.displayErrorMessage();
    }

    public void retryRequest(String countryName, String regionCode, double latitude, double longitude, String type) {
        displayDataObject.setLoadingState();
        getData(countryName, regionCode, latitude, longitude, type);
    }
}
