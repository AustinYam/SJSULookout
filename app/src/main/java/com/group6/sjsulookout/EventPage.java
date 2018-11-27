package com.group6.sjsulookout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class EventPage extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private TextView eventTitle, eventLocation, eventDate, eventDesc, eventContact;
    private Button eventButton;
    private DatabaseReference myEventRef;
    private DatabaseReference myUserRef;
    private int newCount;
    private boolean isAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        //Data from last activity
        final String eventTitle = getIntent().getStringExtra("EventTitle");
        final String eventDesc = getIntent().getStringExtra("EventDesc");
        final String eventLoca = getIntent().getStringExtra("EventLocation");
        final String eventDate = getIntent().getStringExtra("EventDate");
        final String eventContact = getIntent().getStringExtra("EventContact");
        final String eventCount = getIntent().getStringExtra("EventCount");
        final int eventId = getIntent().getIntExtra("EventId", 0);
        final int attendees = Integer.parseInt(eventCount);
        newCount = attendees;

        //Firebase
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id = ""+user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //QUERIE
        myUserRef = mFirebaseDatabase.getReference().child("Users");
        myEventRef = mFirebaseDatabase.getReference().child("events");
        DatabaseReference mySpecUserRef = myUserRef.child(user_id);
        final DatabaseReference attendUserEventRef = mySpecUserRef.child("Attending Events");
        final DatabaseReference eventsRef = myEventRef.child("event"+eventId);

        final DatabaseReference userEventIdRef = attendUserEventRef.child("event"+eventId);
        userEventIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    isAdded = true;
                }else{
                    isAdded = false;
                    //Add to event Attendees
                    newCount +=1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                if(isAdded == false){
                    DatabaseReference newAttendeesRef = eventsRef.child("attendees");
                    newAttendeesRef.setValue(newCount);
                    //Create user event
                    DatabaseReference userEventRef = attendUserEventRef.child("event"+eventId);
                    DatabaseReference idRef = userEventRef.child("id");
                    DatabaseReference titleRef = userEventRef.child("title");
                    DatabaseReference locationRef = userEventRef.child("location");
                    DatabaseReference dateRef = userEventRef.child("start date");
                    DatabaseReference attendeeRef = userEventRef.child("attendees");
                    DatabaseReference contactRef = userEventRef.child("contact");
                    DatabaseReference descRef = userEventRef.child("description");
                    idRef.setValue(eventId);
                    titleRef.setValue(eventTitle);
                    locationRef.setValue(eventLoca);
                    dateRef.setValue(eventDate);
                    attendeeRef.setValue(attendees);
                    contactRef.setValue(eventContact);
                    descRef.setValue(eventDesc);

                    toastMessage("Successfully added Event: " + eventTitle);
                }else{
                    toastMessage("Event Already Added");
                }


            }
        });

    }

    public void refreshActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }

    @Override
    public void onBackPressed() {
        refreshActivity();
        super.onBackPressed();
    }


    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}