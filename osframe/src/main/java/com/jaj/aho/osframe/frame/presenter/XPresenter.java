package com.jaj.aho.osframe.frame.presenter;

import com.apkfuns.logutils.LogUtils;
import com.jaj.aho.osframe.util.rxjava.RxManager;
import com.jaj.aho.osframe.util.rxjava.RxUtil;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Administrator on 2016/9/19.
 */
public class XPresenter<V> extends BasePresenter<V> {

    public V view;
    public CompositeDisposable compositeSubscription;
    public RxManager rxManager = new RxManager();

    /**
     * Presenter初始化
     */
    public void onCreated() {
        this.compositeSubscription = RxUtil.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        view = getView();
    }

    /**
     * Presenter开始工作
     */
    public void start() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rxManager != null) {
            rxManager.clear();
        }
        LogUtils.d("intoDestroy", "进入销毁");
        RxUtil.unsubscribeIfNotNull(compositeSubscription);
    }

}
