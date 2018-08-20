package com.example.mourice.esalon.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mourice.esalon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Hours extends AppCompatActivity {
private TextView a,b,c,d,e,f;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours);

        final ProgressDialog progressDialog = new ProgressDialog(Hours.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        a = (TextView)findViewById(R.id.aa);
        b = (TextView)findViewById(R.id.bb);
        c = (TextView)findViewById(R.id.cc);
        d = (TextView)findViewById(R.id.dd);
        e = (TextView)findViewById(R.id.ee);
        f = (TextView)findViewById(R.id.ff);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Hours");
        String key = databaseReference.push().getKey();
        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String aaa = (String) dataSnapshot.child("day1").getValue();
               /* String bbb = dataSnapshot.child("day2").getValue().toString();
                String ccc = dataSnapshot.child("day3").getValue().toString();
                String ddd = dataSnapshot.child("day4").getValue().toString();
                String eee = dataSnapshot.child("day5").getValue().toString();
                String fff = dataSnapshot.child("day6").getValue().toString();*/
                Toast.makeText(Hours.this, aaa, Toast.LENGTH_SHORT).show();
               /* a.setText(aaa);
                b.setText(bbb);
                c.setText(ccc);
                d.setText(ddd);
                e.setText(eee);
                f.setText(fff);*/
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
