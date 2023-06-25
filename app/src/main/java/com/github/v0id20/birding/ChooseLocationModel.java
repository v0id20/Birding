package com.github.v0id20.birding;

import android.util.Log;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChooseLocationModel {

    public static final String BASE_URL = "https://api.ebird.org/";
    public static final String REGION_TYPE = "country";
    private static final String PARENT_REGION_CODE = "world";
    public static final String apiKey = "k5529ocdk9i0";

    public ArrayList<LocationViewType> getLocationList() {
        return locationList;
    }

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private ArrayList<LocationViewType> locationList;
    private ChooseLocationPresenter chooseLocationPresenter;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();

    MyApiEndpointInterface myApi = retrofit.create(MyApiEndpointInterface.class);

    public ChooseLocationModel(ChooseLocationPresenter chooseLocationPresenter) {
        this.chooseLocationPresenter = chooseLocationPresenter;

//
//        locationList = new ArrayList<LocationViewType>();
//        locationList.add(new LocationViewType(1, "My location"));
//        locationList.add(new LocationViewType(2, "Albania", "AL"));
//        locationList.add(new LocationViewType(2, "Botswana", "BW"));
//        locationList.add(new LocationViewType(2, "Brazil", "BR"));
//        locationList.add(new LocationViewType(2, "Canada", "CA"));
//        locationList.add(new LocationViewType(2, "Ethiopia", "ET"));
//        locationList.add(new LocationViewType(2, "Germany", "DE"));
//        locationList.add(new LocationViewType(2, "Guatemala", "GT"));
//        locationList.add(new LocationViewType(2, "Kazakhstan", "KZ"));
//        locationList.add(new LocationViewType(2, "Latvia", "LV"));
//        locationList.add(new LocationViewType(2, "Malawi", "MW"));
//        locationList.add(new LocationViewType(2, "Romania", "RO"));
//        locationList.add(new LocationViewType(2, "Slovenia", "SI"));
//        locationList.add(new LocationViewType(2, "Turkey", "TR"));
//        locationList.add(new LocationViewType(2, "USA", "US"));
//        locationList.add(new LocationViewType(2, "Zambia", "ZM"));
    }

    public void requestCountriesList() {

        Call<ArrayList<LocationViewType>> call = null;
        call = myApi.getCountriesList(REGION_TYPE, PARENT_REGION_CODE, apiKey);
        if (call != null) {
            sendQuery(call);
        }
    }


    public void sendQuery(Call<ArrayList<LocationViewType>> call) {
        call.enqueue(new Callback<ArrayList<LocationViewType>>() {
            @Override
            public void onResponse(Call<ArrayList<LocationViewType>> call, Response<ArrayList<LocationViewType>> response) {
                locationList = response.body();
                for (LocationViewType v : locationList) {
                    v.setViewType(2);
                }
                locationList.add(0, new LocationViewType(1, "My Location"));
                chooseLocationPresenter.onLocationDataReceived(locationList);
            }

            @Override
            public void onFailure(Call<ArrayList<LocationViewType>> call, Throwable t) {
                Log.d("LocationModel", "onFailure: was not able to retrieve data", t);
            }
        });

    }
}
