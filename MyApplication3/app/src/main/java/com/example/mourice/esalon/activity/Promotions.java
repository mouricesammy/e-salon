package com.example.mourice.esalon.activity;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Promotions extends AppCompatActivity {
    private RecyclerView app_list;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        if (FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseApp.initializeApp(getApplicationContext());
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        app_list = (RecyclerView) findViewById(R.id.promotion_list);
        app_list.setHasFixedSize(true);
        app_list.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Promotions");

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
            startActivity(new Intent(Promotions.this,ProfileActivity.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ServiceGetter,Promotions.AppHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ServiceGetter, Promotions.AppHolder>(
                ServiceGetter.class,
                R.layout.promotions_row,
                Promotions.AppHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(Promotions.AppHolder appHolder, ServiceGetter appGetter, int i) {
                final String promotions_key = getRef(i).getKey();
                appHolder.setTitle(appGetter.getTitle());
                appHolder.setDesc(appGetter.getDesc());
                appHolder.setImage(getApplicationContext(),appGetter.getImage());



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
            TextView title = (TextView) mview.findViewById(R.id.p_title);
            title.setText(Title);
        }
        public  void setDesc(String Desc){
            TextView desc = (TextView)mview.findViewById(R.id.p_desc);
            desc.setText(Desc);
        }


        public void setImage(Context ctx, String Image){
            ImageView service_image= (ImageView) mview.findViewById(R.id.p_image);
            Picasso.with(ctx).load(Image).into(service_image);


        }


    }
}
