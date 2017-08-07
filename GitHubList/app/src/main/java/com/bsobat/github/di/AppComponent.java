package com.bsobat.github.di;

import android.content.Context;

import com.bsobat.github.di.module.ApiModule;
import com.bsobat.github.di.module.AppModule;
import com.bsobat.github.di.module.DaoModule;
import com.bsobat.github.di.module.NetModule;
import com.bsobat.github.di.module.RepositoryModule;
import com.bsobat.github.viewmodel.MainActivity2ViewModel;
import com.bsobat.github.viewmodel.MainActivityViewModel;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
        modules = {AppModule.class, NetModule.class, RepositoryModule.class, ApiModule.class, DaoModule.class}
)
public interface AppComponent {
    public void inject(MainActivityViewModel viewModelModule);
    public void inject(MainActivity2ViewModel viewModelModule);

    public void inject(Context content);


}
