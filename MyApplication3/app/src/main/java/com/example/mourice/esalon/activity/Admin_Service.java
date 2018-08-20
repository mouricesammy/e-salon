package com.example.mourice.esalon.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mourice.esalon.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Admin_Service extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mdatabase;
    private DatabaseReference mdatabaseUsers;
    private DatabaseReference mdatabaseBook;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin__service);

        Toolbar toolbar3 = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);

        initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.admin_appointment_list);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.addItemDecoration(new Admin_Service.GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (FirebaseApp.getApps(getApplicationContext()).isEmpty()){
            FirebaseApp.initializeApp(getApplicationContext());
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Services");
        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabaseBook = FirebaseDatabase.getInstance().getReference().child("Bookings");

        mdatabase.keepSynced(true);
        mdatabaseUsers.keepSynced(true);
        mdatabaseBook.keepSynced(true);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivity(new Intent(Admin_Service.this,Fservices.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ServiceGetter, Admin_Service.ServiceHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ServiceGetter, Admin_Service.ServiceHolder>(

                ServiceGetter.class,
                R.layout.admin_service_row,
                Admin_Service.ServiceHolder.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(Admin_Service.ServiceHolder serviceHolder, ServiceGetter serviceGetter, int i) {
                final String service_key = getRef(i).getKey();
                serviceHolder.setTitle(serviceGetter.getTitle());
                serviceHolder.setDesc(serviceGetter.getDesc());
                serviceHolder.setImage(getApplicationContext(),serviceGetter.getImage());

                serviceHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final PopupMenu menus = new PopupMenu(Admin_Service.this,v);
                        MenuInflater inflater = menus.getMenuInflater();
                        inflater.inflate(R.menu.remove_service,menus.getMenu());
                        menus.show();

                        menus.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()){
                                    case R.id.deleteService:
                                        removeService();
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

                    public void removeService(){
                        mdatabase = FirebaseDatabase.getInstance().getReference().child("Services").child(service_key);
                        mdatabase.keepSynced(true);
                        mdatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("Title").removeValue();
                                dataSnapshot.getRef().child("Price").removeValue();
                                dataSnapshot.getRef().child("Image").removeValue();
                                dataSnapshot.getRef().child("Desc").removeValue();
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
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        try {
            Glide.with(this).load(R.drawable.blue_skincare_skin_person_caucasian).into((ImageView) findViewById(R.id.backdrop3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class ServiceHolder extends RecyclerView.ViewHolder {
        View mview;
        ImageButton delete;


        public ServiceHolder(final View itemView) {
            super(itemView);
            mview = itemView;

            delete = (ImageButton) mview.findViewById(R.id.service_remove);

        }


        public void setTitle(String Title) {
            TextView title = (TextView) mview.findViewById(R.id.title_admin);
            title.setText(Title);
        }

        public void setDesc(String Desc) {
            TextView desc = (TextView) mview.findViewById(R.id.desc_admin);
            desc.setText(Desc);
        }

        public void setImage(Context ctx, String Image){
            ImageView service_image= (ImageView) mview.findViewById(R.id.image_admin);
            Picasso.with(ctx).load(Image).into(service_image);


        }

    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;}

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar3);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar3);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Services Available");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
