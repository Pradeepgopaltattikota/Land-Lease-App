package com.example.myproject.landleaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLogin extends AppCompatActivity {

    private EditText aUsername,aPassword;
    private String username,password;
    private Button aLoginB;
    private ProgressBar aProgressBar;

    private FirebaseAuth aAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        aAuth = FirebaseAuth.getInstance();

        aProgressBar = (ProgressBar)findViewById(R.id.a_progress_bar);

        aUsername = (EditText)findViewById(R.id.username);
        aPassword = (EditText)findViewById(R.id.password);

        aLoginB = (Button) findViewById(R.id.button);

        aLoginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aProgressBar.setVisibility(View.VISIBLE);

                //username="na@gmil.com";
                //password="12345678";
                username = aUsername.getText().toString();
                password = aPassword.getText().toString();
                if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
                    Toast.makeText(AdminLogin.this, "Please enter the following fields", Toast.LENGTH_SHORT).show();
                    aProgressBar.setVisibility(View.GONE);
                }else {
                    aAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(AdminLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Intent na = new Intent(AdminLogin.this, MainActivity.class);
                                        Toast.makeText(AdminLogin.this, "Welcome Admin !", Toast.LENGTH_SHORT).show();
                                        na.putExtra("s","1");
                                        aProgressBar.setVisibility(View.GONE);
                                        startActivity(na);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(AdminLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        aProgressBar.setVisibility(View.GONE);
                                    }

                                }
                            });
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = aAuth.getCurrentUser();
        if (!(currentUser == null)) {
            Intent na = new Intent(AdminLogin.this, MainActivity.class);
            Toast.makeText(this, "Welcome !", Toast.LENGTH_SHORT).show();
            startActivity(na);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminLogin.this,StartUp.class));
        finish();
    }
}
