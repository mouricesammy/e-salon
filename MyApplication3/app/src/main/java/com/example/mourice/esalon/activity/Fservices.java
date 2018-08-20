package com.example.mourice.esalon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.net.Uri;

import com.example.mourice.esalon.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Fservices extends AppCompatActivity implements View.OnClickListener{

    private EditText desc_of_service;
    private EditText title_of_service;
    private EditText price;
    private ImageButton add;
    private Button btn_submit;
    private Uri img_uri=null;
    private static final int GALLERY_REQUEST=1;
    private StorageReference mstorage;
    private DatabaseReference databaseReference;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fservices);

        btn_submit=(Button)findViewById(R.id.submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitService();
            }
        });
        dialog = new ProgressDialog(this);

        mstorage = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Services");

        desc_of_service = (EditText)findViewById(R.id.service_desc);
        title_of_service = (EditText)findViewById(R.id.service_title);
        add = (ImageButton)findViewById(R.id.add_image);
        add.setOnClickListener(this);
        price= (EditText)findViewById(R.id.price_field);

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
            startActivity(new Intent(Fservices.this,Salon.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        if (v == add){
            Intent galleryIntent =new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,GALLERY_REQUEST);
        }
    }

    private void submitService() {
        dialog.setMessage("Adding service...");
        dialog.show();
        final String desc_val = desc_of_service.getText().toString().trim();
        final String title_val = title_of_service.getText().toString().trim();
        final String price_val = price.getText().toString().trim();


        if (!TextUtils.isEmpty(desc_val)&& !TextUtils.isEmpty(title_val)&& !TextUtils.isEmpty(price_val)&& img_uri !=null){
            StorageReference filepath = mstorage.child("Service_Images").child(img_uri.getLastPathSegment());
            filepath.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newService = databaseReference.push();
                    newService.child("Title").setValue(title_val);
                    newService.child("Desc").setValue(desc_val);
                    newService.child("Price").setValue(price_val);
                    newService.child("Image").setValue(downloadUrl.toString());
                    //newService.child("User_id").setValue(firebaseAuth.getCurrentUser().getUid());
                    dialog.dismiss();
                    startActivity(new Intent(Fservices.this,Salon.class));
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode ==RESULT_OK){

            Uri resultUri = data.getData();
            CropImage.activity(resultUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(2,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                img_uri = result.getUri();
                add.setImageURI(img_uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
