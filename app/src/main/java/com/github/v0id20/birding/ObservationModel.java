package com.github.v0id20.birding;

import androidx.recyclerview.widget.RecyclerView;

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

public class ObservationModel {
    public static final String BASE_URL = "https://api.ebird.org/";
    public static final String apiKey = "k5529ocdk9i0";
    private String scientificName;
    ArrayList<BirdObservationDTO> apiResponse;
    ArrayList<BirdObservation> birdObservationsData;
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
            Call<ArrayList<BirdObservationDTO>> callSync = myApi.getObservations(regionCode, apiKey);
            sendQuery(callSync, recyclerView);
        } else if (latitude != -1 && longitude != -1) {
            Call<ArrayList<BirdObservationDTO>> callSync = myApi.getNearbyObservations(latitude, longitude, apiKey);
            sendQuery(callSync, recyclerView);
        }
    }

    public void sendQuery(Call<ArrayList<BirdObservationDTO>> callSync, RecyclerView recyclerView) {
        callSync.enqueue(new Callback<ArrayList<BirdObservationDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<BirdObservationDTO>> call, Response<ArrayList<BirdObservationDTO>> response) {
                int statusCode = response.code();
                apiResponse = response.body();
                birdObservationsData = mapBirdObservations(apiResponse);
                observationAdapter.updateData(birdObservationsData);
                recyclerView.setAdapter(observationAdapter);
                observationPresenter.onContentLoaded();
            }

            @Override
            public void onFailure(Call<ArrayList<BirdObservationDTO>> call, Throwable t) {
                // Log error here since request failed
                observationPresenter.onContentLoaded();
                System.out.println("Error");
            }
        });
    }

    public ArrayList<BirdObservation> mapBirdObservations(ArrayList<BirdObservationDTO> observationDTOArrayList) {
        return arrayListMappingFunction(observationDTOArrayList,
                this::mapBirdObservationDto);
    }

    private static <T, R> ArrayList<R> arrayListMappingFunction(ArrayList<T> input, Function<T, R> mapper) {
        ArrayList<R> result = new ArrayList<>();
        for (T birdObservationDTO : input) {
            result.add(mapper.apply(birdObservationDTO));
        }
        return result;
    }

    public BirdObservation mapBirdObservationDto(BirdObservationDTO birdObservationDTO) {
        String date = null;
        String time = null;
        Date inputDate = null;
        BirdObservation birdObservation = new BirdObservation();
        String inputDatePattern = "yyyy-mm-dd hh:mm";
        String outputDatePattern = "dd-MMM-yy";
        String outputTimePattern = "hh:mm";
        SimpleDateFormat inputDateFormatter = new SimpleDateFormat(inputDatePattern);
        try {
            inputDate = inputDateFormatter.parse(birdObservationDTO.getObsDt());
            SimpleDateFormat outputDateFormatter = new SimpleDateFormat(outputDatePattern);
            date = outputDateFormatter.format(inputDate);
            SimpleDateFormat outputTimeFormatter = new SimpleDateFormat(outputTimePattern);
            time = outputTimeFormatter.format(inputDate);
        } catch (ParseException e) {
            time = null;
            date = null;
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

}
