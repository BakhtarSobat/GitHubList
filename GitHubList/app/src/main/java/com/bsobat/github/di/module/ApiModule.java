package com.bsobat.github.di.module;

import com.bsobat.github.api.GitHubApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {
    @Provides
    @Singleton
    public GitHubApi providesCatalogApi(Retrofit retrofit) {
        return retrofit.create(GitHubApi.class);
    }
}
