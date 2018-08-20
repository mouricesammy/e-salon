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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientRegister extends AppCompatActivity implements View.OnClickListener {
    private EditText input_email;
    private EditText input_password;
    private Button btn_signup;
    private TextView link_login;
    private EditText pass_repeat;
    private EditText contact;
    private EditText username;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    String name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        link_login = (TextView) findViewById(R.id.link_login);
        pass_repeat = (EditText)findViewById(R.id.pass_repeat);

        btn_signup.setOnClickListener(this);
        link_login.setOnClickListener(this);

        contact = (EditText) findViewById(R.id.contact);
        username = (EditText) findViewById(R.id.username);



    }

    public void register() {

        final String name = username.getText().toString().trim();
        final String phone = contact.getText().toString().trim();
        final String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();
        String passwd = pass_repeat.getText().toString().trim();



        if (TextUtils.isEmpty(name)){
            username.setError("Input your name");
        }
        if (TextUtils.isEmpty(phone) || phone.length() < 10 || phone.length() > 13) {
            contact.setError("Enter a valid phone number");
            return;
        }
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            return;
        } else {
            input_email.setError(null);
        }

        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 10) {
            input_password.setError("between 6 and 10 alphanumeric characters");
            return;
        }
        if (password.isEmpty() || passwd.length() < 4 || passwd.length() > 10 || !(passwd.equals(password))) {
            input_password.setError("Password Do not match");
            pass_repeat.setError("Password Do not match");
            return;


        } else {
            input_password.setError(null);}


        final ProgressDialog progressDialog = new ProgressDialog(ClientRegister.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String user_id = firebaseAuth.getCurrentUser().getUid();
                    DatabaseReference current_user = databaseReference.child(user_id);
                    current_user.child("name").setValue(name);
                    current_user.child("phone").setValue(phone);
                    current_user.child("image").setValue("default");

                    final ProgressDialog progressDialog = new ProgressDialog(ClientRegister.this);
                    progressDialog.dismiss();
                    //start profile activity
                    finish();

                    Intent profile = new Intent(ClientRegister.this,ProfileActivity.class);
                    profile.putExtra("name",name);
                    startActivity(profile);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Toast.makeText(ClientRegister.this, "connection timeout, check your internet connection", Toast.LENGTH_LONG).show();
                    final ProgressDialog progressDialog = new ProgressDialog(ClientRegister.this);
                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_signup){
            register();
            //register user
        }
        if (v == link_login){
            finish();
            startActivity(new Intent(ClientRegister.this,ClientLogin.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }


}

