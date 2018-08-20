package com.example.mourice.esalon.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClientAppointments extends AppCompatActivity{
  private RecyclerView app_list;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,mdatabase,mdatabaseUsers,mdatabaseBook;
    public boolean book = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_appointments);
        if (FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseApp.initializeApp(getApplicationContext());
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        app_list = (RecyclerView) findViewById(R.id.appointment_list);
        app_list.setHasFixedSize(true);
        app_list.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();

        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Services");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Bookings").child(user.getUid());
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Services");
        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabaseBook = FirebaseDatabase.getInstance().getReference().child("Bookings");


        mdatabase.keepSynced(true);
        mdatabaseUsers.keepSynced(true);
        mdatabaseBook.keepSynced(true);

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
            startActivity(new Intent(ClientAppointments.this,ProfileActivity.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ClientAppGetter,AppHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClientAppGetter, AppHolder>(
                ClientAppGetter.class,
                R.layout.client_appointment_row,
                AppHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(AppHolder appHolder, ClientAppGetter appGetter, int i) {
               final String appointment_key = getRef(i).getKey();
                appHolder.setTitle(appGetter.getTitle());
                appHolder.setPrice(appGetter.getPrice());
                appHolder.setDate(appGetter.getDate());
                appHolder.setTime(appGetter.getTime());
                appHolder.setImage(getApplicationContext(),appGetter.getImage());

               appHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final PopupMenu popup = new PopupMenu(ClientAppointments.this,v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.menu_details,popup.getMenu());
                        popup.show();

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()){
                                    case R.id.cancelService:
                                        cancelService();
                                        return true;
                                    case R.id.closepopup:
                                        popup.dismiss();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });popup.show();
                    }

                   private void cancelService() {
                       book = true;

                       databaseReference.child(appointment_key).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {

                               if (book) {
                                   dataSnapshot.getRef().child("user_Id").removeValue();
                                   dataSnapshot.getRef().child("Client_name").removeValue();
                                   dataSnapshot.getRef().child("Phone").removeValue();
                                   dataSnapshot.getRef().child("Time").removeValue();
                                   dataSnapshot.getRef().child("Date").removeValue();
                                   dataSnapshot.getRef().child("service_id").removeValue();
                                   dataSnapshot.getRef().child("Title").removeValue();
                                   dataSnapshot.getRef().child("Price").removeValue();
                                   dataSnapshot.getRef().child("Image").removeValue();
                                   Toast.makeText(ClientAppointments.this, "Appointment Cancelled", Toast.LENGTH_LONG).show();

                                   book = false;

                                   /* Query appointmentquerry = mdatabaseBook.child(appointment_key).orderByChild("Title").equalTo("braids");
                                    appointmentquerry.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appSnapshot:dataSnapshot.getChildren()){
                                                appSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });*/

                               }
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

        public AppHolder(View itemView) {
            super(itemView);
            mview = itemView;
            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            mdatabaseBook = FirebaseDatabase.getInstance().getReference().child("Bookings").child(firebaseUser.getUid());
            mdatabaseBook.keepSynced(true);
            button = (Button)mview.findViewById(R.id.cancel_book);
        }

        public void setTitle(String Title) {
            TextView title = (TextView) mview.findViewById(R.id.c_title);
            title.setText(Title);
        }

        public void setPrice(String Price) {
            TextView price = (TextView) mview.findViewById(R.id.c_price);
            price.setText(Price);
        }
        public void setDate(String Date){
            TextView date_f = (TextView)mview.findViewById(R.id.date);
            date_f.setText(Date);
        }
        public void setTime(String Time){
            TextView time_f = (TextView)mview.findViewById(R.id.time_);
            time_f.setText(Time);
        }


        public void setImage(Context ctx, String Image){
            ImageView service_image= (ImageView) mview.findViewById(R.id.c_image);
            Picasso.with(ctx).load(Image).into(service_image);


        }


    }

}
