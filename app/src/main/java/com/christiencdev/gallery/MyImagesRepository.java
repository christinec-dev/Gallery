package com.christiencdev.gallery;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Our repository defines a method of storing, updating, and extracting the data stored in db
public class MyImagesRepository {
    private MyImagesDAO myImagesDAO;
    private LiveData<List<MyImages>> imagesList;

    //Executors class allows us to run methods in the background
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    //initializes repository
    public MyImagesRepository (Application application){
        //DB
        MyImagesDatabase database = MyImagesDatabase.getInstance(application);
        //DAO
        myImagesDAO = database.myImagesDAO();
        //Images Array
        imagesList = myImagesDAO.getAllImages();
    }

    //insert method
    public void insert(MyImages myImages)
    {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDAO.insert(myImages);
            }
        });
    }

    //delete method
    public void delete(MyImages myImages)
    {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDAO.delete(myImages);
            }
        });
    }

    //update method
    public void update(MyImages myImages)
    {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDAO.update(myImages);
            }
        });
    }

    //get all images method
    public LiveData<List<MyImages>> getAllImages()
    {
        return imagesList;
    }
}
