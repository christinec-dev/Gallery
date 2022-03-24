package com.christiencdev.gallery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//View model bridges the UI and db operations - hence it can be used in activities
public class MyImagesViewModel extends AndroidViewModel {
    private  MyImagesRepository repository;
    private LiveData<List<MyImages>> imagesList;

    public MyImagesViewModel(@NonNull Application application) {
        super(application);

        repository = new MyImagesRepository(application);
        imagesList = repository.getAllImages();
    }

    //initializes repository methods
    public void insert(MyImages myImages){
        repository.insert(myImages);
    }

    public void update(MyImages myImages){
        repository.update(myImages);
    }

    public void delete(MyImages myImages){
        repository.delete(myImages);
    }

    public LiveData<List<MyImages>> getAllImages(){
        return imagesList;
    }
}
