package com.example.projet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        View err = findViewById(R.id.error);
        err.setVisibility(View.INVISIBLE);

        Button sign = findViewById(R.id.signin_button);
        sign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText n = findViewById(R.id.name_edit_text);
                String nom = String.valueOf(n.getText());
                EditText e = findViewById(R.id.email_edit_text);
                String email = String.valueOf(e.getText());
                EditText p = findViewById(R.id.phone_edit_text);
                String phone = String.valueOf(p.getText());
                EditText pw = findViewById(R.id.password_edit_text);
                String password = String.valueOf(pw.getText());

                if (nom.equals("") | email.equals("") | phone.equals("") | password.equals("")) {
                    err.setVisibility(View.VISIBLE);
                } else {
                    err.setVisibility(View.INVISIBLE);
                    databaseHelper.addUser(nom, email, phone, password);
                    n.setText("");
                    e.setText("");
                    p.setText("");
                    pw.setText("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                    builder.setTitle("Succès");
                    builder.setMessage("Votre compte a bien été créé ! Veuillez vous connecter");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        });
    }
}