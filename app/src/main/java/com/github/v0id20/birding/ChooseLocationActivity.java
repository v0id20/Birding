package com.github.v0id20.birding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class ChooseLocationActivity extends AppCompatActivity {
    public static ArrayList<LocationViewType> locationList;
    public static String REGION_CODE_EXTRA = "Code";
    public static final int REQUEST_CODE = 231;
    private FusedLocationProviderClient fusedLocationClient;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location_activity);
        context = this;
        locationList = new ArrayList<LocationViewType>();
        locationList.add(new LocationViewType(1, "My location"));
        locationList.add(new LocationViewType(2, "Canada", "CA"));
        locationList.add(new LocationViewType(2, "USA", "US"));
        locationList.add(new LocationViewType(2, "Kazakhstan", "KZ"));
        locationList.add(new LocationViewType(2, "Romania"));
        locationList.add(new LocationViewType(2, "Albania", "AL"));
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LocationAdapter(locationList, this));

    }


    public void requestLocationPermission() {
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ChooseLocationActivity.REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted. Continue the action or workflow
                // in your app.
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude;
                                double longitude;
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Intent i = new Intent(context, MainActivity.class);
                                i.putExtra("Latitude", latitude);
                                i.putExtra("Longitude", longitude);
                                context.startActivity(i);
                            }
                        }
                    });
                }
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                // Other 'case' lines to check for other
                // permissions this app might request.
            }

        }

    }
}
