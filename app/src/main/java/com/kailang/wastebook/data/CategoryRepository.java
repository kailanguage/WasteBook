package com.kailang.wastebook.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kailang.wastebook.data.Entity.Category;

import java.util.List;

public class CategoryRepository {
    private LiveData<List<Category>> allCategoriesLive;
    private CategoryDao categoryDao;

    public CategoryRepository(Context context){
        CategoryDatabase categoryDatabase = CategoryDatabase.getDatabase(context);
        categoryDao = categoryDatabase.getCategoryDao();
        allCategoriesLive=categoryDao.getAllCategoriesLive();
    }
    public void insertCategory(Category...categories){
        new InsertAsyncTask(categoryDao).execute(categories);
    }

    public void updateCategory(Category...categories){
        new UpdateAsyncTask(categoryDao).execute(categories);
    }
    public void deleteCategory(Category...categories){
        new DeleteAsyncTask(categoryDao).execute(categories);
    }

    public LiveData<List<Category>> getAllCategoriesLive(){
        return allCategoriesLive;
    }

    private static class InsertAsyncTask extends AsyncTask<Category,Void,Void> {
        private CategoryDao categoryDao;
        public InsertAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.insertCategory(categories);
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<Category,Void,Void> {
        private CategoryDao categoryDao;
        public UpdateAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.updateCategory(categories);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<Category,Void,Void> {
        private CategoryDao categoryDao;
        public DeleteAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.deleteCategory(categories);
            return null;
        }
    }
}
