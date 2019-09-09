package com.example.myproject.landleaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class CreateLandPost extends AppCompatActivity {

    private EditText clName, clPlace, clAdd, clLink,clCost;
    private ImageView clImg;
    private Button clSubmit;
    private String name, place, add, link,img_url,cost;

    private Uri postImageUri;
    private byte[] data1;

    private DatabaseReference clDataRefer;


    private StorageReference mStorageRef;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_land_post);

        clName = (EditText) findViewById(R.id.create_name);
        clPlace = (EditText) findViewById(R.id.create_place);
        clAdd = (EditText) findViewById(R.id.create_add);
        clCost = (EditText)findViewById(R.id.create_rate);
        clLink = (EditText) findViewById(R.id.create_link);

        clSubmit = (Button)findViewById(R.id.create_button);

        clDataRefer = FirebaseDatabase.getInstance().getReference().child("posts");
        mStorageRef = FirebaseStorage.getInstance().getReference();


        clImg = (ImageView) findViewById(R.id.create_img);
        clImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(CreateLandPost.this);
            }
        });

        clSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = clName.getText().toString();
                place = clPlace.getText().toString();
                add = clAdd.getText().toString();
                cost = clCost.getText().toString();
                link = clLink.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(place) && !TextUtils.isEmpty(add) && !TextUtils.isEmpty(link)) {
                    final StorageReference mStoragePath = mStorageRef.child("feed").child(name + ".jpg");

                    final UploadTask imagePath = (UploadTask) mStoragePath.putFile(postImageUri);

                    Task<Uri> urlTask = imagePath.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            return mStoragePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri dUrl =task.getResult();

                                String uniqueId = clDataRefer.push().getKey();

                                Map userMap = new HashMap();
                                userMap.put("name", name);
                                userMap.put("place", place);
                                userMap.put("add", add);
                                userMap.put("rate",cost);
                                userMap.put("link", link);
                                userMap.put("img",dUrl.toString());

                                clDataRefer.child(uniqueId).setValue(userMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    Toast.makeText(CreateLandPost.this,"Post Created Success !",Toast.LENGTH_SHORT).show();
                                                    Intent na = new Intent(CreateLandPost.this,MainActivity.class);
                                                    startActivity(na);
                                                    finish();

                                                }else{
                                                    Toast.makeText(CreateLandPost.this, "Failure 11", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }else{
                                Toast.makeText(CreateLandPost.this, "Failure 22"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(CreateLandPost.this, "Please Enter the following fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageUri = result.getUri();
                if (postImageUri.toString().isEmpty()) {

                    Toast.makeText(this, "Please insert image !", Toast.LENGTH_SHORT).show();

                } else {

                    clImg.setImageURI(postImageUri);

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }

}

