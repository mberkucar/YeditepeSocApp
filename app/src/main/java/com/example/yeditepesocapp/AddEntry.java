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

public class AddEntry extends AppCompatActivity {

    private TextView topicName;
    private EditText entry;
    private FloatingActionButton fabAddEntry, fabEntry;
    private Bitmap bitmap;
    private SharedPreferences preferences;
    private RequestQueue requestQueue;
    private String user_id, topic_id, topic_name, page="2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        topicName = (TextView) findViewById(R.id.textViewTopicName);
        entry = (EditText) findViewById(R.id.editTextEntry);
        fabAddEntry = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddedEntry);
        fabEntry = (FloatingActionButton) findViewById(R.id.floatingActionButtonReturnEntries);

        preferences = PreferenceManager.getDefaultSharedPreferences(AddEntry.this);
        requestQueue = Volley.newRequestQueue(AddEntry.this);

        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("user_id");
        topic_id = bundle.getString("topic_id");
        topic_name = bundle.getString("topic_name");
        //page = bundle.getString("page");
        topicName.setText(topic_name);
        Log.i("AddEntry page", page);


        fabAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (entry.getText().toString().trim().length() != 0 ){
                    SendRequest();
                    Snackbar.make(findViewById(R.id.floatingActionButtonAddedEntry), "Entry is added.", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),EntryActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("topic_id", topic_id);
                    intent.putExtra("page", page);
                    startActivity(intent);
                    //finish();
                }
                else{
                    Snackbar.make(findViewById(R.id.floatingActionButtonAddedEntry), "There is nothing here for sending.", Snackbar.LENGTH_LONG).show();
                }


            }
        });

        fabEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                //Intent intent = new Intent(getApplicationContext(),EntryActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("topic_id", topic_id);
                intent.putExtra("page", page);
                startActivity(intent);
            }
        });
    }

    private void SendRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADD_ENTRY, new Response.Listener<String>() {
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
                    Snackbar.make(findViewById(R.id.floatingActionButtonAddedEntry), "", Snackbar.LENGTH_LONG).show();
                }
                else{
                    Snackbar.make(findViewById(R.id.floatingActionButtonAddedEntry), message, Snackbar.LENGTH_LONG).show();
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
                params.put("entry_content", entry.getText().toString());
                params.put("user_id", String.valueOf(user_id));
                params.put("topic_id", String.valueOf(topic_id));

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

}
