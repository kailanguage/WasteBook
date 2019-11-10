package com.kailang.wastebook.data.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "user_id")
    private int id;
    @ColumnInfo(name = "user_name")
    private String nickname;
    @ColumnInfo(name = "user_sex")
    private char sex;
    @ColumnInfo(name = "user_phone")
    private String phone;
    @ColumnInfo(name = "user_password")
    private String password;

    //user the id to login ,no password
    public User(int id){
        this.id=id;
    }

    public User(@NonNull int id, String nickname, char sex, String phone, String password, String icon) {
        this.id = id;
        this.nickname = nickname;
        this.sex = sex;
        this.phone = phone;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public char getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
