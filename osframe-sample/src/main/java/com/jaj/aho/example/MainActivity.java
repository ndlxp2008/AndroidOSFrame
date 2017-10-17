package com.jaj.aho.example;


import android.os.Bundle;
import android.support.annotation.IdRes;

import com.jaj.aho.example.modules.view.fragment.FirstFragment;
import com.jaj.aho.example.modules.view.fragment.SecondFragment;
import com.jaj.aho.example.modules.view.fragment.ThirdFragment;
import com.jaj.aho.osframe.frame.activity.BaseMvpActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.Bind;

/**
 * 主跳转界面
 * 默认显示FirstFragment内容
 */

public class MainActivity extends BaseMvpActivity {
    @Bind(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        setBottomBar();
    }

    @Override
    protected void initRootFragment(Bundle savedInstanceState) {
        loadRootFragment(R.id.content, FirstFragment.getInstance(new Bundle()));
    }

    private void setBottomBar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_first) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    loadRootFragment(R.id.content, FirstFragment.getInstance(new Bundle()));
                } else if (tabId == R.id.tab_second) {
                    loadRootFragment(R.id.content, SecondFragment.getInstance(new Bundle()));
                } else if (tabId == R.id.tab_third) {
                    loadRootFragment(R.id.content, ThirdFragment.getInstance(new Bundle()));
                }
            }
        });
    }


}