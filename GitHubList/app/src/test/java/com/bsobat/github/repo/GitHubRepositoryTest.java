package com.bsobat.github.repo;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.bsobat.github.api.GitHubApi;
import com.bsobat.github.dao.GitHubDao;
import com.bsobat.github.dto.GitHubDto;
import com.bsobat.github.dto.Owner;
import com.bsobat.github.dto.GitHubResponse;
import com.bsobat.github.dto.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import githublist.bsobat.com.githublist.TestUtil;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;

public class GitHubRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    private Observer<Resource<GitHubResponse>> observer;
    private GitHubApi api;
    private GitHubDao dao;
    private GitHubRepository repo;
    private Executor executor;
    @Before
    public void init(){
        api = Mockito.mock(GitHubApi.class);
        dao = Mockito.mock(GitHubDao.class);
        executor = Mockito.mock(Executor.class);
        repo = new GitHubRepository(api, dao, executor);
    }

    @Test
    public void testBrowseRepo() throws InterruptedException {
        MutableLiveData<List<GitHubDto>> daoResponse = new MutableLiveData();
        List<GitHubDto> listOfGitHubDto = new ArrayList<>();
        {
            GitHubDto dto = new GitHubDto();
            dto.setDescription("Description");
            dto.setId("dto1");
            dto.setName("Some name");
            Owner owner = new Owner();
            owner.setId("user1");
            owner.setAvatarUrl("");
            owner.setLogin("BSobat");
            dto.setOwner(owner);
            listOfGitHubDto.add(dto);
        }
        {
            GitHubDto dto = new GitHubDto();
            dto.setDescription("Description2");
            dto.setId("dto2");
            dto.setName("Some name2");
            Owner owner = new Owner();
            owner.setId("user2");
            owner.setAvatarUrl("");
            owner.setLogin("BSobat");
            dto.setOwner(owner);
            listOfGitHubDto.add(dto);
        }
        daoResponse.setValue(listOfGitHubDto);
        Mockito.when(dao.get(anyInt(), anyInt())).thenReturn(daoResponse);
        Mockito.when(dao.hasData(anyInt(), anyInt())).thenReturn(listOfGitHubDto);
        LiveData<Resource<GitHubResponse>> response = repo.browseRepo(1, 15);

        Resource<GitHubResponse> value = TestUtil.getValue(response);
        assertNotNull(value);
        assertEquals(Resource.Status.SUCCESS, value.getStatus());
        assertEquals(2, value.getData().getList().size());
    }

}