package com.example.mourice.esalon.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
//import android.icu.util.Calendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.mourice.esalon.R;
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

public class Bookings extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button posta;
    private EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatabaseReference mdatabaseBook, mdatabase, mdatabaseUsers,databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private TextView title, desc, price;
    private ImageView image;
    private RelativeLayout rel;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private String item;

    private String mservice_key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        if (FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseApp.initializeApp(getApplicationContext());
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        mservice_key = getIntent().getExtras().getString("service_id");

        mdatabaseBook = FirebaseDatabase.getInstance().getReference().child("Bookings");
        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Services");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AdminAppointments");

        mdatabaseBook.keepSynced(true);
        mdatabaseUsers.keepSynced(true);
        mdatabase.keepSynced(true);


        title = (TextView) findViewById(R.id.title_b);
        desc = (TextView) findViewById(R.id.desc_b);
        price = (TextView) findViewById(R.id.price_b);
        image = (ImageView) findViewById(R.id.image_b);
        rel = (RelativeLayout) findViewById(R.id.picker);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        posta = (Button) findViewById(R.id.post_data);
        posta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookService();
            }
        });


        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> quantity = new ArrayList<String>();
        quantity.add("1");
        quantity.add("2");
        quantity.add("3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, quantity);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        mdatabase.child(mservice_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String b_title = (String) dataSnapshot.child("Title").getValue();
                String b_desc = (String) dataSnapshot.child("Desc").getValue();
                String b_Price = (String) dataSnapshot.child("Price").getValue();
                String b_image = (String) dataSnapshot.child("Image").getValue();

                title.setText(b_title);
                desc.setText(b_desc);
                price.setText(b_Price);
                Picasso.with(Bookings.this).load(b_image).into(image);

                Toast.makeText(Bookings.this, b_title, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void BookService() {

        final String time = txtTime.getText().toString();
        final String date = txtDate.getText().toString();

        int number = 0;
        try {
            number = Integer.parseInt(item);
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Could not pass" + nfe, Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < number; i++) {
        final DatabaseReference post = mdatabaseBook.child(user.getUid()).push();
            final DatabaseReference post2 = databaseReference.push();
        if (!TextUtils.isEmpty(time) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(item)) {
            mdatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    post.child("user_Id").setValue(user.getUid());
                    post.child("Client_name").setValue(dataSnapshot.child(user.getUid()).child("name").getValue());
                    post.child("Phone").setValue(dataSnapshot.child(user.getUid()).child("phone").getValue());
                    post.child("Time").setValue(time);
                    post.child("Date").setValue(date);
                    post.child("service_id").setValue(mservice_key);

                    post2.child("user_Id").setValue(user.getUid());
                    post2.child("Client_name").setValue(dataSnapshot.child(user.getUid()).child("name").getValue());
                    post2.child("Phone").setValue(dataSnapshot.child(user.getUid()).child("phone").getValue());
                    post2.child("Time").setValue(time);
                    post2.child("Date").setValue(date);
                    post2.child("service_id").setValue(mservice_key);
                    mdatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            post2.child("Title").setValue(dataSnapshot.child(mservice_key).child("Title").getValue());
                            post2.child("Price").setValue(dataSnapshot.child(mservice_key).child("Price").getValue());
                            post2.child("Image").setValue(dataSnapshot.child(mservice_key).child("Image").getValue());

                            post.child("Title").setValue(dataSnapshot.child(mservice_key).child("Title").getValue());
                            post.child("Price").setValue(dataSnapshot.child(mservice_key).child("Price").getValue());
                            post.child("Image").setValue(dataSnapshot.child(mservice_key).child("Image").getValue());

                            Toast.makeText(Bookings.this, "Appointment Booked Successfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(Bookings.this, ClientAppointments.class));
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}

    @Override
    public void onClick(View v) {
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        if (v == txtDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
           // datePickerDialog.getDatePicker().setMinDate(7-6-2017);
            datePickerDialog.show();
        }
        if (v == txtTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //}
}
