package com.bakar.chatyychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatsListActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference usersDatabaseReference;
    DatabaseReference mdbRefrerence;
    ListView userCurrentChatsList;
    String currentUserName;
    String currentUserEmail;
    private ChatsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        userCurrentChatsList = findViewById(R.id.userCurrentChatsList);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mdbRefrerence = FirebaseDatabase.getInstance().getReference();
        usersDatabaseReference = mDatabase.getReference("users");

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
        mAdapter = new ChatsListAdapter(this, mdbRefrerence, currentUserName);
//        messages.setAdapter(mAdapter);
    }

    private void getCurrentUserName(){
        Query query = usersDatabaseReference.orderByChild("email").equalTo(currentUserEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d("ChatyyChat", "key: "+snapshot.getKey()+" vlaue: "+snapshot.getValue());
                    String getUnameRes = snapshot.getValue().toString();
                    currentUserName = new GetdbVal(getUnameRes).name;
                    Log.d("ChatyyChat", "currentUserName: "+currentUserName);
                }else{Log.d("ChatyyChat", "onDataChange: not exist ");}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ChatyyChat", "onCancelled: "+error);
            }
        });
    }
    public void goToSearchUser(View v){
        Intent intent = new Intent(this, com.bakar.chatyychat.SearchUserActivity.class );
        startActivity(intent);
    }

    public void goTobetaActivity(View view){
        Intent intent = new Intent(this, com.bakar.chatyychat.MainChatActivity.class);
        startActivity(intent);
    }
}