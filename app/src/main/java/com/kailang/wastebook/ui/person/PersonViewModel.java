package com.kailang.wastebook.ui.person;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PersonViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is person fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}