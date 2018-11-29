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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserEvents extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ListView mListView;
    private ArrayList<String> myArrayList = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private int eventCount;
    Map<String,String> mapDesc = new HashMap<>();
    Map<String,String> mapLoca = new HashMap<>();
    Map<String,String> mapStartDate = new HashMap<>();
    Map<String, String> mapEndDate = new HashMap<>();
    Map<String, String> mapStartTime = new HashMap<>();
    Map<String, String> mapEndTime = new HashMap<>();
    Map<String,String> mapCont = new HashMap<>();
    Map<String,Integer> mapCount = new HashMap<>();
    Map<String,String> mapId = new HashMap<>();
    Map<String,String> mapUserId = new HashMap<>();
    public String mEventTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);

        // TOOLBAR & NAV DRAWER
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_menuicon);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("UserEvents");
        mAuth = FirebaseAuth.getInstance(); // for sign out button function
        mListView = (ListView) findViewById(R.id.ListUserEvent);

        final UserEvents.CustomAdapter customAdapter = new UserEvents.CustomAdapter();
        mListView.setAdapter(customAdapter);

        ChildEventListener childEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String desc = dataSnapshot.child("description").getValue(String.class);
                String location = dataSnapshot.child("location").getValue(String.class);
                String startDate = dataSnapshot.child("start date").getValue(String.class);
                String endDate = dataSnapshot.child("end date").getValue(String.class);
                String startTime = dataSnapshot.child("start time").getValue(String.class);
                String endTime = dataSnapshot.child("end time").getValue(String.class);
                String contact = dataSnapshot.child("contact").getValue(String.class);
                String id = dataSnapshot.child("id").getValue(String.class);
                String userId = dataSnapshot.child("user id").getValue(String.class);

                if(dataSnapshot.child("attendees").getValue(Integer.class) == null){
                    eventCount = 0;
                }else {
                    eventCount = dataSnapshot.child("attendees").getValue(Integer.class);
                }

                mEventTitle = title;
                myArrayList.add(mEventTitle);
                mapDesc.put(title,desc);
                mapLoca.put(title,location);
                mapStartDate.put(title,startDate);
                mapEndDate.put(title,endDate);
                mapStartTime.put(title,startTime);
                mapEndTime.put(title,endTime);
                mapCount.put(title,eventCount);
                mapId.put(title,id);
                mapUserId.put(title,userId);
                mapCont.put(title,contact);
                customAdapter.notifyDataSetChanged();
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserEvents.this, EventPage.class);
                intent.putExtra("EventTitle", myArrayList.get(position));
                intent.putExtra("EventDesc", mapDesc.get(myArrayList.get(position)));
                intent.putExtra("EventLocation", mapLoca.get(myArrayList.get(position)));
                intent.putExtra("EventStartDate", mapStartDate.get(myArrayList.get(position)));
                intent.putExtra("EventEndDate", mapEndDate.get(myArrayList.get(position)));
                intent.putExtra("EventStartTime", mapStartTime.get(myArrayList.get(position)));
                intent.putExtra("EventEndTime", mapEndTime.get(myArrayList.get(position)));
                intent.putExtra("EventContact", mapCont.get(myArrayList.get(position)));
                intent.putExtra("EventId", mapId.get(myArrayList.get(position)));
                intent.putExtra("EventCount",mapCount.get(myArrayList.get(position))+"");
                intent.putExtra("Attending", false);
                intent.putExtra("isUserEvent", true);
                intent.putExtra("UserId",mapUserId.get(myArrayList.get(position)));
                startActivity(intent);
            }
        });


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

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {

            return myArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.custom_layout,null);
            ImageView icon = (ImageView) view.findViewById(R.id.imageView);
            TextView myTitle = (TextView) view.findViewById(R.id.titleView);
            TextView myDate = (TextView) view.findViewById(R.id.dateView);
            TextView myCount = (TextView) view.findViewById(R.id.CountView);
            myTitle.setText(myArrayList.get(position));
            myDate.setText(mapStartDate.get(myArrayList.get(position)));
            myCount.setText(mapCount.get(myArrayList.get(position))+"");

            return view;
        }
    }

    // NAV DRAWER BUTTON FUNCTIONALITY
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

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
