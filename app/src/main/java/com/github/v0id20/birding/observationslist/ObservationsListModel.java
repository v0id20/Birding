package com.github.v0id20.birding.observationslist;

import android.util.Log;

import com.github.v0id20.birding.BuildConfig;
import com.github.v0id20.birding.birdobservationitem.BirdObservation;
import com.github.v0id20.birding.birdobservationitem.BirdObservationDate;
import com.github.v0id20.birding.birdobservationitem.BirdObservationItem;
import com.github.v0id20.birding.data.BirdApiEndpointInterface;
import com.github.v0id20.birding.data.BirdObservationDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ObservationsListModel {
    public static final String BASE_URL = "https://api.ebird.org/";
    private static final String apiKey = BuildConfig.EBIRD_API_KEY;
    private static final String TAG = ObservationsListModel.class.toString();
    private ArrayList<BirdObservationDto> apiResponse;
    private ArrayList<BirdObservationItem> birdObservationsData;
    private String country;
    private final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();
    private final BirdApiEndpointInterface myApi = retrofit.create(BirdApiEndpointInterface.class);

    public void getData(OnApiResponseReceived onApiResponseReceived, String countryName, String regionCode, String type) {
        Call<ArrayList<BirdObservationDto>> callSync = null;
        country = countryName;
        if (type.equals(ObservationsListActivity.OBSERVATIONS_TYPE_RECENT)) {
            callSync = myApi.getRecentObservations(regionCode, apiKey);
        } else if (type.equals(ObservationsListActivity.OBSERVATIONS_TYPE_NOTABLE)) {
            callSync = myApi.getNotableObservations(regionCode, apiKey);
        }
        if (callSync != null) {
            sendQuery(callSync, onApiResponseReceived, country);
        }
    }

    public void getData(OnApiResponseReceived onApiResponseReceived, double latitude, double longitude, String type) {
        Call<ArrayList<BirdObservationDto>> callSync = null;
        if (type.equals(ObservationsListActivity.OBSERVATIONS_TYPE_RECENT)) {
            callSync = myApi.getNearbyRecentObservations(latitude, longitude, apiKey);
        } else if (type.equals(ObservationsListActivity.OBSERVATIONS_TYPE_NOTABLE)) {
            callSync = myApi.getNearbyNotableObservations(latitude, longitude, apiKey);
        }
        if (callSync != null) {
            sendQuery(callSync, onApiResponseReceived, country);
        }
    }

    private void sendQuery(Call<ArrayList<BirdObservationDto>> callSync, OnApiResponseReceived onApiResponse, String countryName) {
        callSync.enqueue(new Callback<ArrayList<BirdObservationDto>>() {
            @Override
            public void onResponse(Call<ArrayList<BirdObservationDto>> call, Response<ArrayList<BirdObservationDto>> response) {
                if (response.isSuccessful()) {
                    apiResponse = response.body();
                    if (country == null || country.equals(countryName)) {
                        birdObservationsData = mapBirdObservations(apiResponse);
                        onApiResponse.onApiResponseReceived(birdObservationsData);
                    }
                } else {
                    onApiResponse.onErrorOccurred();
                    Log.d(TAG, "sendQuery onResponse: could not to retrieve bird observations");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BirdObservationDto>> call, Throwable t) {
                onApiResponse.onErrorOccurred();
                Log.d(TAG, "sendQuery onFailure: could not to retrieve bird observations. ", t);
            }
        });
    }

    public ArrayList<BirdObservationItem> mapBirdObservations(ArrayList<BirdObservationDto> observationDTOArrayList) {
        return arrayListMappingFunction(observationDTOArrayList, this::mapBirdObservationDto);
    }

    private static ArrayList<BirdObservationItem> arrayListMappingFunction(ArrayList<BirdObservationDto> input, Function<BirdObservationDto, BirdObservationItem> mapper) {
        ArrayList<BirdObservationItem> result = new ArrayList<>();
        String headerDate = "";
        if (input != null) {
            for (BirdObservationDto birdObservationDTO : input) {
                BirdObservationItem birdObservationItem = mapper.apply((birdObservationDTO));
                if (headerDate.equals(birdObservationItem.getObservationDate())) {
                    result.add(birdObservationItem);
                } else {

                    if (birdObservationItem.getObservationDate() != null) {
                        BirdObservationDate obsDate = new BirdObservationDate(birdObservationItem.getObservationDate());
                        result.add(obsDate);
                        result.add(birdObservationItem);
                        headerDate = obsDate.getObservationDate();
                    }
                }
            }
        }
        return result;
    }

    public BirdObservationItem mapBirdObservationDto(BirdObservationDto birdObservationDTO) {
        String date = "";
        String time = "";
        Date inputDate;
        String inputDatePattern = "yyyy-MM-dd HH:mm";
        String outputDatePattern = "dd MMM";
        String outputTimePattern = "HH:mm";
        SimpleDateFormat inputDateFormatter = new SimpleDateFormat(inputDatePattern);
        try {
            inputDate = inputDateFormatter.parse(birdObservationDTO.getObsDt());
            SimpleDateFormat outputDateFormatter = new SimpleDateFormat(outputDatePattern);
            date = outputDateFormatter.format(inputDate);
            SimpleDateFormat outputTimeFormatter = new SimpleDateFormat(outputTimePattern);
            time = outputTimeFormatter.format(inputDate);
        } catch (ParseException e) {
            Log.d(TAG, "mapBirdObservationDto: could not parse date or time. ", e);
        }
        BirdObservation birdObservation = new BirdObservation(birdObservationDTO.getComName(), birdObservationDTO.getSciName(), country, birdObservationDTO.getLocName(), date, time, birdObservationDTO.getLat(), birdObservationDTO.getLng(), birdObservationDTO.getHowMany());
        birdObservation.setLocationDecoded(false);
        return birdObservation;
    }

    public interface OnApiResponseReceived {
        void onApiResponseReceived(ArrayList<BirdObservationItem> apiResponse);

        void onErrorOccurred();
    }

}
