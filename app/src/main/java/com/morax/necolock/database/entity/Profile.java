package com.morax.necolock.database.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profile {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public long seconds = 0;
    public boolean block = false;

    public Profile(String name) {
        this.name = name;
    }
}
