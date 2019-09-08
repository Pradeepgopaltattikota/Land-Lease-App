package com.example.myproject.landleaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartUp extends AppCompatActivity {

    private Button aButton,uButton;
    private FirebaseAuth asAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        asAuth = FirebaseAuth.getInstance();

        aButton = (Button)findViewById(R.id.admin_button);
        // On Clicking this button it goes to the Admin Page
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent na = new Intent(StartUp.this,AdminLogin.class);
                startActivity(na);
                finish();
            }
        });

        uButton = (Button)findViewById(R.id.user_button);
        //On Clicking this button it goes to the User Page
        uButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = asAuth.getCurrentUser();

        if(!(currentUser == null)){
            Intent na = new Intent(StartUp.this,MainActivity.class);
            Toast.makeText(this, "Welcome !", Toast.LENGTH_SHORT).show();
            startActivity(na);
            finish();
        }
    }

}
