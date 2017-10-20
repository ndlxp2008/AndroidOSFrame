package com.jaj.aho.example.modules.view.fragment;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.jaeger.library.StatusBarUtil;
import com.jaj.aho.example.R;
import com.jaj.aho.example.modules.view.adapter.RecyclerAdapter;
import com.jaj.aho.example.modules.view.adapter.SubAdapter;
import com.jaj.aho.osframe.frame.fragment.BaseMvpFragment;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

/**
 * 固定顶部标签 vlayout
 */

public class SecondFragment extends BaseMvpFragment {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    public static SecondFragment getInstance(Bundle bundle) {
        SecondFragment df = new SecondFragment();
        df.setArguments(bundle);
        return df;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_second;
    }

    @Override
    protected void initData() {
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), null);


        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((VirtualLayoutManager.LayoutParams) view.getLayoutParams()).getViewPosition();
                outRect.set(4, 4, 4, 4);
            }
        };

        //复用View
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        recyclerView.addItemDecoration(itemDecoration);
        viewPool.setMaxRecycledViews(0, 20);

        //adapter
        final DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
        //多布局
        //状态栏目
        LinearLayoutHelper layoutHelper1 = new LinearLayoutHelper();
        layoutHelper1.setBgColor(Color.YELLOW);
        layoutHelper1.setAspectRatio(2.0f);
        layoutHelper1.setMargin(10, 10, 10, 10);
        layoutHelper1.setPadding(10, 10, 10, 10);
        adapters.add(new SubAdapter(getActivity(), layoutHelper1, 1) {
            @Override
            public void onBindViewHolder(final MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                final SubAdapter subAdapter = this;
                //mainHandler.postDelayed(new Runnable() {
                //    @Override
                //    public void run() {
                //        //delegateAdapter.removeAdapter(subAdapter);
                //        //notifyItemRemoved(1);
                //        holder.itemView.setVisibility(View.GONE);
                //        notifyItemChanged(1);
                //        layoutManager.runAdjustLayout();
                //    }
                //}, 2000L);
            }
        });
        //标签栏
        StickyLayoutHelper layoutHelper = new StickyLayoutHelper();
        //layoutHelper.setOffset(100);
        layoutHelper.setAspectRatio(4);
        adapters.add(new SubAdapter(getContext(), layoutHelper, 1, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)));
        //recycler栏目
        LinearLayoutHelper layoutHelper2 = new LinearLayoutHelper();
        layoutHelper2.setBgColor(Color.RED);
        layoutHelper2.setAspectRatio(2.0f);
        layoutHelper2.setMargin(10, 10, 10, 10);
        layoutHelper2.setPadding(10, 10, 10, 10);
        adapters.add(new RecyclerAdapter(getActivity(), layoutHelper2, 10));

        //添加到recycler
        delegateAdapter.setAdapters(adapters);
    }
}
