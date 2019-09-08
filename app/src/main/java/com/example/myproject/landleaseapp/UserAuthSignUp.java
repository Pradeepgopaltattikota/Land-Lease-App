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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserAuthSignUp extends AppCompatActivity {

    private EditText asName, asEmail, asPassword,asPhone;
    private Button asSignB;
    private String name, email, password,phone;
    private ProgressBar asProgressbar;

    private FirebaseAuth asAuth;

    private DatabaseReference asDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth_sign_up);

        asAuth = FirebaseAuth.getInstance();
        asDatabase = FirebaseDatabase.getInstance().getReference();

        asName = (EditText) findViewById(R.id.s_username);
        asEmail = (EditText) findViewById(R.id.s_email);
        asPassword = (EditText) findViewById(R.id.s_password);
        asPhone = (EditText)findViewById(R.id.s_phone);

        asSignB = (Button) findViewById(R.id.s_sign);

        asProgressbar = (ProgressBar) findViewById(R.id.s_progress_bar);

        asSignB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(UserAuthSignUp.this, "Clicked", Toast.LENGTH_SHORT).show();
                asProgressbar.setVisibility(View.VISIBLE);

                name = asName.getText().toString();
                email = asEmail.getText().toString();
                password = asPassword.getText().toString();
                phone = asPhone.getText().toString();

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(password) && TextUtils.isEmpty(email) && password.length() > 8) {
                    Toast.makeText(UserAuthSignUp.this, "Please enter the following feilds", Toast.LENGTH_SHORT).show();
                    asProgressbar.setVisibility(View.GONE);
                }else {

                    asAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(UserAuthSignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        name = asName.getText().toString();
                                        email = asEmail.getText().toString();
                                        password = asPassword.getText().toString();
                                        phone = asPhone.getText().toString();

                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = asAuth.getCurrentUser();
                                        String user_id = user.getUid();
                                        Map userMap = new HashMap();
                                        userMap.put("name", name);
                                        userMap.put("password", password);
                                        userMap.put("email", email);
                                        userMap.put("phone", phone);
                                        userMap.put("s","2");

                                        asDatabase.child("users").child(user_id).setValue(userMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(UserAuthSignUp.this, "Login Success !", Toast.LENGTH_LONG).show();
                                                            Intent na = new Intent(UserAuthSignUp.this, MainActivity.class);
                                                            startActivity(na);
                                                        } else {
                                                            Toast.makeText(UserAuthSignUp.this, "Failure !", Toast.LENGTH_LONG).show();
                                                            asProgressbar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(UserAuthSignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        asProgressbar.setVisibility(View.GONE);
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
        FirebaseUser currentUser = asAuth.getCurrentUser();
        if(!(currentUser == null)){
            Intent na = new Intent(UserAuthSignUp.this,MainActivity.class);
            Toast.makeText(this, "Welcome !", Toast.LENGTH_SHORT).show();
            startActivity(na);
        }
    }
}
