package com.group6.sjsulookout;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class AttendingEvents extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private int listCounter = 0;
    Map<String,String> mapDesc = new HashMap<>();
    Map<String,String> mapLoca = new HashMap<>();
    Map<String,String> mapDate = new HashMap<>();
    Map<String,String> mapCont = new HashMap<>();
    Map<String,String> mapId = new HashMap<>();
    ArrayList<String> myArrayList = new ArrayList<>();
    public String mEventTitle;
    ListView mListView;

    String[] names = {"ONE","TWO"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id = ""+user.getUid();
        myRef = mFirebaseDatabase.getReference().child("Users");
        DatabaseReference currentRef = myRef.child(user_id);
        DatabaseReference eventRef = currentRef.child("AttendingEvents");
        mListView = (ListView) findViewById(R.id.listView);

        final CustomAdapter customAdapter = new CustomAdapter();
        mListView.setAdapter(customAdapter);

        ChildEventListener childEventListener = eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String desc = dataSnapshot.child("description").getValue(String.class);
                String location = dataSnapshot.child("location").getValue(String.class);

                String id = dataSnapshot.child("event_id").getValue(Integer.class) +"";
                mEventTitle = title;
                myArrayList.add(mEventTitle);
                mapDesc.put(title,desc);
                mapLoca.put(title,location);
                mapId.put(title,id);
                listCounter+=1;
                Log.d("TAG", title + "");
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

    }

    class CustomAdapter extends BaseAdapter{
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
            Log.d("TAG", myArrayList.size() + "");
            Log.d("TAG", myArrayList.get(position) + "");
            myTitle.setText(myArrayList.get(position));


            return view;
        }
    }

}
