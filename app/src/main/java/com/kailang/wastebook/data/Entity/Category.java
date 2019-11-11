package com.kailang.wastebook.data.Entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "category")
public class Category {

    /**
     * 分类记录 ID
     */
    @ColumnInfo(name = "category_id")
    @PrimaryKey(autoGenerate = true)
    private long id;


    /**
     * 分类名称
     */
    @ColumnInfo(name = "category_name")
    private String name = "";

    /**
     * 图标
     */
    @NonNull
    @ColumnInfo(name = "category_icon")
    private String icon = "";

    /**
     * 排序
     */
    @ColumnInfo(name = "category_order")
    private int order;

    /**
     * 分类类型
     */
    @ColumnInfo(name = "category_type")
    private boolean type;

    /**
     * 用户 ID
     */
    @ColumnInfo(name = "category_user_id")
    private long accountId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getIcon() {
        return icon;
    }

    public void setIcon(@NonNull String icon) {
        this.icon = icon;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
