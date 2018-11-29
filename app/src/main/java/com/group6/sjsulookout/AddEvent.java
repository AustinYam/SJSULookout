package com.group6.sjsulookout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;
import android.widget.TimePicker;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.Calendar;

public class AddEvent extends AppCompatActivity {

    private Button sendData;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mDesc;
    private EditText mStartDate, mEndDate, mStartTime, mEndTime;

    private int eventCounter;
    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private static final String TAG = "AddEvent";
    private TextView mDisplayStartDate;
    private TextView mDisplayEndDate;
    private TextView mDisplayStartTime;
    private TextView mDisplayEndTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private TimePickerDialog.OnTimeSetListener mDateSetListener2;
    private TimePickerDialog.OnTimeSetListener mDateSetListener3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        mDisplayStartDate = (TextView) findViewById(R.id.StartDate);
        mDisplayEndDate = (TextView) findViewById(R.id.EndDate);
        mDisplayStartTime = (TextView) findViewById(R.id.StartTime);
        mDisplayStartTime = (TextView) findViewById(R.id.EndTime);


        mDisplayStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                int year = cal.get(java.util.Calendar.YEAR);
                int month = cal.get(java.util.Calendar.MONTH);
                int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddEvent.this, android.R.style.Theme_Holo_Light_Dialog,mDateSetListener,year, month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDisplayStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(AddEvent.this, android.R.style.Theme_Holo_Light_Dialog,mDateSetListener2,hour, minute, true);


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDisplayEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                int year = cal.get(java.util.Calendar.YEAR);
                int month = cal.get(java.util.Calendar.MONTH);
                int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddEvent.this, android.R.style.Theme_Holo_Light_Dialog,mDateSetListener1,year, month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayStartDate.setText(date);

            }
        };

        mDateSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                mDisplayStartTime.setText(time);
            }
        };

        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayEndDate.setText(date);

            }
        };


        mDisplayStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                java.util.Calendar mcurrentTime = java.util.Calendar.getInstance();
                int hour = mcurrentTime.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(java.util.Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mDisplayStartTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });





        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance(); // for sign out button function

        //USER INFORMATION
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        final String userEmail = user.getEmail();

        sendData = (Button) findViewById(R.id.BtnAddEvent);
        mTitle = (EditText) findViewById(R.id.Title);
        mLocation = (EditText) findViewById(R.id.Location);
        mDesc = (EditText) findViewById(R.id.Description);
        mStartDate = (EditText) findViewById(R.id.StartDate);
        mEndDate = (EditText) findViewById(R.id.EndDate);
        mStartTime = (EditText) findViewById(R.id.StartTime);
        mEndTime = (EditText) findViewById(R.id. EndTime);
        eventCounter = 0;

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID id = UUID.randomUUID();
                String uid = id.toString();
                String title = mTitle.getText().toString();
                String location = mLocation.getText().toString();
                String desc = mDesc.getText().toString();
                String startDate = mStartDate.getText().toString();
                String endDate = mEndDate.getText().toString();
                String startTime = mStartTime.getText().toString();
                String endTime = mEndTime.getText().toString();

                //Referencing UserEvents Database
                DatabaseReference myEvents = myRef.child("UserEvents");
                DatabaseReference myEventChild = myEvents.child(""+uid);
                DatabaseReference myEventsTitle = myEventChild.child("title");
                DatabaseReference myEventLocation = myEventChild.child("location");
                DatabaseReference myEventDesc = myEventChild.child("description");
                DatabaseReference myEventStartDate = myEventChild.child("start date");
                DatabaseReference myEventEndDate = myEventChild.child("end date");
                DatabaseReference myEventStartTime = myEventChild.child("start time");
                DatabaseReference myEventEndTime = myEventChild.child("end time");
                DatabaseReference myEventId = myEventChild.child("id");
                DatabaseReference myEventAttendees = myEventChild.child("attendees");
                DatabaseReference myUserIdRef = myEventChild.child("user id");

                //Setting event values in database
                myEventAttendees.setValue(1);
                myEventId.setValue(uid);
                myEventsTitle.setValue(title);
                myEventLocation.setValue(location);
                myEventDesc.setValue(desc);
                myEventStartDate.setValue(startDate);
                myEventEndDate.setValue(endDate);
                myEventStartTime.setValue(startTime);
                myEventEndTime.setValue(endTime);
                myUserIdRef.setValue(userId);

                //Adding own event to attending events
                DatabaseReference myUserRef = mFirebaseDatabase.getReference().child("Users");
                DatabaseReference mySpecUserRef = myUserRef.child(userId);
                DatabaseReference attendUserEventRef = mySpecUserRef.child("AttendingEvents");
                DatabaseReference userEventRef = attendUserEventRef.child("event" + uid);
                DatabaseReference idRef = userEventRef.child("id");
                DatabaseReference titleRef = userEventRef.child("title");
                DatabaseReference locationRef = userEventRef.child("location");
                DatabaseReference startDateRef = userEventRef.child("start date");
                DatabaseReference endDateRef = userEventRef.child("end date");
                DatabaseReference startTimeRef = userEventRef.child("start time");
                DatabaseReference endTimeRef = userEventRef.child("end time");
                DatabaseReference attendeeRef = userEventRef.child("attendees");
                DatabaseReference contactRef = userEventRef.child("contact");
                DatabaseReference descRef = userEventRef.child("description");

                idRef.setValue(uid);
                titleRef.setValue(title);
                locationRef.setValue(location);
                startDateRef.setValue(startDate);
                endDateRef.setValue(endDate);
                startTimeRef.setValue(startTime);
                endTimeRef.setValue(endTime);
                attendeeRef.setValue(1);
                contactRef.setValue(userEmail);
                descRef.setValue(desc);

                toastMessage("Successfully posted: "+title);

                mTitle.getText().clear();
                mLocation.getText().clear();
                mDesc.getText().clear();
                mStartDate.getText().clear();
                mEndDate.getText().clear();
                mStartTime.getText().clear();
                mEndTime.getText().clear();



            }
        });

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

