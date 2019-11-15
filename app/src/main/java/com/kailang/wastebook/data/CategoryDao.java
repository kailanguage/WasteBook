package com.kailang.wastebook.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kailang.wastebook.data.Entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(Category...categories);

    @Update
    void updateCategory(Category...categories);

    @Delete
    void deleteCategory(Category...categories);

    @Query("SELECT * FROM CATEGORY")
    LiveData<List<Category>>getAllCategoriesLive();

    @Query("SELECT * FROM CATEGORY")
    List<Category> getAllCategories();
}
