package com.example.honeyapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.honeyapp.dao.StoreDao;
import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.entities.StoreEntity;
import com.example.honeyapp.entities.UserCartEntity;

@Database(entities = {UserCartEntity.class, StoreEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserCartDao userCartDao();
    public abstract StoreDao storeDao();

}
