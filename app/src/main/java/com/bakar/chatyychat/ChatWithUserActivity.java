package com.bakar.chatyychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatWithUserActivity extends AppCompatActivity {

    private EditText messageView;
    private Button sendBtn;
    private ListView messages;
    private DatabaseReference mdbRefrerence;
    private ChatListAdapterSingleUser mAdapter;
    private String currentUserEmail;
    private DatabaseReference usersDatabaseReference;
    private String currentUserName;
    private String targetedUserName;
    private String targetedUserEmail;
    private String childRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_user);

        Intent intent = getIntent();
        targetedUserName =  intent.getStringExtra("targetedUserName");
        targetedUserEmail =  intent.getStringExtra("targetedUserEmail");

        mdbRefrerence = FirebaseDatabase.getInstance().getReference();
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        messageView = findViewById(R.id.messageView);
        sendBtn = findViewById(R.id.sendToSingleUser);
        messages = findViewById(R.id.messages_list);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            currentUserEmail = currentUser.getEmail();
            Log.d("ChatyyChat", ""+currentUserEmail);
            getCurrentUserName();
        }

        if(currentUserEmail.compareTo(targetedUserEmail)>0)childRef = currentUserEmail+"To"+targetedUserEmail;
        else childRef = targetedUserEmail+"To"+currentUserEmail;
        childRef = childRef.replace(".","%");
        Log.d("ChatyyChat", "sendMessage:childrefupdated "+childRef);


//        Send message on enter
        messageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("ChatyyChat", "onEditorAction: clicked");
                sendMessage();
                return false;
            }
        });

//        Send message on button
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        mAdapter = new ChatListAdapterSingleUser(this, mdbRefrerence, currentUserName, childRef);
        messages.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        sendMessage();
//        mAdapter = new ChatListAdapterSingleUser(this, mdbRefrerence, currentUserName, childRef);
//        messages.setAdapter(mAdapter);
//        Log.d("ChatyyChat", "onResume: ");
    }
    @Override
    public void onStop(){
        super.onStop();
        mAdapter.cleanup();
    }

    private void sendMessage(){
        String message = messageView.getText().toString();
        String author = currentUserName;
        if (!message.equals("")) {
            InstantMessage chat = new InstantMessage(message, author);

            mdbRefrerence.child(childRef).push().setValue(chat);
            messageView.setText("");
            Log.d("ChatyyChat", "sendMessage: Success");
        }
    }

    private void getCurrentUserName(){
        Query query = usersDatabaseReference.orderByChild("email").equalTo(currentUserEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String getUnameRes = snapshot.getValue().toString();
                    currentUserName = new GetdbVal(getUnameRes).name;
                }else{Log.d("ChatyyChat", "onDataChange: not exist ");}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}