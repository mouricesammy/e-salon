package com.example.mourice.esalon.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mourice.esalon.R;

public class LandingPage extends AppCompatActivity implements View.OnClickListener {

    private ImageView salon_b,client_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        salon_b = (ImageView)findViewById(R.id.salon);
        client_b = (ImageView)findViewById(R.id.client);
        salon_b.setOnClickListener(this);
        client_b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == salon_b){
            final Dialog dialog = new Dialog(LandingPage.this);
            dialog.setContentView(R.layout.dialog_admin);

            final TextInputEditText text = (TextInputEditText) dialog.findViewById(R.id.key_word);
            Button dialogButton = (Button) dialog.findViewById(R.id.proceed);
            // if button is clicked,check admin key
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String getKey = text.getText().toString();
                    String admin = "00granted";
                    if (getKey.equals(admin)){
                        startActivity(new Intent(LandingPage.this, SalonLogin.class));
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                    else{
                        Toast.makeText(LandingPage.this, "Wrong Admin Key", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();

        }
        if ( v == client_b){
            startActivity(new Intent(LandingPage.this, ClientLogin.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }
}
