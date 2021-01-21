package com.study.locationawareapp.ui.directions;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;
import com.study.locationawareapp.ui.AppViewModel;
import com.study.locationawareapp.ui.destination.DestinationAdapter;
import com.study.locationawareapp.ui.map.MapViewModel;

import java.util.Observable;
import java.util.Observer;

public class DirectionsFragment extends Fragment implements Observer {

    private AppViewModel appViewModel;
    private DirectionAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_directions, container, false);

        this.appViewModel = new ViewModelProvider(this.getActivity()).get(AppViewModel.class);

        final RecyclerView recyclerView = root.findViewById(R.id.RecyclerView_directions);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new DirectionAdapter(appViewModel);
        recyclerView.setAdapter(adapter);
        appViewModel.routeChangedSubject.attachObserver(this);

        return root;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(getActivity() != null)
            getActivity().runOnUiThread(()-> this.adapter.notifyDataSetChanged());
    }
}