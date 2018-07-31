package com.basil.teknasiyontrivia.di.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Module
public class ApplicationModule {
    Application mApplication;


    public ApplicationModule(Application application){
        mApplication = application;
    }

    @Provides
    public Application providesApplication(){
        return mApplication;
    }




}
