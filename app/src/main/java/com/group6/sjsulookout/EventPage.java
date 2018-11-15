package com.group6.sjsulookout;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventPage extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private TextView eventTitle, eventLocation, eventDate, eventDesc, eventContact;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        //Data from last activity
        String eventTitle = getIntent().getStringExtra("EventTitle");

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("events");


        TextView title = (TextView) findViewById(R.id.TitleView);
        TextView location = (TextView) findViewById(R.id.LocationView);
        TextView date = (TextView) findViewById(R.id.DateView);
        TextView desc = (TextView) findViewById(R.id.DescView);
        TextView contact = (TextView) findViewById(R.id.ContactView);

        title.setText(eventTitle);

        myRef.orderByChild("title").equalTo(eventTitle).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
