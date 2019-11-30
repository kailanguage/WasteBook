package com.kailang.wastebook.ui.person;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kailang.wastebook.data.CategoryRepository;
import com.kailang.wastebook.data.Entity.Category;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.data.WasteBookRepository;

import java.util.List;

public class PersonViewModel extends AndroidViewModel {
    private WasteBookRepository wasteBookRepository;
    private CategoryRepository categoryRepository;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        wasteBookRepository=new WasteBookRepository(application);
        categoryRepository= new CategoryRepository(application);
    }
    public LiveData<List<WasteBook>> getAllWasteBookLive(){
        return wasteBookRepository.getAllWasteBooksLive();
    }
    public LiveData<List<Category>> getAllCategoriesLive(){
        return categoryRepository.getAllCategoriesLive();
    }
    public void insertWasteBook(WasteBook... wasteBooks){
        wasteBookRepository.insertWasteBook(wasteBooks);
    }
    public void deleteAllWasteBooks(){
        wasteBookRepository.deleteAllWasteBooks();
    }
}