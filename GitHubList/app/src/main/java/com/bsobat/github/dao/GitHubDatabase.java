package com.bsobat.github.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.bsobat.github.dto.GitHubDto;

@Database(entities = {GitHubDto.class}, version = 2)
public abstract class GitHubDatabase extends RoomDatabase {
    public abstract GitHubDao gitHubDao();
}
