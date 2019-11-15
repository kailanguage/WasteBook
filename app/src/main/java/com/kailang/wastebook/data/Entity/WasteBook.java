package com.kailang.wastebook.data.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "WasteBook")
public class WasteBook {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    /** 用户 ID */
    @ColumnInfo(name = "user_id")
    private long accountId;

    @ColumnInfo(name = "amount")
    private double amount;
    //false:支出、true:收入
    @ColumnInfo(name = "type")
    private boolean type;
    //具体类型
    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "icon")
    private String icon;
    //时间
    @ColumnInfo(name = "time")
    private long time;
    //备注
    private String note;

    public WasteBook( boolean type, double amount,String category,String icon, long time, String note) {
        this.type = type;
        this.amount=amount;
        this.category = category;
        this.icon=icon;
        this.time = time;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
