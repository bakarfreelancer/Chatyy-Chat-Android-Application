package com.bakar.chatyychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    AutoCompleteTextView emailView, passView;
    Button loginBtn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailView = findViewById(R.id.login_email);
        passView = findViewById(R.id.login_pass);
        loginBtn = findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();

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
                attemptLogin(v);
                return false;
            }
        });
    }
    public void goToSignUpPage(View v){
        Intent intent = new Intent(this, com.bakar.chatyychat.SignupActivity.class);
        startActivity(intent);
    }

    public void attemptLogin(View v){
        String email = emailView.getText().toString();
        String pass = passView.getText().toString();

        if(email.equals("") || pass.equals("")) return;
        loginBtn.setText("Loading...");
        Toast.makeText(this, "Login in progress....", Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    showErrorDialog();
                    loginBtn.setText("Sign in");
                    Log.d("ChatyyChat", "onComplete:login " + task.getException());
                }else{
                    Intent intent = new Intent(LoginActivity.this, com.bakar.chatyychat.ChatsListActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    private void showErrorDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage("Email or password is incorrect.")
                .setPositiveButton("Ok",null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}