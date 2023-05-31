package com.github.v0id20.birding;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.function.Function;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ObservationModel {
    public static final String BASE_URL = "https://api.ebird.org/";
    public static final String apiKey = "k5529ocdk9i0";
    private String scientificName;

    ArrayList<BirdObservation> apiResponse;
    private String commonName;
    private String regionCode;
    private double latitude;
    private double longitude;

    private ObservationAdapter observationAdapter;

    public void setObservationPresenter(ObservationPresenter observationPresenter) {
        this.observationPresenter = observationPresenter;
    }

    private ObservationPresenter observationPresenter;

    public ObservationModel(String commonName, String scientificName, String regionCode, double latitude, double longitude) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.regionCode = regionCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ObservationModel(String regionCode, double latitude, double longitude, ObservationAdapter observationAdapter) {
        this.regionCode = regionCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.observationAdapter = observationAdapter;
    }


    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();
    MyApiEndpointInterface myApi = retrofit.create(MyApiEndpointInterface.class);

    public void getData(RecyclerView recyclerView) {
        if (regionCode != null) {
            Call<ArrayList<BirdObservation>> callSync = myApi.getObservations(regionCode, apiKey);
            sendQuery(callSync, recyclerView);
        } else if (latitude!=-1 && longitude!=-1) {
            Call<ArrayList<BirdObservation>> callSync = myApi.getNearbyObservations(latitude, longitude, apiKey);
            sendQuery(callSync, recyclerView);
        }
    }
    public void sendQuery(Call<ArrayList<BirdObservation>> callSync, RecyclerView recyclerView){
        callSync.enqueue(new Callback<ArrayList<BirdObservation>>() {
            @Override
            public void onResponse(Call<ArrayList<BirdObservation>> call, Response<ArrayList<BirdObservation>> response) {
                int statusCode = response.code();
                apiResponse = response.body();
                observationAdapter.updateData(apiResponse);
                recyclerView.setAdapter(observationAdapter);
                observationPresenter.onContentLoaded();
            }

            @Override
            public void onFailure(Call<ArrayList<BirdObservation>> call, Throwable t) {
                // Log error here since request failed
                observationPresenter.onContentLoaded();
                System.out.println("Error");
            }
        });
    }
}
