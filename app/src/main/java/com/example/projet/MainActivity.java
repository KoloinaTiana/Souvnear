package com.example.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            public void onClick(View view){
                EditText e = findViewById(R.id.email_edit_text);
                String email = String.valueOf(e.getText());
                EditText pw = findViewById(R.id.password_edit_text);
                String password = String.valueOf(pw.getText());

                if(email.equals("") | password.equals("")){
                    err.setVisibility(View.VISIBLE);
                }else {
                    if (databaseHelper.checkLogin(email, password)) {
                        Intent intent = new Intent(MainActivity.this, Logged.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
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
}