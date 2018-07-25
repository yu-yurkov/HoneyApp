package com.example.honeyapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.entities.UserCartEntity;

@Database(entities = {UserCartEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserCartDao userCartDao();

}
