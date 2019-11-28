package com.kailang.wastebook.data.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    private String id;
    @ColumnInfo(name = "user_password")
    private String password;

    public User(@NonNull String id, String password) {
        this.id = id;
        this.password = password;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
