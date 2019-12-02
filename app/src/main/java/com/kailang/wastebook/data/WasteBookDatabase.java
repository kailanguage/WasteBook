package com.kailang.wastebook.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kailang.wastebook.data.Entity.WasteBook;


@Database(entities = {WasteBook.class}, version = 1, exportSchema = false)
public abstract class WasteBookDatabase extends RoomDatabase {
    private static WasteBookDatabase INSTANCE;

    static synchronized WasteBookDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WasteBookDatabase.class, "wastebook_database")
                    .build();
        }
        return INSTANCE;
    }

    public abstract WasteBookDao getWasteBookDao();
}
