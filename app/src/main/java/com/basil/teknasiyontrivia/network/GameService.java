package com.basil.teknasiyontrivia.network;

import com.basil.teknasiyontrivia.game.GameEngine;
import com.basil.teknasiyontrivia.model.Question;
import com.basil.teknasiyontrivia.model.Standing;
import com.basil.teknasiyontrivia.model.StandingMessage;
import com.basil.teknasiyontrivia.model.WildCardResponse;
import com.basil.teknasiyontrivia.observers.GameEventsObservable;
import com.basil.teknasiyontrivia.observers.GameEventsObserver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Basil on 7/30/2018.
 */

public class GameService implements GameNetworkInterface, GameEventsObservable {
    OkHttpClient mClient;
    Retrofit mRetrofit;
    Gson gson;
    public static final String EVENT_WILDCARD = "WILD_CARD_EVENT";
    public static final String EVENT_QUESTION = "QUESTION_EVENT";
    public static final String FIRST_QUESTION = "FIRST_QUESTION";
    public static final String ANSWER = "ANSWER";
    public static final String RIGHT_ANSWER = "RIGHT_ANSWER";
    public static final String WRONG_ANSWER = "WRONG_ANSWER";
    public static final String QUESTION = "QUESTION";
    public static final String WILD_CARD = "WILD_CARD";
    public static final String RESULT = "RESULT";
    public static final String REJECT_WILD_CARD = "REJECT_WILD_CARD";
    public static final String ANSWER_STREAMED = "ANSWER_STREAMED";
    static private List<GameEventsObserver> questionObservers;
    static private List<GameEventsObserver> wildCardObservers;
    static GameService instance;

    public static GameService getInstance() {
        if (instance == null)
            instance = new GameService();
        return instance;
    }

    private GameService() {
        mRetrofit = new Retrofit.Builder()
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(WildCardApi.EndPoint)
                .build();
        mClient = new OkHttpClient.Builder().build();
        questionObservers = new ArrayList<>();
        wildCardObservers = new ArrayList<>();
        gson = new Gson();
        currentQuestionIndex = 0;
    }

    @Override
    public void fetchWildCardInfo() {
        mRetrofit.create(WildCardApi.class)
                .getWildCardCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WildCardResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(WildCardResponse wildCardResponse) {
                        notifyWildCardObservers(wildCardResponse.getWildcardNum());
                        //start chat with sockerserver
                        GameEngine.getInstance().setTotalQuestionNumber(wildCardResponse.getTotalQuestions());
                        Request request = new Request.Builder()
                                .url("ws://localhost:9090/")
                                .build();
                        webSocket = mClient.newWebSocket(request, mGameListeners);


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    WebSocket webSocket;


    @Override
    public void registerObserver(GameEventsObserver observer, String eventType) {
        if (eventType.equals(EVENT_QUESTION))
            questionObservers.add(observer);
        else if (eventType.equals(EVENT_WILDCARD))
            wildCardObservers.add(observer);
    }


    private void notifyWildCardObservers(int wildCardNum) {
        for (GameEventsObserver observer : wildCardObservers) {
            observer.notifyWildCardNum(0, wildCardNum);
        }
    }

    private void notifyQuestionObserver(Question question) {
        for (GameEventsObserver observer : questionObservers)
            observer.notifyNewQuestion(question);
        currentQuestionIndex++;
        notifyQuestionIndex(currentQuestionIndex);
    }

    int currentQuestionIndex = 0;

    private void notifyQuestionIndex(int questionIndex) {
        for (GameEventsObserver observer : questionObservers) {
            observer.notifyCurrentQuestionIndex(questionIndex);
        }
    }

    private void notifyResultsReceived(List<Standing> results) {
        for (GameEventsObserver observer : questionObservers) {
            observer.notifyResultReceived(results);
        }
        //observer.notifyNewQuestion(question);
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    WebSocketListener mGameListeners = new WebSocketListener() {
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            try {
                JSONObject object = new JSONObject(text);
                if (object.getString("message").equals(QUESTION)) {
                    Question question = gson.fromJson(object.getString("data"), Question.class);
                    notifyQuestionObserver(question);

                } else if (object.getString("message").equals(WRONG_ANSWER)) {
                    JSONObject dataObject = new JSONObject(object.getString("data"));
                    notifyWrongAnswer(dataObject.getInt("wrong_answer"), dataObject.getInt("right_answer"));
                } else if (object.getString("message").equals(RIGHT_ANSWER)) {
                    notifyCorrectAnswer(chosenAnswer);
                } else if (object.getString("message").equals(RESULT)) {
                    StandingMessage message = gson.fromJson(object.toString(), StandingMessage.class);
                    notifyResultsReceived(message.getData().getStandings());
                } else if (object.getString("message").equals(ANSWER_STREAMED)) {
                    notifyCorrectAnswer(object.getInt("answer_id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
        }
    };


    public void sendAnswerForQuestion(int questionId, int answerId) {
        chosenAnswer = answerId;
        JSONObject object = new JSONObject();
        try {
            object.put("message", GameService.ANSWER);
            JSONObject answer = new JSONObject();
            answer.put("question_id", questionId);
            answer.put("answer_id", answerId);
            object.put("data", answer);
            webSocket.send(object.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notifyCorrectAnswer(int answerId) {
        for (GameEventsObserver observer : questionObservers) {
            observer.notifyRightAnswer(answerId);
        }
    }

    private void notifyWrongAnswer(int wrongAnswerId, int rightAnswerId) {
        for (GameEventsObserver observer : questionObservers) {
            observer.notifyWrongAnswer(wrongAnswerId, rightAnswerId);
        }
    }


    public void useWildCard() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", WILD_CARD);
            webSocket.send(jsonObject.toString());
            notifyCardUsed();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void rejectWildCard() {
        JSONObject object = new JSONObject();
        try {
            object.put("message", REJECT_WILD_CARD);
            webSocket.send(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    int chosenAnswer;

    private void notifyCardUsed() {
        for (GameEventsObserver observer : wildCardObservers)
            observer.notifyWildCardNum(GameEngine.getInstance().getUsedWildCardNum(), GameEngine.getInstance().getWildCardNum());
    }
}
