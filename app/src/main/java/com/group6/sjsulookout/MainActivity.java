package com.group6.sjsulookout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private Button btnSignOut;
    private DatabaseReference myRef;

    //events test
    //private ListView eventList;
    //private ArrayList<String> events = new ArrayList<>();

    //Home menu options
    private Button btnAddEvents;
    private Button btnExploreEvents;
    private Button btnUserEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Home Menu
        btnAddEvents = (Button) findViewById(R.id.AddEvents);
        btnExploreEvents = (Button) findViewById(R.id.ExploreEvents);
        btnUserEvents = (Button) findViewById(R.id.UserEvents);

        //events
       // eventList = (ListView) findViewById(R.id.EventList);
        btnSignOut = (Button) findViewById(R.id.signOut);

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,events);

       // eventList.setAdapter(arrayAdapter);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                    if(!user.isEmailVerified()){
                        user.sendEmailVerification();
                    }
                }else{
                    Log.d(TAG, "onAuthStateChanged:signed_out");


                }
            }
        };

        btnExploreEvents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent exploreEvents = new Intent(getApplicationContext(), AllEvents.class);
                startActivity(exploreEvents);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                toastMessage("Signing out...");
                Intent backToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(backToLogin);
            }
        });

        btnAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddEvent = new Intent(getApplicationContext(), AddEvent.class);
                startActivity(intentAddEvent);
            }
        });

        btnUserEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUserEvent = new Intent(getApplicationContext(), UserEvents.class);
                startActivity(intentUserEvent);
            }
        });

    }


    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
