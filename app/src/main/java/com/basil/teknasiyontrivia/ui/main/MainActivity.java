package com.basil.teknasiyontrivia.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basil.teknasiyontrivia.R;
import com.basil.teknasiyontrivia.databinding.ActivitySplashBinding;
import com.basil.teknasiyontrivia.databinding.QuestionViewBinding;
import com.basil.teknasiyontrivia.di.component.ActivityComponent;
import com.basil.teknasiyontrivia.di.component.DaggerActivityComponent;
import com.basil.teknasiyontrivia.di.module.ActivityModule;
import com.basil.teknasiyontrivia.game.GameEngine;
import com.basil.teknasiyontrivia.model.Answer;
import com.basil.teknasiyontrivia.model.Question;
import com.basil.teknasiyontrivia.ui.results.ResultsActivity;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements IMainView {
    @Inject
    IMainPresenter mPresenter;

    ActivityComponent mActivityComponent;
    ActivitySplashBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        mActivityComponent = DaggerActivityComponent.builder().activityModule(new ActivityModule()).build();
        mActivityComponent.inject(this);
        mPresenter.onViewReady(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void showLoading(String loadingMessage) {
        mBinding.loadingMessage.setVisibility(View.VISIBLE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading(@StringRes int resId) {
        mBinding.loadingMessage.setText(resId);
        mBinding.loadingMessage.setVisibility(View.VISIBLE);
        mBinding.progressBar.setVisibility(View.VISIBLE);;
    }

    @Override
    public void hideLoading() {
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.loadingMessage.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String messageText) {


    }

    @Override
    public void showMessage(@StringRes int resId) {

    }

    @Override
    public void showNoInternet(@StringRes int resId) {
        mBinding.offlineIcon.setVisibility(View.VISIBLE);
        mBinding.loadingMessage.setText(resId);
        mBinding.loadingMessage.setVisibility(View.VISIBLE);
    }



    @Override
    public void swapQuestion(final Question question, final boolean isTouchable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                mBinding.container.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                QuestionViewBinding binding = DataBindingUtil.inflate(inflater,R.layout.question_view,mBinding.container,false);
                binding.setQuestion(question);
                mBinding.container.addView(binding.getRoot());
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onAnswerSelected((Integer) view.getTag());
                    }
                };
                for (Answer answer:question.getAnswers()){
                    View view = inflater.inflate(R.layout.answer_view,binding.answersContainer,false);
                    ((TextView)view.findViewById(R.id.answer_text)).setText(answer.getAnswerText());
                    view.setTag(answer.getId());
                    if (isTouchable) {
                        view.setOnClickListener(listener);
                    }else {
                        view.setFocusable(false);
                        view.setClickable(false);
                    }
                    binding.answersContainer.addView(view);
                }

            }

        });

    }

    @Override
    public void updateWildCardNumber(final int usedWildCardNumber,final int wildCardNumber) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBinding.wildCard.setText(getString(R.string.wild_card_text,usedWildCardNumber,wildCardNumber));

            }
        });
    }

    @Override
    public void showRightAnswer(final int answerId) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              LinearLayout answerContainer = mBinding.container;
                              CardView answerView = answerContainer.findViewWithTag(answerId);
                              answerView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                          }
                      }
        );

    }
    @Override
    public void showWrongAnswer(final int wrongAnswerID, final int rightAnswerId) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              LinearLayout answerContainer = mBinding.container;
                              CardView rightAnswerView = answerContainer.findViewWithTag(rightAnswerId);
                              rightAnswerView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                              CardView wrongAnswerView = answerContainer.findViewWithTag(wrongAnswerID);
                              wrongAnswerView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                          }
                      }
        );
    }

    @Override
    public void showWildCardDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    showDialog();
                }catch (WindowManager.BadTokenException e){
                    e.printStackTrace();
                }


            }
        });
    }
     void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.use_wild_card_message);
        builder.setNegativeButton(R.string.no_thanks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.rejectWildCard();
            }
        });
        builder.setPositiveButton(R.string.use_wild_card, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.useWildCard();
            }
        });
        AlertDialog dialog = builder.show();
        dialog.setCancelable(false);
    }

    @Override
    public void moveToResults() {
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateQuestionIndex(int questionIndex, int totalQuestionNumber) {
        mBinding.questionNum.setText(getResources().getString(R.string.question_text,questionIndex,totalQuestionNumber));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }
}
