package com.group6.sjsulookout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllEvents extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    ListView myListView;
    ArrayList<String> myArrayList = new ArrayList<>();
    Map<String,String> mapDesc = new HashMap<>();
    Map<String,String> mapLoca = new HashMap<>();
    Map<String,String> mapDate = new HashMap<>();
    Map<String,String> mapCont = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("events");
        myListView = (ListView) findViewById(R.id.ListEvent);
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myArrayList);
        myListView.setAdapter(myArrayAdapter);

        ChildEventListener childEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String desc = dataSnapshot.child("description").getValue(String.class);
                String loca = dataSnapshot.child("location").getValue(String.class);
                String date = dataSnapshot.child("date").getValue(String.class);
                String contact = dataSnapshot.child("contact").getValue(String.class);
                myArrayList.add(title);
                mapDesc.put(title,desc);
                mapLoca.put(title,loca);
                mapDate.put(title,date);
                mapCont.put(title,contact);
                myArrayAdapter.notifyDataSetChanged();
                Log.d("TAG", title + "");

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
                Intent intent = new Intent(AllEvents.this, EventPage.class);
                intent.putExtra("EventTitle", myArrayList.get(position));
                intent.putExtra("EventDesc", mapDesc.get(myArrayList.get(position)));
                intent.putExtra("EventDate", mapDate.get(myArrayList.get(position)));
                intent.putExtra("EventLocation", mapLoca.get(myArrayList.get(position)));
                intent.putExtra("EventContact", mapCont.get(myArrayList.get(position)));
                startActivity(intent);
            }
        });

    }

}
