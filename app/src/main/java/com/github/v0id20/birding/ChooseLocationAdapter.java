package com.github.v0id20.birding;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChooseLocationAdapter extends RecyclerView.Adapter {

    private ArrayList<LocationViewType> locationViewTypeArrayList;

    private OnChosenLocationClickListener onChosenLocationClickListener;
    private OnMyLocationClickListener onMyLocationClickListener;

    public void setOnMyLocationClickListener(OnMyLocationClickListener onMyLocationClickListener) {
        this.onMyLocationClickListener = onMyLocationClickListener;
    }

    public void setOnChosenLocationClickListener(OnChosenLocationClickListener onChosenLocationClickListener) {
        this.onChosenLocationClickListener = onChosenLocationClickListener;
    }

    public ChooseLocationAdapter() {
    }

    public void setLocationViewTypeArrayList(ArrayList<LocationViewType> locationViewTypeArrayList) {
        this.locationViewTypeArrayList = locationViewTypeArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int[] attrs = new int[]{androidx.constraintlayout.widget.R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        //

        if (viewType == 1) {
            itemView = inflater.inflate(R.layout.item_mylocation, parent, false);
            ChooseLocationAdapter.MyLocationViewHolder viewHolder = new ChooseLocationAdapter.MyLocationViewHolder(itemView);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMyLocationClickListener.onMyLocationClick();
                }
            });
            itemView.setBackgroundResource(backgroundResource);
            return viewHolder;
        } else {
            itemView = inflater.inflate(R.layout.item_location, parent, false);
            ChooseLocationAdapter.ChosenLocationViewHolder viewHolder = new ChooseLocationAdapter.ChosenLocationViewHolder(itemView);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChosenLocationClickListener.onChosenLocationClick(locationViewTypeArrayList.get(viewHolder.getAdapterPosition()).getCode(), locationViewTypeArrayList.get(viewHolder.getAdapterPosition()).getName());
                }
            });
            itemView.setBackgroundResource(backgroundResource);
            return viewHolder;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            ((ChooseLocationAdapter.MyLocationViewHolder) holder).myLocation.setText(locationViewTypeArrayList.get(position).getName());
            ((MyLocationViewHolder) holder).myLocationIcon.setImageResource(R.drawable.ic_my_location);
        } else {
            ((ChooseLocationAdapter.ChosenLocationViewHolder) holder).location.setText(locationViewTypeArrayList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return locationViewTypeArrayList.size();
    }


    public int getItemViewType(int position) {
        if (position == 0) {
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

    interface OnMyLocationClickListener {
        void onMyLocationClick();
    }

    interface OnChosenLocationClickListener {
        void onChosenLocationClick(String locationCode, String countryName);
    }
}
