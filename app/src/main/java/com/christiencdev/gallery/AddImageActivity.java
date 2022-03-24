package com.christiencdev.gallery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImageActivity extends AppCompatActivity {

    private ImageView addImage;
    private EditText title, description;
    private Button btnAdd;

    //image conversion
    private Bitmap selectedImage;
    private Bitmap scaledImage;

    //for intent activity
    ActivityResultLauncher<Intent> activityResultLauncherForSelectImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Image");
        setContentView(R.layout.activity_add_image);

        addImage = findViewById(R.id.imageViewAddImage);
        title = findViewById(R.id.editTextTitleAdd);
        description = findViewById(R.id.editTextDescriptionAdd);
        btnAdd = findViewById(R.id.buttonAdd);

        //register intent activity
        registerActivityForSelectImage();

        //image select
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //first ask permission
                if (ContextCompat.checkSelfPermission(AddImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(AddImageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncherForSelectImages.launch(intent);
                }
            }
        });

        //to save
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if user doesn't select an image
                if (selectedImage == null){
                    Toast.makeText(AddImageActivity.this, "Please select an image!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String titleValue = title.getText().toString();
                    String descriptionValue = description.getText().toString();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    scaledImage = makeSmall(selectedImage, 300);
                    scaledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                    byte[] image = outputStream.toByteArray();

                    //send data to MainActivity
                    Intent intent = new Intent();
                    intent.putExtra("titleValue", titleValue);
                    intent.putExtra("descriptionValue", descriptionValue);
                    intent.putExtra("image", image);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    //if permission granted, we display selected image
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
                        addImage.setImageBitmap(selectedImage);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //what to do with permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForSelectImages.launch(intent);
        }
    }

    //reduce image size
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