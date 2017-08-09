package com.bsobat.github.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.bsobat.github.api.GitHubApi;
import com.bsobat.github.dao.GitHubDao;
import com.bsobat.github.dto.ApiResponse;
import com.bsobat.github.dto.GitHubDto;
import com.bsobat.github.dto.GitHubResponse;
import com.bsobat.github.dto.Resource;
import com.bsobat.github.utils.NetworkBoundResource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * This the Single source of truth!
 * This one will use the NetworkBoundResource
 */
public class GitHubRepository2 {
    final private GitHubApi api;
    final private GitHubDao dao;
    final private Executor executor;

    @Inject
    public GitHubRepository2(GitHubApi api, GitHubDao dao, Executor executor) {
        this.api = api;
        this.dao = dao;
        this.executor = executor;
    }

    public LiveData<Resource<List<GitHubDto>>> browseRepo(final int page, final int limit) {
        final int offset = (page - 1) * limit;
        LiveData<Resource<List<GitHubDto>>> liveData = new NetworkBoundResource<List<GitHubDto>, List<GitHubDto>>() {
            @Override
            protected void saveCallResult(@NonNull List<GitHubDto> items) {
                GitHubDto[] arr = new GitHubDto[items.size()];
                items.toArray(arr);
                dao.insertAll(arr);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<GitHubDto> data) {
                return true;//let's always refresh to be up to date. data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<GitHubDto>> loadFromDb() {
                return dao.get(offset, limit);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GitHubDto>>> createCall() {
                LiveData<ApiResponse<List<GitHubDto>>> response = api.browseRepoLiveData(page, limit);
                return response;
            }
        }.getAsLiveData();

        return liveData;
    }

    public void clearCache() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }
}
