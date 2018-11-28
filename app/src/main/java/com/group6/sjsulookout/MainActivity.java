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
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    ListView myListView;
    ArrayList<String> myArrayList = new ArrayList<>();
    Map<String, String> mapDesc = new HashMap<>();
    Map<String, String> mapLoca = new HashMap<>();
    Map<String, String> mapDate = new HashMap<>();
    Map<String, String> mapCont = new HashMap<>();
    Map<String, Integer> mapId = new HashMap<>();
    Map<String, Integer> mapAttend = new HashMap<>();
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onRestart();
        // TOOLBAR & NAV DRAWER
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_menuicon);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("events");
        mAuth = FirebaseAuth.getInstance();

        myListView = (ListView) findViewById(R.id.ListEvent);
        final MainActivity.CustomAdapter myArrayAdapter = new MainActivity.CustomAdapter();
        myListView.setAdapter(myArrayAdapter);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                    if (!user.isEmailVerified()) {
                        user.sendEmailVerification();
                    }
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


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

    //All Events
        ChildEventListener childEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String desc = dataSnapshot.child("description").getValue(String.class);
                String location = dataSnapshot.child("location information").getValue(String.class);
                String date = dataSnapshot.child("start date").getValue(String.class);
                String contact = dataSnapshot.child("email").getValue(String.class);
                int attendees = dataSnapshot.child("attendees").getValue(Integer.class);
                int id = dataSnapshot.child("id").getValue(Integer.class);
                myArrayList.add(title);
                mapDesc.put(title, desc);
                mapLoca.put(title, location);
                mapDate.put(title, date);
                mapCont.put(title, contact);
                mapAttend.put(title, attendees);
                mapId.put(title, id);
                myArrayAdapter.notifyDataSetChanged();
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



        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EventPage.class);
                intent.putExtra("EventTitle", myArrayList.get(position));
                intent.putExtra("EventDesc", mapDesc.get(myArrayList.get(position)));
                intent.putExtra("EventLocation", mapLoca.get(myArrayList.get(position)));
                intent.putExtra("EventDate", mapDate.get(myArrayList.get(position)));
                intent.putExtra("EventContact", mapCont.get(myArrayList.get(position)));
                intent.putExtra("EventCount", mapAttend.get(myArrayList.get(position))+"");
                intent.putExtra("EventId", mapId.get(myArrayList.get(position)));
                intent.putExtra("Attending",false);
                intent.putExtra("isUserEvent", false);
                startActivity(intent);
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
            View view = getLayoutInflater().inflate(R.layout.custom_layout, null);
            ImageView icon = (ImageView) view.findViewById(R.id.imageView);
            TextView myTitle = (TextView) view.findViewById(R.id.titleView);
            TextView myDate = (TextView) view.findViewById(R.id.dateView);
            TextView myAttendees = (TextView) view.findViewById(R.id.CountView);
            Log.d("TAG", myArrayList.size() + "");
            Log.d("TAG", myArrayList.get(position) + "");
            myTitle.setText(myArrayList.get(position));
            myDate.setText(mapDate.get(myArrayList.get(position)));
            myAttendees.setText(mapAttend.get(myArrayList.get(position))+"");

            return view;
        }
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


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
