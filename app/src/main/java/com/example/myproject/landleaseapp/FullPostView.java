package com.example.myproject.landleaseapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class FullPostView extends AppCompatActivity {

    private TextView fName, fPlace, fAdd, fRate, fLink;
    private ImageView fImg;
    private Button fButton;
    private String name,place,add,link,img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post_view);

        fName = (TextView) findViewById(R.id.full_name);
        fPlace = (TextView) findViewById(R.id.full_place);
        fAdd = (TextView) findViewById(R.id.full_add);
        fLink = (TextView) findViewById(R.id.full_link);
        fImg = (ImageView) findViewById(R.id.full_img);
        fButton = (Button) findViewById(R.id.full_button);

        final Intent intent = getIntent();

        name = intent.getStringExtra("name");
        place = intent.getStringExtra("place");
        add = intent.getStringExtra("add");
        link = intent.getStringExtra("link");
        img = intent.getStringExtra("img");

        if(name.isEmpty() && place.isEmpty() && add.isEmpty() && link.isEmpty() && img.isEmpty()){
            Toast.makeText(this, "Error in retrieving data ! pls try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
        }


        fName.setText(name);
        fAdd.setText("Address : "+add);
        fPlace.setText("Place : "+place);
        Picasso.get().load(img).into(fImg);
        fLink.setText("Link : "+link);

        fButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + intent.getStringExtra("number")));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FullPostView.this,MainActivity.class));
    }
}
