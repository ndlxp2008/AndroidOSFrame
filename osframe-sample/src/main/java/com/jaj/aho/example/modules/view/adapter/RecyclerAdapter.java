package com.jaj.aho.example.modules.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.jaj.aho.example.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by aho on 2017/10/20.
 */

public class RecyclerAdapter extends DelegateAdapter.Adapter<SubAdapter.MainViewHolder> {
    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private VirtualLayoutManager.LayoutParams mLayoutParams;
    private int mCount = 0;

    public RecyclerAdapter(Context context, LayoutHelper layoutHelper, int count) {
        this(context, layoutHelper, count, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
    }

    public RecyclerAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
        this.mContext = context;
        this.mLayoutHelper = layoutHelper;
        this.mCount = count;
        this.mLayoutParams = layoutParams;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public SubAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubAdapter.MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item2, parent, false));
    }

    @Override
    public void onBindViewHolder(SubAdapter.MainViewHolder holder, int position) {
        // only vertical
        holder.itemView.setLayoutParams(
                new VirtualLayoutManager.LayoutParams(mLayoutParams));
    }


    @Override
    protected void onBindViewHolderWithOffset(SubAdapter.MainViewHolder holder, int position, int offsetTotal) {
        RecyclerView recyclerView = ((RecyclerView) holder.itemView.findViewById(R.id.recycler));

        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(mContext);
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

        //状态栏目
        LinearLayoutHelper layoutHelper1 = new LinearLayoutHelper();
        layoutHelper1.setBgColor(Color.YELLOW);
        layoutHelper1.setAspectRatio(2.0f);
        layoutHelper1.setMargin(10, 10, 10, 10);
        layoutHelper1.setPadding(10, 10, 10, 10);
        adapters.add(new SubAdapter(mContext, layoutHelper1, 10) {
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

    }

    @Override
    public int getItemCount() {
        return mCount;
    }


    public static class MainViewHolder extends RecyclerView.ViewHolder {

        public static volatile int existing = 0;
        public static int createdTimes = 0;

        public MainViewHolder(View itemView) {
            super(itemView);
            createdTimes++;
            existing++;
        }

        @Override
        protected void finalize() throws Throwable {
            existing--;
            super.finalize();
        }
    }
}
