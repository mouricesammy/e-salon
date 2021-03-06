package com.example.mourice.esalon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mourice.esalon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SalonLogin extends AppCompatActivity implements View.OnClickListener {
    private EditText input_email;
    private EditText input_password;
    private Button btn_login;
    private TextView link_signup;
    private TextView forgot_password;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salon_login);

        firebaseAuth = FirebaseAuth.getInstance();
        input_email = (EditText) findViewById(R.id.salon_email);
        input_password = (EditText) findViewById(R.id.salon_password);
        btn_login = (Button) findViewById(R.id.btn_login_salon);
        link_signup = (TextView) findViewById(R.id.link_signup_admin);

        forgot_password = (TextView) findViewById(R.id.forgot_password_admin);
        forgot_password.setOnClickListener(this);

        btn_login.setOnClickListener(this);

        link_signup.setOnClickListener(this);




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
            startActivity(new Intent(SalonLogin.this,LandingPage.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        return super.onOptionsItemSelected(item);
    }

    public void login() {


        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            return;
        } else {
            input_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            input_password.setError("between 6 and 10 alphanumeric characters");
        }
        else {
            input_password.setError(null);}


        final ProgressDialog progressDialog = new ProgressDialog(SalonLogin.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Validating...");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    //start profile activity

                    startActivity(new Intent(getApplicationContext(), Salon.class));
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Toast.makeText(SalonLogin.this, "connection timeout", Toast.LENGTH_LONG).show();


                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        // Disable going back to the SalonRegister
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_login){
            login();
        }
        if (v == link_signup){
            startActivity(new Intent(SalonLogin.this, SalonRegister.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if(v == forgot_password){
            startActivity(new Intent(SalonLogin.this, Forgot_password.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

    }
}