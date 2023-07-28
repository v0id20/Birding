package com.github.v0id20.birding;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private int birdObservationArrayListCounter = 0;
    private String country;
    private final Handler handler = new Handler();
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

    public void decodeLocations(LocationDecoder locationDecoder, ArrayList<BirdObservationItem> arrayList, onLocationsDecodedListener locationsDecoded) {
        Geocoder geocoder = locationDecoder.getGeocoder();
        if (geocoder != null && arrayList.size() != 0) {
            birdObservationArrayListCounter = 0;
            for (BirdObservationItem birdObservationItem : arrayList) {
                if (birdObservationItem instanceof BirdObservation) {
                    BirdObservation birdObservation = (BirdObservation) birdObservationItem;
                    if (birdObservation.getLatitude() != null && birdObservation.getLongitude() != null) {
                        try {
                            double lat = Double.parseDouble(birdObservation.getLatitude());
                            double lon = Double.parseDouble(birdObservation.getLongitude());
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    geocoder.getFromLocation(lat, lon, 2, new Geocoder.GeocodeListener() {
                                        @Override
                                        public void onGeocode(@NonNull List<Address> addresses) {
                                            if (!addresses.isEmpty()) {
                                                String newAddress = formatAddress(addresses);
                                                birdObservation.setLocationName(newAddress);
                                                Log.i(TAG, "decodeLocations: decoded see result " + newAddress + " " + lat + " " + lon);
                                            }
                                            birdObservationArrayListCounter++;
                                            runIfEndOfList(arrayList, locationsDecoded);
                                        }

                                        public void onError(String errorMessage) {
                                            Log.e(TAG, "onError: " + errorMessage);
                                            birdObservationArrayListCounter++;
                                            runIfEndOfList(arrayList, locationsDecoded);
                                        }
                                    });
                                } else {
                                    List<Address> addresses = geocoder.getFromLocation(lat, lon, 2);
                                    if (!addresses.isEmpty()) {
                                        String newAddress = formatAddress(addresses);
                                        Log.i(TAG, "decodeLocations: decoded see result " + newAddress + " " + lat + " " + lon);
                                        birdObservation.setLocationName(newAddress.trim());
                                    }
                                }
                            } catch (IllegalArgumentException e) {
                                Log.e(TAG, "decodeLocations: invalid latitude or longitude", e);
                                birdObservationArrayListCounter++;
                            } catch (IOException e) {
                                Log.e(TAG, "decodeLocations: was not able to convert coordinates into location for SDK version<33");
                            }
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "decodeLocations: could not convert coordinates to double", e);
                            birdObservationArrayListCounter++;
                        }
                    } else {
                        Log.i(TAG, "decodeLocations: lat or lon were null: " + birdObservation.getLocationName());
                        birdObservationArrayListCounter++;
                    }
                } else {
                    birdObservationArrayListCounter++;
                }
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                locationsDecoded.onLocationsDecoded(arrayList);
            } else {
                if (birdObservationArrayListCounter >= arrayList.size()) {
                    locationsDecoded.onLocationsDecoded(arrayList);
                }
            }
        } else {
            locationsDecoded.onLocationsDecoded(arrayList);
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
        birdObservation.setDate(date);
        birdObservation.setTime(time);
        birdObservation.setCommonName(birdObservationDTO.getComName());
        birdObservation.setScientificName(birdObservationDTO.getSciName());
        birdObservation.setCountryName(country);
        birdObservation.setLocationName(birdObservationDTO.getLocName());
        birdObservation.setLatitude(birdObservationDTO.getLat());
        birdObservation.setLongitude(birdObservationDTO.getLng());
        return birdObservation;
    }

    public interface onApiResponseReceived {
        void onApiResponseReceived(ArrayList<BirdObservationItem> apiResponse);
    }

    public interface onLocationsDecodedListener {
        void onLocationsDecoded(ArrayList<BirdObservationItem> apiResponse);
    }

    private void runIfEndOfList(ArrayList<BirdObservationItem> arrayList, onLocationsDecodedListener locationsDecoded) {
        if (birdObservationArrayListCounter == arrayList.size()) {
            handler.post(() -> locationsDecoded.onLocationsDecoded(arrayList));
        }
    }

    private String removeElemntFromAddress(String targetString, String element) {
        String result = targetString;
        if (element != null) {
            result = targetString.replace(", " + element, "");
            result = result.replace("," + element, "");
            result = result.replace(" " + element, "");
            result = result.replace(element, "");
        }
        return result;
    }

    private String extractAddress(Address address) {
        String resultAddress;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            String a = address.getAddressLine(i);
            builder.append(a);
        }
        resultAddress = removeElemntFromAddress(builder.toString(), address.getCountryName());
        resultAddress = removeElemntFromAddress(resultAddress, address.getPostalCode());
        return resultAddress;
    }

    private String replaceNullOrEmpty(Address address) {
        String formattedAddress;
        if (address.getAddressLine(0) != null && !address.getAddressLine(0).equals("")) {
            formattedAddress = address.getAddressLine(0);
        } else {
            formattedAddress = address.getCountryName();
        }
        return formattedAddress;
    }

    private String formatAddress(List<Address> addresses) {
        String newAddress = "";
        if (!addresses.isEmpty()) {
            Address address = addresses.get(0);
            newAddress = extractAddress(address);
            if (newAddress.trim().equals("")) {
                if (addresses.size() > 1 && addresses.get(1) != null) {
                    Address address2 = addresses.get(1);
                    newAddress = extractAddress(address2);
                    if (newAddress.trim().equals("")) {
                        newAddress = replaceNullOrEmpty(address2);
                    }
                } else {
                    newAddress = replaceNullOrEmpty(address);
                }
            }
        }
        return newAddress;
    }
}
