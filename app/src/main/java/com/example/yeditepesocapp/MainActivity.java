package com.example.yeditepesocapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ActivityMain";
    Button login_button, signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");
        login_button = (Button) findViewById(R.id.main_login_button);
        signup_button = (Button) findViewById(R.id.main_signup_button);
        
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage(v);
            }
        });
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupPage(v);
            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "onStart");

    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG, "onStop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
    public void openLoginPage(View view) {
        startActivity(new Intent(this,LoginActivity.class));
        Log.i(TAG, "Started Login");
    }

    public void openSignupPage(View view) {
        startActivity(new Intent(this,SignUpActivity.class));
        Log.i(TAG, "Started SignUp");

    }
}
