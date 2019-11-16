package com.kailang.wastebook.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kailang.wastebook.data.Entity.Category;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.data.WasteBookRepository;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {

    private WasteBookRepository wasteBookRepository;
    public DetailViewModel(@NonNull Application application) {
        super(application);
        wasteBookRepository = new WasteBookRepository(application);
    }
    public LiveData<List<WasteBook>> getAllWasteBookLive(){
        return wasteBookRepository.getAllWasteBooksLive();
    }
    public LiveData<List<WasteBook>> findWasteBookWithPattern(String pattern){
        return wasteBookRepository.findWasteBookWithPattern(pattern);
    }
    public void insertWasteBook(WasteBook... wasteBooks){
        wasteBookRepository.insertWasteBook(wasteBooks);
    }

    public void updateWasteBook(WasteBook... wasteBooks){
        wasteBookRepository.updateWasteBook(wasteBooks);
    }
    public void deleteWasteBook(WasteBook... wasteBooks){
        wasteBookRepository.deleteWasteBook(wasteBooks);
    }


}