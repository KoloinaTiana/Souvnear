package com.example.projet.ui.profil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.projet.DatabaseHelper;
import com.example.projet.R;

public class ProfilFragment extends Fragment {

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        EditText nEdit = view.findViewById(R.id.name_edit_text);
        nEdit.setEnabled(false);
        EditText eEdit = view.findViewById(R.id.email_edit_text);
        eEdit.setEnabled(false);
        EditText tEdit = view.findViewById(R.id.phone_edit_text);
        tEdit.setEnabled(false);
        EditText pEdit = view.findViewById(R.id.password_edit_text);
        pEdit.setEnabled(false);

        Button b = view.findViewById(R.id.modif_button);

        Context context = getContext();
        assert context != null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        int uid = sharedPreferences.getInt("id", 0);
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor c = databaseHelper.findUserById(uid);
        if (c.moveToFirst()) {
            nEdit.setText(c.getString(c.getColumnIndex("nom")));
            eEdit.setText(c.getString(c.getColumnIndex("email")));
            tEdit.setText(c.getString(c.getColumnIndex("phone")));
            pEdit.setText(c.getString(c.getColumnIndex("password")));
        }


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_logged, new ModifProfilFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}