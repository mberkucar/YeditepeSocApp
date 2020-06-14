package com.example.yeditepesocapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WriteOpinion extends AppCompatActivity {

    private TextView numberofChac;
    private EditText opinion;
    private FloatingActionButton fab, fabHome;
    private Bitmap bitmap;
    private SharedPreferences preferences;
    private RequestQueue requestQueue;
    private String user_id, page ="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_opinion);

        numberofChac = (TextView) findViewById(R.id.textViewNumberofChac);
        opinion = (EditText) findViewById(R.id.editTextOpinion);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonOpinion);
        fabHome = (FloatingActionButton) findViewById(R.id.floatingActionButtonHome);

        preferences = PreferenceManager.getDefaultSharedPreferences(WriteOpinion.this);
        requestQueue = Volley.newRequestQueue(WriteOpinion.this);

        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("user_id");
        //page = bundle.getString("page");
        Log.i("WriteOpinion page", page);



        opinion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int i = 300 - s.toString().trim().length();
                numberofChac.setText(String.valueOf(i));

                if (i < 10) {
                    numberofChac.setTextColor(Color.RED);
                    fab.hide();
                } else {
                    numberofChac.setTextColor(Color.parseColor("#444444"));
                    //numberofChac.setTextColor(Color.parseColor("#444")); -->hatalı kullanım
                    fab.show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (opinion.getText().toString().trim().length() != 0 ){
                    SendRequest();
                    Snackbar.make(findViewById(R.id.floatingActionButtonOpinion), "Opinion is saved.", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("page", page);
                    Log.i("WriteOpinion page", page);
                    startActivity(intent);
                    //finish();
                }
                else{
                    Snackbar.make(findViewById(R.id.floatingActionButtonOpinion), "There is nothing here for sending.", Snackbar.LENGTH_LONG).show();
                }


            }
        });

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("page", page);
                Log.i("WriteOpinion page", page);
                startActivity(intent);
            }
        });
    }

    private void SendRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_HOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json info: ", response);

                String status=null, message=null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")){
                    Snackbar.make(findViewById(R.id.floatingActionButtonOpinion), "", Snackbar.LENGTH_LONG).show();
                    //opinion.setText("");
                }
                else{
                    Snackbar.make(findViewById(R.id.floatingActionButtonOpinion), message, Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //String uuid = UUID.randomUUID().toString();
                //parametreler ekleniyor
                Log.i("InMap","Params");
                Log.i("InMap",String.valueOf(user_id));
                params.put("user_id", String.valueOf(user_id));
                params.put("opinion_body", opinion.getText().toString());

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

}
