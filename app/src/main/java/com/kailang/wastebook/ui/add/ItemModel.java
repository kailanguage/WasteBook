package com.kailang.wastebook.ui.add;

import android.view.View;
import android.widget.ImageView;

public interface ItemModel {
    /**
     * 返回item名字
     *
     * @return 返回名字
     */
    String getItemName();

    /**
     * 设置图标
     *
     * @param imageView item图标
     */
    void setIcon(ImageView imageView);

    /**
     * 特殊需求，重写该方法，设置item
     *
     * @param itemView itemView
     */
    void setItemView(View itemView);
}
