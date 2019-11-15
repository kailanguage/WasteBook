package com.kailang.wastebook.ui.add;

import android.view.View;
import android.widget.ImageView;

import com.wihaohao.PageGridView;

public class MyIconModel implements PageGridView.ItemModel {
    private String name;
    private String iconName;
    private int iconId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public MyIconModel(String name, String iconName,int iconId) {
        this.name = name;
        this.iconName=iconName;
        this.iconId = iconId;

    }

    public String getIconName(){
        return iconName;
    }

    @Override
    public String getItemName() {
        return name;
    }

    @Override
    public void setIcon(ImageView imageView) {
        imageView.setImageResource(iconId);
    }

    @Override
    public void setItemView(View itemView) {

    }
}

