package com.study.locationawareapp.ui.destination;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;

public class DestinationFragment extends Fragment {

    private DestinationViewModel destinationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_destination, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.ListView_lastDestinations);

        final ConstraintLayout placeholder = root.findViewById(R.id.ConstraintLayout_destination_current);
        getLayoutInflater().inflate(R.layout.item_destination, placeholder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DestinationAdapter(destinationViewModel));

        return root;
    }
}