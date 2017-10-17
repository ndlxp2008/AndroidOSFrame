package com.jaj.aho.example.modules.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaj.aho.example.R;

import java.util.List;

/**
 * Created by aho on 2017/9/1.
 */

public class TestAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TestAdapter(@Nullable List<String> data) {
        super(R.layout.recycler_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text, item);
    }
}
