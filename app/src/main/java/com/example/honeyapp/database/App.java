package com.example.honeyapp.database;

import android.app.Application;
import android.arch.persistence.room.Room;

public class App extends Application {

    public static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database =  Room.databaseBuilder(this, AppDatabase.class, "theHoney").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
