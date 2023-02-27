package com.example.projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.Manifest;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projet.ui.list.ListFragment;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class Photo extends AppCompatActivity {

    private ImageView imageView;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_ENABLE = 1000;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imageView = findViewById(R.id.image_view);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Récupération de la photo transmise par l'activité précédente
        Bitmap imageBitmap = (Bitmap) getIntent().getParcelableExtra("imageBitmap");

        // Affichage de la photo dans l'ImageView
        imageView.setImageBitmap(imageBitmap);
        TextView d = findViewById(R.id.date);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateTime = dateFormat.format(calendar.getTime());
        d.setText(dateTime);
        requestLocationEnabled();


        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText titre = findViewById(R.id.Titre);
                String t = String.valueOf(titre.getText());
                EditText desc = findViewById(R.id.desc);
                String des  = String.valueOf(desc.getText());

                if (t.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Photo.this);
                    builder.setTitle("Titre obligatoire");
                    builder.setMessage("Veuillez mettre un titre");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    Context context = getApplicationContext();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
                    int uid = sharedPreferences.getInt("id", 0);
                    databaseHelper.insertPhoto(uid,t, des, imageBitmap, latitude, longitude, dateTime);
                    titre.setText("");
                    desc.setText("");
                    finish();
                }
            }
        });
    }

    private void requestLocationEnabled() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // Interval de mise à jour en ms (ici, 10 secondes)
        locationRequest.setFastestInterval(5000); // La mise à jour la plus rapide en ms (ici, 5 secondes)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Précision élevée

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // Toutes les conditions requises sont satisfaites. Vous pouvez maintenant initialiser la localisation ici.
                initLocation();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Certaines conditions ne sont pas remplies, mais peuvent être corrigées en affichant un message à l'utilisateur.
                    try {
                        // Afficher le dialogue de l'assistant pour activer la localisation.
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(Photo.this, REQUEST_LOCATION_ENABLE);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignorez l'erreur.
                    }
                }
            }
        });
    }

    private void initLocation() {
        // Vérifier si la permission de localisation est accordée
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // La permission a été accordée, récupérer la position
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000) // Intervalles de mise à jour de la localisation
                    .setFastestInterval(5000); // La localisation sera mise à jour au plus vite si une autre application demande une mise à jour

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else{
            // La permission n'a pas été accordée, demander la permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // Callback pour recevoir la position de l'utilisateur
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            // Récupérer la position actuelle
            Location location = locationResult.getLastLocation();
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            // Mettre à jour les TextViews avec la position
            TextView lat = findViewById(R.id.latitude);
            lat.setText("Latitude: " + latitude);
            TextView lon = findViewById(R.id.longitude);
            lon.setText("Longitude: " + longitude);

            Geocoder geocoder = new Geocoder(Photo.this, Locale.getDefault());
            List<Address> addresses = null; // Nombre maximal d'adresses à récupérer
            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String l = addresses.get(0).getLocality();

            TextView lieu = findViewById(R.id.lieu);
            lieu.setText("Lieu: " + l);

            // Stopper la mise à jour de la position
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    };
}