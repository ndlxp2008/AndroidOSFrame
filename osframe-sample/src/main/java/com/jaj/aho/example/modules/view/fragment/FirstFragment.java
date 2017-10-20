package com.jaj.aho.example.modules.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.RecyclablePagerAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.jaeger.library.StatusBarUtil;
import com.jaj.aho.example.R;
import com.jaj.aho.example.modules.view.adapter.SubAdapter;
import com.jaj.aho.osframe.frame.fragment.BaseMvpFragment;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by aho on 2017/8/31.
 */

public class FirstFragment extends BaseMvpFragment implements Toolbar.OnMenuItemClickListener, IOnSearchClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.te)
    TextView te;
    @Bind(R.id.recycler)
    RecyclerView recycler;
    SearchFragment searchFragment;
    List<DelegateAdapter.Adapter> adapters;
    RecyclerView.RecycledViewPool viewPool;

    public static FirstFragment getInstance(Bundle bundle) {
        FirstFragment df = new FirstFragment();
        df.setArguments(bundle);
        return df;
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_fist;
    }

    @Override
    protected void initData() {
        setStatusBar();
        initRcyclerView();
    }

    private void setStatusBar() {
        setHasOptionsMenu(true);
        StatusBarUtil.setColor(getActivity(), Color.parseColor("#469FEC"), 0);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        searchFragment = SearchFragment.newInstance();
        toolbar.setOnMenuItemClickListener(this);
        searchFragment.setOnSearchClickListener(this);
    }

    private void initRcyclerView() {
        //绑定layoutmanager
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        //复用回收池
        viewPool = new RecyclerView.RecycledViewPool();
        recycler.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        //管理多布局,委托adapter管理LayoutHelper
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            }
        });

        layoutManager.setRecycleOffset(300);

        recycler.setLayoutManager(layoutManager);
        //多布局adapter数据
        adapters = new LinkedList<>();
        recycler.setAdapter(delegateAdapter);

        //布局一
        adapters.add(new SubAdapter(getContext(), new LinearLayoutHelper(), 1) {

            @Override
            public void onViewRecycled(MainViewHolder holder) {
                if (holder.itemView instanceof ViewPager) {
                    ((ViewPager) holder.itemView).setAdapter(null);
                }
            }

            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (viewType == 1)
                    return new MainViewHolder(
                            LayoutInflater.from(getContext()).inflate(R.layout.view_pager, parent, false));

                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public int getItemViewType(int position) {
                return 1;
            }

            @Override
            protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {

            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                if (holder.itemView instanceof ViewPager) {
                    ViewPager viewPager = (ViewPager) holder.itemView;

                    viewPager.setLayoutParams(new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));

                    // from position to get adapter
                    viewPager.setAdapter(new PagerAdapter(this, viewPool));
                }
            }
        });
        //布局二
        GridLayoutHelper layoutHelper = new GridLayoutHelper(2);
        layoutHelper.setMargin(7, 0, 7, 0);
        layoutHelper.setWeights(new float[]{46.665f});
        layoutHelper.setHGap(3);
        adapters.add(new SubAdapter(getContext(), layoutHelper, 2));
        //布局三
        layoutHelper = new GridLayoutHelper(4);
        layoutHelper.setWeights(new float[]{20f, 26.665f});
        layoutHelper.setMargin(7, 0, 7, 0);
        layoutHelper.setHGap(3);
        adapters.add(new SubAdapter(getContext(), layoutHelper, 40));


        delegateAdapter.setAdapters(adapters);
    }

    static class PagerAdapter extends RecyclablePagerAdapter<SubAdapter.MainViewHolder> {
        public PagerAdapter(SubAdapter adapter, RecyclerView.RecycledViewPool pool) {
            super(adapter, pool);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public void onBindViewHolder(SubAdapter.MainViewHolder viewHolder, int position) {
            // only vertical
            viewHolder.itemView.setLayoutParams(
                    new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ((TextView) viewHolder.itemView.findViewById(R.id.title)).setText("Banner: " + position);
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //加载菜单文件
        if (menu.size() == 0)
            inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search://点击搜索
                searchFragment.show(getFragmentManager(), SearchFragment.TAG);
                break;
        }
        return true;
    }

    @Override
    public void OnSearchClick(String keyword) {
        te.setText(keyword);
    }
}