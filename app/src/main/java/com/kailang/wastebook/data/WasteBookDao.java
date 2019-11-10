package com.kailang.wastebook.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kailang.wastebook.data.Entity.WasteBook;

import java.util.List;
@Dao
public interface WasteBookDao {
    @Insert
    void insertWasteBook(WasteBook... wasteBook);

    @Update
    void updateWasteBook(WasteBook... wasteBook);

    //删除单条记录
    @Delete
    void deleteWasteBook(WasteBook... wasteBook);

    //获取所有的记录
    @Query("SELECT * FROM WasteBook ")
    LiveData<List<WasteBook>> getAllWasteBookLive();
}
