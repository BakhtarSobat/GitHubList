package com.bsobat.github.repo;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bsobat.github.api.GitHubApi;
import com.bsobat.github.dao.GitHubDao;
import com.bsobat.github.dto.Repo;
import com.bsobat.github.dto.RepoResponse;
import com.bsobat.github.dto.Resource;
import com.bsobat.github.exception.AppException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This the Single source of truth!
 */
public class GitHubRepository {
    final private GitHubApi api;
    private final GitHubDao dao;
    private Object MutableLiveData;

    @Inject
    public GitHubRepository(GitHubApi api, GitHubDao dao) {
        this.api = api;
        this.dao = dao;
    }

    public LiveData<Resource<RepoResponse>> browseRepo(final int page, final int limit) {
        refresh(page, limit);
        int offset = (page - 1) * limit;
        final MediatorLiveData mediator = new MediatorLiveData();
        final LiveData<List<Repo>> source = dao.get(offset, limit);

        /**
         * We will create a mediator to observe the changes. Room will automatically notify
         * all active observers when the data changes.
         * Because it is using LiveData,
         * this will be efficient because it will update the data only if there is at least one active observer.
         */
        mediator.addSource(source, new Observer<List<Repo>>() {
            @Override
            public void onChanged(@Nullable List<Repo> repos) {
                Log.d("DATA", "Observed: "+page+" , "+limit);
                RepoResponse resp = new RepoResponse(page, limit, repos);
                Resource<RepoResponse> success = Resource.<RepoResponse>success(resp);
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
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    int offset = (page - 1) * limit;
                    List<Repo> list = dao.hasData(offset, limit);
                    if (list != null && !list.isEmpty()) {
                        //The data is cached, we don't need to go retrieve the data from the server.
                        Log.d("DATA", "From cache");
                        return null;
                    }
                    Log.d("DATA", "Fetching from server: "+page+" , "+limit);
                    Response<List<Repo>> response = api.browseRepo(page, limit).execute();
                    List<Repo> body = response.body();
                    if (body != null) {
                        Repo[] arr = new Repo[body.size()];
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
                return null;
            }
        }).execute();


    }

    public void clearCache() {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dao.deleteAll();
                return null;
            }
        }).execute();
    }
}
