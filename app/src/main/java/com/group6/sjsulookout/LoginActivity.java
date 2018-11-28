package com.group6.sjsulookout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmail, mPassword;
    private TextView mSignUp;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        //declare buttons and editTexts in oncreate
        mEmail = (EditText) findViewById(R.id.lEmail);
        mPassword = (EditText) findViewById(R.id.lPassword);
        mSignUp = (TextView) findViewById(R.id.tSignUp);
        btnLogin = (Button) findViewById(R.id.signIn);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                }else{
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(!email.equals("") && !password.equals("")){
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                            }else{
                                mEmail.setError("");
                                mEmail.requestFocus();
                                mPassword.setError("");
                                mPassword.requestFocus();
                                toastMessage("Incorrect Login Information");
                            }
                        }
                    });
                }else{
                    toastMessage("You did not fill in all the fields.");
                }
            }
        });

    }

    public void onEventClick(View v){
        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
