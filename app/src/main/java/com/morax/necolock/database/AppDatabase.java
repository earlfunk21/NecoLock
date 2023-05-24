package com.morax.necolock.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.morax.necolock.database.dao.ProfileDao;
import com.morax.necolock.database.entity.Profile;

@Database(entities = {Profile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProfileDao profileDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "test_db2").allowMainThreadQueries().build();
        }
        return instance;
    }


}