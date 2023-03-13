package com.github.v0id20.birding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ObservationAdapter<ObservationViewHolder> extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {
    ArrayList<BirdObservation> birdObservationArrayList;

    public ObservationAdapter (ArrayList<BirdObservation> birdObservationArrayList) {
        this.birdObservationArrayList = birdObservationArrayList;
    }
    @NonNull
    @Override
    public ObservationAdapter.ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_obs, parent, false);
        ObservationAdapter.ObservationViewHolder viewHolder = new ObservationAdapter.ObservationViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationAdapter.ObservationViewHolder holder, int position) {
        holder.commonNameTV.setText(birdObservationArrayList.get(position).getComName());
        holder.sciNameTV.setText(birdObservationArrayList.get(position).getSciName());
        holder.locationTV.setText((birdObservationArrayList.get(position).getSubnational1Name()));
    }


    @Override
    public int getItemCount() {
        return birdObservationArrayList.size();
    }

    public class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView commonNameTV;
        TextView sciNameTV;
        TextView locationTV;
        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            commonNameTV = itemView.findViewById(R.id.commonName);
            sciNameTV = itemView.findViewById(R.id.sciName);
            locationTV = itemView.findViewById(R.id.location);

        }
    }
}