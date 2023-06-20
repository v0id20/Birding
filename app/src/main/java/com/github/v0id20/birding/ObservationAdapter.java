package com.github.v0id20.birding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ObservationAdapter<ObservationViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<BirdObservationItem> birdObservationArrayList;
    OnObservationClickListener onObservationClickListener;

    public ObservationAdapter(ArrayList<BirdObservationItem> birdObservationArrayList) {
        this.birdObservationArrayList = birdObservationArrayList;
    }

    public void setOnObservationClickListener(OnObservationClickListener onObservationClickListener) {
        this.onObservationClickListener = onObservationClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        if (viewType == 1) {
            itemView = inflater.inflate(R.layout.item_obs, parent, false);

            ObservationAdapter.ObservationViewHolder viewHolder = new ObservationAdapter.ObservationViewHolder(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BirdObservation birdObservation = (BirdObservation) birdObservationArrayList.get(viewHolder.getAdapterPosition());
                    onObservationClickListener.onBirdObservationClick(birdObservation);
                }
            });
            return viewHolder;
        } else if (viewType == 2) {
            itemView = inflater.inflate(R.layout.item_obs_date, parent, false);
            return new ObservationAdapter.DateObservationViewHolder(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            BirdObservation birdObservationItem = (BirdObservation) birdObservationArrayList.get(position);
            ((ObservationAdapter.ObservationViewHolder) holder).timeTV.setText(birdObservationItem.getTime());
            ((ObservationAdapter.ObservationViewHolder) holder).commonNameTV.setText(birdObservationItem.getCommonName());
            ((ObservationAdapter.ObservationViewHolder) holder).sciNameTV.setText(birdObservationItem.getScientificName());
            ((ObservationAdapter.ObservationViewHolder) holder).locationTV.setText(birdObservationItem.getLocationName());
        } else if (holder.getItemViewType() == 2) {
            BirdObservationDate birdObservationItem = (BirdObservationDate) birdObservationArrayList.get(position);
            ((DateObservationViewHolder) holder).dateTV.setText(birdObservationItem.getObservationDate());
        }
    }

    @Override
    public int getItemCount() {
        if (birdObservationArrayList != null) {
            return birdObservationArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (birdObservationArrayList.get(position) instanceof BirdObservation) {
            return 1;
        } else if (birdObservationArrayList.get(position) instanceof BirdObservationDate) {
            return 2;
        }
        return super.getItemViewType(position);
    }

    public class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView commonNameTV;
        TextView sciNameTV;
        TextView locationTV;
        TextView timeTV;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTV = itemView.findViewById(R.id.obs_time);
            commonNameTV = itemView.findViewById(R.id.commonName);
            sciNameTV = itemView.findViewById(R.id.sciName);
            locationTV = itemView.findViewById(R.id.location);
        }
    }

    public class DateObservationViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV;

        public DateObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.obs_date);
        }
    }

    public void updateData(ArrayList<BirdObservationItem> newObservationsList) {
        birdObservationArrayList.clear();
        birdObservationArrayList = newObservationsList;
        this.notifyDataSetChanged();
    }

    public interface OnObservationClickListener {
        void onBirdObservationClick(BirdObservation obs);
    }


}
