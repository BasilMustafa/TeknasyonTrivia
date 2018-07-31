package com.basil.teknasiyontrivia.ui.main;

import com.basil.teknasiyontrivia.model.Question;
import com.basil.teknasiyontrivia.utils.mvp.IBasePresenter;
import com.basil.teknasiyontrivia.utils.mvp.IBaseView;

/**
 * Created by Basil on 7/28/2018.
 */

public interface IMainPresenter extends IBasePresenter{
    void onViewReady(IBaseView view);
    void onAnswerSelected(int answerId);
    void useWildCard();
    void rejectWildCard();

}
