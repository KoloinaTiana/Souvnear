package com.example.projet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser la localisation et la requête
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // 10 seconds
                .setFastestInterval(5000); // 5 seconds

        // Vérifier la permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            initLocation();
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        TextView err = findViewById(R.id.error);
        err.setVisibility(View.INVISIBLE);

        Button nolog = findViewById(R.id.nologin_button);
        nolog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent nologin_intent = new Intent(MainActivity.this, Nologin.class);
                startActivity(nologin_intent);
            }
        });

        Button log = findViewById(R.id.login_button);
        log.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("Range")
            public void onClick(View view){
                EditText e = findViewById(R.id.email_edit_text);
                String email = String.valueOf(e.getText());
                EditText pw = findViewById(R.id.password_edit_text);
                String password = String.valueOf(pw.getText());

                if(email.equals("") | password.equals("")){
                    err.setVisibility(View.VISIBLE);
                }else {
                    if (databaseHelper.checkLogin(email, password)) {
                        Context context = getApplicationContext();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Cursor c = databaseHelper.findUser(email, password);
                        if (c.moveToFirst()) {
                            editor.putInt("id",c.getInt(c.getColumnIndex("id")));
                            editor.putString("nom",c.getString(c.getColumnIndex("nom")));
                            editor.putString("email",c.getString(c.getColumnIndex("email")));
                        }
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, Logged.class);
                        startActivity(intent);
                        err.setVisibility(View.INVISIBLE);
                        e.setText("");
                        pw.setText("");
                    } else {
                        err.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        Button sign = findViewById(R.id.signin_button);
        sign.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent sign_intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(sign_intent);
            }
        });
    }

    private void initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // Interval de mise à jour en ms (ici, 10 secondes)
        locationRequest.setFastestInterval(5000); // La mise à jour la plus rapide en ms (ici, 5 secondes)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Précision élevée
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Afficher une boîte de dialogue pour demander l'activation de la localisation
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Activer la localisation");
            builder.setMessage("La localisation est nécessaire pour utiliser cette application");
            builder.setPositiveButton("Activer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Ouvrir les paramètres de localisation
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.show();
        }
    }
}