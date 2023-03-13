package com.example.projet.ui.profil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.projet.DatabaseHelper;
import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.SignIn;

public class ModifProfilFragment extends Fragment {

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_modif_profil, container, false);

        View err = view.findViewById(R.id.error);
        err.setVisibility(View.INVISIBLE);

        EditText nEdit = view.findViewById(R.id.name_edit_text);
        EditText eEdit = view.findViewById(R.id.email_edit_text);
        EditText tEdit = view.findViewById(R.id.phone_edit_text);
        EditText pEdit = view.findViewById(R.id.password_edit_text);

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

        Button s = view.findViewById(R.id.save_button);
        Button a = view.findViewById(R.id.annuler_button);

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = String.valueOf(nEdit.getText());
                String email = String.valueOf(eEdit.getText());
                String phone = String.valueOf(tEdit.getText());
                String password = String.valueOf(pEdit.getText());

                if (nom.equals("") | email.equals("") | phone.equals("") | password.equals("")) {
                    err.setVisibility(View.VISIBLE);
                } else {
                    err.setVisibility(View.INVISIBLE);
                    databaseHelper.uptdateUser(uid,nom, email, phone, password);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Succès");
                    builder.setMessage("Votre compte a bien été mise à jour");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment_content_logged, new ProfilFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                    builder.show();
                }
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_logged, new ProfilFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }
}