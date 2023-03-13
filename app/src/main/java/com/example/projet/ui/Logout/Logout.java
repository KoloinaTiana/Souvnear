package com.example.projet.ui.Logout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projet.MainActivity;
import com.example.projet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class Logout extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        Context context = getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Suppression d'une chaîne de caractères
        editor.remove("id").apply();
        editor.remove("nom").apply();
        editor.remove("email").apply();
        editor.remove("connexion");
        editor.apply();


        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

        return view;
    }
}