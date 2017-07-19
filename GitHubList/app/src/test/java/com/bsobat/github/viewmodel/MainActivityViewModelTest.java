package com.bsobat.github.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.bsobat.github.dto.GitHubResponse;
import com.bsobat.github.dto.Resource;
import com.bsobat.github.repo.GitHubRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import githublist.bsobat.com.githublist.TestUtil;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MainActivityViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private MainActivityViewModel mavm;
    private GitHubRepository repo;
    private MutableLiveData<Resource<GitHubResponse>> value;
    private Observer<Resource<GitHubResponse>> observer;
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        repo = Mockito.mock(GitHubRepository.class);
        value = Mockito.mock(MutableLiveData.class);
        observer = Mockito.mock(Observer.class);
        mavm = new MainActivityViewModel();
        mavm.setRepository(repo);
        //mavm.getResult().observeForever(observer);
    }
    @Test
    public void load() throws Exception {
        GitHubResponse responseData = new GitHubResponse(1, 15, Collections.EMPTY_LIST);
        Resource<GitHubResponse> resource = Resource.success(responseData);
        MutableLiveData<Resource<GitHubResponse>> resp = new MutableLiveData<>();
        resp.setValue(resource);
        when(repo.browseRepo(1, 15)).thenReturn(resp);
        mavm.setRepository(repo);

        mavm.load(1, 15);
        Resource<GitHubResponse> data = TestUtil.getValue(mavm.getResult());
        Assert.assertEquals(Resource.Status.SUCCESS, data.getStatus());
        Assert.assertEquals(true, data.getData().getList().isEmpty());
        Assert.assertEquals(1, data.getData().getPage());
        Assert.assertEquals(15, data.getData().getLimit());

    }

}