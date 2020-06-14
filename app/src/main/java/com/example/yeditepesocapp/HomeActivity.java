package com.example.yeditepesocapp;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivity";

    public String user_id, page="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            user_id = bundle.getString("user_id");
            page = bundle.getString("page");
        }
        else
            page="1";
        /*else {
            savedInstanceState.getString("user_id");
            savedInstanceState.getString("page");
        }*/
        //Log.i("HomeActivity",user_id);
        switch (page) {
            case "1":
                loadFragment(new OpinionFragment()); //burası düzelticeecek
                break;
            case "2":
                loadFragment(new TopicFragment()); //burası düzelticeecek
                break;
            case "4":
                loadFragment(new EventFragment()); //burası düzelticeecek
                break;
            case "5":
                loadFragment(new ProfileFragment()); //burası düzelticeecek
                break;

        }
        handleIntent(getIntent());


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }
    protected void onStart(){
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            user_id = bundle.getString("user_id");
            page = bundle.getString("page");
        }
        switch (page) {
            case "1":
                loadFragment(new OpinionFragment()); //burası düzelticeecek
                break;
            case "2":
                loadFragment(new TopicFragment()); //burası düzelticeecek
                break;
            case "4":
                loadFragment(new EventFragment()); //burası düzelticeecek
                break;
            case "5":
                loadFragment(new ProfileFragment()); //burası düzelticeecek
                break;

        }
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
    private boolean loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user_id);
        fragment.setArguments(bundle);
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.action_yedsocapp_event:
                fragment = new EventFragment();
                break;

            case R.id.action_yedsocapp_op:
                fragment = new OpinionFragment();
                break;

            case R.id.action_yedsocapp_profile:
                fragment = new ProfileFragment();
                break;

            case R.id.action_yedsocapp_topic:
                fragment = new TopicFragment();
                break;
        }

        return loadFragment(fragment);
    }
    @Override
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
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                Log.v("App", "setOnSearchClickListener");
                if (searchView.getQuery().length() == 0)
                    searchView.setQuery("", true);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId()==R.id.search){
            Intent i = new Intent(this,SearchActivity.class);
            i.putExtra("user_id", user_id);
            i.putExtra("page", page);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        //setIntent(intent);
        Log.v("App", "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Log.v("App", query);
        }
    }


}
