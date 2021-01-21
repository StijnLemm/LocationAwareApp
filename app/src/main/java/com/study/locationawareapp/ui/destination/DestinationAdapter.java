package com.study.locationawareapp.ui.destination;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;

import java.util.Observable;
import java.util.Observer;


public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> implements Observer {
    private final DestinationSetter destinationSetter;
    private final PreviousDestinationsListProvider previousDestinationsListProvider;

    public DestinationAdapter(PreviousDestinationsListProvider previousDestinationsListProvider, DestinationSetter destinationSetter) {
        this.previousDestinationsListProvider = previousDestinationsListProvider;
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
        Destination destination = previousDestinationsListProvider.getPreviousPOIsList().get(position);

        holder.setTitle(destination.getName());

        holder.itemView.setOnClickListener(view -> {
            destinationSetter.setDestination(destination);
        });
    }

    @Override
    public int getItemCount() {
        return previousDestinationsListProvider.getPreviousPOIsList().size();
    }

    @Override
    public void update(Observable o, Object arg) {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        
        public void setTitle(String title) {
            this.title.setText(title);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.TextView_destinationItem_title);
        }
    }
}
