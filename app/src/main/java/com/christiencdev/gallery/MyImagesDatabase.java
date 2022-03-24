package com.christiencdev.gallery;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//Database class which handles db functionality
@Database(entities = MyImages.class, version = 1)
public abstract class MyImagesDatabase extends RoomDatabase {

    //static = anywhere
    private static MyImagesDatabase instance;

    //abstract = within class
    public abstract MyImagesDAO myImagesDAO();

    //database method to create db
    public static synchronized MyImagesDatabase getInstance(Context context){
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), MyImagesDatabase.class, "my_images_db").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
