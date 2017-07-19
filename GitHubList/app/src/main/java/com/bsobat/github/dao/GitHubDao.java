package com.bsobat.github.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bsobat.github.dto.GitHubDto;

import java.util.List;

@Dao
public interface GitHubDao {

    @Query("SELECT * FROM GitHubDto LIMIT :limit OFFSET :offset")
    LiveData<List<GitHubDto>> get(int offset, int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(GitHubDto... gitHubDtos);

    @Delete
    void delete(GitHubDto gitHubDto);

    @Query("SELECT * FROM GitHubDto LIMIT :limit OFFSET :offset")
    List<GitHubDto> hasData(int offset, int limit);

    @Query("DELETE FROM GitHubDto")
    void deleteAll();
}
