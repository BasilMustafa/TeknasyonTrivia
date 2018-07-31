package com.basil.teknasiyontrivia.di.module;

import com.basil.teknasiyontrivia.game.GameEngine;
import com.basil.teknasiyontrivia.ui.main.IMainPresenter;
import com.basil.teknasiyontrivia.ui.main.IMainView;
import com.basil.teknasiyontrivia.ui.main.MainPresenter;
import com.basil.teknasiyontrivia.ui.results.IResultsPresenter;
import com.basil.teknasiyontrivia.ui.results.ResultsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Basil on 7/29/2018.
 */

@Module
public class ActivityModule {

    @Provides
    public IMainPresenter providesMainPresnter(GameEngine gameEngine){
        return new MainPresenter<IMainView>(gameEngine);
    }

    @Provides
    public IResultsPresenter prvidesResultsPresenter(GameEngine gameEngine){
        return new ResultsPresenter<>(gameEngine);
    }



}
