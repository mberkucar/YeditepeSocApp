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

public class CourseEntryActivity extends AppCompatActivity {

    String TAG = "EntryActivity";
    private String user_id, course_id, course_name, page="2";
    private Context context;
    private List<CourseEntryModel> modelList;
    private ListView listViewCourseEntry;
    private TextView textViewCourseEntry;
    private SwipeRefreshLayout refreshLayout;
    private RequestQueue requestQueue;


    private FloatingActionButton fabCourse, fabAddCEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_entry);

        fabAddCEntry = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddCourseEntry);
        fabCourse = (FloatingActionButton) findViewById(R.id.floatingActionButtonReturnCourse);

        requestQueue = Volley.newRequestQueue(CourseEntryActivity.this);

        listViewCourseEntry = (ListView) findViewById(R.id.listviewCourseEntry);
        textViewCourseEntry = (TextView) findViewById(R.id.textViewCourseEntry);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshCourseEntry);


        modelList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            user_id = bundle.getString("user_id");
            course_id = bundle.getString("course_id");
            //course_name = bundle.getString("course_name");
            //page = bundle.getString("page");
        }
        Log.i("CourseEntryActivity user_id ", user_id);
        Log.i("CourseEntryActivity course_id", course_id);
        Log.i("CourseEntryActivity page", page);
        //Log.i("CourseEntryActivity course_name", course_name);





        fabAddCEntry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), AddCourseEntryActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("course_id", course_id);
                intent.putExtra("course_name", course_name);
                intent.putExtra("page", page);
                startActivity(intent);
            }}
        );

        fabCourse.setOnClickListener(new View.OnClickListener(){
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
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_COURSE_ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi CEntries: ", response);

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
                        textViewCourseEntry.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < entries.length(); i++){
                            JSONObject entry;
                            CourseEntryModel model = new CourseEntryModel();
                            try {
                                entry = entries.getJSONObject(i);
                                String userNameSurname = entry.getString("user_name")+
                                        " "+
                                        entry.getString("user_surname");
                                course_name = entry.getString("course_name");
                                model.setUser_name(userNameSurname);
                                model.setCourse_entry_content(entry.getString("course_entry_content"));
                                model.setCourse_entry_date(entry.getString("course_entry_date"));
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
                    Snackbar.make(listViewCourseEntry, message, Snackbar.LENGTH_LONG).show();
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
                params.put("course_id", course_id);
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);




    }

    private void setAdapter() {

        CourseEntryAdapter adapter = new CourseEntryAdapter(getApplicationContext(), modelList, true);
        listViewCourseEntry.setAdapter(adapter);


    }


    private void sendRequestRefresh() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_COURSE_ENTRY, new Response.Listener<String>() {
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
                        textViewCourseEntry.setText("There is nothing here.");
                    }else {

                        for (int i = 0; i < entries.length(); i++) {
                            JSONObject entry;
                            CourseEntryModel model = new CourseEntryModel();
                            try {
                                entry = entries.getJSONObject(i);
                                String userNameSurname = entry.getString("user_name")+
                                        " "+
                                        entry.getString("user_surname");
                                course_name = entry.getString("course_name");
                                model.setUser_name(userNameSurname);
                                model.setCourse_entry_content(entry.getString("course_entry_content"));
                                model.setCourse_entry_date(entry.getString("course_entry_date"));
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }
                        setAdapter();
                    }

                } else {
                    Snackbar.make(listViewCourseEntry, message, Snackbar.LENGTH_LONG).show();
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
                params.put("course_id", course_id);
                Log.i("CEntryActivity course_id", course_id);
                Log.i("CEntryActivity user_id", user_id);
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}
