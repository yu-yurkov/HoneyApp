package com.example.honeyapp.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class StoreEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public String address;
}
