package com.github.v0id20.birding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter {

    ArrayList<LocationViewType> locationViewTypeArrayList;


    OnChosenLocationClickListener onChosenLocationClickListener;
    Activity context1;
    OnLocationClickListener onLocationClickListener;
    private FusedLocationProviderClient fusedLocationClient;

    public void setOnLocationClickListener(OnLocationClickListener onLocationClickListener) {
        this.onLocationClickListener = onLocationClickListener;
    }

    public void setOnChosenLocationClickListener(OnChosenLocationClickListener onChosenLocationClickListener) {
        this.onChosenLocationClickListener = onChosenLocationClickListener;
    }



    public LocationAdapter(ArrayList<LocationViewType> locationViewTypeArrayList, Activity a) {
        this.locationViewTypeArrayList = locationViewTypeArrayList;
        this.context1 = a;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == 1) {
            itemView = inflater.inflate(R.layout.item_mylocation, parent, false);
            LocationAdapter.MyLocationViewHolder viewHolder = new LocationAdapter.MyLocationViewHolder(itemView);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLocationClickListener.onLocationClick();
                }
            });
            return viewHolder;
        } else {
            itemView = inflater.inflate(R.layout.item_location, parent, false);
            LocationAdapter.ChosenLocationViewHolder viewHolder = new LocationAdapter.ChosenLocationViewHolder(itemView);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChosenLocationClickListener.onChosenLocationClick(locationViewTypeArrayList.get(viewHolder.getAdapterPosition()).getLocationCode(), locationViewTypeArrayList.get(viewHolder.getAdapterPosition()).getLocation());
                }
            });
            return viewHolder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (locationViewTypeArrayList.get(position).getViewType() == 1) {
            ((LocationAdapter.MyLocationViewHolder) holder).myLocation.setText(locationViewTypeArrayList.get(position).getLocation());
            ((MyLocationViewHolder) holder).myLocationIcon.setImageResource(R.drawable.ic_my_location);
        } else {
            ((LocationAdapter.ChosenLocationViewHolder) holder).location.setText(locationViewTypeArrayList.get(position).getLocation());
        }
    }


    @Override
    public int getItemCount() {
        return locationViewTypeArrayList.size();
    }

    public int getItemViewType(int position) {
        if (locationViewTypeArrayList.get(position).getViewType() == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    public class MyLocationViewHolder extends RecyclerView.ViewHolder {
        public TextView myLocation;
        ImageView myLocationIcon;

        public MyLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            myLocation = itemView.findViewById(R.id.item_mylocation);
            myLocationIcon = itemView.findViewById(R.id.mylocation_icon);
            //<a href="https://www.flaticon.com/free-icons/aim" title="aim icons">Aim icons created by Google - Flaticon</a>
        }
    }

    public class ChosenLocationViewHolder extends RecyclerView.ViewHolder {
        TextView location;

        public ChosenLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.item_location);
        }
    }

    interface OnLocationClickListener{
        void onLocationClick();
    }

    interface OnChosenLocationClickListener{
        void onChosenLocationClick(String locationCode, String countryName);
    }
}
