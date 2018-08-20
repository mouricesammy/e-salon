package com.example.mourice.esalon.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mourice.esalon.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminAppointments extends AppCompatActivity {
    private RecyclerView app_list;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,mdatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_appointments);

        app_list = (RecyclerView) findViewById(R.id.appointment_list_admin);
        app_list.setHasFixedSize(true);
        app_list.setLayoutManager(new LinearLayoutManager(this));
        if (FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseApp.initializeApp(getApplicationContext());
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AdminAppointments");
        //Toast.makeText(AdminAppointments.this, uid, Toast.LENGTH_LONG).show();


        databaseReference.keepSynced(true);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_back) {
            startActivity(new Intent(AdminAppointments.this,Salon.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ClientAppGetter,AdminAppointments.AppHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClientAppGetter, AdminAppointments.AppHolder>(
                ClientAppGetter.class,
                R.layout.admin_appointment_row,
                AdminAppointments.AppHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(final AdminAppointments.AppHolder appHolder,ClientAppGetter appGetter, int i) {
                final String appointment_key = getRef(i).getKey();
                appHolder.setTitle(appGetter.getTitle());
                appHolder.setPrice(appGetter.getPrice());
                appHolder.setDate(appGetter.getDate());
                appHolder.setTime(appGetter.getTime());
                appHolder.setPhone(appGetter.getPhone());
                appHolder.setImage(getApplicationContext(),appGetter.getImage());

                appHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final PopupMenu menus = new PopupMenu(AdminAppointments.this,v);
                        MenuInflater inflater = menus.getMenuInflater();
                        inflater.inflate(R.menu.remove_service,menus.getMenu());
                        menus.show();

                        menus.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()){
                                    case R.id.deleteService:
                                        removeAppointment();
                                        return true;
                                    case R.id.close1:
                                        menus.dismiss();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });menus.show();
                    }

                    public void removeAppointment(){
                        databaseReference = FirebaseDatabase.getInstance().getReference().child(appointment_key);
                        databaseReference.keepSynced(true);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("Client_name").removeValue();
                                dataSnapshot.getRef().child("Date").removeValue();
                                dataSnapshot.getRef().child("Image").removeValue();
                                dataSnapshot.getRef().child("Phone").removeValue();
                                dataSnapshot.getRef().child("Price").removeValue();
                                dataSnapshot.getRef().child("Time").removeValue();
                                dataSnapshot.getRef().child("service_id").removeValue();
                                dataSnapshot.getRef().child("user_Id").removeValue();
                                //Toast.makeText(Admin_Service.this, "Service Removed", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        };
        app_list.setAdapter(firebaseRecyclerAdapter);

    }



    public static class AppHolder extends RecyclerView.ViewHolder {
        View mview;
        DatabaseReference mdatabaseBook;
        FirebaseAuth firebaseAuth;
        Button button;
        ImageButton btn_delete;

        public AppHolder(View itemView) {
            super(itemView);
            mview = itemView;
            btn_delete = (ImageButton) itemView.findViewById(R.id.del);
        }

        public void setTitle(String Title) {
            TextView title = (TextView) mview.findViewById(R.id.title_admin1);
            title.setText(Title);
        }

        public void setPrice(String Price) {
            TextView price = (TextView) mview.findViewById(R.id.price_admin);
            price.setText(Price);
        }
       public void setDate(String Date){
            TextView date_f = (TextView)mview.findViewById(R.id.date_admin);
            date_f.setText(Date);
        }
        public void setTime(String Time){
            TextView time_f = (TextView)mview.findViewById(R.id.time_admin);
            time_f.setText(Time);
        }
        public void setPhone(String Phone) {
            TextView name = (TextView) mview.findViewById(R.id.client_name);
            name.setText(Phone);
        }


        public void setImage(Context ctx, String Image){
            ImageView service_image= (ImageView) mview.findViewById(R.id.image_service_admin);
            Picasso.with(ctx).load(Image).into(service_image);


        }


    }
}
