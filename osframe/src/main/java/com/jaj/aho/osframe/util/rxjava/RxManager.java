package com.jaj.aho.osframe.util.rxjava;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 用于管理RxBus的事件和Rxjava相关代码的生命周期处理
 */
public class RxManager {

    public RxBus mRxBus = RxBus.$();
    private Map<String, Observable<?>> mObservables;// 管理观察源
    private CompositeDisposable mCompositeSubscription;// 管理订阅者者

    public RxManager() {
        mObservables = new HashMap<>();
        mCompositeSubscription = RxUtil.getNewCompositeSubIfUnsubscribed(mCompositeSubscription);
    }


    public void on(String eventName, Consumer<Object> consumer) {
        Observable<?> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        RxUtil.getNewCompositeSubIfUnsubscribed(mCompositeSubscription).add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    public void add(Disposable subscription) {
        RxUtil.getNewCompositeSubIfUnsubscribed(mCompositeSubscription).add(subscription);
    }

    /**
     * 取消presenter所有订阅，可以取消retrofit
     */
    public void clear() {
        RxUtil.unsubscribeIfNotNull(mCompositeSubscription);// 取消订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet())
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除观察
    }

    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }
}
