package com.kailang.wastebook.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kailang.wastebook.data.Entity.WasteBook;

import java.util.List;

public class WasteBookRepository {
    private LiveData<List<WasteBook>> allWasteBooksLive;
    private WasteBookDao wasteBookDao;

   public  WasteBookRepository(Context context){
        WasteBookDatabase wasteBookDatabase = WasteBookDatabase.getDatabase(context.getApplicationContext());
        wasteBookDao = wasteBookDatabase.getWasteBookDao();
        allWasteBooksLive = wasteBookDao.getAllWasteBookLive();
    }

    public void insertWasteBook(WasteBook...wasteBooks){
        new InsertAsyncTask(wasteBookDao).execute(wasteBooks);
    }

    public void updateWasteBook(WasteBook...wasteBooks){
        new UpdateAsyncTask(wasteBookDao).execute(wasteBooks);
    }
    public void deleteWasteBook(WasteBook...wasteBooks){
        new DeleteAsyncTask(wasteBookDao).execute(wasteBooks);
    }

//    public LiveData<List<WasteBook>> selectWasteBookByLongTime(long a,long b){
//       return wasteBookDao.selectWasteBookByLongTime(a,b);
//    }

    public LiveData<List<WasteBook>>findWasteBookWithPattern(String pattern){
        return wasteBookDao.findWordsWithPattern("%" + pattern + "%");
    }
    public LiveData<List<WasteBook>> getAllWasteBooksLive(){
        return allWasteBooksLive;
    }

    private static class InsertAsyncTask extends AsyncTask<WasteBook,Void,Void> {
        private WasteBookDao wasteBookDao;
        public InsertAsyncTask(WasteBookDao wasteBookDao) {
            this.wasteBookDao = wasteBookDao;
        }

        @Override
        protected Void doInBackground(WasteBook... wasteBooks) {
            wasteBookDao.insertWasteBook(wasteBooks);
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<WasteBook,Void,Void> {
        private WasteBookDao wasteBookDao;
        public UpdateAsyncTask(WasteBookDao wasteBookDao) {
            this.wasteBookDao = wasteBookDao;
        }

        @Override
        protected Void doInBackground(WasteBook... wasteBooks) {
            wasteBookDao.updateWasteBook(wasteBooks);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<WasteBook,Void,Void> {
        private WasteBookDao wasteBookDao;
        public DeleteAsyncTask(WasteBookDao wasteBookDao) {
            this.wasteBookDao = wasteBookDao;
        }

        @Override
        protected Void doInBackground(WasteBook... wasteBooks) {
            wasteBookDao.deleteWasteBook(wasteBooks);
            return null;
        }
    }

}
