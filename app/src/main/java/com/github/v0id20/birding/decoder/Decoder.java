package com.github.v0id20.birding.decoder;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.github.v0id20.birding.BirdObservation;
import com.github.v0id20.birding.viewobservationslist.ViewObservationsListModel;

import java.io.IOException;
import java.util.List;

public class Decoder {
    private final static String TAG = "Decoder";
    private final Handler handler = new Handler();
    private ViewObservationsListModel.onLocationsDecodedListener onLocationsDecodedListener;
    private final Geocoder geocoder;

    public Decoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public void setOnLocationsDecodedListener(ViewObservationsListModel.onLocationsDecodedListener onLocationsDecodedListener) {
        this.onLocationsDecodedListener = onLocationsDecodedListener;
    }

    public void decodeLocation(BirdObservation birdObservation, int position) {
        if (geocoder != null) {
            String lat = birdObservation.getLatitude();
            String lon = birdObservation.getLongitude();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                decodeAndroidVersionTiramisu(lat, lon, position);
            } else {
                decodeOld(lat, lon, position);
            }
        } else {
            if (onLocationsDecodedListener != null) {
                onLocationsDecodedListener.onLocationDecodingFailure(position);
            }
        }
    }

    private double[] validCoordinates(String latitude, String longitude) {
        if (latitude != null && longitude != null) {
            try {
                double lat = Double.parseDouble(latitude);
                double lon = Double.parseDouble(longitude);
                return new double[]{lat, lon};
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "validCoordinates: could not convert coordinates values to double", e);
            }
        } else {
            Log.i(TAG, "validCoordinates: latitude or longitude were null");
        }
        return new double[0];
    }

    private void decodeOld(String latString, String lonString, int position) {
        double[] coordinates = validCoordinates(latString, lonString);
        if (coordinates.length >= 2) {
            double lat = coordinates[0];
            double lon = coordinates[1];
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 2);
                if (!addresses.isEmpty()) {
                    String newAddress = formatAddress(addresses);
                    Log.i(TAG, "decodeLocations: decoded see result " + newAddress + " " + lat + " " + lon);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (onLocationsDecodedListener != null) {
                                onLocationsDecodedListener.onLocationDecoded(newAddress, position);
                            }
                        }
                    });
                    return;
                }
            } catch (IOException e) {
                Log.e(TAG, "decodeLocations: was not able to convert coordinates into location for SDK version<33");
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "decodeLocations: invalid latitude or longitude", e);
            }
        }
        if (onLocationsDecodedListener != null) {
            handler.post(() -> onLocationsDecodedListener.onLocationDecodingFailure(position));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void decodeAndroidVersionTiramisu(String latString, String lonString, int position) {
        double[] coordinates = validCoordinates(latString, lonString);
        if (coordinates.length >= 2) {
            try {
                geocoder.getFromLocation(coordinates[0], coordinates[1], 2, new Geocoder.GeocodeListener() {
                    @Override
                    public void onGeocode(@NonNull List<Address> addresses) {
                        if (!addresses.isEmpty()) {
                            String newAddress = formatAddress(addresses);
                            sendDecodedLocation(true, newAddress, position);
                            Log.i(TAG, "decodeLocations: decoded see result " + newAddress + " " + coordinates[0] + " " + coordinates[1]);
                        }
                    }

                    public void onError(String errorMessage) {
                        Log.e(TAG, "onError: " + errorMessage);
                        sendDecodedLocation(false, null, position);
                    }
                });
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "decodeLocations: invalid latitude or longitude", e);
            }
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

    private void sendDecodedLocation(boolean decodedSuccessfully, String decodedAddress, int position) {
        if (onLocationsDecodedListener != null) {
            if (decodedSuccessfully) {
                handler.post(() -> onLocationsDecodedListener.onLocationDecoded(decodedAddress, position));
            } else {
                handler.post(() -> onLocationsDecodedListener.onLocationDecodingFailure(position));
            }
        }
    }

}
