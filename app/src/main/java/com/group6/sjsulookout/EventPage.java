package com.group6.sjsulookout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventPage extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private TextView eventTitle, eventLocation, eventDate, eventDesc, eventContact;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        TextView title = (TextView) findViewById(R.id.TitleView);
        TextView location = (TextView) findViewById(R.id.LocationView);
        TextView date = (TextView) findViewById(R.id.DateView);
        TextView desc = (TextView) findViewById(R.id.DescView);
        TextView contact = (TextView) findViewById(R.id.ContactView);




    }
}
