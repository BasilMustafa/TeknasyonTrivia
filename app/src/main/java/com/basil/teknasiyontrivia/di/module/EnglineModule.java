package com.basil.teknasiyontrivia.di.module;

import com.basil.teknasiyontrivia.game.GameEngine;
import com.basil.teknasiyontrivia.network.GameService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Basil on 7/29/2018.
 */

@Module
public class EnglineModule {
    static GameEngine gameEngine;

    @Provides
    GameEngine providesGameEngine(){
        return GameEngine.getInstance();
    }

}
