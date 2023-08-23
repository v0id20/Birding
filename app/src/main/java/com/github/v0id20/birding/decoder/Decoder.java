package com.github.v0id20.birding.decoder;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.github.v0id20.birding.birdobservationitem.BirdObservation;

import java.io.IOException;
import java.util.List;

public class Decoder {
    private final static String TAG = "Decoder";
    private final Handler handler = new Handler();
    private OnLocationsDecodedListener onLocationsDecodedListener;
    private final Geocoder geocoder;

    public Decoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public void setOnLocationsDecodedListener(OnLocationsDecodedListener onLocationsDecodedListener) {
        this.onLocationsDecodedListener = onLocationsDecodedListener;
    }

    public void decodeLocation(BirdObservation birdObservation, int position) {
        if (geocoder != null) {
            String lat = birdObservation.getLatitude();
            String lon = birdObservation.getLongitude();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                decodeAndroidVersionTiramisu(lat, lon, position);
            } else {
                decodeForOldAndroidVersions(lat, lon, position);
            }
        } else {
            if (onLocationsDecodedListener != null) {
                onLocationsDecodedListener.onLocationDecodingFailure(position);
            }
        }
    }

    private Pair<Double, Double> convertCoordinatesToDouble(String latitude, String longitude) {
        if (latitude != null && longitude != null) {
            try {
                double lat = Double.parseDouble(latitude);
                double lon = Double.parseDouble(longitude);
                return new Pair<>(lat, lon);
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "convertCoordinatesToDouble: could not convert coordinates values to double. ", e);
            }
        } else {
            Log.d(TAG, "convertCoordinatesToDouble: latitude or longitude were null");
        }
        return null;
    }

    /**
     * SDK version < 33
     */
    private void decodeForOldAndroidVersions(String latString, String lonString, int position) {
        Pair<Double, Double> coordinates = convertCoordinatesToDouble(latString, lonString);
        if (coordinates != null) {
            double lat = coordinates.first;
            double lon = coordinates.second;
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 2);
                if (!addresses.isEmpty()) {
                    String newAddress = formatAddress(addresses);
                    Log.d(TAG, "decodeForOldAndroidVersions: decoded successfully: " + newAddress + " " + lat + " " + lon);
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
                Log.d(TAG, "decodeForOldAndroidVersions: was not able to convert coordinates into location");
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "decodeForOldAndroidVersions: invalid latitude or longitude. ", e);
            }
        }
        if (onLocationsDecodedListener != null) {
            handler.post(() -> onLocationsDecodedListener.onLocationDecodingFailure(position));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void decodeAndroidVersionTiramisu(String latString, String lonString, int position) {
        Pair<Double, Double> coordinates = convertCoordinatesToDouble(latString, lonString);
        if (coordinates != null) {
            try {
                geocoder.getFromLocation(coordinates.first, coordinates.second, 2, new Geocoder.GeocodeListener() {
                    @Override
                    public void onGeocode(@NonNull List<Address> addresses) {
                        if (!addresses.isEmpty()) {
                            String newAddress = formatAddress(addresses);
                            sendDecodedLocation(true, newAddress, position);
                            Log.d(TAG, "onGeocode: decoded successfully, see result: " + newAddress + " " + coordinates.first + " " + coordinates.second);
                        } else {
                            sendDecodedLocation(false, null, position);
                        }
                    }

                    public void onError(String errorMessage) {
                        Log.d(TAG, "decodeAndroidVersionTiramisu onError: could not decode location. " + errorMessage);
                        sendDecodedLocation(false, null, position);
                    }
                });
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "decodeAndroidVersionTiramisu: invalid latitude or longitude", e);
                sendDecodedLocation(false, null, position);
            }
        } else {
            sendDecodedLocation(false, null, position);
        }
    }

    private String removeElementFromAddress(String targetString, String element) {
        String result = targetString;
        if (element != null) {
            result = targetString.replace(", " + element, "");
            result = result.replace("," + element, "");
            result = result.replace(" " + element, "");
            result = result.replace(element, "");
        }
        return result;
    }

    /**
     * extract string value of address and clean it up
     * by removing from it country name and country postal code
     */
    private String extractAddress(Address address) {
        String resultAddress;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            String a = address.getAddressLine(i);
            builder.append(a);
        }
        resultAddress = removeElementFromAddress(builder.toString(), address.getCountryName());
        resultAddress = removeElementFromAddress(resultAddress, address.getPostalCode());
        return resultAddress;
    }

    private static String extractAddressLineWithFallback(Address address) {
        String formattedAddress;
        if (address.getAddressLine(0) != null && !address.getAddressLine(0).equals("")) {
            formattedAddress = address.getAddressLine(0);
        } else {
            formattedAddress = address.getCountryName();
        }
        return formattedAddress;
    }

    /**
     * formatAddresses returns string representing address.
     * //List addresses may contain up to 2 elements.
     * //Try to format address using the first element of addresses, if the returned result is empty string,
     * //then try to extract address from the second element of addresses
     */

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
                        newAddress = extractAddressLineWithFallback(address2);
                    }
                } else {
                    newAddress = extractAddressLineWithFallback(address);
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

    public interface OnLocationsDecodedListener {
        void onLocationDecoded(String newAddress, int position);

        void onLocationDecodingFailure(int position);
    }

}
