package com.basil.teknasiyontrivia.ui.main;

import com.basil.teknasiyontrivia.R;
import com.basil.teknasiyontrivia.game.GameEngine;
import com.basil.teknasiyontrivia.model.Question;
import com.basil.teknasiyontrivia.model.Standing;
import com.basil.teknasiyontrivia.network.GameService;
import com.basil.teknasiyontrivia.server.DummyRestServer;
import com.basil.teknasiyontrivia.server.EmbeddedSocketServer;
import com.basil.teknasiyontrivia.observers.GameEventsObserver;
import com.basil.teknasiyontrivia.utils.Utils;
import com.basil.teknasiyontrivia.utils.mvp.BasePresenter;
import com.basil.teknasiyontrivia.utils.mvp.IBaseView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Basil on 7/28/2018.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V> implements IMainPresenter, GameEventsObserver {
    IMainView mView;
    EmbeddedSocketServer mSocketServer;
    DummyRestServer mApiServer;
    public MainPresenter(GameEngine engine){
        super(engine);

        mSocketServer = new EmbeddedSocketServer(9090,false);
        mApiServer = new DummyRestServer(9191);
        try {
            mSocketServer.start(0);
            mApiServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        engine.addGameEventListener(this,GameService.EVENT_WILDCARD);
        engine.addGameEventListener(this,GameService.EVENT_QUESTION);
        engine.fetchWildCardInfo();
        GameService.getInstance().setCurrentQuestionIndex(0);
        engine.setMode(GameEngine.GameMode.PLAYER_MODE);
    }


    @Override
    public void onViewReady(IBaseView view) {
        this.mView = (IMainView)view;
        if (Utils.isInternetAvailable((MainActivity)view)){
            mView.showLoading(R.string.waiting_for_start);
        }else {
            mView.showNoInternet(R.string.no_connection);
        }

    }

    @Override
    public void onAnswerSelected(int answerId) {
        mGameEngine.sendAnswer(answerId);

    }


    @Override
    public void useWildCard() {
        mGameEngine.useWildCard();
    }

    @Override
    public void rejectWildCard() {
        mGameEngine.rejectWildCard();;

    }

    @Override
    public void notifyNewQuestion(Question question) {
        mView.swapQuestion(question,mGameEngine.getMode() == GameEngine.GameMode.PLAYER_MODE);
    }

    @Override
    public void notifyWildCardNum(int usedWildCardNumber,int wildCardNum) {
        mView.updateWildCardNumber(usedWildCardNumber,wildCardNum);
    }

    @Override
    public void notifyWildCardUserd() {

    }

    @Override
    public void notifyRightAnswer(int rightAnswerId) {
        mView.showRightAnswer(rightAnswerId);
    }

    @Override
    public void notifyWrongAnswer(int wrongAnswerId, int rightAnswerId) {
        mView.showWrongAnswer(wrongAnswerId,rightAnswerId);
        if (mGameEngine.canUseWildCard()){
            mView.showWildCardDialog();
        }
    }

    @Override
    public void notifyResultReceived(List<Standing> standings) {
        mView.moveToResults();
//        mSocketServer.closeAllConnections();
//        mApiServer.closeAllConnections();
    }

    @Override
    public void notifyCurrentQuestionIndex(int questionIndex) {
        mView.updateQuestionIndex(questionIndex,mGameEngine.getTotalQuestionNumber());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mApiServer.closeAllConnections();;
        mSocketServer.closeAllConnections();


    }
}
