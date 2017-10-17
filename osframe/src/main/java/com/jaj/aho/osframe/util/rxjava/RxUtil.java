package com.jaj.aho.osframe.util.rxjava;

import android.support.annotation.NonNull;

import com.apkfuns.logutils.LogUtils;

import org.reactivestreams.Subscriber;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/3.
 */
public class RxUtil {

    //解绑订阅
    public static void unsubscribeIfNotNull(Disposable subscription) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    public static CompositeDisposable getNewCompositeSubIfUnsubscribed(CompositeDisposable subscription) {
        if (subscription == null || subscription.isDisposed()) {
            return new CompositeDisposable();
        }
        return subscription;
    }

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applySchedulersForRetrofit() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
            }
        };
    }

    public static <T> Observable<T> getObservable(final Callable<T> callable) {
        return Observable.create(new ObservableOnSubscribe<T>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                try {
                    e.onNext(callable.call());
                } catch (Exception ex) {
                    LogUtils.e(ex);
                }
            }
        });

    }

    public static <T> Observable<T> subscribe(Callable<T> callable, Subscriber<T> subscriber, CompositeDisposable cs) {
        Observable<T> observable = getObservable(callable);
        Disposable subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        getNewCompositeSubIfUnsubscribed(cs);
        cs.add(subscription);
        return observable;
    }

    public static <T> Observable<T> subscribe(Observable<T> observable, Subscriber<T> subscriber, CompositeDisposable cs) {
        Disposable subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        getNewCompositeSubIfUnsubscribed(cs);
        cs.add(subscription);
        return observable;
    }

    public static <T> Observable<T> subscribeForRetrofit(Observable<T> observable, Subscriber<T> subscriber, CompositeDisposable cs) {
        Disposable subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe();
        getNewCompositeSubIfUnsubscribed(cs);
        cs.add(subscription);
        return observable;
    }


}
