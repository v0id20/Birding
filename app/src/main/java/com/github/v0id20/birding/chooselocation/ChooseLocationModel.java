package com.github.v0id20.birding.chooselocation;

import android.util.Log;

import com.github.v0id20.birding.mylocation.LocationCountry;
import com.github.v0id20.birding.LocationDto;
import com.github.v0id20.birding.mylocation.LocationRegion;
import com.github.v0id20.birding.MyApiEndpointInterface;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChooseLocationModel {
    public static final String BASE_URL = "https://api.ebird.org/";
    public static final String REGION_TYPE_COUNTRY = "country";
    public static final String REGION_TYPE_SUBNATIONAL1 = "subnational1";
    private static final String PARENT_REGION_CODE = "world";
    public static final String apiKey = "k5529ocdk9i0";
    private ArrayList<LocationDto> apiResponse;
    private final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();
    private final MyApiEndpointInterface myApi = retrofit.create(MyApiEndpointInterface.class);

    public void requestCountriesList(OnRequestResultCallback callback) {
        Call<ArrayList<LocationDto>> call = myApi.getLocationsList(REGION_TYPE_COUNTRY, PARENT_REGION_CODE, apiKey);
        if (call != null) {
            sendCountryQuery(call, callback);
        }
    }

    public void requestSubnationalRegionsList(LocationCountry country, OnRequestResultCallback callback) {
        Call<ArrayList<LocationDto>> call = myApi.getLocationsList(REGION_TYPE_SUBNATIONAL1, country.getLocationCode(), apiKey);
        if (call != null) {
            sendRegionQuery(call, callback, country);
        }
    }

    private void sendCountryQuery(Call<ArrayList<LocationDto>> call, OnRequestResultCallback callback) {
        call.enqueue(new Callback<ArrayList<LocationDto>>() {
            @Override
            public void onResponse(Call<ArrayList<LocationDto>> call, Response<ArrayList<LocationDto>> response) {
                apiResponse = response.body();
                ArrayList<LocationCountry> locationList = convertResultToLocationCountryArrayList(apiResponse);
                callback.onRequestResult(locationList);
            }

            @Override
            public void onFailure(Call<ArrayList<LocationDto>> call, Throwable t) {
                Log.d("LocationModel", "onFailure: was not able to retrieve country data", t);
                callback.onRequestFailure();
            }
        });
    }

    private void sendRegionQuery(Call<ArrayList<LocationDto>> call, OnRequestResultCallback callback, LocationCountry country) {
        call.enqueue(new Callback<ArrayList<LocationDto>>() {
            @Override
            public void onResponse(Call<ArrayList<LocationDto>> call, Response<ArrayList<LocationDto>> response) {
                apiResponse = response.body();
                ArrayList<LocationRegion> locationList = convertResultToLocationRegionArrayList(apiResponse, country);
                callback.onRequestResult(locationList);
            }

            @Override
            public void onFailure(Call<ArrayList<LocationDto>> call, Throwable t) {
                Log.d("LocationModel", "onFailure: was not able to retrieve subnational regions data", t);
                callback.onRequestFailure();
            }
        });
    }

    private ArrayList<LocationCountry> convertResultToLocationCountryArrayList(ArrayList<LocationDto> locationDtoArrayList) {
        ArrayList<LocationCountry> result = new ArrayList<>();
        for (LocationDto locationDto : locationDtoArrayList) {
            result.add(locationDto.mapLocationDto());
        }
        return result;
    }

    private ArrayList<LocationRegion> convertResultToLocationRegionArrayList(ArrayList<LocationDto> locationDtoArrayList, LocationCountry parentCountry) {
        ArrayList<LocationRegion> result = new ArrayList<>();
        result.add(new LocationRegion("All Regions", parentCountry.getLocationCode(), parentCountry));
        for (LocationDto locationDto : locationDtoArrayList) {
            result.add(locationDto.mapLocationDto(parentCountry));
        }
        return result;
    }

    public interface OnRequestResultCallback<T> {
        void onRequestResult(ArrayList<T> locationList);

        void onRequestFailure();
    }

}
