package com.bsobat.github.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.bsobat.github.dto.Repo;

@Database(entities = {Repo.class}, version = 1)
public abstract class GitHubDatabase extends RoomDatabase {
    public abstract GitHubDao gitHubDao();
}
