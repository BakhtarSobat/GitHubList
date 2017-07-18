package com.bsobat.github;

import android.app.Application;

import com.bsobat.github.di.AppComponent;
import com.bsobat.github.di.DaggerAppComponent;
import com.bsobat.github.di.module.ApiModule;
import com.bsobat.github.di.module.AppModule;
import com.bsobat.github.di.module.DaoModule;
import com.bsobat.github.di.module.NetModule;
import com.bsobat.github.di.module.RepositoryModule;

public class MyApplication extends Application{

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public void onCreate(){
        super.onCreate();

        appComponent = DaggerAppComponent.builder().
                appModule(new AppModule(this)).
                apiModule(new ApiModule()).
                netModule(new NetModule("https://api.github.com")).
                daoModule(new DaoModule()).
                repositoryModule(new RepositoryModule()).
                build();

    }
}
