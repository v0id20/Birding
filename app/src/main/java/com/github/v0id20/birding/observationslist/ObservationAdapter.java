package com.github.v0id20.birding.observationslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.birding.R;
import com.github.v0id20.birding.birdobservationitem.BirdObservation;
import com.github.v0id20.birding.birdobservationitem.BirdObservationDate;
import com.github.v0id20.birding.birdobservationitem.BirdObservationItem;
import com.github.v0id20.birding.decoder.Decoder;

import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeader.StickyHeaderInterface, Decoder.OnLocationsDecodedListener {

    private ArrayList<BirdObservationItem> birdObservationArrayList;
    private OnObservationClickListener onObservationClickListener;
    private final Decoder decoder;

    public ObservationAdapter(ArrayList<BirdObservationItem> birdObservationArrayList, Decoder decoder) {
        this.birdObservationArrayList = birdObservationArrayList;
        this.decoder = decoder;
        decoder.setOnLocationsDecodedListener(ObservationAdapter.this);
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
        //type - observation
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
            //type - date
        } else {
            itemView = inflater.inflate(R.layout.item_obs_date, parent, false);
            return new DateObservationViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            BirdObservation birdObservation = (BirdObservation) birdObservationArrayList.get(position);
            if (holder instanceof ObservationAdapter.ObservationViewHolder) {
                ObservationAdapter.ObservationViewHolder observationViewHolder = (ObservationAdapter.ObservationViewHolder) holder;
                (observationViewHolder).timeTV.setText(birdObservation.getTime());
                (observationViewHolder).commonNameTV.setText(birdObservation.getCommonName());
                (observationViewHolder).sciNameTV.setText(birdObservation.getScientificName());
                if (birdObservation.isLocationDecoded()) {
                    (observationViewHolder).locationTV.setText(birdObservation.getLocationName());
                } else {
                    (observationViewHolder).locationTV.setText("");
                    decoder.decodeLocation(birdObservation, position);
                }
            }
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
        } else {
            return 2;
        }
    }

    /**
     * determine index of BirdObservationDate in birdObservationArrayList for an item with given position
     */
    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int i = itemPosition;
        while (i > 0) {
            if (birdObservationArrayList.get(i) instanceof BirdObservationDate) {
                return i;
            }
            i--;
        }
        return 0;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.item_obs_date;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        String date = birdObservationArrayList.get(headerPosition).getObservationDate();
        TextView dateTV = header.findViewById(R.id.obs_date);
        dateTV.setText(date);
    }

    @Override
    public boolean isHeader(int itemPosition) {
        return birdObservationArrayList.get(itemPosition) instanceof BirdObservationDate;
    }

    @Override
    public void onLocationDecoded(String newAddress, int position) {
        if (birdObservationArrayList.get(position) instanceof BirdObservation) {
            ((BirdObservation) birdObservationArrayList.get(position)).setLocationName(newAddress);
            ((BirdObservation) birdObservationArrayList.get(position)).setLocationDecoded(true);
            this.notifyItemChanged(position);
        }
    }

    @Override
    public void onLocationDecodingFailure(int position) {
        if (birdObservationArrayList.get(position) instanceof BirdObservation) {
            ((BirdObservation) birdObservationArrayList.get(position)).setLocationDecoded(true);
            this.notifyItemChanged(position);
        }
    }

    public static class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView commonNameTV;
        TextView sciNameTV;
        TextView locationTV;
        TextView timeTV;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTV = itemView.findViewById(R.id.obs_time);
            commonNameTV = itemView.findViewById(R.id.common_name);
            sciNameTV = itemView.findViewById(R.id.sciName);
            locationTV = itemView.findViewById(R.id.location);
        }
    }

    public static class DateObservationViewHolder extends RecyclerView.ViewHolder {
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
