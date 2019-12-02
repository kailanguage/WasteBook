package com.kailang.wastebook.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kailang.wastebook.data.Entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User... users);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM user")
    void deleteAllUser();

    @Query("SELECT * FROM user ")
    LiveData<List<User>> getUserLive();
}
