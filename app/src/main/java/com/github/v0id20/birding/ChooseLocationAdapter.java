package com.github.v0id20.birding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChooseLocationAdapter extends RecyclerView.Adapter {
    private ArrayList<LocationCountry> countryArrayList;
    private OnRegionClickListener onRegionClickListener;
    private OnMyLocationClickListener onMyLocationClickListener;
    private OnCountryClickListener onCountryClickListener;
    private int lasClickedLocationPos = -1;
    private RecyclerView myRecyclerView;

    public void setOnMyLocationClickListener(OnMyLocationClickListener onMyLocationClickListener) {
        this.onMyLocationClickListener = onMyLocationClickListener;
    }

    public void setOnChosenLocationClickListener(OnRegionClickListener onCountryClickListener) {
        this.onRegionClickListener = onCountryClickListener;
    }

    public void setOnCountryClickListener(OnCountryClickListener onCountryClickListener) {
        this.onCountryClickListener = onCountryClickListener;
    }

    public void setCountryArrayList(ArrayList<LocationCountry> countryArrayList) {
        this.countryArrayList = countryArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == 1) {
            View itemView = inflater.inflate(R.layout.item_mylocation, parent, false);
            ChooseLocationAdapter.MyLocationViewHolder viewHolder = new ChooseLocationAdapter.MyLocationViewHolder(itemView);
            viewHolder.itemView.setOnClickListener(v -> onMyLocationClickListener.onMyLocationClick());
            return viewHolder;
        } else {
            View itemView = inflater.inflate(R.layout.item_location_country, parent, false);
            ChooseLocationAdapter.ChosenLocationViewHolder viewHolder = new ChooseLocationAdapter.ChosenLocationViewHolder(itemView);
            viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext()));
            viewHolder.regionAdapter = new RegionAdapter();
            viewHolder.recyclerView.setAdapter(viewHolder.regionAdapter);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lasClickedLocationPos != -1 && lasClickedLocationPos != viewHolder.getAdapterPosition()) {
                        (countryArrayList.get(lasClickedLocationPos)).setExpanded(false);
                        ChooseLocationAdapter.this.notifyItemChanged(lasClickedLocationPos);
                    }
                    int clickedLocationAdapterPosition = viewHolder.getAdapterPosition();
                    LocationCountry clickedLocationCountry = countryArrayList.get(clickedLocationAdapterPosition);
                    boolean isExpanded = !clickedLocationCountry.isExpanded();
                    clickedLocationCountry.setExpanded(isExpanded);
                    ChooseLocationAdapter.this.notifyItemChanged(clickedLocationAdapterPosition);
                    onCountryClickListener.onCountryClick(clickedLocationCountry, clickedLocationAdapterPosition);
                    lasClickedLocationPos = clickedLocationAdapterPosition;
                    myRecyclerView.scrollToPosition(viewHolder.getAdapterPosition());
                }
            });
            return viewHolder;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        myRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        myRecyclerView = null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            ((ChooseLocationAdapter.MyLocationViewHolder) holder).myLocation.setText(countryArrayList.get(position).getLocationName());
            ((MyLocationViewHolder) holder).myLocationIcon.setImageResource(R.drawable.ic_my_location);
        } else {
            LocationCountry aCountry = countryArrayList.get(position);
            if (aCountry.isExpanded()) {
                ((ChooseLocationAdapter.ChosenLocationViewHolder) holder).location.setText(aCountry.getLocationName());
                if (aCountry.getSubRegions() != null) {
                    ((ChosenLocationViewHolder) holder).regionAdapter.setRegionList(aCountry.getSubRegions());
                    ((ChosenLocationViewHolder) holder).regionAdapter.setOnChosenLocationClickListener(onRegionClickListener);
                    ((ChosenLocationViewHolder) holder).regionAdapter.notifyDataSetChanged();
                }
            } else {
                ((ChooseLocationAdapter.ChosenLocationViewHolder) holder).location.setText(aCountry.getLocationName());
                ((ChosenLocationViewHolder) holder).regionAdapter.setRegionList(new ArrayList<>());
                ((ChosenLocationViewHolder) holder).regionAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        return countryArrayList.size();
    }

    public void bind(ArrayList<LocationRegion> locationDataArrayList, int position) {
        (countryArrayList.get(position)).setSubRegions(locationDataArrayList);
        ChooseLocationAdapter.this.notifyItemChanged(position);
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public static class MyLocationViewHolder extends RecyclerView.ViewHolder {
        public TextView myLocation;
        ImageView myLocationIcon;

        public MyLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            myLocation = itemView.findViewById(R.id.item_mylocation);
            myLocationIcon = itemView.findViewById(R.id.mylocation_icon);
            //<a href="https://www.flaticon.com/free-icons/aim" title="aim icons">Aim icons created by Google - Flaticon</a>
        }
    }

    public static class ChosenLocationViewHolder extends RecyclerView.ViewHolder {
        TextView location;
        RecyclerView recyclerView;
        RegionAdapter regionAdapter;

        public ChosenLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.item_location);
            recyclerView = itemView.findViewById(R.id.recycler_view);
        }
    }

    interface OnMyLocationClickListener {
        void onMyLocationClick();
    }

    interface OnCountryClickListener {
        void onCountryClick(LocationCountry country, int position);
    }

    interface OnRegionClickListener {
        void onRegionClick(LocationRegion region);
    }
}
