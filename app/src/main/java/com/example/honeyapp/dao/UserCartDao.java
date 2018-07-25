package com.example.honeyapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.honeyapp.entities.UserCartEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface UserCartDao {

    @Query("SELECT * FROM UserCartEntity")
    List<UserCartEntity> getAll();

    @Query("SELECT * FROM UserCartEntity WHERE id = :id")
    UserCartEntity getById(int id);


    @Insert(onConflict = IGNORE)
    void insert(UserCartEntity userCartEntity);

    @Update
    void update(UserCartEntity userCartEntity);

    @Delete
    void delete(UserCartEntity userCartEntity);

    @Query("DELETE FROM UserCartEntity")
    void deleteAll();

}
