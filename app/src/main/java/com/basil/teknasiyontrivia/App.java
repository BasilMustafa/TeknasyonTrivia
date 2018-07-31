package com.basil.teknasiyontrivia;

import android.app.Application;


import com.basil.teknasiyontrivia.di.component.ApplicationComponent;
import com.basil.teknasiyontrivia.di.component.DaggerApplicationComponent;
import com.basil.teknasiyontrivia.di.module.ApplicationModule;

public class App extends Application {

    private ApplicationComponent mApplicationComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        mApplicationComponent.inject(this);

    }

}
