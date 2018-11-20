package com.group6.sjsulookout;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class EventPage extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private TextView eventTitle, eventLocation, eventDate, eventDesc, eventContact;
    private Button eventButton;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        //Data from last activity
        final String eventTitle = getIntent().getStringExtra("EventTitle");
        String eventDesc = getIntent().getStringExtra("EventDesc");
        String eventLoca = getIntent().getStringExtra("EventLocation");
        String eventDate = getIntent().getStringExtra("EventDate");
        String eventContact = getIntent().getStringExtra("EventContact");
        final int eventId = getIntent().getIntExtra("EventId", 0);


        //Firebase
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id = ""+user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Users");

        TextView title = (TextView) findViewById(R.id.TitleView);
        TextView location = (TextView) findViewById(R.id.LocationView);
        TextView date = (TextView) findViewById(R.id.DateView);
        TextView desc = (TextView) findViewById(R.id.DescView);
        TextView contact = (TextView) findViewById(R.id.ContactView);
        Button addEvent = (Button) findViewById(R.id.attendEvent);

        title.setText(eventTitle);
        desc.setText(eventDesc);
        location.setText(eventLoca);
        date.setText(eventDate);
        contact.setText(eventContact);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userRef = myRef.child(user_id);
                DatabaseReference eventRef = userRef.child("AttendingEvents");
                DatabaseReference myEventChild = eventRef.child(""+UUID.randomUUID());
                DatabaseReference eventIdRef = myEventChild.child("event_id");

                eventIdRef.setValue(eventId);

                toastMessage("Attending Event: " + eventTitle);
            }
        });

    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
