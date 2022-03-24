package com.christiencdev.gallery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //calls view model
    private MyImagesViewModel myImagesViewModel;

    //components
    private RecyclerView rv;
    private FloatingActionButton fab;

    private ActivityResultLauncher<Intent> activityResultLauncherForAddImage;
    private ActivityResultLauncher<Intent> activityResultLauncherForUpdateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerActivityForAddImage();
        registerActivityForUpdateImage();

        //components link to id
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        rv.setLayoutManager(new LinearLayoutManager(this));

        //initialize adapter and assign it to rv to display items
        MyImagesAdapter adapter = new MyImagesAdapter();
        rv.setAdapter(adapter);

        //cvm
        myImagesViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(MyImagesViewModel.class);

        //all images in rv
        myImagesViewModel.getAllImages().observe(MainActivity.this, new Observer<List<MyImages>>() {
            @Override
            public void onChanged(List<MyImages> myImages) {
                adapter.setImagesList(myImages);
            }
        });

        //onclick to add new image
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddImageActivity.class);
                //activity result launcher
                activityResultLauncherForAddImage.launch(intent);
            }
        });

        //delete data with left/right swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                myImagesViewModel.delete(adapter.getPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(rv);

        //for updating data
        adapter.setListener(new MyImagesAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(MyImages myImages) {
                Intent intent = new Intent(MainActivity.this, UpdateImageActivity.class);
                intent.putExtra("id", myImages.getImage_id());
                intent.putExtra("titleValue", myImages.getImage_title());
                intent.putExtra("descriptionValue", myImages.getImage_description());
                intent.putExtra("image", myImages.getImage());

                activityResultLauncherForUpdateImage.launch(intent);
            }
        });
    }

    //will add image to db
    public void registerActivityForAddImage() {
        activityResultLauncherForAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //check if data was sent
                int resultCode = result.getResultCode();
                Intent data = result.getData();

                if (resultCode == RESULT_OK && data != null)
                {
                    String titleValue = data.getStringExtra("titleValue");
                    String descriptionValue = data.getStringExtra("descriptionValue");
                    byte[] image = data.getByteArrayExtra("image");

                    //will save data
                    MyImages myImages = new MyImages(titleValue, descriptionValue, image);
                    myImagesViewModel.insert(myImages);
                }
            }
        });
    }

    //will update image in db
    public void registerActivityForUpdateImage() {
        activityResultLauncherForUpdateImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //check if data was sent
                int resultCode = result.getResultCode();
                Intent data = result.getData();

                if (resultCode == RESULT_OK && data != null)
                {
                    String titleValue = data.getStringExtra("updateTitleValue");
                    String descriptionValue = data.getStringExtra("updateDescriptionValue");
                    byte[] image = data.getByteArrayExtra("image");
                    int id = data.getIntExtra("id", -1);

                    //will save data
                    MyImages myImages = new MyImages(titleValue, descriptionValue, image);
                    myImages.setImage_id(id);
                    myImagesViewModel.update(myImages);
                }
            }
        });
    }
}