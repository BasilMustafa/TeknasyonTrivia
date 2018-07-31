package com.basil.teknasiyontrivia.di.component;

import com.basil.teknasiyontrivia.di.PerActivity;
import com.basil.teknasiyontrivia.di.module.ActivityModule;
import com.basil.teknasiyontrivia.di.module.EnglineModule;
import com.basil.teknasiyontrivia.ui.main.MainActivity;
import com.basil.teknasiyontrivia.ui.results.ResultsActivity;

import dagger.Component;


/**
 * Created by Basil on 7/28/2018.
 */

@PerActivity
@Component(modules = {ActivityModule.class, EnglineModule.class})
public interface ActivityComponent {


    void inject(MainActivity activity);

    void inject(ResultsActivity activity);




}
