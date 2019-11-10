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
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM USER")
    void deleteAllUser();

    @Query("SELECT * FROM USER ")
    LiveData<List<User>>getUserLive();
}
