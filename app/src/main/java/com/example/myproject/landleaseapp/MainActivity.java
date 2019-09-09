package com.example.myproject.landleaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private View nvHeader;
    private TextView maName,maEmail;
    private FloatingActionButton fButton;
    private Toolbar toolbar;

    private FirebaseDatabase maFirebasedb;
    private DatabaseReference maDataR;
    private FirebaseAuth maFirebaseAuth;

    private FirebaseDatabase dpFirebaseDb;
    private DatabaseReference dpDatabaseRef;
    private RecyclerView dpRecyclerView;
    private FirebaseRecyclerAdapter<DataModel,ViewHolder> adapter;
    private LinearLayoutManager llm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maFirebasedb = FirebaseDatabase.getInstance();
        maFirebaseAuth = FirebaseAuth.getInstance();

        toolbar =(Toolbar)findViewById(R.id.maToolbar);

        final String uid=FirebaseAuth.getInstance().getUid().toString();

        fButton = (FloatingActionButton)findViewById(R.id.create_post);
        final ArrayList<String> adminsId=new ArrayList<>();
        adminsId.add("bl9goWqgfKdjALZkXrFhuU3oHm03");
        if(adminsId.contains(uid)){
            fButton.setImageResource(R.drawable.ic_edit);
        }else {
            fButton.setImageResource(R.drawable.ic_edit_search);
        }

        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adminsId.contains(uid)) {
                    Intent na = new Intent(MainActivity.this, CreateLandPost.class);
                    startActivity(na);
                }else {
                    //TODO:add searching feature....
                    Toast.makeText(MainActivity.this,"Search",Toast.LENGTH_LONG).show();
                }
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        actionbar.setDisplayHomeAsUpEnabled(true);

        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();


        nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch(id)
                {
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this,StartUp.class));
                        finish();
                        break;

                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this,AboutUs.class));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

        nvHeader = nv.getHeaderView(0);
        maName = (TextView)nvHeader.findViewById(R.id.nav_header_name);
        maEmail = (TextView)nvHeader.findViewById(R.id.nav_header_email);

        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");

        FirebaseAuth maFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = maFirebaseAuth.getCurrentUser();
        final String user_id = user.getUid();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String na = dataSnapshot.child(user_id).child("name").getValue().toString();
                String na1 = dataSnapshot.child(user_id).child("email").getValue().toString();
                maName.setText(na);
                maEmail.setText(na1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dpRecyclerView = (RecyclerView)findViewById(R.id.m_recycler);
        llm = new LinearLayoutManager(this);
        dpRecyclerView.setLayoutManager(llm);
        dpRecyclerView.setHasFixedSize(true);
        fetch();
    }

    public void fetch(){


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts");

        FirebaseRecyclerOptions<DataModel> options =
                new FirebaseRecyclerOptions.Builder<DataModel>()
                        .setQuery(query, new SnapshotParser<DataModel>() {
                            @NonNull
                            @Override
                            public DataModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new DataModel(
                                        snapshot.child("name").getValue().toString(),
                                        snapshot.child("place").getValue().toString(),
                                        snapshot.child("add").getValue().toString(),
                                        snapshot.child("rate").getValue().toString(),
                                        snapshot.child("link").getValue().toString(),
                                        snapshot.child("img").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<DataModel, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_recycler, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, final DataModel model) {

                holder.setAddT(model.getAdd());
                holder.setNameT(model.getName());
                holder.setRateT(model.getRate());
                holder.setImgT(model.getImg());

                holder.cardT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent na = new  Intent(MainActivity.this,FullPostView.class);
                        na.putExtra("name",model.getName());
                        na.putExtra("place",model.getPlace());
                        na.putExtra("add",model.getAdd());
                        na.putExtra("rate",model.getRate());
                        na.putExtra("link",model.getLink());
                        na.putExtra("img",model.getImg());
                        startActivity(na);
                    }
                });

            }

        };
        dpRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent na = getIntent();
        String status = na.getStringExtra("s");

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView nameT,rateT,addT;
        private ImageView imgT;
        private CardView cardT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            nameT = mView.findViewById(R.id.site_name);
            rateT = mView.findViewById(R.id.site_rate);
            addT = mView.findViewById(R.id.site_place);
            cardT = mView.findViewById(R.id.site_main);
            imgT =mView.findViewById(R.id.site_img);

        }

        public void setNameT(String name) {
            nameT.setText(name);
        }

        public void setRateT(String rate) {
            rateT.setText(rate);
        }

        public void setAddT(String add) {
            addT.setText(add);
        }

        public void setImgT(String img) {
            Picasso.get().load(img).into(imgT);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}


