package com.basil.teknasiyontrivia.di.component;

import com.basil.teknasiyontrivia.App;
import com.basil.teknasiyontrivia.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(App app);

}
