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
    OnObservationClickListener onObservationClickListener;

    public void setOnObservationClickListener(OnObservationClickListener onObservationClickListener) {
        this.onObservationClickListener = onObservationClickListener;
    }


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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onObservationClickListener.onBirdObservationClick(birdObservationArrayList.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationAdapter.ObservationViewHolder holder, int position) {
        holder.commonNameTV.setText(birdObservationArrayList.get(position).getCommonName());
        holder.sciNameTV.setText(birdObservationArrayList.get(position).getScientificName());
        holder.locationTV.setText((birdObservationArrayList.get(position).getLocationName()));
        holder.dateTV.setText((birdObservationArrayList.get(position).getDate()));
    }


    @Override
    public int getItemCount() {
        return birdObservationArrayList.size();
    }

    public class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView commonNameTV;
        TextView sciNameTV;
        TextView locationTV;
        TextView dateTV;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            commonNameTV = itemView.findViewById(R.id.commonName);
            sciNameTV = itemView.findViewById(R.id.sciName);
            locationTV = itemView.findViewById(R.id.location);
            dateTV = itemView.findViewById(R.id.obsDate);
        }
    }

    public void updateData(ArrayList<BirdObservation> newObservationList) {
        birdObservationArrayList.clear();
        birdObservationArrayList = newObservationList;
        this.notifyDataSetChanged();
    }

    public interface OnObservationClickListener {
        void onBirdObservationClick(BirdObservation obs);
    }
}
