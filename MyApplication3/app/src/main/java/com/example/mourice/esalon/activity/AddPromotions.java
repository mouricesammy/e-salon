package com.example.mourice.esalon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mourice.esalon.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddPromotions extends AppCompatActivity implements View.OnClickListener {

    private EditText title,desc;
    private ImageButton add;
    private Button post;
    private Uri img_uri=null;
    private static final int GALLERY_REQUEST=1;
    private StorageReference mstorage;
    private DatabaseReference databaseReference;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_promotions);

        post=(Button)findViewById(R.id.submitProduct);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitService();
            }
        });
        dialog = new ProgressDialog(this);

        mstorage = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Promotions");

        title = (EditText)findViewById(R.id.product_title);
        desc = (EditText)findViewById(R.id.product_desc);
        add = (ImageButton)findViewById(R.id.add_imageP);
        add.setOnClickListener(this);
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
        dialog.setMessage("Posting Promotion...");
        dialog.show();
        final String desc_val = desc.getText().toString().trim();
        final String title_val = title.getText().toString().trim();


        if (!TextUtils.isEmpty(desc_val)&& !TextUtils.isEmpty(title_val)&& img_uri !=null){
            StorageReference filepath = mstorage.child("Product_Images").child(img_uri.getLastPathSegment());
            filepath.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newService = databaseReference.push();
                    newService.child("Title").setValue(title_val);
                    newService.child("Desc").setValue(desc_val);
                    newService.child("Image").setValue(downloadUrl.toString());
                    dialog.dismiss();
                    startActivity(new Intent(AddPromotions.this,ProfileActivity.class));
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
