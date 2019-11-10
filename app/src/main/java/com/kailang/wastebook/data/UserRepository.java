package com.kailang.wastebook.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    public UserRepository(Context context){
        UserDatabase userDatabase = UserDatabase.getDatabase(context.getApplicationContext());
        userDao = userDatabase.getUserDao();
    }
}
