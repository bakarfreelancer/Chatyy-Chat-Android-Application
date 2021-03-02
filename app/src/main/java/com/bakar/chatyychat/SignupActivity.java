package com.bakar.chatyychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    AutoCompleteTextView unameView,emailView,passView,confirmPassView;
    Button registerView;
    private String uname, email, pass;

//    Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        unameView = findViewById(R.id.signup_uname);
        emailView = findViewById(R.id.signup_email);
        passView = findViewById(R.id.signup_pass);
        confirmPassView = findViewById(R.id.signup_pass_confirm);
        registerView = findViewById(R.id.register_btn);

        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attemptRegister()){
                    createFirebaseUser();
                }
            }
        });

        unameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                emailView.requestFocus();
                return false;
            }
        });

        emailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                passView.requestFocus();
                return false;
            }
        });

        passView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                confirmPassView.requestFocus();
                return false;
            }
        });

        confirmPassView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                attemptRegister();
                return false;
            }
        });

//        Create instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

    }
    private boolean attemptRegister(){
        registerView.setText("Loading...");
        uname = unameView.getText().toString();
        email = emailView.getText().toString();
        pass = passView.getText().toString();
        if(email.isEmpty() || email.equals("")) {
            emailView.setError(getString(R.string.field_required));
            return false;
        }else if(!email.contains("@")) {
            emailView.setError(getString(R.string.email_required));
            return false;
        }else if(pass.isEmpty() || pass.equals("")){
            passView.setError(getString(R.string.field_required));
            return false;
        }else if(!pass.equals(confirmPassView.getText().toString())){
            passView.setError(getString(R.string.pass_not_match));
            return false;
        }else if(pass.length() < 6){
            passView.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

//    Create firebase user
    private void createFirebaseUser(){
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                registerView.setText("Register");
                if(!task.isSuccessful()){
                    showErrorDialog();
                }else{
                    addUserToDataBase();
                }
            }
        });
    }

    private void showErrorDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Registeration Failed")
                .setMessage("Email already exist or internet connection problem.")
                .setPositiveButton("Ok",null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    DialogInterface.OnClickListener goTosignIn = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent(SignupActivity.this, com.bakar.chatyychat.LoginActivity.class);
            finish();
            startActivity(intent);
        }
    };

    private void showSuccessDialog(){
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Welcome "+uname)
                .setMessage("Registration successful.")
                .setPositiveButton("Ok", goTosignIn)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
    private void addUserToDataBase(){
        InstantUser user = new InstantUser(uname, email);
        mDatabaseReference.child("users").push().setValue(user);
        showSuccessDialog();
    }

}