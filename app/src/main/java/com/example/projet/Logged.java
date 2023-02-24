package com.example.projet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projet.databinding.ActivityLoggedBinding;

public class Logged extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLoggedBinding binding;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoggedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarLogged.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_list, R.id.nav_help)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_logged);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        NavigationView nav = findViewById(R.id.nav_view);
        View headerView = nav.getHeaderView(0);
        TextView name= headerView.findViewById(R.id.username);
        TextView email= headerView.findViewById(R.id.useremail);
        Intent intent=getIntent();
        String e, p;
        Bundle extras = intent.getExtras();

        if(extras == null) {
            e= null;
            p = null;
        } else {
            e= intent.getStringExtra("email");
            p= intent.getStringExtra("password");
            Cursor c = databaseHelper.findUser(e, p);
            if (c.moveToFirst()) {
                name.setText(c.getString(c.getColumnIndex("nom")));
            }
            email.setText(e);
            c.close();
            databaseHelper.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logged, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_logged);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}