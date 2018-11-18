package com.group6.sjsulookout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddEvent extends AppCompatActivity {

    private Button sendData;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mDesc;
    private int eventCounter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        sendData = (Button) findViewById(R.id.BtnAddEvent);
        mTitle = (EditText) findViewById(R.id.Title);
        mLocation = (EditText) findViewById(R.id.Location);
        mDesc = (EditText) findViewById(R.id.Description);
        eventCounter = 0;

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString();
                String location = mLocation.getText().toString();
                String desc = mDesc.getText().toString();
                DatabaseReference myEvents = myRef.child("UserEvents");
                DatabaseReference myEventChild = myEvents.child(""+UUID.randomUUID());
                DatabaseReference myEventsTitle = myEventChild.child("Title");
                DatabaseReference myEventLocation = myEventChild.child("Location");
                DatabaseReference myEventDesc = myEventChild.child("Description");

                myEventsTitle.setValue(title);
                myEventLocation.setValue(location);
                myEventDesc.setValue(desc);

                toastMessage("Successfully posted: "+title);
            
                mTitle.getText().clear();
                mLocation.getText().clear();
                mDesc.getText().clear();


            }
        });

    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
