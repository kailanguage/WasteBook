package com.kailang.wastebook.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kailang.wastebook.data.Entity.Category;

@Database(entities = {Category.class}, version = 1, exportSchema = false)
public abstract class CategoryDatabase extends RoomDatabase {
    private static CategoryDatabase INSTANCE;

    static synchronized CategoryDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CategoryDatabase.class, "category").build();
        }
        return INSTANCE;
    }

    public abstract CategoryDao getCategoryDao();
}
