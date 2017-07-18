package com.bsobat.github.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.bsobat.github.dto.RepoResponse;
import com.bsobat.github.dto.Resource;
import com.bsobat.github.repo.GitHubRepository;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {
    private GitHubRepository repository;

    final private MutableLiveData<Request> request = new MutableLiveData();
    final private LiveData<Resource<RepoResponse>> result = Transformations.switchMap(request, new Function<Request, LiveData<Resource<RepoResponse>>>() {
        @Override
        public LiveData<Resource<RepoResponse>> apply(Request input) {

            return repository.browseRepo(input.page, input.limit);
        }
    });

    public static MainActivityViewModel create(FragmentActivity activity) {
        MainActivityViewModel viewModel = ViewModelProviders.of(activity).get(MainActivityViewModel.class);
        return viewModel;
    }

    public void load(int page, int limit) {
        request.setValue(new Request(page, limit));
    }

    @Inject
    public void setRepository(GitHubRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<RepoResponse>> getResult() {
        return result;
    }

    public void clearCache() {
        repository.clearCache();
    }

    public static class Request {
        final private int page, limit;

        public Request(int page, int limit) {
            this.page = page;
            this.limit = limit;
        }

        public int getLimit() {
            return limit;
        }
    }

}
