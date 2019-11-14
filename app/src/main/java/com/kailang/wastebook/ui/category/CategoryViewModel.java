package com.kailang.wastebook.ui.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.kailang.wastebook.data.CategoryRepository;
import com.kailang.wastebook.data.Entity.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository=new CategoryRepository(application);
    }

    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "index:" + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Category>> getAllCategoriesLive(){
        return categoryRepository.getAllCategoriesLive();
    }
    public void insertCategory(Category...categories){
        categoryRepository.insertCategory(categories);
    }
    public void updateCategory(Category...categories){
        categoryRepository.updateCategory(categories);
    }
    public void deleteCategory(Category...categories){
        categoryRepository.deleteCategory(categories);
    }
}
