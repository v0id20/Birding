package com.github.v0id20.birding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String apiKey = "k5529ocdk9i0";

    //
    public static ArrayList<BirdObservation> birdObservationArrayList;
    ArrayList<BirdObservation> apiResponse;
    public static final String BASE_URL = "https://api.ebird.org/";
    private String regionCode;
    private double latitude;
    private double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        regionCode = i.getStringExtra("Code");
        latitude = i.getDoubleExtra("Latitude",-1);
        longitude = i.getDoubleExtra("Longitude",-1);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiResponse = new ArrayList<BirdObservation>();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        MyApiEndpointInterface myApi = retrofit.create(MyApiEndpointInterface.class);

        if (regionCode!=null) {
            Call<ArrayList<BirdObservation>> callSync = myApi.getObservations(regionCode, apiKey);
            sendQuery(callSync, recyclerView);
        } else if (latitude!=-1 && longitude!=-1) {
            Call<ArrayList<BirdObservation>> callSync = myApi.getNearbyObservations(latitude, longitude, apiKey);
            sendQuery(callSync, recyclerView);
        }

    }

    public void sendQuery( Call<ArrayList<BirdObservation>> callSync, RecyclerView recyclerView){
        callSync.enqueue(new Callback<ArrayList<BirdObservation>>() {
            @Override
            public void onResponse(Call<ArrayList<BirdObservation>> call, Response<ArrayList<BirdObservation>> response) {
                int statusCode = response.code();
                apiResponse = response.body();
                recyclerView.setAdapter(new ObservationAdapter(apiResponse));
            }
            @Override
            public void onFailure(Call<ArrayList<BirdObservation>> call, Throwable t) {
                // Log error here since request failed
                System.out.println("Error");
            }
        });
    }
}