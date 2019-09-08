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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuthLogin extends AppCompatActivity {


        private TextView lSign;
        private EditText lUsername, lPassword;
        private String username, password;
        private Button lLoginB;

        private ProgressBar lProgressbar;
        private FirebaseAuth lAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_auth_login);

            lAuth = FirebaseAuth.getInstance();

            lSign = (TextView) findViewById(R.id.l_goto_sign);

            lUsername = (EditText) findViewById(R.id.l_username);
            lPassword = (EditText) findViewById(R.id.l_password);

            //lProgressbar = (ProgressBar)findViewById(R.id.al_progress_bar);

            lLoginB = (Button)findViewById(R.id.lf_button);

            lSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent na = new Intent(UserAuthLogin.this, UserAuthSignUp.class);
                    startActivity(na);
                }
            });


            lLoginB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lProgressbar.setVisibility(View.VISIBLE);
                    username = lUsername.getText().toString();
                    password = lPassword.getText().toString();
                    if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
                        Toast.makeText(UserAuthLogin.this, "Please enter the following felids", Toast.LENGTH_SHORT).show();
                        lProgressbar.setVisibility(View.GONE);
                    }else {

                        lAuth.signInWithEmailAndPassword(username, password)
                                .addOnCompleteListener(UserAuthLogin.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Intent na = new Intent(UserAuthLogin.this, MainActivity.class);
                                            Toast.makeText(UserAuthLogin.this, "Welcome User !", Toast.LENGTH_SHORT).show();
                                            na.putExtra("s","2");
                                            lProgressbar.setVisibility(View.GONE);
                                            startActivity(na);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(UserAuthLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            lProgressbar.setVisibility(View.GONE);
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
            FirebaseUser currentUser = lAuth.getCurrentUser();
            if (!(currentUser == null)) {
                Intent na = new Intent(UserAuthLogin.this, MainActivity.class);
                Toast.makeText(this, "Welcome !", Toast.LENGTH_SHORT).show();
                startActivity(na);
            }
        }
    }
