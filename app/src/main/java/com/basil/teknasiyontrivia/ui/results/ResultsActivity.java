package com.basil.teknasiyontrivia.ui.results;

import android.databinding.DataBindingUtil;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.basil.teknasiyontrivia.R;
import com.basil.teknasiyontrivia.databinding.ActivityResultsBinding;
import com.basil.teknasiyontrivia.di.component.ActivityComponent;
import com.basil.teknasiyontrivia.game.GameEngine;
import com.basil.teknasiyontrivia.model.Standing;

import java.util.List;

import javax.inject.Inject;

public class ResultsActivity extends AppCompatActivity implements IResultsView{


    IResultsPresenter mPresenter;
    ResultsAdapter mAdapter;
    ActivityComponent mActivityComponent;
    ActivityResultsBinding mBindidng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindidng = DataBindingUtil.setContentView(this,R.layout.activity_results);
        mBindidng.standingList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mBindidng.standingList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mPresenter = new ResultsPresenter<>(GameEngine.getInstance());
        mAdapter = new ResultsAdapter();
        mBindidng.standingList.setAdapter(mAdapter);
        mPresenter.onViewReady(this);

    }


    @Override
    public void publishStandings(List<Standing> standings) {
        mAdapter.setmData(standings);
    }
}
