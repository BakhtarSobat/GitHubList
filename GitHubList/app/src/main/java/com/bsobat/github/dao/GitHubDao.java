package com.bsobat.github.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bsobat.github.dto.Repo;

import java.util.List;

@Dao
public interface GitHubDao {

    @Query("SELECT * FROM repo LIMIT :limit OFFSET :offset")
    LiveData<List<Repo>> get(int offset, int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Repo... repos);

    @Delete
    void delete(Repo repo);

    @Query("SELECT * FROM repo LIMIT :limit OFFSET :offset")
    List<Repo> hasData(int offset, int limit);

    @Query("DELETE FROM repo")
    void deleteAll();
}
