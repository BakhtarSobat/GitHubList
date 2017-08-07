package com.bsobat.github.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.bsobat.github.dto.GitHubDto;
import com.bsobat.github.dto.GitHubResponse;
import com.bsobat.github.dto.Resource;
import com.bsobat.github.repo.GitHubRepository2;

import java.util.List;

import javax.inject.Inject;

public class MainActivity2ViewModel extends ViewModel {
    private GitHubRepository2 repository;

    final private MutableLiveData<Request> request = new MutableLiveData();
    final private LiveData<Resource<GitHubResponse>> result = Transformations.switchMap(request, new Function<Request, LiveData<Resource<GitHubResponse>>>() {
        @Override
        public LiveData<Resource<GitHubResponse>> apply(final Request input) {
            LiveData<Resource<List<GitHubDto>>> resourceLiveData = repository.browseRepo(input.page, input.limit);
            final MediatorLiveData<Resource<GitHubResponse>> mediator = new MediatorLiveData<Resource<GitHubResponse>>();
            mediator.addSource(resourceLiveData, new Observer<Resource<List<GitHubDto>>>() {
                @Override
                public void onChanged(@Nullable Resource<List<GitHubDto>> gitHubDtos) {
                    GitHubResponse resp = new GitHubResponse(input.page, input.limit, gitHubDtos.getData());
                    Resource<GitHubResponse> response = null;
                    switch (gitHubDtos.getStatus()){
                        case LOADING:
                            response =  Resource.<GitHubResponse>loading(resp);
                            break;
                        case SUCCESS:
                            response =  Resource.<GitHubResponse>success(resp);
                            break;
                        case ERROR:
                            response =  Resource.<GitHubResponse>error(gitHubDtos.getException(), null);
                            break;

                    }
                    mediator.setValue(response);
                }
            });
            return mediator;
        }
    });

    public static MainActivity2ViewModel create(FragmentActivity activity) {
        MainActivity2ViewModel viewModel = ViewModelProviders.of(activity).get(MainActivity2ViewModel.class);
        return viewModel;
    }

    public void load(int page, int limit) {
        request.setValue(new Request(page, limit));
    }

    @Inject
    public void setRepository(GitHubRepository2 repository) {
        this.repository = repository;
    }

    public LiveData<Resource<GitHubResponse>> getResult() {
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
