package com.bsobat.github.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.bsobat.github.dao.GitHubDao;
import com.bsobat.github.dao.GitHubDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {
    @Provides
    @Singleton
    public GitHubDao provideGitHubDao(Application app) {
        GitHubDatabase db = Room.databaseBuilder(app,
                GitHubDatabase.class, "github-db").build();
        return db.gitHubDao();
    }
}
