package com.github.v0id20.birding.chooselocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.birding.mylocation.LocationCountry;
import com.github.v0id20.birding.mylocation.LocationRegion;
import com.github.v0id20.birding.R;

import java.util.ArrayList;

public class ChooseLocationAdapter extends RecyclerView.Adapter {
    private static final int INVALID_POSITION = -1;
    private ArrayList<LocationCountry> countryArrayList;
    private OnRegionClickListener onRegionClickListener;
    private OnMyLocationClickListener onMyLocationClickListener;
    private OnCountryClickListener onCountryClickListener;
    private int lastClickedLocationPos = INVALID_POSITION;
    private int primaryColor;
    private int blackColor;
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

    public void setColors(int primaryColor, int blackColor) {
        this.primaryColor = primaryColor;
        this.blackColor = blackColor;
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
            CountryViewHolder viewHolder = new CountryViewHolder(itemView);
            viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext()));
            viewHolder.regionAdapter = new RegionAdapter();
            viewHolder.recyclerView.setAdapter(viewHolder.regionAdapter);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentAdapterPosition = viewHolder.getAdapterPosition();
                    if (lastClickedLocationPos != INVALID_POSITION) {
                        (countryArrayList.get(lastClickedLocationPos)).setExpanded(false);
                        notifyItemChanged(lastClickedLocationPos);
                    }
                    if (currentAdapterPosition != lastClickedLocationPos) {
                        LocationCountry clickedLocationCountry = countryArrayList.get(currentAdapterPosition);
                        boolean isExpanded = !clickedLocationCountry.isExpanded();
                        clickedLocationCountry.setExpanded(isExpanded);
                        onCountryClickListener.onCountryClick(clickedLocationCountry, currentAdapterPosition);
                        myRecyclerView.scrollToPosition(currentAdapterPosition);
                        lastClickedLocationPos = currentAdapterPosition;
                    } else {
                        lastClickedLocationPos = INVALID_POSITION;
                    }
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
            ((MyLocationViewHolder) holder).myLocation.setText(countryArrayList.get(position).getLocationName());
        } else {
            LocationCountry aCountry = countryArrayList.get(position);
            CountryViewHolder countryViewHolder = (CountryViewHolder) holder;
            countryViewHolder.location.setText(aCountry.getLocationName());
            if (aCountry.isExpanded()) {
                countryViewHolder.location.setTextColor(primaryColor);
                countryViewHolder.expandable.setVisibility(View.VISIBLE);
                switch (countryArrayList.get(position).getLoadingState()) {
                    case ERROR:
                        if (countryViewHolder.regionAdapter.getRegionList() != null) {
                            countryViewHolder.regionAdapter.setRegionList(aCountry.getSubRegions());
                            countryViewHolder.regionAdapter.setOnChosenLocationClickListener(onRegionClickListener);
                            countryViewHolder.regionAdapter.notifyItemChanged(position);
                        }
                        break;
                    case SUCCESS:
                        if (aCountry.getSubRegions() != null) {
                            countryViewHolder.regionAdapter.setRegionList(aCountry.getSubRegions());
                            countryViewHolder.regionAdapter.setOnChosenLocationClickListener(onRegionClickListener);
                            countryViewHolder.regionAdapter.notifyItemChanged(position);
                        }
                        break;
                    default:
                        break;
                }
            } else {
                countryViewHolder.location.setTextColor(blackColor);
                countryViewHolder.expandable.setVisibility(View.GONE);
                countryViewHolder.regionAdapter.setRegionList(new ArrayList<>());
                countryViewHolder.regionAdapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return countryArrayList.size();
    }

    public void bind(ArrayList<LocationRegion> locationDataArrayList, int position) {
        countryArrayList.get(position).setLoadingState(LocationCountry.LoadingState.SUCCESS);
        (countryArrayList.get(position)).setSubRegions(locationDataArrayList);
        notifyItemChanged(position);
    }

    public void setViewHolderErrorState(int position) {
        countryArrayList.get(position).setLoadingState(LocationCountry.LoadingState.ERROR);
        notifyItemChanged(position);
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public void refreshRegionList(int position) {
        onCountryClickListener.onCountryClick(countryArrayList.get(position), position);
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

    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView location;
        RecyclerView recyclerView;
        ConstraintLayout expandable;
        RegionAdapter regionAdapter;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.item_location);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            expandable = itemView.findViewById(R.id.expandable);
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
