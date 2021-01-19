package com.study.locationawareapp.ui.destination;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;


public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {
    private final DestinationSetter destinationSetter;
    private final DestinationListProvider destinationListProvider;

    public DestinationAdapter(DestinationListProvider destinationListProvider, DestinationSetter destinationSetter) {
        this.destinationListProvider = destinationListProvider;
        this.destinationSetter = destinationSetter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_destination, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destination destination = destinationListProvider.getDestinationList().get(position);

        holder.setTitle(destination.getName());
        holder.setDuration(""+destination.getDurationInMinutes());
        holder.setSwitches(""+destination.getSwitches());

        holder.itemView.setOnClickListener(view -> {
            destinationSetter.setDestination(destination);
        });
    }

    @Override
    public int getItemCount() {
        return destinationListProvider.getDestinationList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView duration;
        private TextView switches;


        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setDuration(String duration) {
            this.duration.setText(duration);
        }

        public void setSwitches(String switches) {
            this.switches.setText(switches);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.TextView_destinationItem_title);
            this.duration = itemView.findViewById(R.id.TextView_destinationItem_duration);
            this.switches = itemView.findViewById(R.id.TextView_destinationItem_switches);
        }
    }
}
