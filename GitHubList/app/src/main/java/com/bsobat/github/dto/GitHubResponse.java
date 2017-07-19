package com.bsobat.github.dto;

import java.util.List;

public class GitHubResponse {
    final private int page;
    final private int limit;
    final private List<GitHubDto> list;

    public GitHubResponse(int page, int limit, List<GitHubDto> list) {
        this.page = page;
        this.limit = limit;
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public List<GitHubDto> getList() {
        return list;
    }
}
