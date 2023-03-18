package com.example.projet.ui.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projet.DatabaseHelper;
import com.example.projet.Photo;
import com.example.projet.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
        Button button_share = findViewById(R.id.share);

        //Action de suppression
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
                int uid = sharedPreferences.getInt("id", 0);
                DatabaseHelper databaseHelper = new DatabaseHelper(DetailActivity.this);
                databaseHelper.deleteData(uid, titre,imageBytes, date);
                finish();
            }
        });

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenir l'image sous forme de bitmap
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                // Créer une Uri temporaire
                Uri uri = getImageUri(DetailActivity.this, bitmap, String.valueOf(textViewTitre.getText()));

                // Création de l'Intent pour partager l'image avec un texte
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Titre : " + textViewTitre.getText() + "\n"
                        + "Date : " + textViewDate.getText() + "\n"
                        + "" + textViewDesc.getText() + "\n"
                        + "" + textViewLieu.getText());

                // Autorisation de partager le fichier
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Partager via"));
            }
        });

    }

    //Créer un chemin temporaire pour l'image
    public static Uri getImageUri(Context context, Bitmap inImage, String t) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, t, null);
        return Uri.parse(path);
    }

}