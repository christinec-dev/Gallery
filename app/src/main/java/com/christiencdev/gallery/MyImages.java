package com.christiencdev.gallery;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Entity Component which contains db items
@Entity(tableName = "my_images")
public class MyImages {

    @PrimaryKey(autoGenerate = true)
    public int image_id;

    public String image_title;
    public String image_description;

    //media objects are saved as blob items in db
    public byte[] image;

    //constructor
    public MyImages(String image_title, String image_description, byte[] image) {
        this.image_title = image_title;
        this.image_description = image_description;
        this.image = image;
    }

    //getter & setter
    public int getImage_id() {
        return image_id;
    }

    public String getImage_title() {
        return image_title;
    }

    public String getImage_description() {
        return image_description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
}
