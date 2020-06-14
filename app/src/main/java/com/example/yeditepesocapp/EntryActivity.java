package com.example.yeditepesocapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryActivity extends AppCompatActivity {

    String TAG = "EntryActivity";
    private String user_id, topic_id, topic_name, page="2";
    private Context context;
    private List<EntryModel> modelList;
    private ListView listViewEntry;
    private TextView textViewEntry;
    private SwipeRefreshLayout refreshLayout;
    private RequestQueue requestQueue;
    private SharedPreferences preferences;


    private FloatingActionButton fabTopic, fabAddEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        fabAddEntry = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddEntry);
        fabTopic = (FloatingActionButton) findViewById(R.id.floatingActionButtonReturnTopics);

        preferences = PreferenceManager.getDefaultSharedPreferences(EntryActivity.this);
        requestQueue = Volley.newRequestQueue(EntryActivity.this);

        listViewEntry = (ListView) findViewById(R.id.listviewEntry);
        textViewEntry = (TextView) findViewById(R.id.textViewEntry);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshEntry);

        // refreshLayout a 3 tane renk değeri veriyoruz. İşlem uzadıkçe sırayla verilen renk değerlerini alacak

        modelList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            user_id = bundle.getString("user_id");
            topic_id = bundle.getString("topic_id");
            topic_name = bundle.getString("topic_name");
            //page = bundle.getString("page");
        }
        Log.i("EntryActivity user_id ", user_id);
        Log.i("EntryActivity topic_id", topic_id);
        Log.i("EntryActivity page", page);





        fabAddEntry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), AddEntry.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("topic_id", topic_id);
                intent.putExtra("topic_name", topic_name);
                intent.putExtra("page", page);
                startActivity(intent);
            }}
        );

        fabTopic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("page", page);
                startActivity(intent);
            }}
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                modelList.clear();
                sendRequestRefresh();
            }
        });
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), Color.BLUE,Color.GREEN);

        Log.d("Volley işlemleri testi", ".............................................");

        sendRequest();
    }
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

    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi Entries: ", response);

                String status = null, message = null;
                JSONArray entries = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    entries = jsonObject.getJSONArray("entries");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (entries.length()==0) {
                        textViewEntry.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < entries.length(); i++){
                            JSONObject entry;
                            EntryModel model = new EntryModel();
                            try {
                                entry = entries.getJSONObject(i);
                                String userNameSurname = entry.getString("user_name")+
                                        " "+
                                        entry.getString("user_surname");
                                topic_name = entry.getString("topic_name");
                                model.setUser_name(userNameSurname);
                                model.setEntry_content(entry.getString("entry_content"));
                                model.setEntry_date(entry.getString("entry_date"));
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }

                        setAdapter();
                    }



                }

                else {
                    //request başarısız ise
                    Snackbar.make(listViewEntry, message, Snackbar.LENGTH_LONG).show();
                }
                Log.d("Volley işlemleri testi", "request işlemler tamamlandı.........................");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("topic_id", topic_id);
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }

    private void setAdapter() {

        EntryAdapter adapter = new EntryAdapter(getApplicationContext(), modelList, true);
        listViewEntry.setAdapter(adapter);


    }


    private void sendRequestRefresh() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.d("Json bilgisi Entries: ", response);

                String status = null, message = null;
                JSONArray entries = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    entries = jsonObject.getJSONArray("entries");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")) {

                    if (entries.length()==0) {
                        textViewEntry.setText("There is nothing here.");
                    }else {

                        for (int i = 0; i < entries.length(); i++) {
                            JSONObject entry;
                            EntryModel model = new EntryModel();
                            try {
                                entry = entries.getJSONObject(i);
                                String userNameSurname = entry.getString("user_name")+
                                        " "+
                                        entry.getString("user_surname");
                                topic_name = entry.getString("topic_name");
                                model.setUser_name(userNameSurname);
                                model.setEntry_content(entry.getString("entry_content"));
                                model.setEntry_date(entry.getString("entry_date"));
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }
                        setAdapter();
                    }

                } else {
                    Snackbar.make(listViewEntry, message, Snackbar.LENGTH_LONG).show();
                }
                Log.d("Volley işlemleri testi", "request işlemler tamamlandı.........................");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshLayout.setRefreshing(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("topic_id", topic_id);
                Log.i("EntryActivity topic_id", topic_id);
                Log.i("EntryActivity user_id", user_id);
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}
