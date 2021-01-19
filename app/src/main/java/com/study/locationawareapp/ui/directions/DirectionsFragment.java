package com.study.locationawareapp.ui.directions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;
import com.study.locationawareapp.ui.AppViewModel;

public class DirectionsFragment extends Fragment {

    private AppViewModel appViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_directions, container, false);

        this.appViewModel = new ViewModelProvider(this.getActivity()).get(AppViewModel.class);

        final RecyclerView recyclerView = root.findViewById(R.id.RecyclerView_directions);


        return root;
    }
}