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

public class SearchActivity extends AppCompatActivity {

    String TAG = "SearchActivity";
    private String user_id, page="1";
    private Context context;
    private List<SearchUserModel> modelList;
    private List<SearchUserModel> searchUserModels;
    private ListView listViewSearch;
    private TextView textViewSearch;
    private SwipeRefreshLayout refreshLayout;
    private RequestQueue requestQueue;
    private FloatingActionButton fab;
    private ArrayList<SearchUserModel> userList = new ArrayList<>();
    private SearchAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonHomeActivity);

        requestQueue = Volley.newRequestQueue(SearchActivity.this);

        listViewSearch = (ListView) findViewById(R.id.listviewUser);
        textViewSearch = (TextView) findViewById(R.id.textViewSearch);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshUser);

        searchUserModels = new ArrayList<>();
        modelList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            user_id = bundle.getString("user_id");
            page = bundle.getString("page");
        }


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
                sendRequestRefresh(new CallBack() {
                    @Override
                    public void onSuccess(ArrayList<SearchUserModel> userList) {
                        for(int i=0; i<userList.size(); i++){
                            Log.i(TAG, userList.get(i).user_name);
                            SearchUserModel searchUserModel = new SearchUserModel(userList.get(i).user_name,
                                    userList.get(i).user_surname,
                                    userList.get(i).user_id,
                                    userList.get(i).department_name,
                                    userList.get(i).faculty_name);
                            searchUserModels.add(searchUserModel);

                        }
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            }
        });
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), Color.BLUE,Color.GREEN);

        Log.d("Volley işlemleri testi", ".............................................");

        sendRequest(new CallBack() {
            @Override
            public void onSuccess(ArrayList<SearchUserModel> userList) {
                for(int i=0; i<userList.size(); i++){
                    Log.i(TAG, userList.get(i).user_name);
                    SearchUserModel searchUserModel = new SearchUserModel(userList.get(i).user_name,
                            userList.get(i).user_surname,
                            userList.get(i).user_id,
                            userList.get(i).department_name,
                            userList.get(i).faculty_name);
                    searchUserModels.add(searchUserModel);

                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        ComponentName componentName = new ComponentName(getApplicationContext(), SearchActivity.class);//getComponentName();
        SearchableInfo info = searchManager.getSearchableInfo(componentName);
        searchView.setSearchableInfo(info);



        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "setOnSearchClickListener");
                if (searchView.getQuery().length() == 0)
                    searchView.setQuery("", true);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void sendRequest(final CallBack onCallBack) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_USERLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi User: ", response);

                String status = null, message = null;
                JSONArray UserList = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    UserList = jsonObject.getJSONArray("UserList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (UserList.length()==0) {
                        textViewSearch.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < UserList.length(); i++){
                            JSONObject user;
                            SearchUserModel model = new SearchUserModel();
                            try {
                                user = UserList.getJSONObject(i);
                                String userNameSurname = user.getString("user_name")+
                                        " "+
                                        user.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                String name = user.getString("user_name");
                                String surname = user.getString("user_surname");
                                String user_id = user.getString("user_id");
                                String faculty = user.getString("faculty_name");
                                String department = user.getString("department_name");
                                SearchUserModel searchUserModel = new SearchUserModel(name, surname, user_id, faculty, department);
                                userList.add(searchUserModel);
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                                onCallBack.onFail(e.toString());
                            }

                            modelList.add(model);

                        }
                        onCallBack.onSuccess(userList);
                        setAdapter();
                    }



                }

                else {
                    //request başarısız ise
                    Snackbar.make(listViewSearch, message, Snackbar.LENGTH_LONG).show();
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
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }

    private void setAdapter() {

        adapter = new SearchAdapter(getApplicationContext(), modelList);
        listViewSearch.setAdapter(adapter);


    }


    private void sendRequestRefresh(final CallBack onCallBack) {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_USERLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.d("Json bilgisi UserList: ", response);

                String status = null, message = null;
                JSONArray UserList = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    UserList = jsonObject.getJSONArray("UserList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")) {

                    if (UserList.length()==0) {
                        textViewSearch.setText("There is nothing here.");
                    }else {

                        for (int i = 0; i < UserList.length(); i++) {
                            JSONObject user;
                            SearchUserModel model = new SearchUserModel();
                            try {
                                user = UserList.getJSONObject(i);
                                String userNameSurname = user.getString("user_name")+
                                        " "+
                                        user.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                String name = user.getString("user_name");
                                String surname = user.getString("user_surname");
                                String user_id = user.getString("user_id");
                                String faculty = user.getString("faculty_name");
                                String department = user.getString("department_name");
                                SearchUserModel searchUserModel = new SearchUserModel(name, surname, user_id, faculty, department);
                                userList.add(searchUserModel);
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                                onCallBack.onFail(e.toString());
                            }

                            modelList.add(model);

                        }
                        onCallBack.onSuccess(userList);
                        setAdapter();
                    }

                } else {
                    Snackbar.make(listViewSearch, message, Snackbar.LENGTH_LONG).show();
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
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    public interface CallBack {
        void onSuccess(ArrayList<SearchUserModel> userList);

        void onFail(String msg);
    }


}
