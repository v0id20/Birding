package com.github.v0id20.birding.data;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BirdApiEndpointInterface {

    @GET("/v2/data/obs/{regionCode}/recent")
    Call<ArrayList<BirdObservationDto>> getRecentObservations(@Path("regionCode") String regionCode, @Header("X-eBirdApiToken") String authorization);

    @GET("/v2/data/obs/geo/recent?")
    Call<ArrayList<BirdObservationDto>> getNearbyRecentObservations(@Query("lat") double latitude, @Query("lng") double longitude, @Header("X-eBirdApiToken") String authorization);

    @GET("/v2/data/obs/{regionCode}/recent/notable?detail=full")
    Call<ArrayList<BirdObservationDto>> getNotableObservations(@Path("regionCode") String regionCode, @Header("X-eBirdApiToken") String authorization);

    @GET("/v2/data/obs/geo/recent/notable?")
    Call<ArrayList<BirdObservationDto>> getNearbyNotableObservations(@Query("lat") double latitude, @Query("lng") double longitude, @Header("X-eBirdApiToken") String authorization);

    @GET("v2/ref/region/list/{regionType}/{parentRegionCode}")
    Call<ArrayList<LocationDto>> getLocationsList(@Path("regionType") String regionType, @Path("parentRegionCode") String parentRegionCode, @Header("X-eBirdApiToken") String authorization);


}

