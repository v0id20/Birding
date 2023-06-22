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
    ArrayList<BirdObservationDTO> apiResponse;
    ArrayList<BirdObservationItem> birdObservationsData;

    public ViewObservationsListModel() {
    }

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();
    MyApiEndpointInterface myApi = retrofit.create(MyApiEndpointInterface.class);

    public void getData(ViewObservationsListModel.onApiResponseReceived onApiResponseReceived, String regionCode, double latitude, double longitude, String type) {
        Call<ArrayList<BirdObservationDTO>> callSync = null;
        if (type.equals(ViewObservationsListActivity.OBSERVATIONS_TYPE_RECENT)) {
            if (regionCode != null) {
                callSync = myApi.getObservations(regionCode, apiKey);
            } else if (latitude != -1 && longitude != -1) {
                callSync = myApi.getNearbyObservations(latitude, longitude, apiKey);
            }
        } else if (type.equals(ViewObservationsListActivity.OBSERVATIONS_TYPE_NOTABLE)) {
            if (regionCode != null) {
                callSync = myApi.getNotableObservations(regionCode, apiKey);
            } else if (latitude != -1 && longitude != -1) {
                callSync = myApi.getNearbyNotableObservations(latitude, longitude, apiKey);
            }
        }
        if (callSync != null) {
            sendQuery(callSync, onApiResponseReceived);
        }
    }

    private void sendQuery(Call<ArrayList<BirdObservationDTO>> callSync, ViewObservationsListModel.onApiResponseReceived onApiResponseReceived) {

        callSync.enqueue(new Callback<ArrayList<BirdObservationDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<BirdObservationDTO>> call, Response<ArrayList<BirdObservationDTO>> response) {
                int statusCode = response.code();
                apiResponse = response.body();
                birdObservationsData = mapBirdObservations(apiResponse);
                onApiResponseReceived.onApiResponseReceived(birdObservationsData);
            }

            @Override
            public void onFailure(Call<ArrayList<BirdObservationDTO>> call, Throwable t) {
                // Log error here since request failed
                onApiResponseReceived.onApiResponseReceived(birdObservationsData);
                System.out.println("Error");
            }
        });
    }

    public ArrayList<BirdObservationItem> mapBirdObservations(ArrayList<BirdObservationDTO> observationDTOArrayList) {
        return arrayListMappingFunction(observationDTOArrayList,
                this::mapBirdObservationDto);
    }

    private static <T, R> ArrayList<BirdObservationItem> arrayListMappingFunction(ArrayList<T> input, Function<T, BirdObservationItem> mapper) {
        ArrayList<BirdObservationItem> result = new ArrayList<>();
        String headerDate = "";
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
        return result;
    }

    public BirdObservationItem mapBirdObservationDto(BirdObservationDTO birdObservationDTO) {
        String date = "";
        String time = "";
        Date inputDate = null;
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
            Log.e("ViewObservationModel", "mapBirdObservationDto: ", e);

        }
        birdObservation.setDate(date);
        birdObservation.setTime(time);
        birdObservation.setCommonName(birdObservationDTO.getComName());
        birdObservation.setScientificName(birdObservationDTO.getSciName());
        birdObservation.setCountryName(birdObservationDTO.getCountryName());
        birdObservation.setLocationName(birdObservationDTO.getLocName());
        birdObservation.setLatitude(birdObservationDTO.getLatitude());
        birdObservation.setLongitude(birdObservationDTO.getLongitude());
        return birdObservation;
    }

    public interface onApiResponseReceived {
        void onApiResponseReceived(ArrayList<BirdObservationItem> apiResponse);
    }
}
