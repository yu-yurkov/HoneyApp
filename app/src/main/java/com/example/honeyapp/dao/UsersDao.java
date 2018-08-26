package com.example.honeyapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.honeyapp.entities.UsersEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM UsersEntity")
    List<UsersEntity> getAll();

    @Query("SELECT * FROM UsersEntity WHERE id = :id")
    UsersEntity getById(int id);

    @Insert(onConflict = IGNORE)
    void insert(UsersEntity usersEntity);

    @Update
    void update(UsersEntity usersEntity);

    @Delete
    void delete(UsersEntity usersEntity);

    @Query("DELETE FROM UsersEntity")
    void deleteAll();

}
