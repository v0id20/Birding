package com.github.v0id20.birding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String regionCode;
    private String countryName;
    private double latitude;
    private double longitude;
    private  ObservationModel observationModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        regionCode = i.getStringExtra(LocationPresenter.REGION_CODE_EXTRA);
        latitude = i.getDoubleExtra(LocationPresenter.LATITUDE_EXTRA,-1);
        longitude = i.getDoubleExtra(LocationPresenter.LONGITUDE_EXTRA,-1);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView header = findViewById(R.id.header);

        observationModel = new ObservationModel(regionCode, latitude, longitude);
        observationModel.getData(recyclerView);
        if (regionCode!=null) {
            countryName = i.getStringExtra(LocationPresenter.COUNTRY_NAME_EXTRA);
            header.setText(getString(R.string.recent_obs)+" in "+countryName);
        } else if (latitude!=-1 && longitude!=-1) {
            header.setText(getString(R.string.recent_obs)+" nearby");
        }

    }


}