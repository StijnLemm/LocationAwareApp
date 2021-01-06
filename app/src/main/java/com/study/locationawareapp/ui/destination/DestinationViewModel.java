package com.study.locationawareapp.ui.destination;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DestinationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DestinationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}