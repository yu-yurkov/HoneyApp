package com.example.honeyapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.honeyapp.dao.StoreDao;
import com.example.honeyapp.dao.UserCartDao;
import com.example.honeyapp.dao.UsersDao;
import com.example.honeyapp.entities.StoreEntity;
import com.example.honeyapp.entities.UserCartEntity;
import com.example.honeyapp.entities.UsersEntity;

@Database(entities = {UserCartEntity.class, StoreEntity.class, UsersEntity.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserCartDao userCartDao();
    public abstract StoreDao storeDao();
    public abstract UsersDao usersDao();

}
