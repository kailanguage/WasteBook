package com.kailang.wastebook.ui.person;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kailang.wastebook.data.CategoryRepository;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.data.WasteBookRepository;

import java.util.List;

public class PersonViewModel extends AndroidViewModel {
    private WasteBookRepository wasteBookRepository;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        wasteBookRepository=new WasteBookRepository(application);
    }
    public LiveData<List<WasteBook>> getAllWasteBookLive(){
        return wasteBookRepository.getAllWasteBooksLive();
    }
}