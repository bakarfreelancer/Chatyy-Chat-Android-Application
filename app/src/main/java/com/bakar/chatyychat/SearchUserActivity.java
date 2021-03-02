package com.bakar.chatyychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchUserActivity extends AppCompatActivity {

    private AutoCompleteTextView  searchUserTextView;
    TextView searchResultView;
    String userSearchText;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users");
        searchUserTextView = findViewById(R.id.searchUserTextView);
        searchResultView = findViewById(R.id.searchResult);
    }

    public void searchUser(View view) {
        Log.d("ChatyyChat", "searchUser: clicked");
        userSearchText = searchUserTextView.getText().toString();
        Query querySearch = mReference.orderByChild("email").equalTo(userSearchText);
        querySearch.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                android.util.Log.d("ChatyyChat", "data searched");
                if(snapshot.exists()){
                    Log.d("ChatyyChat", "onDataChange: exist ");
                    Log.d("ChatyyChat", "key: "+snapshot.getKey()+" vlaue: "+snapshot.getValue());
                    String getUnameRes = snapshot.getValue().toString();
                    String searchedName = new GetdbVal(getUnameRes).name;
                    String searchedEmail = new GetdbVal(getUnameRes).email;
                    Log.d("ChatyyChat", "searched: "+searchedName);
                    searchResultView.setText("Name: "+ searchedName + "\nEmail: " + searchedEmail);
                    searchResultView.setTextColor(R.color.primary_text);
                }else{
                    searchResultView.setText("Result not found!");
                    searchResultView.setTextColor(R.color.red);
                    Log.d("ChatyyChat", "onDataChange: not exist ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}