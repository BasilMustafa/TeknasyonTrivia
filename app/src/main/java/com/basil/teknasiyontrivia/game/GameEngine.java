package com.basil.teknasiyontrivia.game;

import com.basil.teknasiyontrivia.model.Question;
import com.basil.teknasiyontrivia.model.Standing;
import com.basil.teknasiyontrivia.network.GameService;
import com.basil.teknasiyontrivia.network.WildCardApi;
import com.basil.teknasiyontrivia.observers.GameEventsObserver;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Basil on 7/28/2018.
 */

public class GameEngine implements GameEventsObserver {
    public enum GameMode {
        PLAYER_MODE,
        VIEWER_MODE
    }

    private String userName;
    private int wildCardNum;
    private int usedWildCardNum;
    private GameMode mode;
    private boolean usedWildCard;
    private int questionNum;
    private boolean isGameEnded;
    private int totalQuestionNumber;
    private int currentQuestionId;
    private boolean gameEnded;
    private int mCurrentQuestionId;
    private GameService mGameService;
    private List<Standing> standings;
    OkHttpClient client;
    private Retrofit retrofit;

    private static GameEngine instance;

    private GameEngine(String userName, int wildCardNum, GameMode mode) {
        this.wildCardNum = wildCardNum;
        this.mode = mode;
        this.userName = userName;
        usedWildCard = false;
        usedWildCardNum = 0;
        currentQuestionId = 0;
        mode = GameMode.PLAYER_MODE;

        retrofit = new Retrofit.Builder()
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(WildCardApi.EndPoint)
                .build();
        mGameService = GameService.getInstance();
        addGameEventListener(this, GameService.EVENT_QUESTION);
        addGameEventListener(this, GameService.EVENT_WILDCARD);

    }

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine("", 0, GameEngine.GameMode.PLAYER_MODE);
        }
        return instance;

    }


    @Override
    public void notifyNewQuestion(Question question) {
        currentQuestionId = question.getId();
    }

    @Override
    public void notifyWildCardNum(int usedWildCardNum, int wildCardNum) {
        this.wildCardNum = wildCardNum;
        this.usedWildCardNum = usedWildCardNum;
        //do nothing
    }

    @Override
    public void notifyWildCardUserd() {
        // do nothing
    }

    @Override
    public void notifyRightAnswer(int rightAnswerId) {
    }

    @Override
    public void notifyWrongAnswer(int wrongAnswerId, int rightAnswerId) {

    }

    @Override
    public void notifyResultReceived(List<Standing> standings) {
        gameEnded = true;
        this.standings = standings;
    }

    @Override
    public void notifyCurrentQuestionIndex(int questionIndex) {

    }

    public void fetchWildCardInfo() {
        mGameService.fetchWildCardInfo();
    }

    public void useWildCard() {
        if (wildCardNum > 0) {
            usedWildCardNum++;
            usedWildCard = true;
            mGameService.useWildCard();
        }
    }

    public void rejectWildCard() {
        mGameService.rejectWildCard();
        mode = GameMode.VIEWER_MODE;
    }

    public int getWildCardNum() {
        return wildCardNum;
    }

    public void setWildCardNum(int wildCardNum) {
        this.wildCardNum = wildCardNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUsedWildCardNum() {
        return usedWildCardNum;
    }

    public void setUsedWildCardNum(int usedWildCardNum) {
        this.usedWildCardNum = usedWildCardNum;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public boolean isUsedWildCard() {
        return usedWildCard;
    }

    public void setUsedWildCard(boolean usedWildCard) {
        this.usedWildCard = usedWildCard;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public int getCurrentQuestionNum() {
        return mCurrentQuestionId;
    }

    public void setCurrentQuestionNum(int currentQuestionNum) {
        this.mCurrentQuestionId = currentQuestionNum;
    }


    public void addGameEventListener(GameEventsObserver observer, String eventType) {
        mGameService.registerObserver(observer, eventType);
    }

    public void sendAnswer(int answerId) {
        mGameService.sendAnswerForQuestion(currentQuestionId, answerId);
    }

    public boolean canUseWildCard() {
        return usedWildCardNum != wildCardNum;
    }


    public List<Standing> getStandings() {
        return standings;
    }

    public int getTotalQuestionNumber() {
        return totalQuestionNumber;
    }

    public void setTotalQuestionNumber(int totalQuestionNumber) {
        this.totalQuestionNumber = totalQuestionNumber;
    }
}
