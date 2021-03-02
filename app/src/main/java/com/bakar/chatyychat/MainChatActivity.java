package com.bakar.chatyychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainChatActivity extends AppCompatActivity {

    private EditText messageView;
    private Button sendBtn;
    private ListView messages;
    private DatabaseReference mdbRefrerence;
    private ChatListAdapter mAdapter;
    private String currentUserEmail;
    private DatabaseReference usersDatabaseReference;
    private String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        mdbRefrerence = FirebaseDatabase.getInstance().getReference();
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        messageView = findViewById(R.id.messageView);
        sendBtn = findViewById(R.id.send);
        messages = findViewById(R.id.messages_list);

//        Send message on enter
        messageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            currentUserEmail = currentUser.getEmail();
            Log.d("ChatyyChat", ""+currentUserEmail);
            getCurrentUserName();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mAdapter = new ChatListAdapter(this, mdbRefrerence, "Anonymous");
        messages.setAdapter(mAdapter);
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
            mdbRefrerence.child("chatyy-chat-default-rtdb").push().setValue(chat);
            messageView.setText("");
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