package com.bsobat.github.di.module;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public Executor provideExecutor(){
        return new Executor() {
            public void execute(Runnable r) {
                new Thread(r).start();
            }
        };
    }
}
