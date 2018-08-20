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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class Services extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mdatabase;
    private DatabaseReference mdatabaseUsers;
    private DatabaseReference mdatabaseBook;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);

        initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.service_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
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
    protected void onStart() {
        super.onStart();
        final ProgressDialog progressDialog = new ProgressDialog(Services.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Available Services");
        progressDialog.show();
        FirebaseRecyclerAdapter<ServiceGetter, ServiceHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ServiceGetter, ServiceHolder>(

                ServiceGetter.class,
                R.layout.service_row,
                ServiceHolder.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(ServiceHolder serviceHolder, ServiceGetter serviceGetter, int i) {
                final String service_key = getRef(i).getKey();
                serviceHolder.setTitle(serviceGetter.getTitle());
                serviceHolder.setDesc(serviceGetter.getDesc());
                serviceHolder.setPrice(serviceGetter.getPrice());
                serviceHolder.setImage(getApplicationContext(),serviceGetter.getImage());
                serviceHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookingsIntent = new Intent(Services.this,Bookings.class);
                        bookingsIntent.putExtra("service_id",service_key);
                        startActivity(bookingsIntent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                    }
                });


            }
        };
        progressDialog.dismiss();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        try {
            Glide.with(this).load(R.drawable.blue_skincare_skin_person_caucasian).into((ImageView) findViewById(R.id.backdrop2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class ServiceHolder extends RecyclerView.ViewHolder {
        View mview;

 public ServiceHolder(View itemView) {
            super(itemView);
            mview = itemView;


        }


        public void setTitle(String Title) {
            TextView title = (TextView) mview.findViewById(R.id.title);
            title.setText(Title);
        }

        public void setDesc(String Desc) {
            TextView desc = (TextView) mview.findViewById(R.id.desc);
            desc.setText(Desc);
        }

        public void setPrice(String Price) {
            TextView price = (TextView) mview.findViewById(R.id.price);
            price.setText(Price);
        }
        public void setImage(Context ctx, String Image){
            ImageView service_image= (ImageView) mview.findViewById(R.id.image);
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
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar1);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar2);
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
