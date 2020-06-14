package com.example.yeditepesocapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateTopic extends AppCompatActivity {
    private TextView numberofChacTopic;
    private EditText topic;
    private FloatingActionButton fabCreateTopic, fabHome;
    private RequestQueue requestQueue;
    private String user_id, page;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

        numberofChacTopic = (TextView) findViewById(R.id.textViewNumberofChacTopic);
        topic = (EditText) findViewById(R.id.editCreateTopic);
        fabCreateTopic = (FloatingActionButton) findViewById(R.id.floatingActionButtonTopic);
        fabHome = (FloatingActionButton) findViewById(R.id.floatingActionButtonHomeforTopic);

        requestQueue = Volley.newRequestQueue(CreateTopic.this);

        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("user_id");
        page = bundle.getString("page");


        topic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int i = 80 - s.toString().trim().length();
                numberofChacTopic.setText(String.valueOf(i));

                if (i < 0) {
                    numberofChacTopic.setTextColor(Color.RED);
                    fabCreateTopic.hide();
                } else {
                    fabCreateTopic.show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        fabCreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (topic.getText().toString().trim().length() != 0 ){
                    SendRequest();
                    Snackbar.make(findViewById(R.id.floatingActionButtonTopic), "Topic is saved.", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("page", page);
                    startActivity(intent);
                }
                else{
                    Snackbar.make(findViewById(R.id.floatingActionButtonTopic), "There is nothing here for sending.", Snackbar.LENGTH_LONG).show();
                }


            }
        });

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("page", page);
                startActivity(intent);
            }
        });
    }

    private void SendRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CREATE_TOPIC, new Response.Listener<String>() {
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
                    Snackbar.make(findViewById(R.id.floatingActionButtonTopic), "", Snackbar.LENGTH_LONG).show();
                }
                else{
                    Snackbar.make(findViewById(R.id.floatingActionButtonTopic), message, Snackbar.LENGTH_LONG).show();
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
                params.put("topic_name", topic.getText().toString());
                params.put("user_id", String.valueOf(user_id));

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }
}
