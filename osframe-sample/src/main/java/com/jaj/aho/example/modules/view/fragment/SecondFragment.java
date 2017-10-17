package com.jaj.aho.example.modules.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jaj.aho.example.R;
import com.jaj.aho.osframe.frame.fragment.BaseMvpFragment;

import butterknife.Bind;

/**
 * Created by aho on 2017/8/31.
 */

public class SecondFragment extends BaseMvpFragment {
    @Bind(R.id.title)
    TextView title;

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
        title.setText("SecondFragment");
    }
}
