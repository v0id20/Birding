package com.github.v0id20.birding;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApiEndpointInterface {
    // Request method and URL specified in the annotation

    @GET("/v2/data/obs/{regionCode}/recent")
    Call<ArrayList<BirdObservation>> getObservations(@Path("regionCode") String regionCode, @Header("X-eBirdApiToken") String authorization);

    @GET("/v2/data/obs/geo/recent?")
    Call<ArrayList<BirdObservation>> getNearbyObservations(@Query("lat") double latitude, @Query("lng") double longitude, @Header("X-eBirdApiToken") String authorization);

}

