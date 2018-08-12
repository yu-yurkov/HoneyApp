package com.example.honeyapp.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.honeyapp.entities.StoreEntity;
import com.example.honeyapp.entities.UserCartEntity;
import com.example.honeyapp.model.Store;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface StoreDao {

    @Query("SELECT * FROM StoreEntity")
    List<StoreEntity> getAll();

    @Query("SELECT * FROM StoreEntity WHERE id = :id")
    StoreEntity getById(int id);

    @Insert(onConflict = IGNORE)
    void insert(StoreEntity storeEntity);

    @Update
    void update(StoreEntity storeEntity);

    @Delete
    void delete(StoreEntity storeEntity);

    @Query("DELETE FROM StoreEntity")
    void deleteAll();
}
