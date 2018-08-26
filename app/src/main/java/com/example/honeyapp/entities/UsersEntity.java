package com.example.honeyapp.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class UsersEntity {

    @PrimaryKey
    public int id;
    public String token;

}
