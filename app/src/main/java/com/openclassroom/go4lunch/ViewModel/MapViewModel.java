package com.openclassroom.go4lunch.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassroom.go4lunch.Model.AutocompleteAPI.Prediction;

public class MapViewModel extends ViewModel {

    private final MutableLiveData<SearchDataResult> selectedPrediction = new MutableLiveData<>();

    public void setSelectedPrediction(Prediction selectedPrediction) {
        this.selectedPrediction.setValue(selectedPrediction);
    }

    public MutableLiveData<Prediction> getSelectedPrediction() {
        return selectedPrediction;
    }
}