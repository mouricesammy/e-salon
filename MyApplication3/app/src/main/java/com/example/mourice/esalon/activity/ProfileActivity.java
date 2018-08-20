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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mourice.esalon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener
{

    private FirebaseAuth firebaseAuth;

    private ImageView mservices,appointments,promotions;
    private ImageView profile_b, about_info;
    private ImageView service_btn;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       /* navigationView.setItemTextColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{}
                },
                new int[]{
                        Color.rgb(255,128,192),
                        Color.rgb(100,200,192),
                        Color.WHITE
                }
        ));*/
        View viewHeader = navigationView.inflateHeaderView(R.layout.nav_header_profile);



        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(ProfileActivity.this, LandingPage.class));
        }


        FirebaseUser user = firebaseAuth.getCurrentUser();

        TextView nav_email = (TextView)viewHeader.findViewById(R.id.user_email);
        nav_email.setText(user.getEmail());

        TextView user_name = (TextView)viewHeader.findViewById(R.id.name);
        Intent profile = getIntent();

        String  name_user = profile.getStringExtra("name");

        user_name.setText(name_user);

        email = (TextView) findViewById(R.id.get_email);
        email.setText(user.getEmail());
        about_info  = (ImageView) findViewById(R.id.about);
        about_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,About.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        appointments = (ImageView) findViewById(R.id.my_appointments);
        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ClientAppointments.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        mservices = (ImageView) findViewById(R.id.male_services);
        mservices.setOnClickListener(this);
        profile_b = (ImageView) findViewById(R.id.profile);
        profile_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ContactInfo.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        service_btn=(ImageView) findViewById(R.id.hrs);
        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,Hours.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        promotions = (ImageView) findViewById(R.id.promotions);
        promotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,Promotions.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



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
        getMenuInflater().inflate(R.menu.profile, menu);
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

   // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(ProfileActivity.this, Services.class));

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(ProfileActivity.this, Promotions.class));


        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(ProfileActivity.this, Hours.class));


        } else if (id == R.id.nav_share) {
            startActivity(new Intent(ProfileActivity.this, ClientAppointments.class));


        } else if (id == R.id.nav_send) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(ProfileActivity.this, ClientLogin.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == mservices){
            startActivity(new Intent(ProfileActivity.this,Services.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }
}
