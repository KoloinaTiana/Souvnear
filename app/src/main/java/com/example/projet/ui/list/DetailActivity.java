package com.example.projet.ui.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projet.DatabaseHelper;
import com.example.projet.Photo;
import com.example.projet.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Récupération des éléments de l'interface
        ImageView imageView = findViewById(R.id.imageview);
        TextView textViewTitre = findViewById(R.id.textView_titre);
        TextView textViewDate = findViewById(R.id.textView_date);
        TextView textViewDesc = findViewById(R.id.textView_desc);
        TextView textViewLieu = findViewById(R.id.lieu);

        // Récupération des données passées depuis l'activité précédente
        Intent intent = getIntent();
        String titre = intent.getStringExtra("titre");
        String date = intent.getStringExtra("date");
        String description = intent.getStringExtra("description");
        String pos = intent.getStringExtra("position");
        byte[] imageBytes = intent.getByteArrayExtra("image");

        // Affichage des données dans l'interface
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(bitmap);
        textViewTitre.setText(titre);
        textViewDate.setText(date);
        textViewDesc.setText("Description: "+ description);
        textViewLieu.setText("Lieu: " + pos);

        Button button_delete = findViewById(R.id.delete);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
                int uid = sharedPreferences.getInt("id", 0);
                DatabaseHelper databaseHelper = new DatabaseHelper(DetailActivity.this);
                databaseHelper.deleteData(uid, titre,imageBytes, date);
                Intent i = new Intent(DetailActivity.this, ListFragment.class);
                startActivity(i);
            }
        });
    }

}