package com.github.v0id20.birding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        birdObservationArrayList = new ArrayList<BirdObservation>();
        //https://api.ebird.org/v2/data/obs/CA/recent/notable
        apiResponse = new ArrayList<BirdObservation>();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        MyApiEndpointInterface myApi = retrofit.create(MyApiEndpointInterface.class);


        Call<ArrayList<BirdObservation>> callSync = myApi.getObservations("RO",apiKey);



        callSync.enqueue(new Callback<ArrayList<BirdObservation>>() {
            @Override
            public void onResponse(Call<ArrayList<BirdObservation>> call, Response<ArrayList<BirdObservation>> response) {
                int statusCode = response.code();
                apiResponse = response.body();
                System.out.println("Fine");
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