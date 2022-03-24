package com.christiencdev.gallery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateImageActivity extends AppCompatActivity {

    private ImageView updateImage;
    private EditText title, description;
    private Button btnUpdate;

    //data to be transferred
    private String titleValue, descriptionValue;
    private  int id;
    private  byte[] image;


    //for intent activity
    ActivityResultLauncher<Intent> activityResultLauncherForSelectImages;

    //image conversion
    private Bitmap selectedImage;
    private Bitmap scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Edit Image Details");
        setContentView(R.layout.activity_update_image);

        //register intent activity
        registerActivityForSelectImage();

        updateImage = findViewById(R.id.imageViewUpdateImage);
        title = findViewById(R.id.editTextTitleUpdate);
        description = findViewById(R.id.editTextDescriptionUpdate);
        btnUpdate = findViewById(R.id.buttonUpdate);

        //get data
        id = getIntent().getIntExtra("id", -1);
        titleValue = getIntent().getStringExtra("titleValue");
        descriptionValue = getIntent().getStringExtra("descriptionValue");
        image = getIntent().getByteArrayExtra("image");

        //set data
        title.setText(titleValue);
        description.setText(descriptionValue);
        updateImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        //image select
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectImages.launch(intent);
            }
        });

        //to save
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    public void updateData(){
        //id check
        if (id == -1){
            Toast.makeText(UpdateImageActivity.this, "Selected image is not available or doesn't exist.", Toast.LENGTH_SHORT).show();
        }
        else{
            String updateTitleValue = title.getText().toString();
            String updateDescriptionValue = description.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.putExtra("updateTitleValue", updateTitleValue);
            intent.putExtra("updateDescriptionValue", updateDescriptionValue);
                //if user doesn't select an image, use original one
                if (selectedImage == null){
                    intent.putExtra("image", image);
                }
                else
                {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    scaledImage = makeSmall(selectedImage, 300);
                    scaledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                    byte[] image = outputStream.toByteArray();

                    //send data to MainActivity
                    intent.putExtra("image", image);
                }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void registerActivityForSelectImage(){
        activityResultLauncherForSelectImages = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode = result.getResultCode();
                Intent data = result.getData();

                if (resultCode == RESULT_OK && data != null)
                {
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        updateImage.setImageBitmap(selectedImage);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public Bitmap makeSmall(Bitmap image, int maxSize)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width / (float) height;

        if (ratio > 1)
        {
            width = maxSize;
            height = (int) (width / ratio);
        }
        else
        {
            height = maxSize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}