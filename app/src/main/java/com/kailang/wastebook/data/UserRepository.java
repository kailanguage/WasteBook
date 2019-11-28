package com.kailang.wastebook.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kailang.wastebook.data.Entity.User;
import com.kailang.wastebook.data.Entity.WasteBook;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsersLive;
    public UserRepository(Context context){
        UserDatabase userDatabase = UserDatabase.getDatabase(context.getApplicationContext());
        userDao = userDatabase.getUserDao();
        allUsersLive=userDao.getUserLive();
    }
    public LiveData<List<User>> getAllUsersLive(){
        return allUsersLive;
    }
    public void insertUser(User...users){
        new InsertAsyncTask(userDao).execute(users);
    }
    private static class InsertAsyncTask extends AsyncTask<User,Void,Void> {
        private UserDao userDao;
        public InsertAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insertUser(users);
            return null;
        }
    }
}
