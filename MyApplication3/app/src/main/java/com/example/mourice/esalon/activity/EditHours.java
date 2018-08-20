package com.example.mourice.esalon.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mourice.esalon.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditHours extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // setContentView(R.layout.edit_hours);

        final Dialog dialog = new Dialog(EditHours.this);
        dialog.setContentView(R.layout.edit_hours);

        final EditText monday = (EditText) dialog.findViewById(R.id.mon);
        final EditText tuesday = (EditText) dialog.findViewById(R.id.tue);
        final EditText wedn = (EditText) dialog.findViewById(R.id.wed);
        final EditText thursday = (EditText) dialog.findViewById(R.id.thur);
        final EditText friday = (EditText) dialog.findViewById(R.id.fri);
        final EditText saturday = (EditText) dialog.findViewById(R.id.sat);
        Button update = (Button) dialog.findViewById(R.id.update_time);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Hours");
        // if button is clicked,check admin key
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(EditHours.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("updating business hours...");
                progressDialog.show();
                String mon = monday.getText().toString();
                String tue = tuesday.getText().toString();
                String wed = wedn.getText().toString();
                String th = thursday.getText().toString();
                String fri = friday.getText().toString();
                String sat = saturday.getText().toString();
               DatabaseReference post = databaseReference.push();
                post.child("day1").setValue(mon);
                post.child("day2").setValue(tue);
                post.child("day3").setValue(wed);
                post.child("day4").setValue(th);
                post.child("day5").setValue(fri);
                post.child("day6").setValue(sat).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditHours.this,"Hours Updated Successfully",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(EditHours.this,Salon.class));
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });



            }
        });
        dialog.show();
    }
}
