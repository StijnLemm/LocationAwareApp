package com.study.locationawareapp.ui.directions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;

import java.util.Observable;
import java.util.Observer;

public class DirectionAdapter extends RecyclerView.Adapter<DirectionAdapter.ViewHolder> {
    private DirectionsListProvider directionsListProvider;

    public DirectionAdapter(DirectionsListProvider directionsListProvider) {
        this.directionsListProvider = directionsListProvider;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);

        return new DirectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step step = directionsListProvider.getRouteSteps().get(position);

        holder.setStreetName(step.getStreetName());
        holder.setDuration(step.getDurationInFormat());
        holder.setDistance(step.getDistanceInKM()+" km");
        holder.setInstruction(step.getInstruction());

    }

    @Override
    public int getItemCount() {
        return directionsListProvider.getRouteSteps().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView streetName;
        private TextView duration;
        private TextView distance;
        private TextView instruction;

        public void setStreetName(String streetName) {
            this.streetName.setText(streetName);
        }

        public void setDuration(String duration) {
            this.duration.setText(duration);
        }

        public void setDistance(String distance) {
            this.distance.setText(distance);
        }

        public void setInstruction(String instruction) {
            this.instruction.setText(instruction);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.streetName = itemView.findViewById(R.id.TextView_directionItem_streetName);
            this.duration = itemView.findViewById(R.id.TextView_directionItem_duration);
            this.distance = itemView.findViewById(R.id.TextView_directionItem_distance);
            this.instruction = itemView.findViewById(R.id.TextView_directionItem_instruction);
        }
    }
}
