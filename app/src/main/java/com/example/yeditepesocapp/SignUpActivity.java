package com.example.yeditepesocapp;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editFirst_name, editLast_name, editNumber, editUser_mail, editPassword, editCPassword, editFaculty_name, editDepartment_name;
    private Button signup_button;
    private String user_type_id;
    protected boolean errorCheck = true;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String TAG = "SignUpActivity";
    private String first_name, last_name, number, user_mail, password, faculty_name, department_name, currentUserId;


    private final Pattern STDMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "std.yeditepe.edu.tr"
    );
    private final Pattern YEDMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "yeditepe.edu.tr"
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user");


        editFirst_name = (EditText)findViewById(R.id.first_name);
        editLast_name = (EditText) findViewById(R.id.last_name);
        editNumber = (EditText) findViewById(R.id.number);
        editUser_mail = (EditText) findViewById(R.id.user_mail);
        editPassword = (EditText) findViewById(R.id.password);
        editCPassword = (EditText) findViewById(R.id.re_password);
        editFaculty_name = (EditText) findViewById(R.id.faculty_name);
        editDepartment_name = (EditText) findViewById(R.id.department_name);

        signup_button = (Button) findViewById(R.id.signup_button);

        signup_button.setOnClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private boolean ValidatePassword() {
        try{
            boolean temp = true;
            String password=editPassword.getText().toString();
            String cpass=editCPassword.getText().toString();
            if(!password.equals(cpass)){
                temp = false;
            }
            return temp;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private int ValidateMail() {
        int temp;
        String checkEmail = editUser_mail.getText().toString();
        if(STDMAIL_ADDRESS_PATTERN.matcher(checkEmail).matches()){
            Log.i("SignUpActivitiy", "YeditepeStdMail");
            temp = 1;
            return temp;
        }
        else if(YEDMAIL_ADDRESS_PATTERN.matcher(checkEmail).matches()){
            Log.i("SignUpActivitiy", "YeditepeMail");
            temp = 2;
            return temp;
        }
        else {
            Toast.makeText(SignUpActivity.this,"Invalid Mail Address. Use your Yeditepe mail.",Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    private void SignUpUser(){

        first_name = editFirst_name.getText().toString();
        last_name = editLast_name.getText().toString();
        number = editNumber.getText().toString();
        user_mail = editUser_mail.getText().toString();
        password = editPassword.getText().toString();
        faculty_name = editFaculty_name.getText().toString();
        department_name = editDepartment_name.getText().toString();

        //final String first_name = editFirst_name.getText().toString().trim();
        //final String last_name = editLast_name.getText().toString().trim();
        //final String number = editNumber.getText().toString().trim();
        //final String user_mail = editUser_mail.getText().toString().trim();
        //final String password = editPassword.getText().toString().trim();
        //final String faculty_name = editFaculty_name.getText().toString().trim();
        //final String department_name = editDepartment_name.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(user_mail, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            currentUserId = mAuth.getCurrentUser().getUid();
                            databaseReference.child(currentUserId).setValue("");
                            databaseReference.child(currentUserId).child("user_name").push().setValue(first_name);
                            databaseReference.child(currentUserId).child("user_surname").push().setValue(last_name);
                            databaseReference.child(currentUserId).child("user_number").push().setValue(number);
                            databaseReference.child(currentUserId).child("faculty_name").push().setValue(faculty_name);
                            databaseReference.child(currentUserId).child("department_name").push().setValue(department_name);
                            Log.i(TAG, first_name);
                            Log.i(TAG, last_name);
                            Log.i(TAG, number);
                            Log.i(TAG, faculty_name);
                            Log.i(TAG, department_name);
                            Log.i(TAG, "I AM HERE in if ");
                            Log.i("FirebaseAuth", "onComplete if" + task.getException().getMessage());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Log.i(TAG, "I AM HERE in else");
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.i("FirebaseAuth", "onComplete else" + task.getException().getMessage());
                        }
                        task.getException();
                        Log.i(TAG, "I AM HERE out if");
                        Log.i("FirebaseAuth", "onComplete out" + task.getException().getMessage());
                        // ...
                    }
                });

        //currentUserId = mAuth.getCurrentUser().getUid();
        //Log.i(TAG, currentUserId);



        if(!(ValidatePassword()))
            Toast.makeText(SignUpActivity.this, "Password Not Matching.\n Try Again.", Toast.LENGTH_SHORT).show();
        if (ValidateMail() == 1)
            user_type_id = "1";
        else if (ValidateMail() == 2)
            user_type_id = "2";
        else {
            Toast.makeText(SignUpActivity.this, "Invalid Mail Address. \n Try Again", Toast.LENGTH_SHORT).show();

        }

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(SignUpActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.replace("\\\"","'");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        errorCheck = false;
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_mail",user_mail);
                params.put("user_number", number);
                params.put("user_name", first_name);
                params.put("user_surname", last_name);
                params.put("faculty_name", faculty_name);
                params.put("department_name", department_name);
                params.put("password", password);
                params.put("user_type_id", user_type_id);
                return params;
            }

        };

        requestQueue.add(stringRequest);

    }
    public void onClick(View view){
        if(view == signup_button) {
            SignUpUser();
            if(errorCheck) {
                //Toast.makeText(getApplicationContext(), "Succesfully registered.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            }
        }

    }

    public void openLoginPage(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }
}