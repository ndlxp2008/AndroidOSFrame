package com.jaj.aho.example.modules.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 多布局实体类
 */

public class Object1 implements MultiItemEntity {

    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int IMG_TEXT = 3;
    public static final int IMG_TEXT_SPAN_SIZE = 4;
    private int itemType;
    private int spanSize;

    private String content;

    public Object1(int itemType, int spanSize, String content) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.content = content;
    }

    public Object1(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
