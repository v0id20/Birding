package com.github.v0id20.birding.chooselocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.birding.locationitem.LocationRegion;
import com.github.v0id20.birding.R;

import java.util.ArrayList;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.RegionViewHolder> {

    private ArrayList<LocationRegion> regionList = new ArrayList<>();
    private ChooseLocationAdapter.OnRegionClickListener onRegionClickListener;

    public void setOnChosenLocationClickListener(ChooseLocationAdapter.OnRegionClickListener onCountryClickListener) {
        this.onRegionClickListener = onCountryClickListener;
    }

    public void setRegionList(ArrayList<LocationRegion> regionList) {
        this.regionList = regionList;
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_region, parent, false);
        RegionViewHolder viewHolder = new RegionViewHolder(itemView);
        if (onRegionClickListener != null) {
            itemView.setOnClickListener(v -> onRegionClickListener.onRegionClick(regionList.get(viewHolder.getAdapterPosition())));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        holder.regionTV.setText(regionList.get(position).getLocationName());
    }

    @Override
    public int getItemCount() {
        return regionList.size();
    }

    public class RegionViewHolder extends RecyclerView.ViewHolder {
        public TextView regionTV;

        public RegionViewHolder(@NonNull View itemView) {
            super(itemView);
            regionTV = itemView.findViewById(R.id.region);
        }
    }
}
