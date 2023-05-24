package com.morax.necolock.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.morax.necolock.database.entity.Profile;

import java.util.List;

@Dao
public interface ProfileDao {

    @Insert
    void insert(Profile profile);

    @Query("SELECT * FROM Profile")
    List<Profile> getProfileList();

    @Query("UPDATE Profile SET seconds = seconds + :seconds")
    void updateProfileList(long seconds);

    @Delete
    void delete(Profile profile);

    @Update
    void update(Profile profile);
}
