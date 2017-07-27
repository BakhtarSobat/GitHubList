package com.bsobat.github.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bsobat.github.api.GitHubApi;
import com.bsobat.github.dao.GitHubDao;
import com.bsobat.github.dto.GitHubDto;
import com.bsobat.github.dto.GitHubResponse;
import com.bsobat.github.dto.Resource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.RunnableFuture;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * This the Single source of truth!
 */
public class GitHubRepository {
    final private GitHubApi api;
    final private GitHubDao dao;
    final private Executor executor;

    @Inject
    public GitHubRepository(GitHubApi api, GitHubDao dao, Executor executor) {
        this.api = api;
        this.dao = dao;
        this.executor = executor;
    }

    public LiveData<Resource<GitHubResponse>> browseRepo(final int page, final int limit) {
        refresh(page, limit);
        int offset = (page - 1) * limit;
        final LiveData<List<GitHubDto>> source = dao.get(offset, limit);

        /**
         * We will create a mediator to observe the changes. Room will automatically notify
         * all active observers when the data changes.
         * Because it is using LiveData,
         * this will be efficient because it will update the data only if there is at least one active observer.
         */
        final MediatorLiveData mediator = new MediatorLiveData();
        mediator.addSource(source, new Observer<List<GitHubDto>>() {
            @Override
            public void onChanged(@Nullable List<GitHubDto> gitHubDtos) {
                Log.d("DATA", "Observed: "+page+" , "+limit);
                GitHubResponse resp = new GitHubResponse(page, limit, gitHubDtos);
                Resource<GitHubResponse> success = Resource.<GitHubResponse>success(resp);
                mediator.setValue(success);
            }
        });

        return mediator;
    }

    private void refresh(final int page, final int limit) {

        /**
         * We will create an asynctask to retrieve the data from the server, online if needed.
         * (this depends on usecase, in our case, if there is data, we wouldn't retrieve.
         * If we need to refresh our db, we retrieve the data from the server and update
         * our local database, Room will automatically notify the active observers (see above).
         */
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int offset = (page - 1) * limit;
                    List<GitHubDto> list = dao.hasData(offset, limit);
                    if (list != null && !list.isEmpty()) {
                        //The data is cached, we don't need to go retrieve the data from the server.
                        Log.d("DATA", "From cache");
                        return;
                    }
                    Log.d("DATA", "Fetching from server: "+page+" , "+limit);
                    Response<List<GitHubDto>> response = api.browseRepo(page, limit).execute();
                    List<GitHubDto> body = response.body();
                    if (body != null) {
                        GitHubDto[] arr = new GitHubDto[body.size()];
                        body.toArray(arr);
                        //The data will be inserted into our database.
                        long[] ids = dao.insertAll(arr);
                        if (ids == null || ids.length != arr.length) {
                            Log.e("API", "Unable to insert");
                        } else {
                            Log.d("DATA", "Data inserted");
                        }
                    }
                } catch (IOException e) {
                    Log.e("API", "" + e.getMessage());
                }
            }
        });
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
