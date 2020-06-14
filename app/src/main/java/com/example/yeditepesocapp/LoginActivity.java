package com.example.yeditepesocapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextMail, editTextPassword;
    private Button buttonLogin;
    private String page = "1";
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String TAG ="LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }*/ // Burası Log in olmak için..

        editTextMail = (EditText) findViewById(R.id.login_mail);
        editTextPassword = (EditText) findViewById(R.id.login_password);
        buttonLogin = (Button) findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();
        buttonLogin.setOnClickListener(this);


    }

    private void userLogin(){
        final String user_mail = editTextMail.getText().toString();
        final String password = editTextPassword.getText().toString();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(user_mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String currentUserId = mAuth.getCurrentUser().getUid();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                        }

                    }
                });

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(LoginActivity.this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getString("user_mail")
                                        );
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("user_id", obj.getString("user_id"));
                                intent.putExtra("page", page);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_mail", user_mail);
                params.put("password", password);
                return params;
            }

        };


        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){
            userLogin();
        }
    }

    public void openSignupPage(View view) {
        startActivity(new Intent(this,SignUpActivity.class));
    }


    public void openLoginPage(View view) {

    }


}
