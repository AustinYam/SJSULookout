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
    private DrawerLayout mDrawerLayout;

    //events test
    //private ListView eventList;
    //private ArrayList<String> events = new ArrayList<>();

//    //Home menu options
//    private Button btnAddEvents;
//    private Button btnExploreEvents;
//    private Button btnUserEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TOOLBAR & NAV DRAWER
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_menuicon);
        mDrawerLayout = findViewById(R.id.drawer_layout);

//        //Home Menu
//        btnAddEvents = (Button) findViewById(R.id.AddEvents);
//        btnExploreEvents = (Button) findViewById(R.id.ExploreEvents);
//        btnUserEvents = (Button) findViewById(R.id.UserEvents);
//
//        //events
//       // eventList = (ListView) findViewById(R.id.EventList);
//        btnSignOut = (Button) findViewById(R.id.signOut);

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,events);

        // eventList.setAdapter(arrayAdapter);

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
                    case R.id.ExploreEvents:
                        item.setChecked(true); // set item as selected to persist highlight
                        mDrawerLayout.closeDrawers(); // close drawer when item is tapped
                        // Handle explore events click
                        Intent exploreEvents = new Intent(getApplicationContext(), AllEvents.class);
                        startActivity(exploreEvents);
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

//        btnExploreEvents.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent exploreEvents = new Intent(getApplicationContext(), AllEvents.class);
//                startActivity(exploreEvents);
//            }
//        });
//
//        btnSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                toastMessage("Signing out...");
//                Intent backToLogin = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(backToLogin);
//                finish();
//            }
//        });
//
//        btnAddEvents.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentAddEvent = new Intent(getApplicationContext(), AddEvent.class);
//                startActivity(intentAddEvent);
//            }
//        });
//
//        btnUserEvents.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentUserEvent = new Intent(getApplicationContext(), UserEvents.class);
//                startActivity(intentUserEvent);
//            }
//        });

    }

    // NAV BAR BUTTON FUNCTIONALITY
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
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

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
