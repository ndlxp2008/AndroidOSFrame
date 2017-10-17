package com.jaj.aho.osframe.frame.presenter.factory;


import com.jaj.aho.osframe.frame.presenter.XPresenter;

public interface PresenterFactory<P extends XPresenter> {
    P createPresenter();
}
