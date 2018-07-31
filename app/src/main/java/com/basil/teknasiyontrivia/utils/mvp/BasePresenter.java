package com.basil.teknasiyontrivia.utils.mvp;

import com.basil.teknasiyontrivia.game.GameEngine;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class BasePresenter<V extends IBaseView> implements IBasePresenter {

    protected GameEngine mGameEngine;
    protected V view;


    @Inject
    protected BasePresenter(GameEngine gameEngine){
        mGameEngine = gameEngine;
    }
    @Override
    public void onAttach(IBaseView iBaseView) {
        view = (V)iBaseView;
    }

    @Override
    public void onDetach() {
        view = null;
    }
}
