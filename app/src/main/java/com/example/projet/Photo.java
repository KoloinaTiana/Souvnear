package com.example.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;


public class Photo extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imageView = findViewById(R.id.image_view);

        // Récupération de la photo transmise par l'activité précédente
        Bitmap imageBitmap = (Bitmap) getIntent().getParcelableExtra("imageBitmap");

        // Affichage de la photo dans l'ImageView
        imageView.setImageBitmap(imageBitmap);
    }


}