package com.basil.teknasiyontrivia.ui.results;

import com.basil.teknasiyontrivia.game.GameEngine;
import com.basil.teknasiyontrivia.ui.main.IMainView;
import com.basil.teknasiyontrivia.utils.mvp.BasePresenter;
import com.basil.teknasiyontrivia.utils.mvp.IBaseView;

import javax.inject.Inject;

/**
 * Created by Basil on 7/31/2018.
 */

public class ResultsPresenter<V extends IResultsView> extends BasePresenter<V> implements IResultsPresenter {
    IResultsView mView;

    public ResultsPresenter(GameEngine gameEngine) {
        super(gameEngine);
    }


    @Override
    public void onAttach(IBaseView iBaseView) {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onViewReady(IBaseView view) {
        mView = (IResultsView)view;
        mView.publishStandings(mGameEngine.getStandings());
    }
}
