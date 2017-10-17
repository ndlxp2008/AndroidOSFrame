package com.jaj.aho.osframe.frame.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaj.aho.osframe.frame.AppManager;
import com.jaj.aho.osframe.frame.ViewWithPresenter;
import com.jaj.aho.osframe.frame.presenter.PresenterLifecycleManager;
import com.jaj.aho.osframe.frame.presenter.XPresenter;
import com.jaj.aho.osframe.frame.presenter.factory.PresenterFactory;
import com.jaj.aho.osframe.frame.presenter.factory.ReflectionPresenterFactory;
import com.jaj.aho.osframe.receiver.NetWorkStateReceiver;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * presentercycle+rxpresentercycle
 */
public abstract class BaseMvpActivity<P extends XPresenter> extends SupportActivity implements ViewWithPresenter<P>, LifecycleProvider<ActivityEvent> {
    private static final String PRESENTER_STATE_KEY = "presenter_state";
    private PresenterLifecycleManager<P> presenterManager =
            new PresenterLifecycleManager<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    private NetWorkStateReceiver netWorkStateReceiver;

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        AppManager.create().addActivity(this);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initRootFragment(savedInstanceState);
        if (savedInstanceState != null) {
            presenterManager.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));
        }
        presenterManager.onCreated(this);
        initData();
    }

    /**
     * rxjava生命周期管理
     *
     * @return
     */
    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }


    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
        presenterManager.onStart();
    }

    @Override
    protected void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
//        System.out.println("注册");
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        presenterManager.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(netWorkStateReceiver);
//        System.out.println("注销");
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        presenterManager.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        presenterManager.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        presenterManager.onDestroy();
        super.onDestroy();
        AppManager.create().finishActivity(this);
    }

    /**
     * 如果是嵌套Fragment的话，初始化根Fragment
     *
     * @param savedInstanceState
     */
    protected void initRootFragment(Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, presenterManager.onSaveInstanceState());
    }


    @Override
    public PresenterFactory<P> getPresenterFactory() {
        return presenterManager.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterManager.setPresenterFactory(presenterFactory);
    }

    @Override
    public P getPresenter() {
        return presenterManager.getPresenter();
    }

    public void gotoActivity(Class<? extends Activity> clazz, boolean finish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, boolean finish) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    public void gotoActivity(Class<? extends Activity> clazz, Bundle bundle, int flags, boolean finish) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) intent.putExtras(bundle);
        intent.addFlags(flags);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    protected abstract int getContentViewId();

    protected abstract void initData();

}
