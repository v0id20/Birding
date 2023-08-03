package com.github.v0id20.birding;

import android.util.Log;

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

public class ViewObservationsListModel {
    public static final String BASE_URL = "https://api.ebird.org/";
    public static final String apiKey = "k5529ocdk9i0";
    private static final String TAG = ViewObservationsListModel.class.toString();
    private ArrayList<BirdObservationDTO> apiResponse;
    private ArrayList<BirdObservationItem> birdObservationsData;
    private String country;
    private final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();
    private final MyApiEndpointInterface myApi = retrofit.create(MyApiEndpointInterface.class);

    public void getData(ViewObservationsListModel.onApiResponseReceived onApiResponseReceived, String countryName, String regionCode, String type) {
        Call<ArrayList<BirdObservationDTO>> callSync = null;
        country = countryName;
        if (type.equals(ViewObservationsListActivity.OBSERVATIONS_TYPE_RECENT)) {
            callSync = myApi.getRecentObservations(regionCode, apiKey);
        } else if (type.equals(ViewObservationsListActivity.OBSERVATIONS_TYPE_NOTABLE)) {
            callSync = myApi.getNotableObservations(regionCode, apiKey);
        }
        if (callSync != null) {
            sendQuery(callSync, onApiResponseReceived, country);
        }
    }

    public void getData(ViewObservationsListModel.onApiResponseReceived onApiResponseReceived, double latitude, double longitude, String type) {
        Call<ArrayList<BirdObservationDTO>> callSync = null;
        if (type.equals(ViewObservationsListActivity.OBSERVATIONS_TYPE_RECENT)) {
            callSync = myApi.getNearbyRecentObservations(latitude, longitude, apiKey);
        } else if (type.equals(ViewObservationsListActivity.OBSERVATIONS_TYPE_NOTABLE)) {
            callSync = myApi.getNearbyNotableObservations(latitude, longitude, apiKey);
        }
        if (callSync != null) {
            sendQuery(callSync, onApiResponseReceived, country);
        }
    }

    private void sendQuery(Call<ArrayList<BirdObservationDTO>> callSync, ViewObservationsListModel.onApiResponseReceived onApiResponse, String countryName) {
        callSync.enqueue(new Callback<ArrayList<BirdObservationDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<BirdObservationDTO>> call, Response<ArrayList<BirdObservationDTO>> response) {
                int statusCode = response.code();
                apiResponse = response.body();
                if (country == null || country.equals(countryName)) {
                    birdObservationsData = mapBirdObservations(apiResponse);
                    onApiResponse.onApiResponseReceived(birdObservationsData);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BirdObservationDTO>> call, Throwable t) {
                onApiResponse.onApiResponseReceived(birdObservationsData);
                System.out.println("Error");
            }
        });
    }

    public ArrayList<BirdObservationItem> mapBirdObservations(ArrayList<BirdObservationDTO> observationDTOArrayList) {
        return arrayListMappingFunction(observationDTOArrayList, this::mapBirdObservationDto);
    }

    private static <T, R> ArrayList<BirdObservationItem> arrayListMappingFunction(ArrayList<T> input, Function<T, BirdObservationItem> mapper) {
        ArrayList<BirdObservationItem> result = new ArrayList<>();
        String headerDate = "";
        if (input != null) {
            for (T birdObservationDTO : input) {
                BirdObservationItem birdObservationItem = mapper.apply((birdObservationDTO));
                if (headerDate.equals(birdObservationItem.getObservationDate())) {
                    result.add(birdObservationItem);
                } else {
                    BirdObservationDate obsDate = new BirdObservationDate();
                    if (birdObservationItem.getObservationDate() != null) {
                        obsDate.setObservationDate(birdObservationItem.getObservationDate());
                        result.add(obsDate);
                        result.add(birdObservationItem);
                        headerDate = obsDate.getObservationDate();
                    }
                }
            }
        }
        return result;
    }

    public BirdObservationItem mapBirdObservationDto(BirdObservationDTO birdObservationDTO) {
        String date = "";
        String time = "";
        Date inputDate;
        BirdObservation birdObservation = new BirdObservation();
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
            Log.w("ViewObservationModel", "mapBirdObservationDto: ", e);
        }
        birdObservation.setObservationDate(date);
        birdObservation.setTime(time);
        birdObservation.setCommonName(birdObservationDTO.getComName());
        birdObservation.setScientificName(birdObservationDTO.getSciName());
        birdObservation.setCountryName(country);
        birdObservation.setLocationName(birdObservationDTO.getLocName());
        birdObservation.setLatitude(birdObservationDTO.getLat());
        birdObservation.setLongitude(birdObservationDTO.getLng());
        birdObservation.setHowMany(birdObservationDTO.getHowMany());
        return birdObservation;
    }

    public interface onApiResponseReceived {
        void onApiResponseReceived(ArrayList<BirdObservationItem> apiResponse);
    }

    public interface onLocationsDecodedListener {
        void onLocationDecoded(String newAddress, int position);

        void onLocationDecodingFailure(int position);
    }

}
