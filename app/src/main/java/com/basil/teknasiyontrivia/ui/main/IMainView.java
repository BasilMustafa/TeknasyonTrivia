package com.basil.teknasiyontrivia.ui.main;

import android.support.annotation.StringRes;

import com.basil.teknasiyontrivia.game.GameEngine;
import com.basil.teknasiyontrivia.model.Question;
import com.basil.teknasiyontrivia.utils.mvp.IBaseView;

/**
 * Created by Basil on 7/28/2018.
 */

public interface IMainView extends IBaseView{
    void showLoading(String loadingMessage);

    void showLoading(@StringRes int resId);

    void hideLoading();

    void showMessage(String messageText);

    void showMessage(@StringRes int resId);

    void showNoInternet(@StringRes int resId);

    void swapQuestion(Question questionm, boolean isTouchable);
    void updateWildCardNumber(int usedWildCardNumber,int wildCardNumber);
    void showRightAnswer(int answerId);
    void showWrongAnswer(int wrongAnswerID, int rightAnswerId);
    void showWildCardDialog();
    void moveToResults();
    void updateQuestionIndex(int questionIndex, int totalQuestionNumber);

}
