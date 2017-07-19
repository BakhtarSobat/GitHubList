package com.bsobat.github.api;

import com.bsobat.github.dto.GitHubDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubApi {
    @GET("/users/JakeWharton/repos")
    public Call<List<GitHubDto>> browseRepo(@Query("page") int page, @Query("per_page") int limit);

}
