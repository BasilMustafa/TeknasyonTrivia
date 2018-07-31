package com.basil.teknasiyontrivia.observers;

import com.basil.teknasiyontrivia.model.Question;
import com.basil.teknasiyontrivia.model.Standing;

import java.util.List;

/**
 * Created by Basil on 7/30/2018.
 */

public interface GameEventsObserver {
    void notifyNewQuestion(Question question);
    void notifyWildCardNum(int usedWildCards, int wildCardNum);
    void notifyWildCardUserd();
    void notifyRightAnswer(int rightAnswerId);
    void notifyWrongAnswer(int wrongAnswerId, int rightAnswerId);
    void notifyResultReceived(List<Standing> standings);
    void notifyCurrentQuestionIndex(int questionIndex);
}
