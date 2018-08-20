package com.example.mourice.esalon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mourice.esalon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Salon extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private ImageView services,appointments,promotions,hours;
    private ImageView profile_b;
    private ImageView service_btn;
    private ImageView about_info;
    private TextView email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View viewHeader = navigationView.inflateHeaderView(R.layout.nav_header_salon);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, ClientLogin.class));
        }


        FirebaseUser user = firebaseAuth.getCurrentUser();

      /*  TextView nav_email = (TextView)viewHeader.findViewById(R.id.user_email);
        nav_email.setText(user.getEmail());

        TextView user_name = (TextView)viewHeader.findViewById(R.id.name);
        Intent profile = getIntent();

        String  name_user = profile.getStringExtra("name");

        user_name.setText(name_user);

        email = (TextView) findViewById(R.id.get_email);
        email.setText(user.getEmail());*/



        appointments = (ImageView) findViewById(R.id.admin_appointments);
        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("Users");
                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        startActivity(new Intent(Salon.this,AdminAppointments.class));
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        services = (ImageView) findViewById(R.id.admin_services);
        services.setOnClickListener(this);
        profile_b = (ImageView) findViewById(R.id.profile1);
        profile_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Salon.this, ContactInfo.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        service_btn=(ImageView) findViewById(R.id.btn_service_admin);
        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Salon.this,About.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        promotions = (ImageView) findViewById(R.id.admin_promotions);
        promotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Salon.this,AddPromotions.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        hours = (ImageView) findViewById(R.id.hours_admin);
        hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Salon.this,EditHours.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.salon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(Salon.this, Fservices.class));

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(Salon.this, AddPromotions.class));

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(Salon.this, AdminAppointments.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(Salon.this, EditHours.class));

        } else if (id == R.id.signOut) {

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(Salon.this, LandingPage.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == services){
            startActivity(new Intent(Salon.this,Admin_Service.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }
}
