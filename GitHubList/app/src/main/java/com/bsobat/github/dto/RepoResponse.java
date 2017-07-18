package com.bsobat.github.dto;

import java.util.List;

public class RepoResponse {
    final private int page;
    final private int limit;
    final private List<Repo> list;

    public RepoResponse(int page, int limit, List<Repo> list) {
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

    public List<Repo> getList() {
        return list;
    }
}
