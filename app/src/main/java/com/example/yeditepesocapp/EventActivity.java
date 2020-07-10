package com.example.yeditepesocapp;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.EventLog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
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

public class EventActivity extends AppCompatActivity {

    String TAG = "EventActivity";
    private String event_id, user_id, page="4";
    private Context context;
    private List<EventUserModel> modelList;
    private ListView listViewEvent;
    private TextView textViewEvent;
    private SwipeRefreshLayout refreshLayout;
    private RequestQueue requestQueue;
    private FloatingActionButton fab;
    private ArrayList<EventUserModel> userList = new ArrayList<>();
    private EventUserAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonEventFragment);

        requestQueue = Volley.newRequestQueue(EventActivity.this);

        listViewEvent = (ListView) findViewById(R.id.listviewEventUser);
        textViewEvent = (TextView) findViewById(R.id.textViewEvent);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshEventUser);

        modelList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            user_id = bundle.getString("user_id");
            event_id = bundle.getString("event_id");
        }
        Log.i(TAG+" event_id ", event_id);


        fab.setOnClickListener(new View.OnClickListener(){
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
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_EVENTUSER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi EventUser: ", response);

                String status = null, message = null;
                JSONArray EventUserList = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    EventUserList = jsonObject.getJSONArray("EventUserList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (EventUserList.length()==0) {
                        textViewEvent.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < EventUserList.length(); i++){
                            JSONObject user;
                            EventUserModel model = new EventUserModel();
                            try {
                                user = EventUserList.getJSONObject(i);
                                String userNameSurname = user.getString("user_name")+
                                        " "+
                                        user.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                String name = user.getString("user_name");
                                String surname = user.getString("user_surname");
                                String department = user.getString("department_name");
                                EventUserModel eventUserModel = new EventUserModel(name, surname, department);
                                userList.add(eventUserModel);
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
                    Snackbar.make(listViewEvent, message, Snackbar.LENGTH_LONG).show();
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
                params.put("event_id", event_id);
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }

    private void setAdapter() {

        adapter = new EventUserAdapter(getApplicationContext(), modelList, false);
        listViewEvent.setAdapter(adapter);


    }


    private void sendRequestRefresh() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_EVENTUSER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.d("Json bilgisi UserList: ", response);

                String status = null, message = null;
                JSONArray EventUserList = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    EventUserList = jsonObject.getJSONArray("UserList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")) {

                    if (EventUserList.length()==0) {
                        textViewEvent.setText("There is nothing here.");
                    }else {

                        for (int i = 0; i < EventUserList.length(); i++) {
                            JSONObject user;
                            EventUserModel model = new EventUserModel();
                            try {
                                user = EventUserList.getJSONObject(i);
                                String userNameSurname = user.getString("user_name")+
                                        " "+
                                        user.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                String name = user.getString("user_name");
                                String surname = user.getString("user_surname");
                                String department = user.getString("department_name");
                                EventUserModel eventUserModel = new EventUserModel(name, surname, department);
                                userList.add(eventUserModel);
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }
                        setAdapter();
                    }

                } else {
                    Snackbar.make(listViewEvent, message, Snackbar.LENGTH_LONG).show();
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
                params.put("event_id", event_id);
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }


}
