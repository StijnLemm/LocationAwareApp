package com.study.locationawareapp.ui.destination;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;


public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {
    private ListProvider listProvider;

    public DestinationAdapter(ListProvider listProvider) {
        this.listProvider = listProvider;
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
        //todo fill the holder
        String name = listProvider.getList().get(position);
        holder.setName(name);
    }

    @Override
    public int getItemCount() {
        return listProvider.getList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public void setName(String title) {
            this.title.setText(title);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.TextView_destinationItem_title);
        }
    }
}
