package com.christiencdev.gallery;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//adapter allows for code reusability when working with the UI, ie. we can use methods for add/update activities without having to code it apart
public class MyImagesAdapter extends RecyclerView.Adapter<MyImagesAdapter.MyImagesHolder>{
    List<MyImages> imagesList = new ArrayList<>();

    //listener for image clicker
    private OnImageClickListener listener;
    public void setListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    //setter method
    public void setImagesList(List<MyImages> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    //interface to activate click feature for updating
    public  interface OnImageClickListener{
        void onImageClick(MyImages myImages);
    }

    //gets position of image for deletion
    public MyImages getPosition(int position){
        return imagesList.get(position);
    }

    //return the view holder object (the cards)
    @NonNull
    @Override
    public MyImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);
        return new MyImagesHolder(view);
    }

    //returns the data from db to ui
    @Override
    public void onBindViewHolder(@NonNull MyImagesHolder holder, int position) {
        MyImages myImages = imagesList.get(position);
        holder.textViewTitle.setText(myImages.getImage_title());
        holder.textViewDescription.setText(myImages.getImage_description());
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(myImages.getImage(), 0, myImages.getImage().length));
    }

    //returns amount of items in array (all images)
    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    //defines components of image_card
    public class MyImagesHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewTitle, textViewDescription;

        public MyImagesHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    //if image not clicked
                    if (listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onImageClick(imagesList.get(position));
                    }
                }
            });
        }

    }
}
