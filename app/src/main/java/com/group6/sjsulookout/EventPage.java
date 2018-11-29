package com.group6.sjsulookout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

import org.w3c.dom.Text;

import java.util.UUID;

public class EventPage extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
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
        final String eventStartDate = getIntent().getStringExtra("EventStartDate");
        final String eventEndDate = getIntent().getStringExtra("EventEndDate");
        final String eventStartTime = getIntent().getStringExtra("EventStartTime");
        final String eventEndTime = getIntent().getStringExtra("EventEndTime");
        final String eventContact = getIntent().getStringExtra("EventContact");
        final String eventCount = getIntent().getStringExtra("EventCount");
        boolean attendingEvent = getIntent().getExtras().getBoolean("Attending");
        final boolean isUserEvent = getIntent().getExtras().getBoolean("isUserEvent");

        final String eventId = getIntent().getStringExtra("EventId");
        final String userUid = getIntent().getStringExtra("UserId");
        Log.d("TAG", eventId+"");
        final int attendees = Integer.parseInt(eventCount);
        newCount = attendees;

        //Firebase
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //QUERIES
        myUserRef = mFirebaseDatabase.getReference().child("Users");
        myEventRef = mFirebaseDatabase.getReference().child("events");
        DatabaseReference mySpecUserRef = myUserRef.child(user_id);
        final DatabaseReference userEventsRef = mFirebaseDatabase.getReference().child("UserEvents");


        final DatabaseReference attendUserEventRef = mySpecUserRef.child("AttendingEvents");
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

        //WHAT YOU SEE ON EVENT PAGE
        TextView title = (TextView) findViewById(R.id.TitleView);
        TextView location = (TextView) findViewById(R.id.LocationView);
        TextView startDate = (TextView) findViewById(R.id.DateStart);
        TextView endDate = (TextView) findViewById(R.id.DateEnd);
        TextView startTime = (TextView) findViewById(R.id.TimeStart);
        TextView endTime = (TextView) findViewById(R.id.TimeEnd);
        TextView desc = (TextView) findViewById(R.id.DescView);
        TextView contact = (TextView) findViewById(R.id.ContactView);
        Button addEvent = (Button) findViewById(R.id.attendEvent);

        title.setText(eventTitle);
        desc.setText(eventDesc);
        location.setText(eventLoca);
        startDate.setText(eventStartDate);
        endDate.setText(eventEndDate);
        startTime.setText(eventStartTime);
        endTime.setText(eventEndTime);
        contact.setText(eventContact);

        //GET DATA FOR USER ID
        if(user_id.equals(userUid)){
            addEvent.setText("Remove Event");
            addEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference myUserEventRef = userEventsRef.child(eventId);
                    myUserEventRef.setValue(null);
                    toastMessage("Successfully removed Event");
                    Intent intent = new Intent(EventPage.this,UserEvents.class);
                    startActivity(intent);
                }
            });
        }
        else if(attendingEvent == false) {
            addEvent.setText("Attend Event");
            addEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAdded == false) {

                        if(isUserEvent){

                            DatabaseReference userEventIdRef = userEventsRef.child(eventId);
                            DatabaseReference userEventAttendees = userEventIdRef.child("attendees");
                            userEventAttendees.setValue(newCount);



                        }else {
                            DatabaseReference newAttendeesRef = eventsRef.child("attendees");
                            newAttendeesRef.setValue(newCount);
                        }

                        //Create user event
                        DatabaseReference userEventRef = attendUserEventRef.child("event" + eventId);
                        DatabaseReference idRef = userEventRef.child("id");
                        DatabaseReference titleRef = userEventRef.child("title");
                        DatabaseReference locationRef = userEventRef.child("location");
                        DatabaseReference startDateRef = userEventRef.child("start date");
                        DatabaseReference endDateRef = userEventRef.child("end date");
                        DatabaseReference startTimeRef = userEventRef.child("start time");
                        DatabaseReference endTimeRef = userEventRef.child("start date");
                        DatabaseReference attendeeRef = userEventRef.child("attendees");
                        DatabaseReference contactRef = userEventRef.child("contact");
                        DatabaseReference descRef = userEventRef.child("description");

                        idRef.setValue(eventId);
                        titleRef.setValue(eventTitle);
                        locationRef.setValue(eventLoca);
                        startDateRef.setValue(eventStartDate);
                        endDateRef.setValue(eventEndDate);
                        startTimeRef.setValue(eventStartTime);
                        endTimeRef.setValue(eventEndTime);
                        attendeeRef.setValue(newCount);
                        contactRef.setValue(eventContact);
                        descRef.setValue(eventDesc);

                        toastMessage("Successfully added Event: " + eventTitle);
                    } else {
                        toastMessage("Event Already Added");
                    }


                }
            });
        }else if(attendingEvent == true){
            addEvent.setText("Leave Event");
            addEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newCount -= 1;
                    if(isUserEvent){
                        DatabaseReference userEventIdRef = userEventsRef.child(eventId);
                        DatabaseReference userEventAttendees = userEventIdRef.child("attendees");

                        userEventAttendees.setValue(newCount);

                    }else{
                        DatabaseReference newAttendeesRef = eventsRef.child("attendees");
                        newAttendeesRef.setValue(newCount);
                    }

                    //Create user event
                    DatabaseReference userEventRef = attendUserEventRef.child("event" + eventId);
                    userEventRef.setValue(null);

                    Intent intent = new Intent(EventPage.this, AttendingEvents.class);
                    toastMessage("Successfully removed Event: " + eventTitle);
                    startActivity(intent);

                }
            });
        }
        // TOOLBAR & NAV DRAWER
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_menuicon);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // NAV DRAWER ITEMS - CLICK LISTENER
        NavigationView navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.attendingEvents:
                        item.setChecked(true); // set item as selected to persist highlight
                        mDrawerLayout.closeDrawers(); // close drawer when item is tapped
                        // Handle explore events click
                        Intent attendEvents = new Intent(getApplicationContext(), AttendingEvents.class);
                        startActivity(attendEvents);
                        return true;
                    case R.id.MyHome:
                        item.setChecked(true); // set item as selected to persist highlight
                        mDrawerLayout.closeDrawers(); // close drawer when item is tapped
                        // Handle explore events click
                        Intent myHome = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myHome);
                        return true;
                    case R.id.UserEvents:
                        item.setChecked(true); // set item as selected to persist highlight
                        mDrawerLayout.closeDrawers(); // close drawer when item is tapped
                        // Handle user events click
                        Intent intentUserEvent = new Intent(getApplicationContext(), UserEvents.class);
                        startActivity(intentUserEvent);
                        return true;
                    case R.id.AddEvents:
                        item.setChecked(true); // set item as selected to persist highlight
                        mDrawerLayout.closeDrawers(); // close drawer when item is tapped
                        // Handle add event click
                        Intent intentAddEvent = new Intent(getApplicationContext(), AddEvent.class);
                        startActivity(intentAddEvent);
                        return true;
                    case R.id.signOut:
                        item.setChecked(true); // set item as selected to persist highlight
                        mDrawerLayout.closeDrawers(); // close drawer when item is tapped
                        // Handle logout click
                        mAuth.signOut();
                        toastMessage("Signing out...");
                        Intent backToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(backToLogin);
                        finish();
                        return true;
                    default:
                        return false;
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

    // NAV BAR BUTTON FUNCTIONALITY
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                TextView character_speaking = findViewById(R.id.NameView);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getEmail();
                character_speaking.setText(uid);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}